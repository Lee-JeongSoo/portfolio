package com.example.portfolio;

import com.example.portfolio.contact.ContactMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ContactMessageRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void savesValidMessage() throws Exception {
        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"김철수","email":"chulsoo@example.com","message":"포트폴리오 잘 봤습니다. 연락 부탁드려요!"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));

        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void rejectsInvalidInputWithFieldErrors() throws Exception {
        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","email":"not-an-email","message":"짧음"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors.email", is("올바른 이메일 형식이 아닙니다.")))
                .andExpect(jsonPath("$.fieldErrors.message", notNullValue()));

        assertThat(repository.count()).isZero();
    }

    @Test
    void silentlyDropsHoneypotSubmissions() throws Exception {
        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"bot","email":"bot@example.com","message":"buy cheap stuff now!!!","website":"http://spam"}
                                """))
                .andExpect(status().isCreated());

        assertThat(repository.count()).isZero();
    }

    @Test
    void rejectsMalformedJson() throws Exception {
        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not json"))
                .andExpect(status().isBadRequest());
    }
}
