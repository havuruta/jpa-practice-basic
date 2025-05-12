package com.example.jpapractice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    // @JsonBackReference // 순환 참조 해결 방법 1: 역방향 참조를 JSON 직렬화에서 제외
    // @JsonIgnore // 순환 참조 해결 방법 2: 해당 필드를 JSON 직렬화에서 완전히 제외

//    @JsonIgnore
    @JsonBackReference
    private ClassRoom classRoom;
} 