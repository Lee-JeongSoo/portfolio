package com.example.portfolio.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** 예시 프로젝트 데이터. 실제 내용으로 바꿔서 사용하세요. */
@Component
public class ProjectDataInitializer implements CommandLineRunner {

    private final ProjectRepository repository;

    public ProjectDataInitializer(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new Project("Portfolio Website",
                        "지금 보고 계신 포트폴리오 사이트입니다. 정적 프론트엔드(GitHub Pages)와 Spring Boot REST API를 분리해 구성했고, API가 응답하지 않을 때는 내장 데이터로 동작하도록 설계했습니다.",
                        List.of("Spring Boot", "JPA", "JavaScript", "GitHub Actions"),
                        "https://github.com/Lee-JeongSoo/portfolio", null, true, 1),
                new Project("ShopFlow - 쇼핑몰 주문 시스템",
                        "상품 조회부터 장바구니, 주문, 결제까지 이어지는 이커머스 백엔드입니다. 재고 차감 시 동시성 문제를 비관적 락으로 해결했고, 주문 조회 쿼리를 개선해 응답 시간을 60% 줄였습니다.",
                        List.of("Java", "Spring Boot", "JPA", "MySQL", "Redis"),
                        "https://github.com/your-id/shopflow", null, true, 2),
                new Project("StudyLog - 스터디 기록 서비스",
                        "스터디 그룹의 일정과 학습 기록을 공유하는 웹 서비스입니다. JWT 기반 인증과 역할별 권한 관리를 구현했고, Docker로 개발·배포 환경을 통일했습니다.",
                        List.of("Spring Boot", "Spring Security", "JWT", "Docker", "JavaScript"),
                        "https://github.com/your-id/studylog", null, false, 3),
                new Project("WeatherNow - 날씨 대시보드",
                        "외부 날씨 API를 받아 지역별 날씨와 예보를 보여주는 대시보드입니다. 응답을 캐싱해 외부 API 호출 횟수를 줄였고, 반응형 UI로 모바일에서도 보기 편하게 만들었습니다.",
                        List.of("JavaScript", "HTML/CSS", "REST API"),
                        "https://github.com/your-id/weathernow", null, false, 4)
        ));
    }
}
