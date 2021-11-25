package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Timestamp;
import ca.mcgill.ecse321.townlibrary.dto.TransactionDTO;
import ca.mcgill.ecse321.townlibrary.model.TransactionType;

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
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


// Heavily inspired by Paul's integration testing files

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
        private WebApplicationContext webApplicationContext;

    private int idLibrarian;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        post("/libraries/10005?address=sad street 2");

        // Probably safe to assume we also need an user
        this.idLibrarian = given()
            .param("password", "jojo123")
            .param("address", "410 Chili Street")
            .param("library", "10005")
            .post("/head-librarians/Joe Schmoe")
            .then()
            .extract()
            .response().body().path("id");
    }

    @AfterEach
    public void cleanup() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void testStartTransactions() {
        // Make sure its empty
        when().get("/transactions")
            .then()
            .statusCode(200)
            .body("$", empty());

        // Since empty, any transaction search should return error
        when().get("/transactions/1")
            .then().statusCode(400)
            .body(equalTo("NOT-FOUND-TRANSACTION"));
    }

    @Test
    public void testCreateTransactionAndQuery() {
        final Timestamp expectedStartDate = Timestamp.valueOf("2021-11-07 00:00:00");
        final Timestamp expectedEndDate = Timestamp.valueOf("2021-11-09 00:00:00");

        // the dto conversion is needed because timestamps strings are awkward
        TransactionDTO dto = given()
            .param("startDate", "2021-11-07T00:00:00")
            .param("endDate", "2021-11-09T00:00:00")
            .param("userId", this.idLibrarian)
            .param("transactionType", TransactionType.Item)
            .when().post("/transactions/0")
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);

        assertThat(dto.startDate, equalTo(expectedStartDate));
        assertThat(dto.endDate, equalTo(expectedEndDate));
        assertThat(dto.userId, equalTo(this.idLibrarian));

        final int id = dto.id;

            dto = when().get("/transactions/" + id)
                .then().statusCode(200)
                .body("id", equalTo(id))
                .extract().response().as(TransactionDTO.class);

        assertThat(dto.startDate, equalTo(expectedStartDate));
        assertThat(dto.endDate, equalTo(expectedEndDate));
        assertThat(dto.userId, equalTo(this.idLibrarian));

            dto = when().get("/transactions/")
                .then().statusCode(200)
                .body("size()", equalTo(1))
                .extract().response().as(TransactionDTO[].class)[0];

        assertThat(dto.startDate, equalTo(expectedStartDate));
        assertThat(dto.startDate, equalTo(expectedStartDate));
        assertThat(dto.endDate, equalTo(expectedEndDate));
        assertThat(dto.userId, equalTo(this.idLibrarian));
    }
}
