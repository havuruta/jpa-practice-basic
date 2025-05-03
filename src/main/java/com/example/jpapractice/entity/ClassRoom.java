package com.example.jpapractice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



/**
 * @Entity -> 해당 클래스를 DB에 엔티티로 선언
 * @Getter/@Setter -> lombok에서 제공해주는 getter/setter메서드 자동 생성 기능
 * @Table -> 접근할 DB의 이름
 * @Id -> PK선언, 아이디
 * @GeneratedValue(strategey = GenerationType.IDENTITY)-> 고유 ID, AUTO_INCREASEMENT
 * @Column -> 컬럼(자동으로 카멜케이스-> 스네이크로 DB접근, nullable -> null값 허용여부) 만약 컬럼이름이 다르다면 명시 필요
 * @OneToMany, @ManyToOne, @OneToOne -> 1:N, N:1, 1:1 앞이 현재 클래스
 * @JoinColumn -> FK 선언
 * @Enumerated -> 엔티티의 상태를 나타내줌 (엔티티의 타입)/ 단순 구분용
 * -> Enum은 단순한 상태를 나타낼 때, 기능에 대한 차이가 있다면 OneToOne으로
 * -> 상속된 테이블은 @Inheritance어노테이션과 추상 클래스로 선언
 * -> 추가적인 방법은 다음에
 *
 */

/** JACKSON
 *  클라이언트의 요청이 json파일로 왔을 때 Spring에서 사용할 수 있게 알맞는DTO로 변환
 *  JACKSON은 단순하게 json을 파싱하고 json으로 변환하는 역할을 함
 *  문제는 단순하게 서로가 서로를 가리키니까 JACKSON에서는 계속해서 가리키고 있는 서로를 찾음
 *  이걸 해결하기 위해 어노테이션을 사용할 수 있음
 *  @JsonIgnore를 사용해 JACKSON이 읽지 않게 만듬
 *  @JsonBackReference/@JsonManagedReference를 이용해 참조 방향을 정해줌
 *  이후 다음 실습에서 해보자
 */


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

    /** 문제 발생(순환 참조)
     *  ClassRoom과 Student는 서로가 서로를 가르킨다
     *  -> 교실이라는 엔티티에는 수용인원, 담당 선생, 소속 학생 이 등록되어있음
     *  -> 교실데이터를 불러올 때 해당하는 학생 정보를 갖고오고 싶음
     *  -> 학생데이터에는 학생이 속해있는 클래스 정보를 담고 있음
     *  ClassRoom과 Student 둘다 호출을 하게 된다면
     *  학생이 속한 교실(선생)-> 교실(선생) 출신의 학생-> 그 학생들이 가지고 있는 교실(선생)을 호출하는 순환참조 발생
     */

    /** @OneToMany(mappedBy ="참조하는 db",fetch = FetchType.LAZY, EAGER)
     *  Lazy_Loading(지연 로딩)vs Eager_Loading(즉시 로딩)
     *  지연로딩은 프록시 객체에서 준비하고 있다가 호출 시 가져옴
     *  즉시로딩은 엔티티 선언시 곧장 값을 가져옴
     *  대체적으로 Lazy를 사용(-> 필요할 때만 데이터를 가져오니까 효율적)
     *  Eager를 사용할 때는 해당 값이 있어야하는지를 고려(상태값 같은 경우 있어야함)
     *
     *  문제발생(N+1)
     *  반을 조회하고 그 안의 학생 목록이 필요함 -> 반목록 불러오기, 각 학급에 대한 학생 리스트 호출
     */

    @OneToMany(mappedBy = "classRoom")
    // @JsonManagedReference // 순환 참조 해결 방법 1: 정방향 참조를 JSON 직렬화에 포함
    // @JsonIgnore // 순환 참조 해결 방법 2: 해당 필드를 JSON 직렬화에서 완전히 제외
    private List<Student> students = new ArrayList<>();
} 