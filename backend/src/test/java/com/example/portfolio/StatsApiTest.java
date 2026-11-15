package com.example.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatsApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void healthIsUp() throws Exception {
        mvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void visitIncrementsCounter() throws Exception {
        String before = mvc.perform(get("/api/stats")).andReturn().getResponse().getContentAsString();
        long visitsBefore = Long.parseLong(before.replaceAll(".*\"visits\":(\\d+).*", "$1"));

        mvc.perform(post("/api/stats/visit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visits", is((int) visitsBefore + 1)))
                .andExpect(jsonPath("$.projects", is(5)));
    }
}
