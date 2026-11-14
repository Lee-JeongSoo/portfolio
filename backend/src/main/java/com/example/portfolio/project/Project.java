package com.example.portfolio.project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_tech", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "position")
    @Column(name = "tech")
    private List<String> techStack = new ArrayList<>();

    private String githubUrl;
    private String demoUrl;
    private boolean featured;
    private int sortOrder;

    protected Project() {
    }

    public Project(String title, String description, List<String> techStack,
                   String githubUrl, String demoUrl, boolean featured, int sortOrder) {
        this.title = title;
        this.description = description;
        this.techStack = new ArrayList<>(techStack);
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.featured = featured;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getTechStack() { return techStack; }
    public String getGithubUrl() { return githubUrl; }
    public String getDemoUrl() { return demoUrl; }
    public boolean isFeatured() { return featured; }
    public int getSortOrder() { return sortOrder; }
}
