package com.example.jpapractice.repository;

import com.example.jpapractice.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Query("SQL문")
 * -> 2번 3번 메서드들은 하나의 SQL문으로 해결이 안됨
 * -> 마이바티스처럼 커스텀 SQL문(Jpql, native SQL)을 만드는 어노테이션
 * -> 이 문제를 마이바티스에서는 mapper.xml을 사용하기 때문에 겪을 일이 없음
 *
 *  ENTITYMANGER
 *  내부 내용은 SimpleJpaRepository에서 볼 수 있음
 *  기능 -> 선언시 (영속성 객체의)프록시 객체로 해당 내용들을 준비하고 있다가 실행 시 db에 접근
 */

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