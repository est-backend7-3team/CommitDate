

# Commit Date
<div align="center">
   
![logo](https://github.com/user-attachments/assets/3ef561c5-6241-4cfc-8cea-d0655049a5d4)

### 개발자를 위한 팀원 매칭 서비스
</div>
<br>

# 목차
1. [팀원 소개 및 역할](#팀원-소개-및-역할)
2. [사용 기술 스택](#사용-기술-스택)
3. [접속 방법 , 배포주소](#접속-방법)
4. [프로젝트 개요 및 개발 일정](#프로젝트-개요-및-개발일정)
5. [Architecture](#architecture)
6. [플로우 차트 , 피그마(화면설계)](#플로우-차트-피그마화면설계)
7. [ERD](#erd)
8. [API 명세](#api-명세)
9. [구성화면 , 시연 영상](#구성화면-시연-영상)
10. [트러블 슈팅](#트러블-슈팅)
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
<b><br><br>#설계</b><br>- ERD 설계<br><br>
<b>#Backend</b><br>- 채팅 기능 개발<br>- 게시판 기능 개발<br>- 댓글, 수정, 삭제 기능 개발<br>- 사용자별 권한 기능 개발<br><br>
<b>#Frontend</b><br>- 채팅 UI 개발<br>- 게시판 UI 설계 및 개발<br> <br> 
</td>
<td style="text-align: center;"><a href="mailto:myh7754@naver.com">myh7754@naver.com</a></td>
</tr>
<tr>
<td style="text-align: center;"><b>심윤보</b></td>
<td style="text-align: center;">팀원 <br> Backend/Frontend</td>
<td style="text-align: center;">
<b><br><br>#설계</b><br>- Figma 설계<br>- ERD 설계<br><br>
<b>#Backend</b><br>- Spring Security를 이용한 폼 로그인 개발<br>- 사용자별 권한 기능 개발<br>- SMTP를 이용한 비밀번호 재설정 기능 개발<br>- 사용자 정보 수정 및 탈퇴 기능 개발<br><br>
<b>#Frontend</b><br>- 게시판 레이아웃 및 디자인 개발<br> <br> 
</td>
<td style="text-align: center;"><a href="mailto:jane.smith@email.com">ensoary@gmail.com</a></td>
</tr>
<tr>
<td style="text-align: center;"><b>이시현</b></td>
<td style="text-align: center;">팀원 <br> Backend/Frontend</td>
<td style="text-align: center;">
<b><br><br>#설계</b><br>- Figma 설계<br>- ERD 설계<br><br>
<b>#Backend</b><br>- 스와이프 기능 개발<br>- 좋아요 기능 개발<br>- 댓글, 수정, 삭제 기능 개발<br><br>
<b>#Frontend</b><br>- 채팅방 UI 개발<br>- 좋아요 목록 UI 개발<br>- 내가 쓴 게시글 목록 UI 개발 <br> <br> 
</td>
<td style="text-align: center;"><a href="mailto:alice.johnson@email.com">tlgus7777@gmail.com</a></td>
</tr>
</tbody>
</table>
<br>
<br>

# 사용 기술 스택
<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-2E3B53?style=for-the-badge&logo=Java&logoColor=white">
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black">
  <img src="https://img.shields.io/badge/SMTP-FF6600?style=for-the-badge&logo=Mail.ru&logoColor=white">
  <img src="https://img.shields.io/badge/SockJS-00A9E0?style=for-the-badge&logo=SockJS&logoColor=white">
  <img src="https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=Amazon%20AWS&logoColor=white">
  <img src="https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=Amazon%20AWS&logoColor=white">
</div>
<br>
<br>

# 접속 방법
### aws 배포 주소 : [52.78.221.220](http://52.78.221.220/)

<br>
<br>

# 프로젝트 개요 및 개발 일정
### 📜 프로젝트 설명 (Project Description)
#### TeamMate는 팀원을 구하기 어려운 개발자들을 위한 혁신적인 커뮤니티 서비스입니다. 개발자들은 자신이 필요로 하는 팀원을 찾기 위해 게시글을 작성하거나, 스와이프 페이지를 통해 소개팅 앱처럼 매칭을 시도할 수 있습니다.
#### 사용자는 게시글을 작성하여 팀원을 구하거나, 다른 개발자의 프로필을 스와이프하며 마음에 드는 코드나 개발자에게 좋아요를 눌러 매칭됩니다. 매칭이 성사되면, 채팅 기능을 통해 손쉽게 소통할 수 있습니다.

### 🔑 핵심 기능 (Key Features)
**게시글 작성**: 팀원을 찾기 위해 게시글을 작성하고 필요한 기술 스택을 공유
- **스와이프 매칭**: 소개팅 앱처럼 사용자의 코드나 프로필을 스와이프하여 매칭
- **좋아요 기능**: 마음에 드는 개발자에게 좋아요를 눌러 매칭
- **채팅 시스템**: 매칭된 사용자와 실시간으로 채팅을 통해 협업 시작
- **프로필 관리**: 개발자들이 자신의 기술 스택과 프로젝트 경험을 프로필에 추가하여 더욱 명확한 매칭

### 개발 일정
![개발일정](https://github.com/user-attachments/assets/e4316236-dbea-4426-ae62-f371a811b773)
![개발일정 표사ㅣ](https://github.com/user-attachments/assets/849aa146-5176-4fa8-9ee9-ffec275aec76)
<br>

# Architecture

<br>

# 플로우 차트 , 피그마(화면설계)
### 플로우 차트
![image](https://github.com/user-attachments/assets/ef3a17d0-aaef-4517-ae2f-df25725bee92)
### Figma(화면설계)
![피그마1](https://github.com/user-attachments/assets/89ef44b8-f73d-424a-8703-ed281c3f07d3)
![피그마2](https://github.com/user-attachments/assets/eaaa9a38-3a72-43d7-af1a-dbd159a4cc4a)
![피그마3](https://github.com/user-attachments/assets/4f351397-3fed-4c59-8596-9a53a96c5366)
<br>

# ERD
![ERD](https://github.com/user-attachments/assets/9ecd9965-85b4-4b05-9f82-be73bba7d0cd)

**ERD 클라우드 주소**: [https://www.erdcloud.com/d/MQqYSZjC4o2ynXC2T](https://www.erdcloud.com/d/MQqYSZjC4o2ynXC2T)

<br>


# API 명세
### BOARD

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| boardView | GET | /board | 게시판 목록 페이지 조회 |
| boardSaveView | GET | /board/saveView | 게시판 생성 화면 조회 |
| boardSave | POST | /board/save | 게시판 저장 |
| boardUpdateView | GET | /board/updateView/{id} | 게시판 수정 화면 조회 |
| boardUpdate | POST | /board/update | 게시판 수정 |
| deleteBoard | POST | /board/delete/{id} | 게시판 삭제 |
| boardRestore | POST | /board/restore/{id} | 게시판 복구 |

### POST

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| postListView | GET | /post | 게시글 메인 페이지 조회 |
| postListViewById | GET | /post/{classification}/{id} | userId or boardId로 게시글 리스트 출력 |
| postDetailView | GET | /post/view/{id} | 게시글 상세 페이지 조회 |
| postSaveView | GET | /post/saveView | 게시글 생성 페이지 조회 |
| postSave | POST | /post/Save | 게시글 생성 |
| postUpdateView | GET | /post/updateView/{Id} | 게시글 수정 페이지 조회 |
| postDelete | POST | /post/delete/{Id} | 게시글 삭제 |
| postCommentDelete | POST | /post/api/commentDelete | 댓글 삭제  |
| postCommentEdit | POST | /post/api/commentEdit | 댓글 수정  |
| postComment | POST | /comment/{id} | 댓글 생성 |
| aboutUs | GET | /aboutUs | 팀원 소개 페이지 조회 |

### SWIPE

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| home | GET | / | 스와이프 화면 조회 |
| getSwipePage | GET | /swipe | 스와이프 화면 조회 |
| toggleLike | POST | /swipe/api/toggleLike | 좋아요 요청 |
| blockPost | POST | /swipe/api/blockPost | 스와이프 관심없음 |

### Choose

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| getChoicePage | GET | /choose/{id} | 좋아요 수락창 조회 |
| getLikesPage | GET | /swipe/likes/{Id} | 좋아요 누른 게시글 목록 조회 |
| getJsons | POST | /swipe/Jsons | 좋아요 수락 |
| getLikeJsons | POST | /swipe/likeJsons | 좋아요 누른 게시글 응답 |

### Member

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| signUpForm | GET | /sign-up | 회원가입 페이지 조회 |
| signUp | POST | /sign-up | 회원가입 |
| loginPage | GET | /login | 로그인 페이지 조회 |
| deleteMember | POST | /member/delete | 회원탈퇴 |
| showProfile | GET | /member/profile | 내 정보 조회 |
| updateProfile | POST | /member/profile | 내 정보 수정 |
| findForgotPassword | GET | /member/forgot-password | 비밀번호 찾기 페이지 조회 |
| forgotPasswordRequest | GET | /member/forgot-password-request | 비밀번호 찾기 |
| checkEmail | GET | /member/check-email | email 중복검사 |
| checkNickname | GET | /member/check-nickname | 닉네임 중복검사 |
| checkPhoneNumber | GET | /member/check-phone-number | 휴대전화 중복검사 |
| extraInfoForm | GET | /addtional-info | OAuth2.0 방식 회원가입 페이지 조회 |
| saveAdditionalInfo | POST | /additional-info | OAuth2.0 방식 회원가입 |
| uploadProfileImage | POST | /uploadProfileImage | 프로필 이미지 업로드  |
| defaultImage | POST | /defaultProfileImage | 기본 프로필 이미지 업로드 |

### Chat

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| chat | GET | /chatting/{rooId} | 채팅방 불러오기 |
| ChattingRoomMatching | POST | /chatroom/api/requestMatchingResult | 채팅방 생성 |

### WebSocket api 명세

| **메서드명** | **메시징 경로** | **역할** |
| --- | --- | --- |
| sendMessage | /app/sendMessage/{roomId} | 채팅 메시지 전송 |
<br>

# 구성화면 , 시연 영상

<br>

# 트러블 슈팅
yh
### 발견한 트러블

- 변경감지를 통해 update를 사용하였는데 test코드에 @Transaction을 붙히지 않아 테스트가 끝나고 update가 반영되는 문제가 발생

### 해결법

- 트랜잭션 내에서 변경된 엔티티가 1차 캐시에서 감지되었고, 트랜잭션 종료 시점에 변경 내용이 db에 반영되는 것을 확인하여 test코드 내에서 @Transaction을 사용하여 변경감지가 바로 반영되도록 수정하여 해결함
   - JPA 사용 시 트랜잭션이 변경감지와 1차 캐싱에 어떤 영향을 미치는지 명확히 이해하는 계기가 되었습니다.
   - 테스트 코드 작성 시 트랜잭션이 필요한 테스트와 그렇지 않은 테스트를 명확히 구분하고, 상황에 맞는 설정을 적용하도록 코드를 개선할 계획입니다.

   
<br>

yb
### 발견한 트러블

- 회원가입시 폼기반회원과 OAuth의 email정보가 중복되어서
`DuplicatedEmailException` 이 출력되고 로그인페이지로 리디렉션되는 현상을 확인

### 해결법

- 회원가입 정보입력 시 유효성검사와 중복검사를 통과했음에도 회원가입버튼이 비활성화 상태로 유지되는것을 확인
    - 입력필드에서의 비동기적 작동방식으로 작동하게하는 함수가 있음으로 `field.isValid` 부분이 현재상태를 반영하지못해 일어난 문제 → 폼제출을 위한 `updateSubmitButtonState();` 를 추가하여 현재 상태를 반영하여 활성화 되도록 구성
<br>

sh

<br>
