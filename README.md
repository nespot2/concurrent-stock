## 재고시스템으로 알아보는 동시성이슈 해결 방법 with kotlin

### 개요

- 재고시스템으로 알아보는 동시성이슈 강의 예제 코드를 kotlin으로 작성하였습니다.
- unit test를 위해 h2 memory db, testcontainer(redis에 사용)를 사용하였습니다.
- 비지니스 코드와 redis lock 코드를 분리하기 위해 Spring AOP를 사용하였습니다.

### 강의 URL

- https://www.inflearn.com/course/%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%B4%EC%8A%88-%EC%9E%AC%EA%B3%A0%EC%8B%9C%EC%8A%A4%ED%85%9C
