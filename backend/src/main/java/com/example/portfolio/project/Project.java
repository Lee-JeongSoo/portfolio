package com.example.portfolio.project;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String subtitle;
    private String period;
    private String teamSize;
    private String role;

    @Column(length = 1000)
    private String summary;

    // 컬렉션은 LAZY로 두고 BatchSize로 묶어서 조회해 N+1 쿼리를 줄인다.
    @ElementCollection
    @BatchSize(size = 50)
    @CollectionTable(name = "project_highlight", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "position")
    @Column(name = "highlight", length = 500)
    private List<String> highlights = new ArrayList<>();

    @ElementCollection
    @BatchSize(size = 50)
    @CollectionTable(name = "project_tech", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "position")
    @Column(name = "tech")
    private List<String> techStack = new ArrayList<>();

    @ElementCollection
    @BatchSize(size = 50)
    @CollectionTable(name = "project_image", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "position")
    private List<ProjectImage> images = new ArrayList<>();

    private String githubUrl;
    private String demoUrl;
    private int sortOrder;

    protected Project() {
    }

    public Project(String title, String subtitle, String period, String teamSize, String role,
                   String summary, List<String> highlights, List<String> techStack,
                   List<ProjectImage> images, String githubUrl, String demoUrl, int sortOrder) {
        this.title = title;
        this.subtitle = subtitle;
        this.period = period;
        this.teamSize = teamSize;
        this.role = role;
        this.summary = summary;
        this.highlights = new ArrayList<>(highlights);
        this.techStack = new ArrayList<>(techStack);
        this.images = new ArrayList<>(images);
        this.githubUrl = githubUrl;
        this.demoUrl = demoUrl;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getPeriod() { return period; }
    public String getTeamSize() { return teamSize; }
    public String getRole() { return role; }
    public String getSummary() { return summary; }
    public List<String> getHighlights() { return highlights; }
    public List<String> getTechStack() { return techStack; }
    public List<ProjectImage> getImages() { return images; }
    public String getGithubUrl() { return githubUrl; }
    public String getDemoUrl() { return demoUrl; }
    public int getSortOrder() { return sortOrder; }
}
