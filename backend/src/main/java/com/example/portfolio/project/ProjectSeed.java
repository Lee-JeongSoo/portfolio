package com.example.portfolio.project;

import java.util.List;

/** resources/data/projects.json 의 한 항목. */
public record ProjectSeed(
        String title,
        String subtitle,
        String period,
        String teamSize,
        String role,
        String summary,
        List<String> highlights,
        List<String> techStack,
        List<ImageSeed> images,
        String githubUrl,
        String demoUrl
) {
    public record ImageSeed(String src, String caption) {
    }

    public Project toEntity(int sortOrder) {
        return new Project(title, subtitle, period, teamSize, role, summary,
                orEmpty(highlights), orEmpty(techStack),
                orEmpty(images).stream().map(i -> new ProjectImage(i.src(), i.caption())).toList(),
                githubUrl, demoUrl, sortOrder);
    }

    private static <T> List<T> orEmpty(List<T> list) {
        return list == null ? List.of() : list;
    }
}
