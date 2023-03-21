# 투개더 📺

![투개더 리드미](https://user-images.githubusercontent.com/120078825/223928527-0912cecf-bc93-44af-b222-50c530ec0f83.png)

## **우리 강아지 주변 친구들이랑 같이 산책하자 !**

위치 정보를 통해 주변 반려인들과 산책의 기회를 제공하는 웹앱 기획

🐶[우리 강아지 친구를 만들어 보자! 투개더 사용해보기](https://twogaether.site)

<br>

## 1. 제작 기간 & 팀원 소개

    [BE]
  - 고현우 : cicd 연결, nginx 등 서버 설정 및 연결, 소셜로그인, 좋아요와 매칭 기능, 이미지 구현
  - 이상휘 : 이메일 인증, 유저 정보
  - 이성진 : 채팅 기능, 강아지 정보

<br>

## 2. 아키텍쳐

![image](https://user-images.githubusercontent.com/119824778/226522556-e856bfc9-8f4e-4fd1-aa10-56316c436490.png)

<br>
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/LINUX-005496?style=for-the-badge&logo=linux&logoColor=white"><img src="https://img.shields.io/badge/NGINX-3D1332?style=for-the-badge&logo=nginx&logoColor=white"><img src="https://img.shields.io/badge/STOMPJS-lightgrey?style=for-the-badge&logo=stompjs&logoColor=white">

<br>

## 3. 실행화면

![스크린샷(103)](https://user-images.githubusercontent.com/120078825/223935126-6017a818-8199-4873-820a-df8edf20788a.png)

![스크린샷(104)](https://user-images.githubusercontent.com/120078825/223935151-b256eff6-b833-483c-892f-16e34681e94f.png)


<br>

## 4. 핵심기능

1. 로그인 기능 구현 ( 기본 회원가입 + 카카오톡 DB 회원가입 )
2. 강아지 추가하기 ( Form Data 방식으로 데이터 값을 백엔드와 교류 + GPS 데이터를 로컬기능을 통해서 위도경도값을 DB로 보내는 방식)
3. 데이터 값을 기반으로 유저 매칭 (유저간의 거리 비교 및 데이터를 가져와서 유저간의 매칭을 가능하게 해주는 기능)
4. 좋아요 기능 구현(받고 보낸 좋아요 데이터를 따로 모으며 프로필 확인과 채팅을 위해 매칭되는 기능 구현)
5. Stompjs를 통해 채팅 기능 구현 ( 채팅기능을 구현하기위해 필요하다 생각한 개념입니다. )

<br>

## 5. ERD

![투개더](https://user-images.githubusercontent.com/119824778/226522267-0b1891f6-9c97-426a-bb85-6ceee31f3094.png)

<br>

## 5. 아키텍쳐 도입 배경
[아키텍쳐 도입 배경 링크](https://pineapple-wrist-347.notion.site/82ef25bf258341a4a31fbad9f7a0e786)

<br>

## 7. 개인 회고

 - 고현우 : 
 - 이상휘 : 
 - 이성진 : 
