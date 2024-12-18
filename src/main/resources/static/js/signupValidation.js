document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signup-form");
    const pageType = document.body.getAttribute("data-page-type");
    // 일반 유저와 OAuth 유저의 가입페이지 구분


    function createField(elementId, validateFn, errorMessage, url = null) {
        const element = document.getElementById(elementId);
        if (!element) return null;
        return {
            element: element,
            error: document.getElementById(`${elementId}Error`),
            validate: validateFn,
            errorMessage,
            url
        };
    }

    // 페이지에 따라 필드 설정
    const fields = {};
    if (pageType === "signup") {
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
                "사용자명은 2자 이상 30자 이하로 입력해주세요."
            ),
            password: createField(
                "password",
                (value) => value.length >= 8 && value.length <= 12,
                "비밀번호는 8자 이상 12자 이하로 입력해주세요."
            )
        });
    } else if (pageType === "oauth") {
        Object.assign(fields, {
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

    // 존재하는 필드만 검증하도록 설정
    const activeFields = Object.values(fields).filter(Boolean);

    // 폼 유효성 검사 함수
    function validateForm() {
        let isValid = true;

        Object.values(fields).forEach(({ element, validate, error, errorMessage }) => {
            let value = element.value.trim();

            // 전화번호의 '-' 제거 후 검증
            if (element.id === "phoneNumber") {
                value = value.replace(/-/g, "");
                element.value = value;
            }

            if (!validate(value)) {
                isValid = false;
                error.textContent = errorMessage;
                element.classList.add("invalid");
            } else {
                error.textContent = "";
                element.classList.remove("invalid");
            }
        });

        return isValid;
    }

    // 폼 제출 이벤트 리스너
    form.addEventListener("submit", (e) => {
        if (!validateForm()) {
            e.preventDefault(); // 발리데이션을 통해 잘못된부분이 있다면 제출을 방지함
        }
    });

    // 필드 입력 시 실시간 유효성 검사 및 Fetch 중복 확인 부분
    Object.values(fields).forEach(({ element, validate, error, errorMessage, url }) => {
        element.addEventListener("input", () => {
            const value = element.value.trim();

            // 전화번호 입력 시 - 포맷 적용
            if (element.id === "phoneNumber") {
                element.value = formatPhoneNumber(value); // 전화번호 포맷 적용
            }

            if (!validate(value)) {
                error.textContent = errorMessage;
                element.classList.add("invalid");
            } else {
                error.textContent = "";
                element.classList.remove("invalid");

                // Fetch API 방식 중복 확인
                if (url) {
                    checkDuplicate(url, value.replace(/-/g, ""), error, element); // '-' 제거 후 서버 요청
                }
            }
        });
    });

    // 전화번호 포맷팅용 함수
    function formatPhoneNumber(value) {
        const numbers = value.replace(/\D/g, ""); // 숫자만 추출
        if (numbers.length === 11) {
            return numbers.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
        } else if (numbers.length === 10) {
            return numbers.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
        }
        return numbers;
    }

    // 중복 확인 함수 (Fetch API 사용)
    async function checkDuplicate(url, value, errorField, inputElement) {
        try {
            const response = await fetch(`${url}?value=${encodeURIComponent(value)}`); // GET 요청
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            // 서버 응답 처리
            if (data.exists) {
                errorField.textContent = "이미 가입된 사용자 입니다.";
                inputElement.classList.add("invalid"); // 유효하지 않은 상태 표시
            } else {
                errorField.textContent = "";
                inputElement.classList.remove("invalid");
            }
        } catch (err) {
            console.error("중복 확인 요청 실패:", err);
            errorField.textContent = "서버와의 통신에 문제가 발생했습니다.";
        }

    }

});