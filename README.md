# boot-redis


## Info 
- Spring Boot - Redis 연동 프로젝트 

## Uses 
1. Spring Boot 2.7.2 
2. Redis 7.0.10
  - lettuce, RedisRepository 방식.
3. <del>Spring Security (without WebAdapter for boot 3.x version)</del>


## TODO
1. <del>Spring Security 적용 (Boot 3.X Version)</del>
   1. JWT 적용
   2. ROLE 적용 -> Privileges 관련 적용.(같은 권한내에 다른 역할부여?)
3. 로그인 회원 -> Redis Cache 적용 


## History
- 2023-07-14 <br/>
1. Redis Lock Test 증 - 회사 계정으로 Commit 함 -> seokjun

- 2023-12-28 <br/>
1. Spring Security 적용 전 랜딩페이지 작성

