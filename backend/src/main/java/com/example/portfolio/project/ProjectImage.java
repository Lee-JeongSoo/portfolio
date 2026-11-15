package com.example.portfolio.project;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** 프로젝트 이미지. src 는 프론트엔드 기준 상대 경로(예: images/projects/a.webp). */
@Embeddable
public class ProjectImage {

    @Column(nullable = false)
    private String src;

    private String caption;

    protected ProjectImage() {
    }

    public ProjectImage(String src, String caption) {
        this.src = src;
        this.caption = caption;
    }

    public String getSrc() { return src; }
    public String getCaption() { return caption; }
}
