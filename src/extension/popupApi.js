// popupapi.js
const BASE_URL = "http://localhost:8080";

// 확장 API 유무 체크
const hasChrome = typeof chrome !== "undefined" && !!chrome.runtime;

// 닫기 버튼: 확장이라면 side panel 닫기 요청, 웹이라면 창 닫기(팝업에서만 먹힘)
function handleClose() {
    if (hasChrome) {
        try {
            chrome.runtime.sendMessage({ type: "CLOSE_SIDE_PANEL" });
            return;
        } catch (_) {}
    }
    // 일반 웹 페이지에서는 window.close()가 대부분 막혀요.
    // 대신 섹션을 숨기거나 안내 토스트를 띄우는 용도로 쓰세요.
    // window.close();
    document.body.innerHTML = "<div style='padding:20px;font-size:14px'>패널을 닫았습니다.</div>";
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("closeIcon")?.addEventListener("click", handleClose);

    // 탭 토글
    const buttons = document.querySelectorAll(".tab-button");
    const contents = document.querySelectorAll(".tab-content");
    buttons.forEach((button) => {
        button.addEventListener("click", function () {
            const target = this.getAttribute("data-tab") + "-tab";
            buttons.forEach((b) => b.classList.remove("active"));
            contents.forEach((c) => c.classList.remove("active"));
            this.classList.add("active");
            document.getElementById(target).classList.add("active");
        });
    });

    // 새로고침 버튼
    document.getElementById("refresh-student-notices")?.addEventListener("click", fetchStudentNotices);
    document.getElementById("refresh-school-notices")?.addEventListener("click", fetchSchoolNotices);
    document.getElementById("refresh-point-notices")?.addEventListener("click", fetchPointNotices);

    // 초기 로드
    fetchNoticesAndDisplay();
});

// 확장 전용 코드는 가드로 막음 (브라우저에선 실행 안 됨)
if (hasChrome && chrome.action && chrome.sidePanel) {
    chrome.action.onClicked.addListener(async (tab) => {
        await chrome.sidePanel.setOptions({
            tabId: tab.id,
            path: "index.html",
            enabled: true
        });
        await chrome.sidePanel.open({ tabId: tab.id });
    });
}

// 공지사항 가져오기
async function fetchNotices(url, elementId) {
    try {
        const response = await fetch(`${BASE_URL}${url}`);
        const data = await response.json();
        const notices = data.items || data; // API 응답이 NoticeListResponse인 경우 items 사용

        const noticesList = document.getElementById(elementId);
        noticesList.innerHTML = "";

        notices.forEach((notice) => {
            const listItem = document.createElement("li");
            if (notice.type === "POINT") {
                listItem.innerHTML = `
          <strong><span class="notice-title">${notice.title}</span></strong>
          - <span class="notice-deadline"><strong>마감:</strong> </span>
          <span class="notice-date">${notice.postedDate}</span>
        `;
            } else {
                listItem.innerHTML = `
          <strong><span class="notice-title">${notice.title}</span></strong>
          - <span class="notice-date">${notice.postedDate}</span>
        `;
            }

            let noticeUrl = notice.url;
            // 확장 스킴 보정 로직은 브라우저에선 불필요하니 제거/주석
            // if (noticeUrl.startsWith("chrome-extension://...")) { ... }

            const link = document.createElement("a");
            link.href = noticeUrl;
            link.target = "_blank";
            link.appendChild(listItem);

            noticesList.appendChild(link);
        });
    } catch (error) {
        console.error("공지사항 불러오기 실패 : ", error);
    }
}

async function fetchStudentNotices() {
    await fetchNotices("/api/notices/student", "student-notice-list");
}
async function fetchSchoolNotices() {
    await fetchNotices("/api/notices/school", "school-notice-list");
}
async function fetchPointNotices() {
    await fetchNotices("/api/notices/point", "point-notice-list");
}
async function fetchNoticesAndDisplay() {
    await fetchStudentNotices();
    await fetchSchoolNotices();
    await fetchPointNotices();
}
