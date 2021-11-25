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
import static org.hamcrest.Matchers.*;




// Heavily inspired by Paul's integration testing files

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EventControllerTest {

        @Autowired
        private WebApplicationContext webApplicationContext;

        @BeforeEach
        public void setup() {
            RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

            post("/libraries/10001?address=sad street");
        }

        @AfterEach
        public void cleanup() {
            RestAssuredMockMvc.reset();
        }

        @Test
        public void testStartEvents() {
            // Make sure its empty
            when().get("/events")
                .then().statusCode(200);

            // Since empty, any event search should return error
            when().get("/events/0")
                .then().statusCode(400)
                .body(equalTo("NOT-FOUND-EVENT"));
        }

        @Test
        public void testGetEvent() {
            final int id = given()
            .param("lib", "10001")
            .post("/events/event1")
            .then().statusCode(200)
            .body("libId", equalTo(10001))
            .extract().response().body().path("id");

            when().get("/events/" + id)
                .then().statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("event1"))
                .body("libId", equalTo(10001));
        }

        @Test
        public void testCreateEventAndQuery() {
            final int id = given()
                .param("lib", "10001")
                .post("/events/event1")
                .then().statusCode(200)
                .body("libId", equalTo(10001))
                .extract().response().body().path("id");

                when().get("/events/" + id)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(id))
                    .body("libId", equalTo(10001));

                when().get("/events")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(1))
                    .body("[0].id", equalTo(id))
                    .body("[0].libId", equalTo(10001));
        }       
}
