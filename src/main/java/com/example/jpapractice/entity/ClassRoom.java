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
    @JsonIgnore
    장점 : 간단하게 순환참조를 피할 수 있음
    단점 : 해당 필드 (Column)값이 완전하게 JSON 응답에서 제외되어 해당 값이 필요한 경우에는 곤란함

    @JsonManagedReference, @JsonBackReference
    장점 : JsonIgnore와 달리 부모 자식 관계를 만들어줘(직렬화 방향을 명확하게 가능) 순환 참조 해결이 가능
    단점 : 반대의 경우로 참조를 할 수 없고(역방향으로는 직렬화 되지 않아 응답에 포함X 라는 뜻), 현재의 경우에는 classsroom과 student의 관계가 단순하지만
          복잡해질 경우 어노테이션이 많아져 가독성이 좋지 않아질 수 있음

     */
} 