package com.example.jpapractice.entity;

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
    private List<Student> students = new ArrayList<>();

    /*
        N+1 문제
        쿼리를 한 번 날렸더니 N개가 서비스?!
        부모 Entity를 1번 조회 했더니 그에 딸린 자식 Entity를 N번 쿼리해서 가져오는 문제

        반을 하나 찾아서 (1) -> 그 반에 있는 학생을 하나하나(N)번 가져왔다

        @OneToMany @ManyToOne등 연관 관계가 있고, LAZY 로딩일 경우 발생

        EAGER -> 연관된 엔티티를 즉시 함께 조회, select쿼리를 날릴때 조인해서 한번에 가져옴
        장점 : 객체를 사용할 때 이미 연관 데이터가 모두 준비되어 있음
        단점 : 필요 없어도 일단 불러워서 성능 낭비일 수 있음

        LAZY -> 연관된 엔티티는 프록시 객체로 두고, 실제 사용될 때 쿼리를 실행
        장점 : 쿼리 수 최소화 가능, 성능 최적화에 유리
        단점 : N+1 문제 발생할 수 있음

        프록시 직렬화 문제
        LAZY 로딩으로 Entity가 아직 초기화 되지 않았고
        Jackson이 이를 직렬화 할 수 없어 예외가 나는 문제

        해결방법
        1. 프록시 무시 설정
        SpringBoot 설정에서 순환 참조/프록시 관련 감지 기능을 끄는 것
        문제를 숨기는 것 구조 개선이 아님
        프록시 객체 내부 값이 초기화 되지 않았을 경우 null로 나갈 수 있음

        2. Hibernate 모듈 등록 (프록시 대응)
        Jackson에게 "Hibernate 프록시 객체 직렬화는 이렇게 해" 라고 알려주는 모듈
        설정할 경우 프록시 객체가 자동으로 처리되거나 무시됨

        3. DTO로 필요한 필드만 꺼내서 반환
        Entity를 직접 JSON으로 보내지 않고, 필요한 정보만 담은 DTO 클래스를 만들어 직렬화 대상에서 프록시 제거
        구조 안정성, 확장성, 보안(민감 정보 제외) 확보
        순환 참조도 방지 가능





     */

    /*
        1. @JsonIgnore 사용
        장점 : 간단하게 순환참조를 피할 수 있음
        단점 : 해당 필드 (Column)값이 완전하게 JSON 응답에서 제외되어 해당 값이 필요한 경우에는 곤란함

        2. @JsonManagedReference, @JsonBackReference
        장점 : JsonIgnore와 달리 부모 자식 관계를 만들어줘(직렬화 방향을 명확하게 가능) 순환 참조 해결이 가능
        단점 : 반대의 경우로 참조를 할 수 없고(역방향으로는 직렬화 되지 않아 응답에 포함X 라는 뜻),
              현재의 경우에는 classsroom과 student의 관계가 단순하지만 복잡해질 경우 어노테이션이 많아져 가독성이 좋지 않아질 수 있음

        3. DTO 패턴 적용
        장점 : 구조가 복잡한 상황에서도 DTO를 JSON으로 전송하기 때문에 위 두 방법보다 안정적, 중요한 정보는 가릴 수 있음
        단점 : DTO를 매번 작성해야하기 때문에 시간이 추가적으로 필요함

        4. Jackson 설정 변경
            (1) @JsonIdentityInfo
            객체를 JSON으로 직렬화 할 때, 같은 객체가 반복적으로 등장하면 ID로 치환해서 순환 참조를 방지하는 기능
            장점 : @JsonBackReference보다 더 유연, 복잡한 객체 그래프도 처리 가능
            단점 : JSON 구조가 ID 위주로 되어 있어 이해가 어려울 수 있음
                  동일 ID가 여러번 등장할 수 있어 일부 클라이언트에서 혼란
                  응답이 객체 중심이 아닌 ID중심이 되기 떄문에 구조 파악이 어려움

            (2) Jackson ObjectMapper 설정 (Spring 전역 설정)
            순환 참조 감지시 예외를 던질 지 여부
            최신 Jackson에서는 기본적으로 순환 참조 감지 후 예외를 던지도록 되어 있음
            장점 : 직렬화 예외를 방지해 응답을 실패 없이 처리 가능
            단점 : 구조적인 해결책 X, 실제로는 무한 루프가 숨어 있을 수 있음
     */

} 