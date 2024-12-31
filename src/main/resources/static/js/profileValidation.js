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

// CSRF 토큰 가져오는 함수 추가했음
    function getCsrfToken() {
        return {
            token: document.querySelector("meta[name='_csrf']").content,
            headerName: document.querySelector("meta[name='_csrf_header']").content
        };
    }

    function validatePassword() {
        const passVal = passwordField.value.trim();
        if (!passVal) {
            passwordError.textContent = "";
            return true; // 비어있으면 변경 안함.
        }
        if (passVal.length < 8 || passVal.length > 12) {
            passwordError.textContent = "비밀번호는 8~12자 이어야 합니다.";
            passwordField.classList.add("input-error");
            return false;
        }
        passwordError.textContent = "";
        passwordField.classList.remove("input-error");
        return true;
    }

    function validateConfirmPassword() {
        const passVal = passwordField.value.trim();
        const confirmVal = confirmPasswordField.value.trim();

        if (!passVal) {
            // 비밀번호가 없으면 confirm 스킵
            confirmPasswordError.textContent = "";
            return true;
        }
        // 비밀번호가 있다면 8~12자 + 일치 여부 검사
        if (confirmVal.length < 8 || confirmVal.length > 12) {
            confirmPasswordError.textContent = "비밀번호가 올바르지 않습니다.";
            confirmPasswordField.classList.add("input-error");
            return false;
        }
        if (passVal !== confirmVal) {
            confirmPasswordError.textContent = "비밀번호가 일치하지 않습니다.";
            confirmPasswordField.classList.add("input-error");
            return false;
        }
        confirmPasswordError.textContent = "";
        confirmPasswordField.classList.remove("input-error");
        return true;
    }

    // 닉네임 기본 유효성 검사
    function validateNicknameBasic() {
        const nickVal = nicknameField.value.trim();
        if (nickVal.length < 2 || nickVal.length > 15) {
            nicknameError.textContent = "닉네임은 2~15자여야 합니다.";
            nicknameField.classList.add("input-error");
            return false;
        }
        nicknameError.textContent = "";
        phoneNumberField.classList.remove("input-error");
        return true;
    }

    // 닉네임 중복검사
    async function validateNicknameDuplicate() {
        const nickVal = nicknameField.value.trim();

        if (!validateNicknameBasic()) return false;

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
                    nicknameField.classList.add("input-error");
                    return false;
                } else {
                    nicknameError.textContent = "";
                    nicknameField.classList.add("input-error");
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

    // 전화번호 유효성 검사
    function validatePhoneNumberBasic() {
        let val = phoneNumberField.value.replace(/-/g, "");
        if (!/^[0-9]{10,11}$/.test(val)) {
            phoneNumberError.textContent = "전화번호는 10~11자리 숫자만 입력가능.";
            phoneNumberField.classList.add("input-error");
            return false;
        }
        phoneNumberError.textContent = "";
        phoneNumberField.classList.remove("input-error");
        return true;
    }

    // 전화번호 중복검사 (모든 중복검사는 입력완료 후 수행)
    async function validatePhoneNumberDuplicate() {
        let val = phoneNumberField.value.replace(/-/g, "");

        if (!validatePhoneNumberBasic()) return false;

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
                    phoneNumberField.classList.add("input-error");
                    return false;
                } else {
                    phoneNumberError.textContent = "";
                    phoneNumberField.classList.remove("input-error");
                    return true;
                }
            } else {
                phoneNumberError.textContent = "중복검사 서버 에러";
                phoneNumberField.classList.add("input-error");
                return false;
            }
        } catch (err) {
            phoneNumberError.textContent = "중복검사 통신 에러";
            phoneNumberField.classList.add("input-error");
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

    // 실시간 유효성 검사 (중복검사 제외)
    function validateFormBasic() {
        let valid = true;
        if (!validatePassword()) valid = false;
        if (!validateConfirmPassword()) valid = false;
        if (!validateNicknameBasic()) valid = false;
        if (!validatePhoneNumberBasic()) valid = false;

        submitBtn.disabled = !valid;
        if (valid) {
            submitBtn.classList.remove('btn-disabled');
        } else {
            submitBtn.classList.add('btn-disabled');
        }
        return valid;
    }

    // 전체 유효성 검사 (중복검사 포함)
    async function validateFormAll() {
        let valid = true;
        if (!validatePassword()) valid = false;
        if (!validateConfirmPassword()) valid = false;

        // 중복검사 포함
        if (!(await validateNicknameDuplicate())) valid = false;
        if (!(await validatePhoneNumberDuplicate())) valid = false;

        submitBtn.disabled = !valid;
        return valid;
    }

    // 이벤트 리스너
    passwordField.addEventListener("input", () => {
        if (passwordField.value.trim()) {
            confirmPasswordWrapper.style.display = "block";
        } else {
            confirmPasswordWrapper.style.display = "none";
        }
        validateFormBasic();
    });

    confirmPasswordField.addEventListener("input", () => {
        validateFormBasic();
    });

    nicknameField.addEventListener("input", () => {
        validateFormBasic();
    });

    // 중복검사
    nicknameField.addEventListener("blur", async () => {
        await validateFormAll();
    });

    phoneNumberField.addEventListener("input", () => {
        formatPhoneNumber();
        validateFormBasic();
    });

    phoneNumberField.addEventListener("blur", async () => {
        await validateFormAll();
    });

    form.addEventListener("submit", async (e) => {
        if (!(await validateFormAll())) {
            e.preventDefault();
            console.log("프로필 수정 폼 제출 차단: 유효성 검사 실패");
        } else {
            phoneNumberField.value = phoneNumberField.value.replace(/-/g, "");
            console.log("프로필 수정 폼 제출 허용");
        }
    });

    confirmPasswordWrapper.style.display = "none";
    formatPhoneNumber();
    validateFormBasic();

    //프로필 이미지 관련 로직 추가.
    //관련 요소 추가
    const profileImg = document.getElementById("img");
    const profileImageUploadBtn = document.getElementById("profileImageUploadBtn");
    const toggleDefaultImageBtn = document.getElementById("toggleDefaultImageBtn");
    const fileInput = document.getElementById("fileInput");

    //ID 가져옴
    const userId = profileImageUploadBtn.getAttribute("data-user-id");

    profileImageUploadBtn.addEventListener('click', ()=>{
        console.log("click");
        fileInput.click();
    });

    toggleDefaultImageBtn.addEventListener('click',()=>{
        profileImg.src = "/image/profiles/Default.jpg"
        const csrfInfo = getCsrfToken();

        fetch('/defaultProfileImage',{
            method : "POST",
            headers:{
                "Content-Type": "application/json",
                [csrfInfo.headerName]: csrfInfo.token // CSRF 토큰 추가
            },
            body : JSON.stringify({"userId": userId})
        })
            .then(response => {
                return response.text().then(message => {
                    if(!response.ok){
                        alert(message)
                    }
                    return message;
                });
            })
            .then(message => {
                alert(message);
            });

    })

    fileInput.addEventListener('change',()=>{
        const file = fileInput.files[0] ;
        const csrfInfo = getCsrfToken();
        if(!file){
            alert("취소합니다.");
            return;
        }

        //보낼 이미지 준비
        const formData = new FormData();
        formData.append("file",file);
        formData.append("userId",userId);

        fetch('/uploadProfileImage',{
            method : "POST",
            headers : {[csrfInfo.headerName]: csrfInfo.token},
            body : formData
        })
            .then(response => {
                return response.text().then(message => {
                    if(!response.ok){
                        alert(message)
                        return;
                    }
                    return message;
                });
            })
            .then(URL => {
                if(URL){
                    profileImg.src = URL;
                    alert("성공적으로 변경되었습니다.");
                }
            });
    });
});

