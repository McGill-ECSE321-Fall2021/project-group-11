package ca.mcgill.ecse321.townlibrary.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.dto.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.hamcrest.Matchers.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HeadLibrarianControllerTest {

    // Because class is marked @Transactional, the database is rolled-back
    // before each test case (as if it was reset).

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
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

    // @Test
    // @Order(2)
    // public void testHeadLibrarianController() throws Exception {
    //     this.mvc.perform(get("/head-librarians"))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(content().string("[]"));

    //     MvcResult r;
    //     HeadLibrarianDTO dto;
    //     r = this.mvc.perform(post("/head-librarians/Joe Schmoe")
    //                     .param("password", "jojo123")
    //                     .param("address", "410 Chili Street")
    //                     .param("library", "0"))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andReturn();

    //     dto = this.mapper.readValue(r.getResponse().getContentAsString(), HeadLibrarianDTO.class);
    //     Assertions.assertEquals("Joe Schmoe", dto.name);
    //     Assertions.assertEquals("410 Chili Street", dto.address);
    //     Assertions.assertEquals(0, dto.libraryId);

    //     this.mvc.perform(get("/head-librarians/" + dto.id))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.id").value(dto.id));

    //     this.mvc.perform(get("/libraries/0"))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.headLibrarianId").value(dto.id));

    //     this.mvc.perform(get("/head-librarians"))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$[0].id").value(dto.id));

    //     this.mvc.perform(post("/auth/head-librarians/" + dto.id)
    //                     .param("password", "123"))
    //             .andDo(print())
    //             .andExpect(status().isBadRequest());

    //     this.mvc.perform(post("/auth/head-librarians/" + dto.id)
    //                     .param("password", "jojo123"))
    //             .andDo(print())
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.id").value(dto.id));
    // }
}