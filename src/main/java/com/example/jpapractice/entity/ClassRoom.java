package com.example.jpapractice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 어노테이션으로 해결
 * 앞서 보았던 Jackson이 단순하게 적혀있는대로 조회를 하다보니 재귀 구조의 호출이 발생한거고,
 * 이걸 해결하기 위해 Jackson에게 어떻게 조회할지를 정해주는 것임
 * Jackson의 직렬화/역직렬화
 * json은 클라이언트와 서버가 소통하는 방법(연락형식)
 * 직렬화 -> api에서 json에게
 * 역직렬화 -> json에서 api에게
 */

/**
 * 해결방법 1. @JsonIgnore
 * 해당 어노테이션이 붙은 필드값은 Jackson에서 무시됨
 * -> 계속하여 정보가 들어가는 것이 문제였는데 직렬화자체에서 무시되어 문제가 해결됨
 * 단점: 역직렬화또한 되지 않기 때문에 순환참조는 해결하였지만, 해당 필드값(=컬럼)이 반환되지 않음
 * -> 현재 코드에서는 학생이 속해 있는 교실 정보가 누락
 * -> 그럼 서비스 단에서 findclass같은 메서드로 추가적인 조회 후 반환 필요
 */

/**
 * 해결방법 2. @JsonManagedReference/@JsonBackReference
 * Back은 양방향 관계 중 자식에게 Managed는 부모에게 붙여준다
 * Jackson은 어노테이션을 통해 Managed는 직렬화 대상(부모)임을 BackReference는 직렬화대상아님(자식)임을 알 수 있게됨
 * 단점: 학생을 조회했을 때 학생이 속한 교실 정보는 여전히 반환되지 않음
 * -> ignore는 무시를 해버려서, 무시하지않고 순서를 정해주는 어노테이션이지만 ignore처럼 학생의 소속반은 나오지 않음
 */

/**
 * 해결방법 3. @JsonIdentityInfo
 * 순환참조를 해결하는 방식이기는 하나 권장되는 방식도 아니고, 편하지도 않음(json(반환 값)도 다름)
 * 다음 기회에 다루겠음..
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

    /** mappedBy 양방향 관계시 부모가 누구인지 적어줘야함
     *  안적어주면 join테이블을 생성함 = 리소스 낭비
     *  그러나 단방향 관계시는 필요가 없음
     */
    @OneToMany(mappedBy = "classRoom")
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();
} 