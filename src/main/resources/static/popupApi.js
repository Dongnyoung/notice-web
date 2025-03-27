async function fetchNotices(url, elementId){
    try{
        const response = await fetch(url);
        const notices = await response.json();

        const noticesList = document.getElementById(elementId);
        noticesList.innerHTML = ""; //기존 초기화
        notices.forEach(notice => {
                    const listItem = document.createElement("li");
                    listItem.innerHTML = `<strong>${notice.title}</strong> - ${notice.postedDate}`;
                    noticesList.appendChild(listItem); // noticeList -> noticesList로 수정
                });
    }
    catch (error){
        console.error("공지사항 불러오기 실패 : ",error);
    }
}

async function fetchStudentNotices(){
    await fetchNotices("http://localhost:8080/api/notifications/student", "student-notice-list");
}

async function fetchSchoolNotices(){
    await fetchNotices("http://localhost:8080/api/notifications/school", "school-notice-list");
}

async function fetchPointNotices(){
    await fetchNotices("http://localhost:8080/api/notifications/point", "point-notice-list");
}

async function fetchNoticesAndDisplay() {
    // 이후 공지사항 데이터를 각 엔드포인트에서 가져옴
    await fetchStudentNotices();
    await fetchSchoolNotices();
    await fetchPointNotices();
}


document.addEventListener("DOMContentLoaded",fetchNoticesAndDisplay);// 페이지 로드 시 크롤링하고 데이터를 가져오는 함수 호출

