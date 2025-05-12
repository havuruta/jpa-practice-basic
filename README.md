
# JPA 실습 - 순환 참조 문제 해결하기

## 현재 발생하는 문제

### 1. 순환 참조 오류
```json
{
    "timestamp": "2025-04-30T15:45:14.678+09:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]",
    "path": "/api/students/1"
}
```

### 2. Hibernate 프록시 직렬화 오류
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor 
and no properties discovered to create BeanSerializer
```

## 해결해야 할 과제

### 1. 엔티티 수정
- `Student`와 `ClassRoom` 엔티티의 양방향 연관관계 설정
- 순환 참조를 방지하기 위한 Jackson 어노테이션 추가
- 지연 로딩 관련 설정 최적화

### 2. 테스트 케이스 작성
```java
@Test
void testCircularReference() {
    // 1. 반 생성
    ClassRoom classRoom = new ClassRoom();
    classRoom.setName("1반");
    classRoom.setCapacity(30);
    classRoom.setTeacherName("김선생");
    ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

    // 2. 학생 생성
    Student student = new Student();
    student.setName("홍길동");
    student.setAge(15);
    student.setClassRoom(savedClassRoom);
    Student savedStudent = studentRepository.save(student);

    // 3. API 호출 테스트
    // GET /api/students/{id} 호출 시 순환 참조 오류 발생 확인
    // GET /api/classrooms/{id} 호출 시 순환 참조 오류 발생 확인
}
```

### 3. 해결 방안 고민
1. `@JsonIgnore` 사용
2. `@JsonManagedReference`와 `@JsonBackReference` 사용
3. DTO 패턴 적용
4. Jackson 설정 변경

## 학습 목표
1. JPA의 지연 로딩과 프록시 객체 이해
2. Jackson 직렬화 과정에서의 순환 참조 문제 이해
3. 다양한 해결 방안의 장단점 비교
4. REST API에서의 엔티티 직렬화 최적화

## 체크리스트
- [ ] 엔티티 수정
- [ ] 테스트 케이스 작성
- [ ] 해결 방안 구현
- [ ] API 테스트
- [ ] 문서화

## 참고 자료
- [Jackson 직렬화 문서](https://github.com/FasterXML/jackson-docs)
- [Hibernate 프록시 문서](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#associations)
- [Spring Data JPA 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)

이 브랜치에서 순환 참조 문제를 해결하면서 JPA와 Jackson의 동작 방식을 깊이 이해할 수 있습니다. 다양한 해결 방안을 시도해보고 각각의 장단점을 비교해보세요.
=======
# JPA 실습 스터디 안내

##  스터디 목적

이 저장소는 **JPA의 실무 활용에 필요한 핵심 개념**을 실습하며 익히기 위한 스터디입니다.  
엔티티 간 연관관계 매핑, 순환 참조 문제 해결, 성능 최적화(N+1 문제 해결) 등을 직접 구현하며 학습합니다.

## 📁 실습 방식

각 실습 주제마다 별도 브랜치를 기준으로 개별 브랜치를 파서 실습합니다.

###  실습 브랜치 규칙

- 형식: `{문제 브랜치명}-{이름}`  
  예시: `fix-circular-reference-with-dtos-홍길동`, `fix-N+1-issue-김싸피`
- 실습 브랜치는 반드시 `main`이 아닌 주제 브랜치에서 파생
- 실습 완료 후 PR (Pull Request)은 **해당 주제 브랜치**로 보낼 것

##  실습 주제 목록

### 1. 순환 참조 문제 해결 실습

> 브랜치: [`check-circular-reference`](https://github.com/havuruta/jpa-practice-basic/tree/check-circular-reference)

- 목표: 양방향 연관관계에서 발생하는 Jackson 순환 참조 문제 이해 및 해결
- 발생 오류 예시:
  - `ByteBuddyInterceptor` 직렬화 실패
  - `InvalidDefinitionException` 예외
- 학습 포인트:
  - @JsonManagedReference / @JsonBackReference
  - @JsonIgnore
  - DTO로 해결하는 방식
  - Hibernate 프록시 객체 직렬화 대응

**실습 브랜치 예시**: `fix-circular-reference-with-dtos-홍길동`

---

### 2. 순환 참조 해결 방식 비교 실습

> 브랜치: [`fix-circular-reference`](https://github.com/havuruta/jpa-practice-basic/tree/fix-circular-reference)

- 목표: 어노테이션 방식으로 순환 참조 해결해보기
- 방법:
  - ClassRoom → Student: `@JsonManagedReference`
  - Student → ClassRoom: `@JsonBackReference`
- 한계: 복잡한 구조에서는 유연성 부족
- 학습 포인트:
  - 어노테이션 방식의 장단점 이해
  - API 응답의 제약 확인

**실습 브랜치 예시**: `fix-circular-reference-홍길동`

---

### 3. DTO 패턴을 통한 순환 참조 해결 실습

> 브랜치: [`fix-circular-reference-with-dtos`](https://github.com/havuruta/jpa-practice-basic/tree/fix-circular-reference-with-dtos)

- 목표: 엔티티를 직접 노출하지 않고 DTO를 통해 순환 참조 회피
- 학습 포인트:
  - Entity → DTO 변환 로직 구현
  - 필요한 필드만 포함한 응답 생성
  - 유지보수성과 확장성 확보

**실습 브랜치 예시**: `fix-circular-reference-with-dtos-홍길동`

---

### 4. 단방향 연관관계로 순환 참조 제거

> 브랜치: [`fix-circular-reference-with-Unidirectional`](https://github.com/havuruta/jpa-practice-basic/tree/fix-circular-reference-with-Unidirectional)

- 목표: 연관관계를 단방향으로 설정하여 순환 참조 자체를 없애기
- 학습 포인트:
  - 설계 단순화
  - 유지보수성 향상
  - 연관관계 주인 설정 이해

**실습 브랜치 예시**: `fix-circular-reference-with-Unidirectional-홍길동`

---

### 5. N+1 문제 해결 실습

> 브랜치: [`fix-N+1-issue`](https://github.com/havuruta/jpa-practice-basic/tree/fix-N%2B1-issue)

- 목표: JPA에서의 N+1 문제 발생 조건과 해결 방법 학습
- 해결 방법:
  - Fetch Join 사용
  - EntityGraph 활용
  - Batch Size 설정
- 테스트 실행 방법 제공:
  ```bash
  ./gradlew test --tests "com.example.jpapractice.JpaNPlusOneTest.testNPlusOneSolution"
  ```

## 학습 목적

- LAZY 로딩의 위험성
- SQL 쿼리 로그 분석
- 성능 튜닝 실습
