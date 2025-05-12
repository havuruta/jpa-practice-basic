package com.example.jpapractice.repository;

import com.example.jpapractice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // 기본 CRUD 메서드는 JpaRepository에서 상속받음
    
    // 연관관계를 활용한 쿼리 예시
    List<Student> findByClassRoomId(Long classRoomId);
    
    // JPQL을 사용한 조인 쿼리 예시
    @Query("SELECT s FROM Student s JOIN s.classRoom c WHERE c.name = :className")
    List<Student> findByClassName(@Param("className") String className);
    
    // 페이징 처리가 포함된 쿼리 예시
    @Query("SELECT s FROM Student s WHERE s.age >= :minAge")
    List<Student> findStudentsByAge(@Param("minAge") Integer minAge);
    
    // 메서드 이름 기반 쿼리 예시
    List<Student> findByNameContaining(String name);
} 