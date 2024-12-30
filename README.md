

# Commit Date
![logo](https://github.com/user-attachments/assets/3ef561c5-6241-4cfc-8cea-d0655049a5d4)

개발자를 위한 팀원 매칭 서비스
<br>

# 목차
1. 팀원 소개 및 역할
2. 사용 기술 스택
3. 접속 방법 , 배포주소
4. 프로젝트 개요 및 개발 일정
6. Architecture
7. 플로우 차트 , 피그마(화면설계)
8. ERD
9. API 명세
10. 구성화면 , 시연 영상
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

# 사용 기술 스택
spring , js, thymeleaf, jpa, security , mariaDB , (smtp? , sockjs) , aws(EC2, RDS)
# 접속 방법
## aws 배포 주소 : http://3.39.0.173/
<br>
<br>

# 프로젝트 개요 및 개발 일정
![개발일정](https://github.com/user-attachments/assets/e4316236-dbea-4426-ae62-f371a811b773)
![개발일정 표사ㅣ](https://github.com/user-attachments/assets/849aa146-5176-4fa8-9ee9-ffec275aec76)
<br>

# Architecture

<br>

# 플로우 차트 , 피그마(화면설계)
## 플로우 차트
![image](https://github.com/user-attachments/assets/ef3a17d0-aaef-4517-ae2f-df25725bee92)
## Figma(화면설계)
![피그마1](https://github.com/user-attachments/assets/89ef44b8-f73d-424a-8703-ed281c3f07d3)
![피그마2](https://github.com/user-attachments/assets/eaaa9a38-3a72-43d7-af1a-dbd159a4cc4a)
![피그마3](https://github.com/user-attachments/assets/4f351397-3fed-4c59-8596-9a53a96c5366)
<br>

# ERD
![ERD](https://github.com/user-attachments/assets/9ecd9965-85b4-4b05-9f82-be73bba7d0cd)
<div align="center">
    **ERD 클라우드 주소** : [https://www.erdcloud.com/d/MQqYSZjC4o2ynXC2T](https://www.erdcloud.com/d/MQqYSZjC4o2ynXC2T)
</div>
<br>


# API 명세
## BOARD

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| boardView | GET | /board | 게시판 목록 페이지 조회 |
| boardSaveView | GET | /board/saveView | 게시판 생성 화면 조회 |
| boardSave | POST | /board/save | 게시판 저장 |
| boardUpdateView | GET | /board/updateView/{id} | 게시판 수정 화면 조회 |
| boardUpdate | POST | /board/update | 게시판 수정 |
| deleteBoard | POST | /board/delete/{id} | 게시판 삭제 |
| boardRestore | POST | /board/restore/{id} | 게시판 복구 |

## POST

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

## SWIPE

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| home | GET | / | 스와이프 화면 조회 |
| getSwipePage | GET | /swipe | 스와이프 화면 조회 |
| toggleLike | POST | /swipe/api/toggleLike | 좋아요 요청 |
| blockPost | POST | /swipe/api/blockPost | 스와이프 관심없음 |

## Choose

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| getChoicePage | GET | /choose/{id} | 좋아요 수락창 조회 |
| getLikesPage | GET | /swipe/likes/{Id} | 좋아요 누른 게시글 목록 조회 |
| getJsons | POST | /swipe/Jsons | 좋아요 수락 |
| getLikeJsons | POST | /swipe/likeJsons | 좋아요 누른 게시글 응답 |

## Member

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

## Chat

| **메서드명** | **HTTP 메서드** | **엔드포인트** | **역할** |
| --- | --- | --- | --- |
| chat | GET | /chatting/{rooId} | 채팅방 불러오기 |
| ChattingRoomMatching | POST | /chatroom/api/requestMatchingResult | 채팅방 생성 |

## WebSocket api 명세

| **메서드명** | **메시징 경로** | **역할** |
| --- | --- | --- |
| sendMessage | /app/sendMessage/{roomId} | 채팅 메시지 전송 |
<br>

# 구성화면 , 시연 영상

<br>

# 트러블 슈팅

<br>

