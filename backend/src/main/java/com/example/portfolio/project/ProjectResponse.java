package com.example.portfolio.project;

import java.util.List;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        List<String> techStack,
        String githubUrl,
        String demoUrl,
        boolean featured
) {
    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getId(), p.getTitle(), p.getDescription(),
                List.copyOf(p.getTechStack()), p.getGithubUrl(), p.getDemoUrl(), p.isFeatured());
    }
}
