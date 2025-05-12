package com.example.jpapractice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "class_rooms")
public class ClassRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(name = "teacher_name")
    private String teacherName;

    @OneToMany(mappedBy = "classRoom")
    // @JsonManagedReference // 순환 참조 해결 방법 1: 정방향 참조를 JSON 직렬화에 포함
    // @JsonIgnore // 순환 참조 해결 방법 2: 해당 필드를 JSON 직렬화에서 완전히 제외

    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    /*
    1. 순환 참조 오류

    ● 직렬화
    - 객체를 저장하거나 전송하기 위해 문자열이나 이진 형태로 변환하는 것.
    - Java 객체 <-> JSON 문자열

    ● 직렬화를 하는 이유
    - API 응답을 JSON으로 주려면 직렬화 필요
    - 데이터를 파일에 저장하거나 네트워크로 보낼 때 문자열로 변환해야 함
    - 객체는 Java 프로그램 안에서만 쓸 수 있음 -> 외부 시스템과 주고받으려면 문자열로 바꿔야 함

    ● Jackson은 직렬화를 위한 라이브러리.
    - @RestController의 응답 자동 변환
    - @RequestBody로 JSON 데이터를 받을 때
    - API 응답/요청 등을 처리해줌


        1) @JsonIgnore
        - Jackson이 직렬화할 때 무시해야 하는 필드에 붙이는 어노테이션
        - 보통 @ManyToOne 또는 @OneToOne 관계에서 사용.
        ● 동작 원리 : Student 객체를 JSON으로 변환할 때 classRoom 필드를 출력하지 않음 -> 순환 구조를 끊게 됨.
        ● 장점
        - 가장 간단하고 직관적임
        - 설정 하나만으로 순환 참조 방지 가능
        - 유지보수 쉬움
        ● 단점
        - classRoom 정보가 응답 JSON에 아예 포함되지 않음 -> 양방향 관계인데 한쪽 응답이 비정상적으로 비어버림
        - 필요한 정보가 날아갈 수 있음


        2) @JsonManagedReference / @JsonBackReference
        Jackson이 양방향 연관관꼐에서 직렬화 시 순환을 피할 수 있게 도와주는 어노테이션 쌍.
        - @JsonManagedReference : 직렬화 대상(부모 -> 자식 방향)
        - @JsonBackReference : 역참조 대상(자식 -> 부모 방향은 직렬화 제외됨)
        ● 장점
        - 양방향 관계 그대로 유지하면서 순환 참조 해결 가능
        - 부모 기준으로 응답을 만들때는 자식까지 출력 가능
        ● 단점
        - 직렬화 방향이 한 방향으로 고정됨 -> 유연하지 않음
        - 복잡한 관계(3단계 이상 참조)에서는 적용하기 어려움
        - Jackson 내부 기능에 강하게 의존 -> 나중에 DTO로 바꾸기 어려움


        3) DTO 패턴 적용
        - Entity를 그대로 반환하지 않고, 필요한 필드만 따로 뽑은 DTO 클래스를 만들어 반환
        - 컨트롤러에서 DTO로 변환 후 반환함
        ● 장점
        - 순환 참조 문제를 원천 차단한(Entity -> JSON 직렬화 안함)
        - 필요한 정보만 제공 -> API 최적화 / 깔끔한 구조 / 유지보수 쉬움 / 확장성 좋음(API 응답을 쉽게 커스터마이징 가능)
        ● 단점
        - DTO 클래스, 변환 코드를 직접 작성해야함
        - 구조가 복잡해질 수 있음(매번 변환 코드 필요)


        4) Jackson 설정 변경
        - @JsonIdentityInfo 사용
        - Jackson에서 순환 참조를 감지하고, 객체를 ID 기준으로 한번만 출력하게 만드는 어노테이션
        ● 동작 방식 : 같은 객체가 두 번 이상 등장하면, ID로만 참조해서 순환을 막음
        ● 장점
        - 순환 잠조 자동 감지 : Jackson이 객체 ID 기준으로 순환을 방지
        - JSON에 정보 모두 포함 - @JsonIgnore와 달리 정보 손실 없음
        - 엔티티 그대로 반환 가능 - DTO 없이도 일정 수준 해결 가능
        ● 단점
        - JSON 형태가 이해하기 어려움 : ID 참조 구조의 생소함
        - 객체 ID 기준으로만 판단 : ID가 없거나 중복되면 충돌 가능성 있음
        => 실무에서 거의 안 씀 : DTO 방식이 더 명확하고 유연함


     */

    /*
    2. Hibernate 프록시 객체 직렬화 문제
    - JPA에서 @ManyToOne(fetch = FetchType.LAZY) 같은 지연 로딩을 쓰면 Hibernate는 실제 객체를 바로 로딩하지 않고, 가자(프록시) 객체를 먼저 만듦.
    - 이 프록시 객체는 실제 DB 조회는 하지 않고, 필요할 때 진짜 객체로 바뀜(초기화)
    - 이 프록시 객체를 Jackson이 JSON으로 바꾸려 하면...
    => 프록시 객체는 실제 클래스가 아니라 동적으로 만들어진 가짜 객체라 Jackson은 어떤 필드를 직렬화해야 할지 몰라서 터짐

    ● 해결책
        1) 강제 초기화 후 DTO로 변환
        DTO 패턴 쓰면서 필요한 값만 꺼내오면 프록시 문제 없음
        ClassRoom classRoom = student.getClassRoom();
        classRoom.getName(); // LAZY 강제 초기화

        StudentResponse response = new StudentResponse(student);
        return response;


        2) Jackson 설정으로 프록시 무시하게 만들기
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper()
                .registerModule(new Hibernate5Module()); // Hibernate 전용 모듈
        }


        3) 프록시 객체를 무시하는 어노테이션 사용
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

     */
} 