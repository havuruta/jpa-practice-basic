package com.example.jpapractice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    
    @ManyToOne
    @JoinColumn(name = "class_room_id")
    @JsonBackReference
    /*
        만약 @JsonIgnore를 사용한다면 여기에 달아야함
        대신 JSON응답에서 사라짐

        @JsonIgnore를 사용시 학생이 속한 반의 학생 정보를 가져오고 싶을 때 순환참조는
        피할 수 있으나 학생 리스트를 가져올 수 없는 문제가 생긴다

        현재의 구조는 Student 클래스의 필드에 classRoom에 @JsonBackReference를
        사용해 직렬화 시 classRoom필드의 참조를 끊어서 순환 참조를 방지한다
     */
    private ClassRoom classRoom;
} 