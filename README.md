# Portfolio

Spring Boot REST API와 Vanilla JS 프론트엔드로 만든 개발자 포트폴리오 사이트입니다.

```
GitHub Pages (frontend/)  ──fetch──▶  Spring Boot API (backend/, Render 배포)
   정적 HTML/CSS/JS                     /api/projects · /api/contact · /api/stats
```

API 서버가 잠들어 있거나 응답이 없으면 프론트엔드는 내장 데이터(`frontend/js/data.js`)로 화면을 그립니다.
그래서 서버 상태와 상관없이 포트폴리오는 항상 정상적으로 보입니다.

## 폴더 구조

```
portfolio/
├── frontend/                 # GitHub Pages로 배포되는 정적 사이트
│   ├── index.html
│   ├── css/style.css         # 디자인 토큰, 다크모드, 애니메이션
│   └── js/
│       ├── config.js         # API 주소 설정
│       ├── data.js           # API 연결 실패 시 사용할 내장 데이터
│       └── main.js
├── backend/                  # Spring Boot 3.5 / Java 21
│   ├── src/main/java/com/example/portfolio/
│   │   ├── project/          # 프로젝트 목록 API
│   │   ├── contact/          # 연락 폼 API (검증 + 스팸 방지)
│   │   ├── stats/            # 헬스체크, 방문자 수
│   │   ├── config/           # CORS 설정
│   │   └── common/           # 공통 예외 처리
│   ├── src/test/             # MockMvc 통합 테스트
│   └── Dockerfile
├── .github/workflows/
│   ├── deploy-frontend.yml   # frontend/ 변경 시 GitHub Pages 자동 배포
│   └── backend-ci.yml        # backend/ 변경 시 테스트 실행
└── render.yaml               # Render 배포 설정
```

## API

| Method | Path | 설명 |
|---|---|---|
| GET | `/api/projects` | 프로젝트 목록 (`?tech=Spring Boot` 로 필터링) |
| GET | `/api/projects/{id}` | 프로젝트 상세 |
| POST | `/api/contact` | 연락 메시지 전송 (`name`, `email`, `message`) |
| GET | `/api/stats` | 방문 수, 프로젝트 수, 메시지 수 |
| POST | `/api/stats/visit` | 방문 기록 |
| GET | `/api/health` | 서버 상태 확인 |

## 로컬 실행

필요한 것: Java 21, Python 3(정적 서버용) 또는 VS Code Live Server

```bash
cd backend
./mvnw spring-boot:run          # http://localhost:8080
```

다른 터미널에서:

```bash
cd frontend
python3 -m http.server 5500     # http://localhost:5500
```

테스트:

```bash
cd backend && ./mvnw test
```

## 배포

### 1. 프론트엔드 → GitHub Pages

1. 이 폴더를 GitHub 저장소에 push 합니다.
2. 저장소 **Settings → Pages → Build and deployment → Source** 를 **GitHub Actions** 로 바꿉니다.
3. `main` 브랜치에 push 하면 자동으로 배포됩니다. 주소는 `https://<아이디>.github.io/<저장소이름>/` 입니다.

### 2. 백엔드 → Render (무료)

1. [render.com](https://render.com) 에 GitHub 계정으로 가입합니다.
2. **New → Blueprint** 에서 이 저장소를 선택하면 `render.yaml` 설정으로 배포됩니다.
3. 환경변수 `APP_CORS_ALLOWED_ORIGINS` 를 본인의 Pages 주소로 바꿉니다. 예: `https://<아이디>.github.io` (경로나 끝의 `/` 없이)
4. 배포가 끝나면 나온 주소(예: `https://portfolio-api-xxxx.onrender.com`)를 `frontend/js/config.js` 의 `PRODUCTION_API_URL` 에 넣고 push 합니다.

> Render 무료 플랜은 15분 동안 요청이 없으면 서버가 잠들고, 다시 깨어나는 데 30초~1분 정도 걸립니다.
> 이 사이트는 그동안 내장 데이터로 먼저 보여주고, 연락 폼은 "서버를 깨우는 중" 안내를 띄웁니다.
> 데이터베이스는 H2 메모리 DB라 서버가 재시작되면 연락 메시지와 방문 수가 초기화됩니다.
> 오래 보관하려면 PostgreSQL 같은 외부 DB를 연결하세요.

## 내 정보로 바꾸기

| 바꿀 내용 | 파일 |
|---|---|
| 이름, 소개, 기술 스택, 경험, 링크 | `frontend/index.html` |
| 히어로 타이핑 문구 | `frontend/js/config.js` 의 `typingWords` |
| 프로젝트 목록 | `backend/.../project/ProjectDataInitializer.java` **와** `frontend/js/data.js` (두 곳을 같게 유지) |
| 색상 | `frontend/css/style.css` 상단의 `--accent`, `--accent-2` |
