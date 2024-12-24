document.addEventListener("DOMContentLoaded", () => {
    const forgotForm = document.getElementById("forgotForm");
    const emailInput = document.getElementById("email");
    const submitBtn = document.getElementById("submitBtn");
    const emailError = document.getElementById("emailError");

    const clearErrors = () => {
        emailError.textContent = "";
    };

    const showError = (element, message) => {
        element.textContent = message;
    };

    submitBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        clearErrors();

        const emailVal = emailInput.value.trim();
        if (!emailVal) {
            showError(emailError, "이메일을 입력해주세요.");
            return;
        }

        // 이메일 형식 검증
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailVal)) {
            showError(emailError, "올바른 이메일 형식이 아닙니다.");
            return;
        }

        try {
            submitBtn.disabled = true;
            submitBtn.classList.add("btn-disabled");
            submitBtn.textContent = "처리중...";

            const res = await fetch("/member/forgot-password-request", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email: emailVal })
            });
            const data = await res.json();

            if (data.success) {
                alert(data.message);
                window.location.href = "/login";
            } else {
                showError(emailError, data.message);
            }
        } catch (error) {
            showError(emailError, "서버 통신 오류가 발생했습니다.");
        } finally {
            submitBtn.disabled = false;
            submitBtn.classList.remove("btn-disabled");
            submitBtn.textContent = "임시 비밀번호 발급";
        }

    });

});