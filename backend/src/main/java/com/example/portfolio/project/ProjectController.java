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

    /** 전체 프로젝트 목록. ?tech=Spring 처럼 기술 이름으로 필터링할 수 있다. */
    @GetMapping
    public List<ProjectResponse> list(@RequestParam(required = false) String tech) {
        return projectService.findAll(tech);
    }

    @GetMapping("/{id}")
    public ProjectResponse get(@PathVariable Long id) {
        return projectService.findById(id);
    }
}
