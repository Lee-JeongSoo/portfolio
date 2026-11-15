package com.example.portfolio.project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 서버 시작 시 resources/data/projects.json 을 읽어 DB에 넣는다.
 * 프로젝트 내용을 바꾸려면 이 JSON 파일과 frontend/data/projects.json 을 같이 수정하면 된다.
 * (두 파일이 다르면 ProjectSeedSyncTest 가 실패한다)
 */
@Component
public class ProjectDataInitializer implements CommandLineRunner {

    static final String SEED_PATH = "data/projects.json";

    private final ProjectRepository repository;
    private final ObjectMapper objectMapper;

    public ProjectDataInitializer(ProjectRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws IOException {
        if (repository.count() > 0) {
            return;
        }
        List<ProjectSeed> seeds;
        try (InputStream in = new ClassPathResource(SEED_PATH).getInputStream()) {
            seeds = objectMapper.readValue(in, new TypeReference<>() {});
        }
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++) {
            projects.add(seeds.get(i).toEntity(i + 1));
        }
        repository.saveAll(projects);
    }
}
