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
public class LibraryControllerTest {

    // Because class is marked @Transactional, the database is rolled-back
    // before each test case (as if it was reset).

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void testNoLibrariesToBeginWith() {
        when().get("/libraries")
            .then()
            .statusCode(200)
            .body("$", empty());

        when().get("/libraries/0")
            .then()
            .statusCode(400)
            .body(equalTo("NOT-FOUND-LIBRARY"));
    }

    @Test
    public void testCreateLibraryThenQueryIt() {
        when().post("/libraries/0?address=300 Pepper Street")
            .then()
            .statusCode(200)
            .body("id", equalTo(0))
            .body("address", equalTo("300 Pepper Street"))
            .body("headLibrarianId", is(nullValue()));

        when().get("/libraries/0")
            .then()
            .statusCode(200)
            .body("id", equalTo(0))
            .body("address", equalTo("300 Pepper Street"))
            .body("headLibrarianId", is(nullValue()));

        when().get("/libraries")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(0))
            .body("[0].address", equalTo("300 Pepper Street"))
            .body("[0].headLibrarianId", is(nullValue()));
    }

    @Test
    public void testRecreateLibrarySameIdAgain() {
        when().post("/libraries/0?address=300 Pepper Street")
            .then()
            .statusCode(200);

        when().post("/libraries/0?address=300 Pepper Street")
            .then()
            .statusCode(400)
            .body(equalTo("DUP-LIBRARY"));
    }
}