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


    /**
     * @JoinColumn(name = "외래키 ID")
     * 자식 엔티티에만 넣어주면 됨
     * N에 해당하는 애들이 갖고 있음
     */

    @ManyToOne
    @JoinColumn(name = "class_room_id")
    @JsonBackReference
//    @JsonIgnore
    private ClassRoom classRoom;
} 