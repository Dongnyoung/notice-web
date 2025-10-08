// background.js
chrome.action.onClicked(async (tab) => {
  // 이 탭에서 사이드패널 경로 지정 + 활성화
  await chrome.sidePanel.setOptions({
    tabId: tab.id,
    path: "index.html",
    enabled: true,
  });
  await chrome.sidePanel.open({ tabId: tab.id });
});

// 사이드패널 닫기 요청 처리(페이지 -> BG로 메시지 보냄)
chrome.runtime.onMessage.addListener(async (msg, sender) => {
  if (msg?.type === "CLOSE_SIDE_PANEL") {
    // 메시지를 보낸 패널이 연결된 탭을 찾아 닫기
    const [active] = await chrome.tabs.query({ active: true, lastFocusedWindow: true });
    if (active?.id) {
      await chrome.sidePanel.setOptions({
        tabId: active.id,
        enabled: false, // false로 두면 해당 탭에서 패널이 닫힘
      });
    }
  }
});
