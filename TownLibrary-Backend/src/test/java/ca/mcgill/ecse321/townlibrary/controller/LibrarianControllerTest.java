package ca.mcgill.ecse321.townlibrary.controller;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LibrarianControllerTest {

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
    public void testLibrariansToBeginWith()  {
        when().get("/librarians")
            .then()
            .statusCode(200)
            .body("$", empty());

        // it's empty, so any id we feed should error
        when().get("/librarians/0")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-LIBRARIAN"));
    }

    @Test
    public void testCreateHeadLibrarianThenQueryIt() {
        // this test is relevant because it shows /librarians will consider
        // head-librarians as well.

        final int id = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/head-librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0))
            .extract()
            .response().body().path("id");

        when().get("/librarians/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0));

        when().get("/librarians/")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(id))
            .body("[0].name", equalTo("Joe Schmoe"))
            .body("[0].address", equalTo("410 Chili Street"))
            .body("[0].libraryId", equalTo(0));
    }

    @Test
    public void testCreateOrdinaryLibrarianThenQueryIt() {
        final int id = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0))
            .extract()
            .response().body().path("id");

        when().get("/librarians/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0));

        when().get("/librarians/")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(id))
            .body("[0].name", equalTo("Joe Schmoe"))
            .body("[0].address", equalTo("410 Chili Street"))
            .body("[0].libraryId", equalTo(0));
    }

    @Test
    public void testAuthHeadLibrarian() {
        // Unlike the one in HeadLibrarianControllerTest, here we authenticate
        // a head librarian through the librarian path mapping.
        final int id = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/head-librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id");

        given()
            .param("password", "jojo123")
            .when().post("/auth/librarians/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0));

        given()
            .param("password", "bob123")
            .when().post("/auth/librarians/" + id)
            .then()
            .statusCode(400)
            .body(equalTo("BAD-ACCESS"));
    }

    @Test
    public void testAuthOrdinaryLibrarian() {
        final int id = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "0")
            .when().post("/librarians/Joe Schmoe")
            .then()
            .statusCode(200)
            .extract()
            .response().body().path("id");

        given()
            .param("password", "jojo123")
            .when().post("/auth/librarians/" + id)
            .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Joe Schmoe"))
            .body("address", equalTo("410 Chili Street"))
            .body("libraryId", equalTo(0));

        given()
            .param("password", "bob123")
            .when().post("/auth/librarians/" + id)
            .then()
            .statusCode(400)
            .body(equalTo("BAD-ACCESS"));
    }
}