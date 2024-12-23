/* signupValidation.js */
document.addEventListener("DOMContentLoaded", () => {
    // [추가/수정] 폼 가져오기
    const form = document.getElementById("signup-form");
    const pageType = document.body.getAttribute("data-page-type");

    // 유효성 검증 필드를 동적으로 구성
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

    const fields = {};
    if (pageType === "signup") {
        Object.assign(fields, {
            email: createField(
                "email",
                (value) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value),
                "유효한 이메일 주소를 입력해주세요.",
                "/member/check-email"  // [추가/수정] 이메일 중복검사 API
            ),
            username: createField(
                "username",
                (value) => value.length >= 2 && value.length <= 30,
                "사용자명은 2자 이상 30자 이하로 입력해주세요."
            ),
            password: createField(
                "password",
                (value) => value.length >= 8 && value.length <= 12,
                "비밀번호는 8자 이상 12자 이하로 입력해주세요."
            ),
            /* [추가] confirmPassword */
            confirmPassword: createField(
                "confirmPassword",
                (value) => {
                    // password 값과 동일해야
                    const pwdVal = document.getElementById("password").value.trim();
                    if (pwdVal.length < 8 || pwdVal.length > 12) return false;
                    return (value === pwdVal);
                },
                "비밀번호가 일치하지 않습니다."
            ),
            nickname: createField(
                "nickname",
                (value) => value.length >= 2 && value.length <= 15,
                "닉네임은 2자 이상 15자 이하로 입력해주세요.",
                "/member/check-nickname" // 닉네임 중복검사 API
            ),
            phoneNumber: createField(
                "phoneNumber",
                // 10~11자리 숫자만
                (value) => /^[0-9]{10,11}$/.test(value.replace(/-/g, "")),
                "전화번호는 10~11자리 숫자만 입력 가능합니다.",
                "/member/check-phone-number" // 전화번호 중복검사 API
            )
        });
    }

    // validateField: 공통 유효성 검사
    async function validateField(field) {
        const { element, validate, error, errorMessage, url } = field;
        let value = element.value.trim();

        // 전화번호일 경우 하이픈 제거 후 포맷
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

            // [추가/수정] url이 있으면 중복검사
            if (url) {
                await checkDuplicate(url, value.replace(/-/g, ""), error, element, field);
            }
        }

        // [추가/수정] 비밀번호/확인 서로 연동
        if (element.id === "password" || element.id === "confirmPassword") {
            const confirmField = fields.confirmPassword;
            const pwdField = fields.password;
            if (confirmField && pwdField) {
                // confirmPassword도 재검증
                if (element.id === "password") {
                    await validateField(confirmField);
                } else {
                    await validateField(pwdField);
                }
            }
        }
    }

    // 중복검사
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
        // 모든 필드가 isValid == true 여야 통과
        return Object.values(fields).every((field) => field.isValid);
    }

    function updateSubmitButtonState() {
        const submitButton = form.querySelector("button[type='submit']");
        submitButton.disabled = !validateForm();
    }

    // 전화번호 포맷
    function formatPhoneNumber(value) {
        const numbers = value.replace(/\D/g, "");
        if (numbers.length === 11) {
            return numbers.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
        } else if (numbers.length === 10) {
            return numbers.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
        }
        return numbers;
    }

    // 각 필드 input 이벤트
    Object.values(fields).forEach((field) => {
        if (!field) return;
        field.element.addEventListener("input", async () => {
            await validateField(field);
            updateSubmitButtonState();
        });
    });

    form.addEventListener("submit", (e) => {
        if (!validateForm()) {
            e.preventDefault();
            console.log("폼 제출 차단: 유효성 검사 실패");
        } else {
            console.log("폼 제출 허용: 유효성 검사 통과");
            // [추가] 최종 제출 시, phoneNumber의 하이픈을 제거해서 서버로 전송
            const phoneField = fields.phoneNumber?.element;
            if (phoneField) {
                phoneField.value = phoneField.value.replace(/-/g, "");
            }
        }
    });

    // 초기 업데이트
    updateSubmitButtonState();
});
