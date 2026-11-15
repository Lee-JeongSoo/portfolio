package com.example.portfolio.project;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 프로젝트 목록.
     * ?track=backend 처럼 포트폴리오 버전으로, ?tech=Spring Boot 처럼 기술 이름으로 걸러낼 수 있다.
     */
    @GetMapping
    public List<ProjectResponse> list(@RequestParam(required = false) String tech,
                                      @RequestParam(required = false) String track) {
        return projectService.findAll(tech, track);
    }

    @GetMapping("/{id}")
    public ProjectResponse get(@PathVariable Long id) {
        return projectService.findById(id);
    }
}
