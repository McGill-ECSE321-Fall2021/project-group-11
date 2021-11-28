package ca.mcgill.ecse321.townlibrary.controller;

import java.sql.Timestamp;
import ca.mcgill.ecse321.townlibrary.dto.TransactionDTO;
import ca.mcgill.ecse321.townlibrary.model.Status;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


// Heavily inspired by Paul's integration testing files

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
        private WebApplicationContext webApplicationContext;

    private int idLibrarian;
    private int idUser;

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

        this.idUser = given()
            .param("password", "Jhon4444")
            .param("email", "jhon@email.com")
            .param("address", "99 boulevard of broken dreams")
            .param("name", "Jhon")
            .param("library", "10005")
            .when().post("/online-members/jhon")
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
        when().get("/transactions/" + idLibrarian)
            .then()
            .statusCode(200)
            .body("$", empty());

        // Since empty, any transaction search should return error
        when().get("/transactions/" + idLibrarian + "/0")
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
            .param("transactionType", TransactionType.books)
            .when().post("/transactions/" + this.idLibrarian)
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);

        assertThat(dto.startDate, equalTo(expectedStartDate.getTime()));
        assertThat(dto.endDate, equalTo(expectedEndDate.getTime()));
        assertThat(dto.userId, equalTo(this.idLibrarian));

        final int id = dto.id;

            dto = when().get("/transactions/" + this.idLibrarian + "/" + id)
                .then().statusCode(200)
                .body("id", equalTo(id))
                .extract().response().as(TransactionDTO.class);

        assertThat(dto.startDate, equalTo(expectedStartDate.getTime()));
        assertThat(dto.endDate, equalTo(expectedEndDate.getTime()));
        assertThat(dto.userId, equalTo(this.idLibrarian));

            dto = when().get("/transactions/" + this.idLibrarian)
                .then().statusCode(200)
                .body("size()", equalTo(1))
                .extract().response().as(TransactionDTO[].class)[0];

        assertThat(dto.startDate, equalTo(expectedStartDate.getTime()));
        assertThat(dto.endDate, equalTo(expectedEndDate.getTime()));
        assertThat(dto.userId, equalTo(this.idLibrarian));
    }
    @Test
    public void testRenewTransactionValidItem(){
        TransactionDTO dto = given()
            .param("startDate", "2021-11-07T00:00:00")
            .param("endDate", "2021-11-09T00:00:00")
			.param("transactionType", TransactionType.books)
            .when().post("/transactions/" + this.idUser)
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);
        
        final int idTransaction = dto.id;
    			
    	final int idItem = given()
    			.param("name", "Dune")
    			.param("libraryId", "10005")
    			.post("/books/7")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	given()
    		.param("transactionId", idTransaction)
    		.when().put("/books/" + idItem + "/checkout")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(idItem))
    		.body("status", equalTo(Status.CHECKED_OUT.toString()));

        TransactionDTO renewedDto = given()
            .when().put("/transactions/" + dto.userId + "/" + dto.id )
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);

        assertThat(dto.endDate + 1000 * 86400 * 14, equalTo(renewedDto.endDate));
        assertThat(dto.type, equalTo(renewedDto.type));
        assertThat(dto.userId, equalTo(renewedDto.userId));
    }

    @Test
    public void testDeleteExistingTransaction(){
            TransactionDTO dto = given()
            .param("startDate", "2021-11-07T00:00:00")
            .param("endDate", "2021-11-09T00:00:00")
			.param("transactionType", TransactionType.books)
            .when().post("/transactions/" + this.idUser)
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);
        
            final int idTransaction = dto.id;

            final int idItem = given()
    			.param("name", "Dune")
    			.param("libraryId", "10005")
    			.post("/books/7")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	given()
    		.param("transactionId", idTransaction)
    		.when().put("/books/" + idItem + "/checkout")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(idItem))
    		.body("status", equalTo(Status.CHECKED_OUT.toString()));

            final Boolean deleted = given()
                .when().delete("/transactions/" + this.idUser + "/" + idTransaction)
                .then()
                .statusCode(200)
                .extract().response().as(Boolean.class);

            assertTrue(deleted);
    }
    @Test
    public void testDeleteNonExistantTransaction(){

            final int idTransaction = 200000;

            given()
                .when().delete("/transactions/" + this.idUser + "/" + idTransaction)
                .then()
                .statusCode(400);
    }
}
