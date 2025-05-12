package com.example.jpapractice.repository;

import com.example.jpapractice.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    
    // 기본 CRUD 메서드는 JpaRepository에서 상속받음
    
    // JPQL을 사용한 쿼리 예시
    @Query("SELECT c FROM ClassRoom c WHERE c.capacity > :minCapacity")
    List<ClassRoom> findByCapacityGreaterThan(@Param("minCapacity") Integer minCapacity);
    
    // 메서드 이름 기반 쿼리 예시
    List<ClassRoom> findByTeacherName(String teacherName);
    
    // @Query와 네이티브 쿼리 사용 예시
    @Query(value = "SELECT * FROM class_rooms WHERE capacity > :minCapacity", nativeQuery = true)
    List<ClassRoom> findLargeClassRooms(@Param("minCapacity") Integer minCapacity);
} 