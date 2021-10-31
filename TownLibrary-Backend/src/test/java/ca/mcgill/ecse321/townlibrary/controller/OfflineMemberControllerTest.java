package ca.mcgill.ecse321.townlibrary.controller;

import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OfflineMemberControllerTest {

    // Because class is marked @Transactional, the database is rolled-back
    // before each test case (as if it was reset).

    @Autowired
    private WebApplicationContext webApplicationContext;

    private int idLibrarian;

    private int idAnotherMember;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        // Also each test assumes there is a proper library.
        post("/libraries/0?address=300 Pepper Street");

        // And some actions need librarian powers.
        this.idLibrarian = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .post("/head-librarians/Joe Schmoe")
            .then()
            .extract()
            .response().body().path("id");

        // And for completeness, why not another (online) member
        this.idAnotherMember = given()
            .param("password", "BarMan123")
            .param("email", "bar.man@untitled.com")
            .param("address", "417 Garlic Street")
            .param("name", "Bar Man")
            .param("library", "0")
            .post("/online-members/bar_man")
            .then()
            .extract()
            .response().body().path("id");
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void testNoOfflineMembersToBeginWith()  {
        when().get("/offline-members")
            .then()
            .statusCode(200)
            .body("$", empty());

        // it's empty, so any id we feed should error
        when().get("/offline-members/0")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-OFFLINE-MEMBER"));
    }

    @Test
    public void testCreateOfflineMemberThenQueryIt() {
        final int id = given()
            .param("address", "415 Garlic Street")
            .param("library", "0")
            .param("initId", this.idLibrarian)
            .param("initPass", "jojo123")
            .when().post("/offline-members/Foo Man")
            .then()
            .statusCode(200)
            .body("name", equalTo("Foo Man"))
            .body("address", equalTo("415 Garlic Street"))
            .body("inTown", equalTo(false))
            .body("libraryId", equalTo(0))
            .extract()
            .response().body().path("id");

        when().get("/offline-members/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Foo Man"))
            .body("address", equalTo("415 Garlic Street"))
            .body("inTown", equalTo(false))
            .body("libraryId", equalTo(0));

        when().get("/offline-members/")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(id))
            .body("[0].name", equalTo("Foo Man"))
            .body("[0].address", equalTo("415 Garlic Street"))
            .body("[0].inTown", equalTo(false))
            .body("[0].libraryId", equalTo(0));
    }

    @Test
    public void testCreateSeveralOfflineMembers() {
        final int[] members = new int[5];
        for (int i = 0; i < members.length; ++i)
            members[i] = given()
                .param("address", "387 Fairfield Boulevard")
                .param("library", 0)
                .param("initId", this.idLibrarian)
                .param("initPass", "jojo123")
                .when().post("/offline-members/Person" + i)
                .then()
                .statusCode(200)
                .extract()
                .response().body().path("id");

        // id's must be unique
        final HashSet<Integer> set = new HashSet<>();
        for (final int id : members)
            Assertions.assertTrue(set.add(id));

        for (final int id : members)
            when().get("/offline-members/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id));

        when().get("/offline-members")
            .then()
            .statusCode(200)
            .body("size()", equalTo(members.length));
    }

    @Test
    public void testIllegalNonLibrarianCreateAccount() {
        given()
            .param("address", "415 Garlic Street")
            .param("library", "0")
            .param("initId", this.idAnotherMember)
            .param("initPass", "BarMan123")
            .when().post("/offline-members/Foo Man")
            .then()
            .statusCode(400)
            .body(equalTo("BAD-ACCESS"));
    }

    @Test
    public void testLibrarianValidatingAddress() {
        final int idOfflineMember = given()
            .param("address", "415 Garlic Street")
            .param("library", "0")
            .param("initId", this.idLibrarian)
            .param("initPass", "jojo123")
            .when().post("/offline-members/Foo Man")
            .then()
            .statusCode(200)
            .body("inTown", equalTo(false))
            .extract()
            .response().body().path("id");

        // say the librarian did a query (like in the other tests), and sees
        // that the address is indeed a local addess.

        given()
            .param("value", true)
            .param("initId", idLibrarian)
            .param("initPass", "jojo123")
            .when().put("/offline-members/" + idOfflineMember + "/in-town")
            .then()
            .statusCode(200)
            .body("id", equalTo(idOfflineMember))
            .body("inTown", equalTo(true));

        // and consequently, future queries on that member will show address is
        // indeed in-town.

        given()
            .when().get("/offline-members/" + idOfflineMember)
            .then()
            .statusCode(200)
            .body("id", equalTo(idOfflineMember))
            .body("inTown", equalTo(true));
    }

    @Test
    public void testIllegalUserRoleAddressValidate() {
        final int idOfflineMember = given()
            .param("address", "415 Garlic Street")
            .param("library", "0")
            .param("initId", this.idLibrarian)
            .param("initPass", "jojo123")
            .when().post("/offline-members/Foo Man")
            .then()
            .statusCode(200)
            .body("inTown", equalTo(false))
            .extract()
            .response().body().path("id");

        // say our online member (created in setup) was looking to break our
        // system...

        given()
            .param("value", true)
            .param("initId", this.idAnotherMember)
            .param("initPass", "BarMan123")
            .when().put("/offline-members/" + idOfflineMember + "/in-town")
            .then()
            .statusCode(400)
            .body(equalTo("BAD-ACCESS"));
    }

    @Test
    public void testLibrarianValidatingAddressOnInexistentUser() {
        given()
            .param("value", true)
            .param("initId", this.idLibrarian)
            .param("initPass", "jojo123")
            .when().put("/offline-members/0/in-town")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-OFFLINE-MEMBER"));
    }
}