package com.example.jpapractice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer age;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    /*
        단방향 연관관계

        지금은 이제 Student Entity에서 일방적으로 ClassRoom Entity를 참조하고 있음
        -> 학생은 자신의 반을 알지만, 반은 어떤 학생이 있는지 모름

        장점 : 구조가 단순해 엔티티 간의 의존성이 줄어들고, 유지보수가 쉬움
              순환참조걱정 X, 직렬화/응답 DTO 처리에 유리
        단점 : 역방향 조회 X 특정 반에 있는 학생들을 모두 조회하려면 쿼리메서드를 따로 만들어야함
              비즈니스 로직에서 반과 학생의 연계처리가 어려워짐


        단방향 -> 관계가 단순하고, 한쪽에서만 참조하면 되는 경우
        양방향 -> 복잡한 API응답이나 조회의 성능이 중요한 경우

     */
    private ClassRoom classRoom;
} 