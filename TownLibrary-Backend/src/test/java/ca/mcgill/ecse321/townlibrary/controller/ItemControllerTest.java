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

import java.sql.Timestamp;

import ca.mcgill.ecse321.townlibrary.dto.TransactionDTO;
import ca.mcgill.ecse321.townlibrary.model.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemControllerTest {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	private int idUser;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        post("/libraries/321?address=99 boulevard of broken dreams");
        
        this.idUser = given()
                .param("password", "Jhon4444")
                .param("email", "jhon@email.com")
                .param("address", "99 boulevard of broken dreams")
                .param("name", "Jhon")
                .param("library", "321")
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
    public void testNoItems() {
    	when().get("/archives")
    		.then()
    		.statusCode(200)
    		.body("$", empty());
    	
    	when().get("/archives/0")
	        .then()
	        .statusCode(400)
	        .body(equalTo("ARCHIVE-NOT-FOUND"));
    	
    	when().get("/newspapers")
			.then()
			.statusCode(200)
			.body("$", empty());
	
		when().get("/newspapers/0")
		    .then()
		    .statusCode(400)
		    .body(equalTo("NEWSPAPER-NOT-FOUND"));
		
		when().get("/books")
			.then()
			.statusCode(200)
			.body("$", empty());
	
		when().get("/books/0")
			.then()
			.statusCode(400)
			.body(equalTo("BOOK-NOT-FOUND"));
		
		when().get("/movies")
			.then()
			.statusCode(200)
			.body("$", empty());
		
		when().get("/movies/0")
			.then()
			.statusCode(400)
			.body(equalTo("MOVIE-NOT-FOUND"));
		
		when().get("/musicalbums")
			.then()
			.statusCode(200)
			.body("$", empty());
		
		when().get("/musicalbums/0")
			.then()
			.statusCode(400)
			.body(equalTo("MUSIC-ALBUM-NOT-FOUND"));
    }
    
    @Test
    public void testCreateAndQueryArchive() {
    	final int id = given()
    			.param("name", "Records2021")
    			.param("libraryId", "321")
    			.post("/archives/5")
    			.then().statusCode(200)
    			.body("name", equalTo("Records2021"))
    			.body("libraryId", equalTo(321))
                .extract().response().body().path("id");
			
    	when().get("/archives/" + id)
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("name", equalTo("Records2021"))
    		.body("libraryId", equalTo(321));
    	
    	when().get("/archives")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(id))
			.body("[0].name", equalTo("Records2021"))
			.body("[0].libraryId", equalTo(321));
    }
    
    @Test
    public void testCreateAndQueryNewspaper() {
    	final int id = given()
    			.param("name", "Gazette")
    			.param("libraryId", "321")
    			.post("/newspapers/6")
    			.then().statusCode(200)
    			.body("name", equalTo("Gazette"))
    			.body("libraryId", equalTo(321))
                .extract().response().body().path("id");
			
    	when().get("/newspapers/" + id)
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("name", equalTo("Gazette"))
    		.body("libraryId", equalTo(321));
    	
    	when().get("/newspapers")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(id))
			.body("[0].name", equalTo("Gazette"))
			.body("[0].libraryId", equalTo(321));
    }
    
    @Test
    public void testCreateAndQueryBook() {
    	final int id = given()
    			.param("name", "Dune")
    			.param("libraryId", "321")
    			.post("/books/7")
    			.then().statusCode(200)
    			.body("name", equalTo("Dune"))
    			.body("libraryId", equalTo(321))
                .extract().response().body().path("id");
			
    	when().get("/books/" + id)
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("name", equalTo("Dune"))
    		.body("libraryId", equalTo(321));
    	
    	when().get("/books")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(id))
			.body("[0].name", equalTo("Dune"))
			.body("[0].libraryId", equalTo(321));
    }
    
    @Test
    public void testCreateAndQueryMovie() {
    	final int id = given()
    			.param("name", "Interstellar")
    			.param("libraryId", "321")
    			.post("/movies/8")
    			.then().statusCode(200)
    			.body("name", equalTo("Interstellar"))
    			.body("libraryId", equalTo(321))
                .extract().response().body().path("id");
			
    	when().get("/movies/" + id)
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("name", equalTo("Interstellar"))
    		.body("libraryId", equalTo(321));
    	
    	when().get("/movies")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(id))
			.body("[0].name", equalTo("Interstellar"))
			.body("[0].libraryId", equalTo(321));
    }
    
    @Test
    public void testCreateAndQueryMusicAlbum() {
    	final int id = given()
    			.param("name", "Evolve")
    			.param("libraryId", "321")
    			.post("/musicalbums/9")
    			.then().statusCode(200)
    			.body("name", equalTo("Evolve"))
    			.body("libraryId", equalTo(321))
                .extract().response().body().path("id");
			
    	when().get("/musicalbums/" + id)
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("name", equalTo("Evolve"))
    		.body("libraryId", equalTo(321));
    	
    	when().get("/musicalbums")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(id))
			.body("[0].name", equalTo("Evolve"))
			.body("[0].libraryId", equalTo(321));
    }
    
    @Test
    public void testReserveBook() {
    	final int id = given()
    			.param("name", "Dune")
    			.param("libraryId", "321")
    			.post("/books/7")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	when().put("/books/" + id + "/reserve")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("status", equalTo(Status.RESERVED.toString()));
    }
    
    @Test
    public void testReserveMovie() {
    	final int id = given()
    			.param("name", "Interstellar")
    			.param("libraryId", "321")
    			.post("/movies/8")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	when().put("/movies/" + id + "/reserve")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("status", equalTo(Status.RESERVED.toString()));
    }
    
    @Test
    public void testReserveMusicAlbum() {
    	final int id = given()
    			.param("name", "Evolve")
    			.param("libraryId", "321")
    			.post("/musicalbums/9")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	when().put("/musicalbums/" + id + "/reserve")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(id))
    		.body("status", equalTo(Status.RESERVED.toString()));
    }
    
    @Test
    public void testCheckoutBook() {
    	
        TransactionDTO dto = given()
            .param("startDate", "2021-11-07T00:00:00")
            .param("endDate", "2021-11-09T00:00:00")
            .param("userId", this.idUser)
            .when().post("/transactions/0")
            .then()
            .statusCode(200)
            .extract().response().as(TransactionDTO.class);
        
        final int idTransaction = dto.id;
    			
    	final int idItem = given()
    			.param("name", "Dune")
    			.param("libraryId", "321")
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
    }
    
    @Test
    public void testCheckoutMovie() {
    	TransactionDTO dto = given()
                .param("startDate", "2021-11-07T00:00:00")
                .param("endDate", "2021-11-09T00:00:00")
                .param("userId", this.idUser)
                .when().post("/transactions/0")
                .then()
                .statusCode(200)
                .extract().response().as(TransactionDTO.class);
            
            final int idTransaction = dto.id;

    	final int idItem = given()
    			.param("name", "Interstellar")
    			.param("libraryId", "321")
    			.post("/movies/8")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	given()
    		.param("transactionId", idTransaction)
    		.when().put("/movies/" + idItem + "/checkout")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(idItem))
    		.body("status", equalTo(Status.CHECKED_OUT.toString()));
    }
    
    @Test
    public void testCheckoutMusicAlbum() {
    	TransactionDTO dto = given()
                .param("startDate", "2021-11-07T00:00:00")
                .param("endDate", "2021-11-09T00:00:00")
                .param("userId", this.idUser)
                .when().post("/transactions/0")
                .then()
                .statusCode(200)
                .extract().response().as(TransactionDTO.class);
            
            final int idTransaction = dto.id;

    	final int idItem = given()
    			.param("name", "Evolve")
    			.param("libraryId", "321")
    			.post("/musicalbums/9")
    			.then().statusCode(200)
    			.body("status", equalTo(Status.AVAILABLE.toString()))
                .extract().response().body().path("id");
    	
    	given()
    		.param("transactionId", idTransaction)
    		.when().put("/musicalbums/" + idItem + "/checkout")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(idItem))
    		.body("status", equalTo(Status.CHECKED_OUT.toString()));
    }
    
}