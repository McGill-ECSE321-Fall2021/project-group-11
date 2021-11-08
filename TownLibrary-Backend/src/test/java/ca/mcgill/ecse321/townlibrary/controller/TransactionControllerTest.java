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
import static org.mockito.Mockito.inOrder;

// Heavily inspired by Paul's integration testing files

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
        private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        post("/libraries/10005?address=sad street 2");
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void testStartTransactions() {
        // Make sure its empty
        when().get("/transactions")
            .then().statusCode(200);

        // Since empty, any transaction search should return error
        when().get("/transactions/1")
            .then().statusCode(400)
            .body(equalTo("NOT-FOUND-TRANSACTION"));
    }

    @Test
    public void testCreateTransactionAndQuery() {
        final int id = given()
            .param("startDate", "2021-11-07")
            .param("endDate", "2021-11-09")
            .param("userId", "10005")
            .when().post("/transactions/0")
            .then().statusCode(200)
            .body("startDate", equalTo("2021-11-07"))
            .body("endDate", equalTo("2021-11-09"))
            .body("userId", equalTo("10005"))
            .extract().response().body().path("id");

            when().get("/transactions/" + id)
                .then().statusCode(200)
                .body("id", equalTo(id))
                .body("startDate", equalTo("2021-11-07"))
                .body("endDate", equalTo("2021-11-09"))
                .body("userId", equalTo("10005"));

            when().get("/transactions/")
                .then().statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].id", equalTo(id))
                .body("[0].startDate", equalTo("2021-11-07"))
                .body("[0].endDate", equalTo("2021-11-09"))
                .body("[0].userId", equalTo("10005"));    
    }
}
