# 회원가입 기능 명세

## **기능 개요**
회원가입 기능은 사용자가 이메일, 사용자명, 비밀번호를 입력하여 회원 정보를 데이터베이스에 저장하고,
가입이 완료되면 인덱스 페이지로 리디렉션하도록함.

---

## **기능 상세**

### **1. 회원가입 페이지 반환**
- **URL**: `/member/sign-up` (GET)
- **설명**:
    - 회원가입 페이지를 반환.
    - HTML 파일 경로: `templates/view/signup.html`.

---

### **2. 회원가입 처리**
- **URL**: `/member/sign-up` (POST)
- **설명**:
    - 사용자가 입력한 이메일, 사용자명, 비밀번호를 처리하여 데이터베이스에 저장.
    - 이메일 중복 확인 및 비밀번호 암호화를 포함.
    - 회원가입 완료 후 인덱스 페이지(`/`)로 리디렉션.

#### 입력값
- **email**: 사용자의 이메일 주소 (필수)
- **username**: 사용자명 (필수)
- **password**: 비밀번호 (필수)

---

### **3. 회원가입 로직**
- **서비스 계층**: `MemberService`
- **설명**:
    - 이메일 중복 확인: 이미 존재하는 이메일일 경우 예외 발생.
    - 비밀번호 암호화: `BCryptPasswordEncoder`를 사용하여 비밀번호를 암호화.
    - 회원 정보 저장: 데이터베이스에 회원 정보를 저장.

---

#### **4. Member 엔티티**
- **테이블 이름**: `Member`
- **필드**:
    - `memberId`: 기본 키 (Auto Increment)
    - `email`: 이메일 (유일값, 필수)
    - `username`: 사용자명 (필수)
    - `password`: 암호화된 비밀번호
    - `role`: 사용자 역할 (기본값: USER)
    - `profileImage`: 프로필 이미지 URL (선택)
    - `introduce`: 사용자 소개 (선택)
    - `createdAt`: 생성일
    - `updatedAt`: 수정일
    - `status`: 상태 값 (1: 활성, 0: 비활성)

---

### **5. 검증 및 예외 처리**
#### 이메일 중복 확인
- 이메일이 이미 데이터베이스에 존재할 경우 `DuplicatedEmailException`을 발생.

#### 입력값 검증
- `MemberSignUpRequest` DTO를 통해 사용자의 입력값을 캡슐화하고 검증.

---

### **6. 회원가입 완료 후 동작**
- 회원가입이 성공적으로 완료되면 루트 경로(`/`)로 리디렉션.
- 루트 경로는 별도의 `IndexController`에서 처리.

---


# Commit Date
이미지img

개발자를 위한 팀원 매칭 서비스
<br>

# 목차
1. 팀원 소개 및 역할
2. 사용 기술 스택
3. 프로젝트 개요 및 개발 일정
4. 접속 방법
5. Architecture
6. 플로우 차트 , 피그마(화면설계)
7. ERD
8. API 명세
9. 구성화면 , 시연 영상
<br>

# 팀원 소개 및 역할
<table>
  <thead>
    <tr>
      <th style="text-align: center;">이름</th>
      <th style="text-align: center;">역할</th>
      <th style="text-align: center;">담당 업무</th>
      <th style="text-align: center;">이메일</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align: center;"><b>문영훈</b></td>
      <td style="text-align: center;">팀장 <br> Backend/Frontend</td>
      <td style="text-align: center;">
        <b>#설계</b><br>- ERD 설계<br><br>
        <b>#Backend</b><br>- 채팅 기능 개발<br>- 게시판 기능 개발<br>- 댓글, 수정, 삭제 기능 개발<br>- 사용자별 권한 기능 개발<br><br>
        <b>#Frontend</b><br>- 채팅 UI 개발<br>- 게시판 UI 설계 및 개발
      </td>
      <td style="text-align: center;"><a href="mailto:myh7754@naver.com">myh7754@naver.com</a></td>
    </tr>
    <tr>
      <td style="text-align: center;"><b>심윤보</b></td>
      <td style="text-align: center;">팀원 <br> Backend/Frontend</td>
      <td style="text-align: center;">
        <b>#설계</b><br>- Figma 설계<br>- ERD 설계<br><br>
        <b>#Backend</b><br>- Spring Security를 이용한 폼 로그인 개발<br>- 사용자별 권한 기능 개발<br>- SMTP를 이용한 비밀번호 재설정 기능 개발<br>- 사용자 정보 수정 및 탈퇴 기능 개발<br><br>
        <b>#Frontend</b><br>- 게시판 레이아웃 및 디자인 개발
      </td>
      <td style="text-align: center;"><a href="mailto:jane.smith@email.com">jane.smith@email.com</a></td>
    </tr>
    <tr>
      <td style="text-align: center;"><b>이시현</b></td>
      <td style="text-align: center;">팀원 <br> Backend/Frontend</td>
      <td style="text-align: center;">
        <b>#설계</b><br>- Figma 설계<br>- ERD 설계<br><br>
        <b>#Backend</b><br>- 스와이프 기능 개발<br>- 좋아요 기능 개발<br>- 댓글, 수정, 삭제 기능 개발<br><br>
        <b>#Frontend</b><br>- 채팅방 UI 개발<br>- 좋아요 목록 UI 개발<br>- 내가 쓴 게시글 목록 UI 개발
      </td>
      <td style="text-align: center;"><a href="mailto:alice.johnson@email.com">alice.johnson@email.com</a></td>
    </tr>
  </tbody>
</table>

<br>

# 사용 기술 스택
spring , js, thymeleaf, jpa, security , mariaDB , (smtp? , sockjs) , aws(EC2)

<br>

# 프로젝트 개요 및 개발 일정

<br>

# 접속 방법

<br>

# Architecture

<br>

# 플로우 차트 , 피그마(화면설계)

<br>

# ERD

<br>


# API 명세

<br>

# 구성화면 , 시연 영상

<br>

# 트러블 슈팅

<br>

