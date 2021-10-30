package ca.mcgill.ecse321.townlibrary.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
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
@Transactional  // <-- this is key*
public class IntegrationTest {

    // *because there's no way a random "this is key" make sense...
    //
    // All tests are run as if the database is reset (deleteAll).

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void foo() {
        when().get("/libraries")
            .then()
            .statusCode(200)
            .body("$", empty());
    }

    @Test
    public void testCreateLibrary() {
        when().post("/libraries/0?address=300 Pepper Street")
            .then()
            .statusCode(200)
            .body("id", equalTo(0))
            .body("address", equalTo("300 Pepper Street"))
            .body("headLibrarianId", is(nullValue()));
    }

    @Test
    public void doItAgain() {
        when().post("/libraries/0?address=300 Pepper Street")
            .then()
            .statusCode(200)
            .body("id", equalTo(0))
            .body("address", equalTo("300 Pepper Street"))
            .body("headLibrarianId", is(nullValue()));

        // when().post("/libraries/0?address=300 Pepper Street")
        //     .then()
        //     .statusCode(200)
        //     .body("id", equalTo(0))
        //     .body("address", equalTo("300 Pepper Street"))
        //     .body("headLibrarianId", is(nullValue()));
    }

//     @Test
//     public void testLibraryController() throws Exception {
//         this.mvc.perform(get("/libraries"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("[]"));

//         this.mvc.perform(get("/libraries/0"))
//                 .andDo(print())
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("NOT-FOUND-LIBRARY"));

//         this.mvc.perform(post("/libraries/0")
//                         .param("address", "300 Pepper Street"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

//         this.mvc.perform(post("/libraries/0")
//                         .param("address", "301 Pepper Street"))
//                 .andDo(print())
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("DUP-LIBRARY"));

//         this.mvc.perform(get("/libraries/0"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

//         this.mvc.perform(get("/libraries"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("[{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}]"));
// System.out.println("DONE1");
//     }

//     @Test
//     public void doItAgain() throws Exception {
//         this.mvc.perform(get("/libraries"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("[]"));

//         this.mvc.perform(get("/libraries/0"))
//                 .andDo(print())
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("NOT-FOUND-LIBRARY"));

//         this.mvc.perform(post("/libraries/0")
//                         .param("address", "300 Pepper Street"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

//         this.mvc.perform(post("/libraries/0")
//                         .param("address", "301 Pepper Street"))
//                 .andDo(print())
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("DUP-LIBRARY"));

//         this.mvc.perform(get("/libraries/0"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

//         this.mvc.perform(get("/libraries"))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("[{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}]"));
// System.out.println("DONE2");
//     }

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