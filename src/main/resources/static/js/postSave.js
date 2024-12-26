document.getElementById('boardId').addEventListener('change', function () {
    const selectedValue = this.options[this.selectedIndex].text;;
    const container = document.getElementById('additionalInputContainer');
    const sourcecode = document.getElementById('sourceCode');

    // 기존 입력폼 초기화
    // container.innerHTML = '';

    // 특정 게시판 선택 시 추가 입력폼 표시


    if (selectedValue === 'Swipe') {
        container.style.display = 'block';
        console.log("생성 완료");
    }else{
        container.style.display = 'none';
        sourcecode.textContent ="";
        console.log(" 초기화 완료");
    }
});
