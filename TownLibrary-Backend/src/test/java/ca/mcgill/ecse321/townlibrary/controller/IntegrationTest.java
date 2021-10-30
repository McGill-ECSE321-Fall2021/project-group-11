package ca.mcgill.ecse321.townlibrary.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ca.mcgill.ecse321.townlibrary.dto.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTest {

    // This test is pretty ugly as it assumes some execution order (see all
    // the Order annotations), but we do this so we can have a nice
    // integrationTest task that is platform (and environment) independent.

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Order(1)
    public void testLibraryController() throws Exception {
        this.mvc.perform(get("/libraries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        this.mvc.perform(get("/libraries/0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NOT-FOUND-LIBRARY"));

        this.mvc.perform(post("/libraries/0")
                        .param("address", "300 Pepper Street"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

        this.mvc.perform(post("/libraries/0")
                        .param("address", "301 Pepper Street"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("DUP-LIBRARY"));

        this.mvc.perform(get("/libraries/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}"));

        this.mvc.perform(get("/libraries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"address\":\"300 Pepper Street\",\"headLibrarianId\":null}]"));
    }

    @Test
    @Order(2)
    public void testHeadLibrarianController() throws Exception {
        this.mvc.perform(get("/head-librarians"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        MvcResult r;
        HeadLibrarianDTO dto;
        r = this.mvc.perform(post("/head-librarians/Joe Schmoe")
                        .param("password", "jojo123")
                        .param("address", "410 Chili Street")
                        .param("library", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        dto = this.mapper.readValue(r.getResponse().getContentAsString(), HeadLibrarianDTO.class);
        Assertions.assertEquals("Joe Schmoe", dto.name);
        Assertions.assertEquals("410 Chili Street", dto.address);
        Assertions.assertEquals(0, dto.libraryId);

        this.mvc.perform(get("/head-librarians/" + dto.id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id));

        this.mvc.perform(get("/libraries/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.headLibrarianId").value(dto.id));

        this.mvc.perform(get("/head-librarians"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.id));

        this.mvc.perform(post("/auth/head-librarians/" + dto.id)
                        .param("password", "123"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mvc.perform(post("/auth/head-librarians/" + dto.id)
                        .param("password", "jojo123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id));
    }

//     @Test
//     @Order(3)
//     public void 
}
