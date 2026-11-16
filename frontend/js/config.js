/**
 * API 서버 주소 설정.
 * - 로컬에서 열면 http://localhost:8080 (Spring Boot 기본 포트)
 * - 배포된 사이트에서는 PRODUCTION_API_URL 을 사용
 *
 * 백엔드를 Render 등에 배포한 뒤 아래 주소를 실제 주소로 바꿔주세요.
 */
(function () {
  const PRODUCTION_API_URL = 'https://portfolio-api-vtwx.onrender.com';

  const isLocal = ['localhost', '127.0.0.1', ''].includes(location.hostname);

  // 사이트 최상위 주소 (이 파일이 js/config.js 에 있으므로 한 단계 위).
  // /backend/ 같은 하위 페이지에서도 images/, data/ 경로가 올바르게 잡히도록 기준으로 쓴다.
  const siteRoot = new URL('..', document.currentScript.src).href;

  window.PORTFOLIO_CONFIG = {
    apiBaseUrl: isLocal ? 'http://localhost:8080' : PRODUCTION_API_URL,
    siteRoot,
    // 이 페이지가 어느 버전인지 (<html data-track="infra"> 또는 "backend")
    track: document.documentElement.dataset.track || null,
    // 화면을 처음 그릴 때 API 응답을 기다리는 최대 시간(ms). 넘으면 내장 데이터로 먼저 보여준다.
    initialWaitMs: 2500,
    // 무료 서버가 잠에서 깨어나는 데 걸릴 수 있는 최대 시간(ms)
    coldStartTimeoutMs: 60000,
    // API에 연결할 수 없을 때 사용할 프로젝트 데이터 (backend/src/main/resources/data/projects.json 과 같은 내용).
    // siteRoot 기준 경로
    fallbackProjectsUrl: 'data/projects.json',
    // 기본 타이핑 문구. 페이지에서 <span id="typing" data-words="A|B|C"> 로 바꿀 수 있다.
    typingWords: ['Infra & System Engineer', 'Linux · 가상화 인프라 운영'],
  };
})();
