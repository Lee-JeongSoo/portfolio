package com.example.portfolio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * 백엔드 초기 데이터와 프론트엔드 예비 데이터(서버가 잠들었을 때 사용)가 같은지 확인한다.
 * 저장소 전체가 있을 때(로컬, GitHub Actions)만 실행된다.
 */
class ProjectSeedSyncTest {

    private static final Path FRONTEND_COPY = Path.of("..", "frontend", "data", "projects.json");

    @Test
    void frontendFallbackMatchesBackendSeed() throws Exception {
        assumeTrue(Files.exists(FRONTEND_COPY), "frontend/ 폴더가 없는 환경이라 건너뜀");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode backend;
        try (InputStream in = new ClassPathResource("data/projects.json").getInputStream()) {
            backend = mapper.readTree(in);
        }
        JsonNode frontend = mapper.readTree(FRONTEND_COPY.toFile());

        assertThat(frontend)
                .as("backend/src/main/resources/data/projects.json 과 frontend/data/projects.json 내용이 같아야 합니다")
                .isEqualTo(backend);
    }

    @Test
    void everyImageReferencedInSeedExistsInFrontend() throws Exception {
        assumeTrue(Files.exists(FRONTEND_COPY), "frontend/ 폴더가 없는 환경이라 건너뜀");

        JsonNode projects = new ObjectMapper().readTree(FRONTEND_COPY.toFile());
        for (JsonNode project : projects) {
            for (JsonNode image : project.path("images")) {
                Path file = Path.of("..", "frontend").resolve(image.get("src").asText());
                assertThat(file).as("이미지 파일이 없습니다: " + file).exists();
            }
        }
    }
}
