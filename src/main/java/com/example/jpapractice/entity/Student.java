package com.example.jpapractice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 *  단방향 연관관계
 *  N:1의 관계에서 N인 엔티티에 참조어노테이션을 첨부
 *  앞서서했던 단계들은 `양방향 참조`를 사용하기 때문에 일어나던 문제임
 *  `단방향 참조`를 해버린다면, 순환참조 문제가 일어나지가 않음
 *  서로가 참조가 아니기 때문에 교실에서는 학생을 찾지 않음
 *  -> 교실조회를 해보면 반의 선생님만 나오게됨
 *  그렇다면 기존처럼 학생이름이 나오기 위해서는
 *  controller에서 studentService를 호출해서 사용하는 방법도 있음
 *
 *  이 방법의 단점: 내가 코드를 더 적어야해서 귀찮다. DB에서는 자연스럽게 FK관계로 연결되던 것들이 여기에서는 그렇지 않음
 */

/**
 * 그러면 왜 이렇게 함?
 * -> 순환참조(직렬화 에러)들로부터 안정성 확보
 * -> 학생만 본인 반이 어딘지만 알고 있으면 됨
 * -> 반을 조회하고 학생서비스의 반으로 찾기를 사용하여 조회함(디버깅/테스트 용이)
 * -> 연관 관계에서 변경사항이 생긴다면 학생쪽만 수정을 하면 되기 때문에 (유지 보수가 편하다)
 * >> 귀찮을 수 있지만, 명확하고 안정적인 구조(=단기 개발보다 장기 유지보수와 확장성에서는 탁월한 선택)
 */



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
    private ClassRoom classRoom;
} 