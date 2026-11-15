package com.example.portfolio.project;

import java.util.List;

/** API 응답 모양. 필드 순서와 이름은 resources/data/projects.json 과 맞춘다(id 제외). */
public record ProjectResponse(
        Long id,
        String title,
        String subtitle,
        String period,
        String teamSize,
        String role,
        String summary,
        List<String> highlights,
        List<String> techStack,
        List<Image> images,
        String githubUrl,
        String demoUrl
) {
    public record Image(String src, String caption) {
    }

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(
                p.getId(), p.getTitle(), p.getSubtitle(), p.getPeriod(), p.getTeamSize(), p.getRole(),
                p.getSummary(),
                List.copyOf(p.getHighlights()),
                List.copyOf(p.getTechStack()),
                p.getImages().stream().map(i -> new Image(i.getSrc(), i.getCaption())).toList(),
                p.getGithubUrl(), p.getDemoUrl());
    }
}
