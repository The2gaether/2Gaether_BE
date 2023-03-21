# 투개더 📺

![투개더 리드미](https://user-images.githubusercontent.com/120078825/223928527-0912cecf-bc93-44af-b222-50c530ec0f83.png)

## **우리 강아지 주변 친구들이랑 같이 산책하자 !**

위치 정보를 통해 주변 반려인들과 산책의 기회를 제공하는 웹앱 기획

🐶[우리 강아지 친구를 만들어 보자! 투개더 사용해보기](https://twogaether.site)

<br>

## 1. 제작 기간 & 팀원 소개

  - **기술 스택 : Java11, Spring boot 2.7.8, Spring Data JPA, MYSQL 8.0, Querydsl**
  - 기간  :  2023.02.02 ~ 2023.03.15

- Backend
  - 고현우 : cicd 연결, nginx 등 서버 설정 및 연결, 소셜로그인, 좋아요와 매칭 기능, 이미지 저장소 구현
  - 이상휘 : 이메일 인증, 유저 정보
  - 이성진 : 채팅 기능, 강아지 정보

<br>

## 2. 아키텍쳐

![image](https://user-images.githubusercontent.com/119824778/226522556-e856bfc9-8f4e-4fd1-aa10-56316c436490.png)

<br>

[아키텍쳐 도입 배경 링크](https://pineapple-wrist-347.notion.site/82ef25bf258341a4a31fbad9f7a0e786)

<br>

## 3. 실행화면

![스크린샷(103)](https://user-images.githubusercontent.com/120078825/223935126-6017a818-8199-4873-820a-df8edf20788a.png)

![스크린샷(104)](https://user-images.githubusercontent.com/120078825/223935151-b256eff6-b833-483c-892f-16e34681e94f.png)


<br>

## 4. 핵심기능

1. 로그인 기능 구현 ( 기본 회원가입 + 카카오톡 소셜 로그인 )
2. 유저 정보 및 강아지 추가하기 ( 카카오지도 Open API를 통해 현재 위치의 위도,경도값을 저장하여 거리를 계산 )
3. 데이터 값을 기반으로 유저 매칭 ( 유저의 강아지 정보 및 거리 비교를 통해 원하는 상대와 매칭이 가능 )
4. 좋아요 기능 구현 ( 보낸 / 받은 좋아요 페이지에서 상대 강아지 프로필 확인 및 실시간 채팅 가능 )
5. Stompjs를 통해 채팅 기능 구현 ( 실시간 채팅을 위해 STOMP 프로토콜 사용 )

<br>

## 5. ERD

![투개더](https://user-images.githubusercontent.com/119824778/226522267-0b1891f6-9c97-426a-bb85-6ceee31f3094.png)

<br>


