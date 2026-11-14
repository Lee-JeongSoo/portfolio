package com.example.portfolio.stats;

import com.example.portfolio.contact.ContactService;
import com.example.portfolio.project.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatsController {

    private final VisitCounter visitCounter;
    private final ProjectService projectService;
    private final ContactService contactService;

    public StatsController(VisitCounter visitCounter, ProjectService projectService,
                           ContactService contactService) {
        this.visitCounter = visitCounter;
        this.projectService = projectService;
        this.contactService = contactService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "time", Instant.now().toString());
    }

    @GetMapping("/stats")
    public StatsResponse stats() {
        return new StatsResponse(visitCounter.current(), projectService.count(), contactService.count());
    }

    @PostMapping("/stats/visit")
    public StatsResponse visit() {
        long visits = visitCounter.increment();
        return new StatsResponse(visits, projectService.count(), contactService.count());
    }
}
