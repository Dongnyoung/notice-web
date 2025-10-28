# BoogieBell (부기벨)

한성대학교 공지(학사/학교)와 비교과·포인트 소식을 **한 화면**에서 빠르게 확인하는 크롬 익스텐션 + Spring Boot 백엔드 프로젝트입니다. 

<img width="30%" height="auto" alt="스크린샷 2025-10-09 163623" src="https://github.com/user-attachments/assets/fbe0a97f-c92e-4782-9fa7-3c3e2e02984f" />
<img width="30%" height="auto" alt="스크린샷 2025-10-09 163647" src="https://github.com/user-attachments/assets/522d1606-91c4-4747-a2be-8b135f36645b" />
<img width="30%" height="auto" alt="스크린샷 2025-10-09 163524" src="https://github.com/user-attachments/assets/54e6f49b-ea97-4a71-b56f-e0326eecdf97" />


---

## ✨ 핵심 기능



https://github.com/user-attachments/assets/b00a9fbd-5502-4dfa-a548-fe196d7762a5



* **최신 공지 모아보기**: 학사/학교/비교과/포인트 공지를 크롤링해 상위 N개(기본 5개) 리스트업
* **카테고리별 보기**: `학생(학사)`, `학교`, `비교과`, `포인트` 탭/필터 제공
* **요약(예정)**: 공지 Hover/클릭 시 핵심 요약
* **알림(예정)**: 새 공지 발생 시 브라우저 알림
* **점메추,저메추(예정)**: 시간에 따라서 점심 or 저녁을 랜덤 메뉴로 추천해주기
---

## 🏗️ 아키텍처

<img width="1608" height="457" alt="스크린샷 2025-10-09 163245" src="https://github.com/user-attachments/assets/bd5a0b64-490a-49ce-a931-b407f9ec3f67" />

* **패턴**: Parser 모듈에 **전략 패턴(Strategy)** 적용 → 사이트별 파싱 로직을 유연하게 교체·확장
* **레이어드 구조**: Spring MVC 흐름을 따르는 Controller → Service → Repository 계층
* **확장성**: 새로운 공지 소스 추가 시 Parser 구현체만 추가하면 영향 최소화

---

## 🧰 기술 스택

* **Frontend**: Chrome Extension (Manifest v3), HTML/CSS/JavaScript
* **Backend**: Java,Spring Boot
* **DB**: MySQL / AWS RDS (개발 편의를 위해 H2 Profile 제공)
---


## 🌐 API

기본 Prefix: `/api/notices`

| Method | Endpoint                        | 설명                  |
| ------ | ------------------------------- | ------------------- |
| GET    | `/api/notices`                  | 전체 최신 공지 (기본 상위 5개) |
| GET    | `/api/notices/student`          | 학사(학생) 공지           |
| GET    | `/api/notices/school`           | 학교 공지               |
| GET    | `/api/notices/point`            | 비교과·포인트 공지          |

> 응답 스키마 예시

```json
[
  {
    "title": "[비교과] 캡스톤 디자인 안내",
    "link": "https://...",
    "createdAt": "2025-10-08T12:34:56",
    "category": "POINT",
  }
]
```

---

## 🚀 로컬 실행 
###  Gradle 빌드 & 실행

```bash
git clone https://github.com/Dongnyoung/notice-web.git
cd notice-web
.\gradlew clean bootJar -x test //MAC 사용자
gradlew clean build //WINDOWS 사용자
java -jar build/libs/notice-0.0.1-SNAPSHOT.jar
```

> JDK 21 필요. Mockito 에이전트 경고가 보인다면 Mockito 문서의 Java agent 설정을 참고하세요.

---

## 🧩 크롬 익스텐션 설치 (로컬)

1. `extension/` 폴더에서 개발한 파일을 준비합니다.
2. Chrome 주소창에 `chrome://extensions` 입력 → **개발자 모드** ON
3. **압축해제된 확장 프로그램을 로드** → `extension/` 선택

---




## 🤝 컨트리뷰션

기여를 환영합니다!

1. 이슈 등록 → 배경/재현/기대 동작/현재 동작 명시
2. 브랜치 전략: `feat/…`, `fix/…`, `chore/…`, `docs/…`
3. PR 템플릿에 체크리스트 (테스트, 문서, 스냅샷) 작성
4. 리뷰 반영 및 스쿼시 머지 권장

