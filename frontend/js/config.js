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

  window.PORTFOLIO_CONFIG = {
    apiBaseUrl: isLocal ? 'http://localhost:8080' : PRODUCTION_API_URL,
    // 화면을 처음 그릴 때 API 응답을 기다리는 최대 시간(ms). 넘으면 내장 데이터로 먼저 보여준다.
    initialWaitMs: 2500,
    // 무료 서버가 잠에서 깨어나는 데 걸릴 수 있는 최대 시간(ms)
    coldStartTimeoutMs: 60000,
    typingWords: ['Backend Developer', 'Spring Boot 개발자', '문제를 끝까지 파고드는 개발자'],
  };
})();
