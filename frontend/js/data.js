/**
 * API 서버에 연결할 수 없을 때 보여줄 내장 데이터.
 * 백엔드의 ProjectDataInitializer.java 와 같은 내용으로 맞춰 두세요.
 */
window.FALLBACK_PROJECTS = [
  {
    id: 1,
    title: 'Portfolio Website',
    description: '지금 보고 계신 포트폴리오 사이트입니다. 정적 프론트엔드(GitHub Pages)와 Spring Boot REST API를 분리해 구성했고, API가 응답하지 않을 때는 내장 데이터로 동작하도록 설계했습니다.',
    techStack: ['Spring Boot', 'JPA', 'JavaScript', 'GitHub Actions'],
    githubUrl: 'https://github.com/Lee-JeongSoo/portfolio',
    demoUrl: null,
    featured: true,
  },
  {
    id: 2,
    title: 'ShopFlow - 쇼핑몰 주문 시스템',
    description: '상품 조회부터 장바구니, 주문, 결제까지 이어지는 이커머스 백엔드입니다. 재고 차감 시 동시성 문제를 비관적 락으로 해결했고, 주문 조회 쿼리를 개선해 응답 시간을 60% 줄였습니다.',
    techStack: ['Java', 'Spring Boot', 'JPA', 'MySQL', 'Redis'],
    githubUrl: 'https://github.com/your-id/shopflow',
    demoUrl: null,
    featured: true,
  },
  {
    id: 3,
    title: 'StudyLog - 스터디 기록 서비스',
    description: '스터디 그룹의 일정과 학습 기록을 공유하는 웹 서비스입니다. JWT 기반 인증과 역할별 권한 관리를 구현했고, Docker로 개발·배포 환경을 통일했습니다.',
    techStack: ['Spring Boot', 'Spring Security', 'JWT', 'Docker', 'JavaScript'],
    githubUrl: 'https://github.com/your-id/studylog',
    demoUrl: null,
    featured: false,
  },
  {
    id: 4,
    title: 'WeatherNow - 날씨 대시보드',
    description: '외부 날씨 API를 받아 지역별 날씨와 예보를 보여주는 대시보드입니다. 응답을 캐싱해 외부 API 호출 횟수를 줄였고, 반응형 UI로 모바일에서도 보기 편하게 만들었습니다.',
    techStack: ['JavaScript', 'HTML/CSS', 'REST API'],
    githubUrl: 'https://github.com/your-id/weathernow',
    demoUrl: null,
    featured: false,
  },
];
