document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signup-form");
    const pageType = document.body.getAttribute("data-page-type");

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

    // 페이지 타입에 따라 다르게 필드 구성
    if (pageType === "signup") {
        // 일반 회원가입 페이지의 필드
        Object.assign(fields, {
            email: createField(
                "email",
                (value) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value),
                "유효한 이메일 주소를 입력해주세요.",
                "/member/check-email"
            ),
            username: createField(
                "username",
                (value) => value.length >= 2 && value.length <= 30,
                "이름은 2자 이상 30자 이하로 입력해주세요."
            ),
            password: createField(
                "password",
                (value) => value.length >= 8 && value.length <= 12,
                "비밀번호는 8자 이상 12자 이하로 입력해주세요."
            ),
            confirmPassword: createField(
                "confirmPassword",
                (value) => {
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
                "/member/check-nickname"
            ),
            phoneNumber: createField(
                "phoneNumber",
                (value) => /^[0-9]{10,11}$/.test(value.replace(/-/g, "")),
                "전화번호는 10~11자리 숫자만 입력 가능합니다.",
                "/member/check-phone-number"
            )
        });
    } else if (pageType === "oauth") {
        // OAuth 추가정보 입력 페이지의 필드
        Object.assign(fields, {
            username: createField(
                "username",
                (value) => value.length >= 2 && value.length <= 30,
                "이름은 2자 이상 30자 이하로 입력해주세요."
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
        });
    }

    // 공통 유효성 검사 (기본 유효성 검사만 수행)
    async function validateField(field) {
        const { element, validate, error, errorMessage } = field;
        let value = element.value.trim();

        if (element.id === "phoneNumber") {
            value = value.replace(/-/g, "");
            element.value = formatPhoneNumber(value);
        }

        if (!validate(value)) {
            field.isValid = false;
            error.textContent = errorMessage;
            element.classList.add("input-error");
        } else {
            error.textContent = "";
            element.classList.remove("input-error");
            field.isValid = true;
        }

        // 비밀번호/확인 서로 연동 (회원가입 페이지일 때만)
        if (pageType === "signup" && (element.id === "password" || element.id === "confirmPassword")) {
            const confirmField = fields.confirmPassword;
            const pwdField = fields.password;
            if (confirmField && pwdField) {
                if (element.id === "password") {
                    await validateField(confirmField);
                }
            }
        }
    }

    // 중복검사를 포함한 전체 유효성 검사
    async function validateFieldWithDuplication(field) {
        const { element, url } = field;

        // 먼저 기본 유효성 검사 수행
        await validateField(field);

        // 기본 유효성 검사를 통과하고 중복검사가 필요한 필드인 경우에만 중복검사 수행
        if (field.isValid && url) {
            const value = element.value.trim().replace(/-/g, "");
            await checkDuplicate(url, value, field.error, element, field);
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
                errorField.textContent = "이미 가입된 계정입니다.";
                inputElement.classList.add("input-error");
                field.isValid = false;
            } else {
                errorField.textContent = "";
                inputElement.classList.remove("input-error");
                field.isValid = true;
            }
        } catch (err) {
            console.error("중복 확인 요청 실패:", err);
            errorField.textContent = "서버와의 통신에 문제가 발생했습니다.";
            inputElement.classList.add("input-error");
            field.isValid = false;
        }
    }

    function validateForm() {
        return Object.values(fields).every((field) => field.isValid);
    }

    function updateSubmitButtonState() {
        const submitButton = form.querySelector("button[type='submit']");
        const isValid = validateForm();

        if (isValid) {
            submitButton.removeAttribute('disabled');
            submitButton.classList.remove('btn-disabled');
        } else {
            submitButton.setAttribute('disabled', 'true');
            submitButton.classList.add('btn-disabled');
        }
    }

    // 전화번호 포매팅
    function formatPhoneNumber(value) {
        const numbers = value.replace(/\D/g, "");
        if (numbers.length === 11) {
            return numbers.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
        } else if (numbers.length === 10) {
            return numbers.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
        }
        return numbers;
    }


    Object.values(fields).forEach((field) => {
        if (!field) return;

        // 기본 유효성 검사만 수행
        field.element.addEventListener("input", async () => {
            await validateField(field);
            updateSubmitButtonState();
        });

        // 중복검사를 포함한 전체 유효성 검사 수행
        if (field.url) {  // 중복검사가 필요한 필드만
            field.element.addEventListener("blur", async () => {
                await validateFieldWithDuplication(field);
                updateSubmitButtonState();
            });
        }
    });

    form.addEventListener("submit", async (e) => {
        if (!validateForm()) {
            e.preventDefault();
            console.log("폼 제출 차단: 유효성 검사 실패");
        } else {
            console.log("폼 제출 허용: 유효성 검사 통과");
            // 최종 제출 시, 전화번호의 - 제거 후 전송
            const phoneField = fields.phoneNumber?.element;
            if (phoneField) {
                phoneField.value = phoneField.value.replace(/-/g, "");
            }
        }
    });

    updateSubmitButtonState();
});