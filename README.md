# moono
Moono Week mentoring project

GET /members/checkID : 아이디 중복 여부 확인

Post /members/register : 회원 가입
- 중복된 id 로 시도시 에러 발생
- 에러 메시지 : “중복된 아이디를 사용할 수 없습니다.“

Delete /members/deregister : 회원 탈퇴
- 토큰 통해 인증 기능 구현

Get /members/myPage : 회원 상세 정보 조회
- 토큰 통해 인증 기능 구현

Post /members/login : 회원가입시 제출한 아이디/비밀번호를 이용해, 다른 API를 호출 할 수 있는 인증토큰 발급
- 이후 발급 받은 인증토큰을 통해 게시글 관리 API 호출 가능

Post /posts : 게시글 등록

Put /posts/{postId} : 본인이 등록한 게시글 수정
- 본인이 작성한 게시글이 아닐 경우 에러 발생
- 에러 메시지 : "본인이 작성한 게시글만 수정할 수 있습니다."

Delete /posts/{postId} : 본인이 등록한 게시글 삭제
- 본인이 작성한 게시글이 아닐 경우 에러 발생
- 에러 메시지 : "본인이 작성한 게시글만 삭제할 수 있습니다."
- 해당 게시글에 달린 댓글 및 대댓글 전부 삭제

Get /posts : 게시글 리스트 조회
- 페이징 처리, 사이즈 5

Get /posts/{postId} : 게시글 상세 정보 조회
- 게시글 조회수 확인 가능
- 본인이 작성한 글의 상세 정보를 조회할 경우 조회수가 카운트 되지 않음
- 상세 정보를 조회한 계정은 24시간 동안 추가로 카운트 되지 않음

Post /posts/{postId}/comments : 댓글 및 대댓글 작성

Get /posts/{postId}/comments : 게시글의 모든 댓글 및 대댓글 조회

Get /posts/{postId}/comments/{commentId} : 댓글 및 대댓글 조회
