package ca.mcgill.ecse321.townlibrary.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.hamcrest.Matchers.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OnlineMemberControllerTest {

    // Because class is marked @Transactional, the database is rolled-back
    // before each test case (as if it was reset).

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        // Also each test assumes there is a proper library.
        post("/libraries/0?address=300 Pepper Street");
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void testNoOnlineMembersToBeginWith()  {
        when().get("/online-members")
            .then()
            .statusCode(200)
            .body("$", empty());

        // it's empty, so any id we feed should error
        when().get("/online-members/0")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-ONLINE-MEMBER"));
    }

    @Test
    public void testCreateOnlineMemberThenQueryIt() {
        final int id = given()
            .param("password", "FooMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Foo Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
            .then()
            .statusCode(200)
            .body("name", equalTo("Foo Man"))
            .body("address", equalTo("415 Garlic Street"))
            .body("inTown", equalTo(false))
            .body("username", equalTo("foo_man"))
            .body("email", equalTo("foo.man@untitled.com"))
            .body("libraryId", equalTo(0))
            .extract()
            .response().body().path("id");

        when().get("/online-members/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Foo Man"))
            .body("address", equalTo("415 Garlic Street"))
            .body("inTown", equalTo(false))
            .body("username", equalTo("foo_man"))
            .body("email", equalTo("foo.man@untitled.com"))
            .body("libraryId", equalTo(0));

        when().get("/online-members/")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(id))
            .body("[0].name", equalTo("Foo Man"))
            .body("[0].address", equalTo("415 Garlic Street"))
            .body("[0].inTown", equalTo(false))
            .body("[0].username", equalTo("foo_man"))
            .body("[0].email", equalTo("foo.man@untitled.com"))
            .body("[0].libraryId", equalTo(0));
    }

    @Test
    public void testRecreateOnlineMemberWithDuplicateInfo() {
        given()
            .param("password", "FooMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Foo Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
            .then()
            .statusCode(200);

        given()
            .param("password", "BarMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Bar Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
            .then()
            .statusCode(400)
            .body(containsString("DUP-USERNAME"))
            .body(containsString("DUP-EMAIL"));
    }

    @Test
    public void testAuthOnlineMember() {
        final int id = given()
            .param("password", "FooMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Foo Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id");

        given()
            .param("password", "FooMan123")
            .when().post("/auth/online-members/foo_man")
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Foo Man"))
            .body("address", equalTo("415 Garlic Street"))
            .body("inTown", equalTo(false))
            .body("username", equalTo("foo_man"))
            .body("email", equalTo("foo.man@untitled.com"))
            .body("libraryId", equalTo(0));

        given()
            .param("password", "bob123")
            .when().post("/auth/online-members/foo_man")
            .then()
            .statusCode(400)
            .body(equalTo("BAD-AUTH-ONLINE-MEMBER"));
    }

    @Test
    public void testLibrarianValidatingAddress() {
        // we also need a librarian (let's create a head librarian) for this.
        final int idLibrarian = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/head-librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id");

        final int idOnlineMember = given()
            .param("password", "FooMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Foo Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
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
            .when().put("/online-members/" + idOnlineMember + "/in-town")
            .then()
            .statusCode(200)
            .body("id", equalTo(idOnlineMember))
            .body("inTown", equalTo(true));

        // and consequently, future queries on that member will show address is
        // indeed in-town.

        given()
            .when().get("/online-members/" + idOnlineMember)
            .then()
            .statusCode(200)
            .body("id", equalTo(idOnlineMember))
            .body("inTown", equalTo(true));
    }

    @Test
    public void testIllegalUserRoleAddressValidate() {
        final int idOnlineMember = given()
            .param("password", "FooMan123")
            .param("email", "foo.man@untitled.com")
            .param("address", "415 Garlic Street")
            .param("name", "Foo Man")
            .param("library", "0")
            .when().post("/online-members/foo_man")
            .then()
            .statusCode(200)
            .body("inTown", equalTo(false))
            .extract()
            .response().body().path("id");

        // say our online member wants to avoid paying, and tries to pull
        // this request themselves...

        given()
            .param("value", true)
            .param("initId", idOnlineMember)
            .param("initPass", "FooMan123")
            .when().put("/online-members/" + idOnlineMember + "/in-town")
            .then()
            .statusCode(400)
            .body(equalTo("BAD-ACCESS"));
    }

    @Test
    public void testLibrarianValidatingAddressOnInexistentUser() {
        // we also need a librarian (let's create a head librarian) for this.
        final int idLibrarian = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/head-librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id");

        given()
            .param("value", true)
            .param("initId", idLibrarian)
            .param("initPass", "jojo123")
            .when().put("/online-members/0/in-town")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-ONLINE-MEMBER"));
    }
}