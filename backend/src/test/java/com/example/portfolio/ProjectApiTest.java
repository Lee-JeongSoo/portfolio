package com.example.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listsAllSeededProjectsInJsonOrder() throws Exception {
        mvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title", is("Portfolio")))
                .andExpect(jsonPath("$[1].title", is("척척약사")))
                .andExpect(jsonPath("$[1].highlights", hasSize(4)))
                .andExpect(jsonPath("$[1].images[0].src", is("images/projects/pill-ui.webp")))
                .andExpect(jsonPath("$[1].images[1].caption", is("시스템 구성도")))
                .andExpect(jsonPath("$[4].title", is("펀칭")));
    }

    @Test
    void filtersByTrack() throws Exception {
        mvc.perform(get("/api/projects").param("track", "infra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].title", not(hasItem("Portfolio"))));

        mvc.perform(get("/api/projects").param("track", "BACKEND"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].tracks", contains("backend")));
    }

    @Test
    void filtersByTechIgnoringCase() throws Exception {
        mvc.perform(get("/api/projects").param("tech", "kotlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].title", not(hasItem("상쓰리"))));
    }

    @Test
    void combinesTrackAndTechFilters() throws Exception {
        mvc.perform(get("/api/projects").param("track", "infra").param("tech", "Spring Boot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("척척약사")));
    }

    @Test
    void returnsProjectById() throws Exception {
        mvc.perform(get("/api/projects/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtitle", is("사진 촬영 기반 알약 식별 서비스")));
    }

    @Test
    void returns404ForUnknownProject() throws Exception {
        mvc.perform(get("/api/projects/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void allowsCorsFromConfiguredOrigin() throws Exception {
        mvc.perform(options("/api/projects")
                        .header("Origin", "http://localhost:5500")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5500"));
    }

    @Test
    void rejectsCorsFromUnknownOrigin() throws Exception {
        mvc.perform(options("/api/projects")
                        .header("Origin", "https://evil.example.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden());
    }
}
