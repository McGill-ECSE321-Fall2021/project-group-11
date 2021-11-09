package ca.mcgill.ecse321.townlibrary.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

        post("/libraries/100?address=99 boulevard of broken dreams");
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
    public void testQueryArchive() {
	
    	when().get("/archives/5")
    		.then()
    		.statusCode(200)
    		.body("id", equalTo(5));
    	
    	when().get("/archives")
			.then()
			.statusCode(200)
			.body("size()", equalTo(1))
			.body("[0].id", equalTo(5));
    }
//    
//    @Test
//    public void testQueryNewspaper() {
//    	when().post("/newspapers/11")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(11));
//    		
//    	when().get("/newspapers/11")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(11));
//    	
//    	when().get("/newspapers")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].id", equalTo(11));
//    }
//    
//    @Test
//    public void testQueryBook() {
//    	when().post("/books/22")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(22));
//    		
//    	when().get("/books/0")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(22));
//    	
//    	when().get("/books")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].id", equalTo(22));
//    }
//    
//    @Test
//    public void testQueryMovie() {
//    	when().post("/movies/33")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(33));
//    		
//    	when().get("/movies/33")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(33));
//    	
//    	when().get("/movies")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].id", equalTo(33));
//    }
//    
//    @Test
//    public void testQueryMusicAlbum() {
//    	when().post("/musicalbums/44")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(44));
//    		
//    	when().get("/musicalbums/44")
//    		.then()
//    		.statusCode(200)
//    		.body("id", equalTo(44));
//    	
//    	when().get("/musicalbums")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].id", equalTo(44));
//    }
    
//    @Test
//    public void testQueryArchiveByName() {
//    	when().post("/archives/byName/Records2021")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Records2021"));
//    		
//    	when().get("/archives/byName/Records2021")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Records2021"));
//    	
//    	when().get("/archives/byName")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].name", equalTo("Records2021"));
//    }
//    
//    @Test
//    public void testQueryNewspaperByName() {
//    	when().post("/newspapers/byName/Gazette")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Gazette"));
//    		
//    	when().get("/newspapers/byName/Gazette")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Gazette"));
//    	
//    	when().get("/newspapers/byName")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].name", equalTo("Gazette"));
//    }
//    
//    @Test
//    public void testQueryBookByName() {
//    	when().post("/books/byName/Dune")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Dune"));
//    		
//    	when().get("/books/byName/Dune")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Dune"));
//    	
//    	when().get("/books/byName")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].name", equalTo("Dune"));
//    }
//    
//    @Test
//    public void testQueryMovieByName() {
//    	when().post("/movies/byName/Interstellar")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Interstellar"));
//    		
//    	when().get("/movies/byName/Interstellar")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Interstellar"));
//    	
//    	when().get("/movies/byName")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].name", equalTo("Interstellar"));
//    }
//    
//    @Test
//    public void testQueryMusicAlbumByName() {
//    	when().post("/musicalbums/byName/Evolve")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Evolve"));
//    		
//    	when().get("/musicalbums/byName/Evolve")
//    		.then()
//    		.statusCode(200)
//    		.body("name", equalTo("Evolve"));
//    	
//    	when().get("/musicalbums/byName")
//			.then()
//			.statusCode(200)
//			.body("size()", equalTo(1))
//			.body("[0].name", equalTo("Evolve"));
//    }

}
