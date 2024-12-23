document.addEventListener("DOMContentLoaded", () => {
    const emailInput = document.getElementById("email");
    const submitBtn = document.getElementById("submitBtn");

    submitBtn.addEventListener("click", async () => {
        const emailVal = emailInput.value.trim();
        if (!emailVal) {
            alert("이메일을 입력해주세요.");
            return;
        }
        try {
            // /member/forgot-password-request 로 전송
            const res = await fetch("/member/forgot-password-request", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email: emailVal })
            });
            const data = await res.json();

            if (data.success) {
                // 정상적인 이메일 -> 임시비밀번호 발급 완료
                alert(data.message);
                // alert 확인 후 /login 페이지로 리디렉트
                window.location.href = "/login";
            } else {
                // 존재하지 않는 회원 등 실패 사유
                alert(data.message);
            }

        } catch (error) {
            alert("서버 통신 오류가 발생했습니다.");
        }
    });
});
