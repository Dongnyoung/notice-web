const BASE_URL = "http://localhost:8080";

// 공지사항 데이터를 불러오는 함수
async function fetchNotices(url, elementId){
    try{
        const response = await fetch(`${BASE_URL}${url}`);
        const notices = await response.json();

        const noticesList = document.getElementById(elementId);
        noticesList.innerHTML = ""; //기존 초기화
        notices.forEach(notice => {
            const listItem = document.createElement("li");
            listItem.innerHTML = `<strong>${notice.title}</strong> - ${notice.postedDate}`;

            let noticeUrl = notice.url;
            if (noticeUrl.startsWith("chrome-extension://apcgkcekbhhjdddlmlfehfomdlnpdeek")) {
                // URL이 확장 프로그램 경로로 시작하면, 실제 URL로 변환
                noticeUrl = `${BASE_URL}${noticeUrl.replace("chrome-extension://apcgkcekbhhjdddlmlfehfomdlnpdeek", "https://www.hansung.ac.kr")}`;
            }
            //클릭시 해당 url이동
            const link = document.createElement("a");
            link.href = noticeUrl;
            link.target = "_blank"; //새탭에서 열기
            link.appendChild(listItem);


            noticesList.appendChild(link);
        });
    }
    catch (error){
        console.error("공지사항 불러오기 실패 : ", error);
    }
}

// 각 공지사항 타입별로 데이터를 가져오는 함수들
async function fetchStudentNotices(){
    await fetchNotices("/api/notifications/student", "student-notice-list");
}

async function fetchSchoolNotices(){
    await fetchNotices("/api/notifications/school", "school-notice-list");
}

async function fetchPointNotices(){
    await fetchNotices("/api/notifications/point", "point-notice-list");
}

// 페이지 로드 시 공지사항을 모두 불러오는 함수
async function fetchNoticesAndDisplay() {
    await fetchStudentNotices();
    await fetchSchoolNotices();
    await fetchPointNotices();
}

// DOMContentLoaded 이벤트로 페이지 로드 후 데이터를 불러옴
document.addEventListener("DOMContentLoaded", fetchNoticesAndDisplay);

// 버튼 클릭 시 데이터를 새로고침하는 이벤트 핸들러
document.getElementById("refresh-student-notices").addEventListener("click", fetchStudentNotices);
document.getElementById("refresh-school-notices").addEventListener("click", fetchSchoolNotices);
document.getElementById("refresh-point-notices").addEventListener("click", fetchPointNotices);
