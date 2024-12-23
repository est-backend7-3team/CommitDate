document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("profileForm");
    if (!form) return;

    // 비밀번호
    const passwordField = document.getElementById("password");
    const confirmPasswordWrapper = document.getElementById("confirmPasswordWrapper");
    const confirmPasswordField = document.getElementById("confirmPassword");

    // 닉네임 & 전화번호
    const nicknameField = document.getElementById("nickname");
    const phoneNumberField = document.getElementById("phoneNumber");

    // hidden 필드
    const originalNickname = document.getElementById("originalNickname")?.value || "";
    const originalPhoneNumber = document.getElementById("originalPhoneNumber")?.value || "";

    // 버튼
    const submitBtn = document.getElementById("profileSubmitBtn");

    // 에러 영역
    const passwordError = document.getElementById("passwordError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");
    const nicknameError = document.getElementById("nicknameError");
    const phoneNumberError = document.getElementById("phoneNumberError");

    function validatePassword() {
        const passVal = passwordField.value.trim();
        if (!passVal) {
            passwordError.textContent = "";
            return true; // 비어있으면 변경 안함.
        }
        if (passVal.length < 8 || passVal.length > 12) {
            passwordError.textContent = "비밀번호는 8~12자 이어야 합니다.";
            return false;
        }
        passwordError.textContent = "";
        return true;
    }

    function validateConfirmPassword() {
        const passVal = passwordField.value.trim();
        const confirmVal = confirmPasswordField.value.trim();

        if (!passVal) {
            // 비밀번호가 없으면 confirm도 스킵
            confirmPasswordError.textContent = "";
            return true;
        }
        // 비밀번호가 있다면 8~12자 + 일치 여부 검사
        if (confirmVal.length < 8 || confirmVal.length > 12) {
            confirmPasswordError.textContent = "비밀번호가 올바르지 않습니다.";
            return false;
        }
        if (passVal !== confirmVal) {
            confirmPasswordError.textContent = "비밀번호가 일치하지 않습니다.";
            return false;
        }
        confirmPasswordError.textContent = "";
        return true;
    }

    // 닉네임 중복검사 시 원본과 같은 값이면 검사 스킵
    async function validateNickname() {
        const nickVal = nicknameField.value.trim();
        if (nickVal.length < 2 || nickVal.length > 15) {
            nicknameError.textContent = "닉네임은 2~15자여야 합니다.";
            return false;
        }

        if (nickVal === originalNickname) {
            nicknameError.textContent = "";
            return true;
        }

        try {
            const res = await fetch(`/member/check-nickname?value=${encodeURIComponent(nickVal)}`);
            if (res.ok) {
                const data = await res.json();
                if (data.exists) {
                    nicknameError.textContent = "이미 사용중인 닉네임입니다.";
                    return false;
                } else {
                    nicknameError.textContent = "";
                    return true;
                }
            } else {
                nicknameError.textContent = "중복검사 서버 에러";
                return false;
            }
        } catch (err) {
            nicknameError.textContent = "중복검사 통신 에러";
            return false;
        }
    }

    // 전화번호 중복검사 시 원본과 같은 값이면 검사 스킵
    async function validatePhoneNumber() {
        let val = phoneNumberField.value.replace(/-/g, "");
        if (!/^[0-9]{10,11}$/.test(val)) {
            phoneNumberError.textContent = "전화번호는 10~11자리 숫자만 입력가능.";
            return false;
        }

        if (val === originalPhoneNumber) {
            phoneNumberError.textContent = "";
            return true;
        }

        try {
            const res = await fetch(`/member/check-phone-number?value=${encodeURIComponent(val)}`);
            if (res.ok) {
                const data = await res.json();
                if (data.exists) {
                    phoneNumberError.textContent = "이미 사용중인 전화번호입니다.";
                    return false;
                } else {
                    phoneNumberError.textContent = "";
                    return true;
                }
            } else {
                phoneNumberError.textContent = "중복검사 서버 에러";
                return false;
            }
        } catch (err) {
            phoneNumberError.textContent = "중복검사 통신 에러";
            return false;
        }

    }

    function formatPhoneNumber() {
        const raw = phoneNumberField.value.replace(/\D/g, "");
        if (raw.length === 11) {
            phoneNumberField.value = raw.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
        } else if (raw.length === 10) {
            phoneNumberField.value = raw.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
        }
    }

    async function validateForm() {
        let valid = true;
        if (!validatePassword()) valid = false;
        if (!validateConfirmPassword()) valid = false;

        if (!(await validateNickname())) valid = false;
        if (!(await validatePhoneNumber())) valid = false;

        submitBtn.disabled = !valid;
        return valid;
    }

    passwordField.addEventListener("input", () => {
        if (passwordField.value.trim()) {
            confirmPasswordWrapper.style.display = "block";
        } else {
            confirmPasswordWrapper.style.display = "none";
        }
        validateForm();
    });

    confirmPasswordField.addEventListener("input", () => {
        validateForm();
    });

    nicknameField.addEventListener("input", () => {
        validateForm();
    });

    phoneNumberField.addEventListener("input", () => {
        formatPhoneNumber();
        validateForm();
    });

    form.addEventListener("submit", async (e) => {
        if (!(await validateForm())) {
            e.preventDefault();
            console.log("프로필 수정 폼 제출 차단: 유효성 검사 실패");
        } else {
            // 전화번호 - 제거
            phoneNumberField.value = phoneNumberField.value.replace(/-/g, "");
            console.log("프로필 수정 폼 제출 허용");
        }
    });

    confirmPasswordWrapper.style.display = "none";
    formatPhoneNumber();
    validateForm();

});
