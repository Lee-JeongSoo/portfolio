package com.example.portfolio.project;

import com.example.portfolio.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectResponse> findAll(String tech) {
        return repository.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> tech == null || tech.isBlank()
                        || p.getTechStack().stream().anyMatch(t -> t.equalsIgnoreCase(tech)))
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse findById(Long id) {
        return repository.findById(id)
                .map(ProjectResponse::from)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다: " + id));
    }

    public long count() {
        return repository.count();
    }
}
