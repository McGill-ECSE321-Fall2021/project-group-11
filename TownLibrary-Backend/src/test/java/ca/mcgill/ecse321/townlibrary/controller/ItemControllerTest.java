package ca.mcgill.ecse321.townlibrary.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemControllerTest {
	
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

}
