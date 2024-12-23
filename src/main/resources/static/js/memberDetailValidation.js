document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("detail-form");

    function createField(elementId, validateFn, errorMessage, url = null) {
        const element = document.getElementById(elementId);
        if (!element) return null;
        return {
            element: element,
            error: document.getElementById(`${elementId}Error`),
            validate: validateFn,
            errorMessage,
            url,
            isValid: false
        };
    }

    const fields = {
        email: createField(
            "email",
            (value) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value),
            "유효한 이메일 주소를 입력해주세요.",
            "/member/check-email"
        ),
        password: createField(
            "password",
            (value) => value.length >= 8 && value.length <= 12,
            "비밀번호는 8자 이상 12자 이하로 입력해주세요."
        ),
        nickname: createField(
            "nickname",
            (value) => value.length >= 2 && value.length <= 15,
            "닉네임은 2자 이상 15자 이하로 입력해주세요.",
            "/member/check-nickname"
        ),
        phoneNumber: createField(
            "phoneNumber",
            (value) => /^[0-9]{10,11}$/.test(value.replace(/-/g, "")),
            "전화번호는 10~11자리 숫자만 입력 가능합니다.",
            "/member/check-phone-number"
        )
    };

    const activeFields = Object.values(fields).filter(Boolean);

    function validateField(field) {
        const { element, validate, error, errorMessage, url } = field;
        let value = element.value.trim();

        if (element.id === "phoneNumber") {
            value = value.replace(/-/g, "");
            element.value = formatPhoneNumber(value);
        }

        if (!validate(value)) {
            field.isValid = false;
            error.textContent = errorMessage;
            element.classList.add("invalid");
        } else {
            error.textContent = "";
            element.classList.remove("invalid");
            field.isValid = true;

            if (url) {
                checkDuplicate(url, value.replace(/-/g, ""), error, element, field);
            }
        }
    }

    async function checkDuplicate(url, value, errorField, inputElement, field) {
        try {
            const response = await fetch(`${url}?value=${encodeURIComponent(value)}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            if (data.exists) {
                errorField.textContent = "이미 가입된 사용자입니다.";
                inputElement.classList.add("invalid");
                field.isValid = false;
            } else {
                errorField.textContent = "";
                inputElement.classList.remove("invalid");
                field.isValid = true;
            }
        } catch (err) {
            console.error("중복 확인 요청 실패:", err);
            errorField.textContent = "서버와의 통신에 문제가 발생했습니다.";
            inputElement.classList.add("invalid");
            field.isValid = false;
        }
    }

    function validateForm() {
        return activeFields.every((field) => field.isValid);
    }

    function updateSubmitButtonState() {
        const submitButton = form.querySelector("button[type='submit']");
        submitButton.disabled = !validateForm();
    }

    Object.values(fields).forEach((field) => {
        field.element.addEventListener("input", () => {
            validateField(field);
            updateSubmitButtonState();
        });
    });

    form.addEventListener("submit", (e) => {
        if (!validateForm()) {
            e.preventDefault();
            console.log("폼 제출 차단: 유효성 검사 실패");
        } else {
            console.log("폼 제출 허용: 유효성 검사 통과");
        }
    });

    function formatPhoneNumber(value) {
        const numbers = value.replace(/\D/g, "");
        if (numbers.length === 11) {
            return numbers.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
        } else if (numbers.length === 10) {
            return numbers.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
        }
        return numbers;
    }

    updateSubmitButtonState(); // 초기 상태 업데이트
});
