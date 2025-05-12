package com.example.jpapractice;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.entity.Student;
import com.example.jpapractice.repository.ClassRoomRepository;
import com.example.jpapractice.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaPracticeTest {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 기본적인 CRUD 테스트
     */
    @Test
    @Transactional
    void basicCrudTest() {
        // Create
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("1반");
        classRoom.setCapacity(30);
        classRoom.setTeacherName("김선생");
        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        // Read
        ClassRoom foundClassRoom = classRoomRepository.findById(savedClassRoom.getId())
                .orElseThrow(() -> new RuntimeException("반을 찾을 수 없습니다"));
        assertEquals("1반", foundClassRoom.getName());

        // Update
        foundClassRoom.setName("2반");
        classRoomRepository.save(foundClassRoom);
        ClassRoom updatedClassRoom = classRoomRepository.findById(foundClassRoom.getId())
                .orElseThrow(() -> new RuntimeException("반을 찾을 수 없습니다"));
        assertEquals("2반", updatedClassRoom.getName());

        // Delete
        classRoomRepository.deleteById(updatedClassRoom.getId());
        assertFalse(classRoomRepository.existsById(updatedClassRoom.getId()));
    }

    /**
     * 연관관계 테스트
     */
    @Test
    @Transactional
    void relationshipTest() {
        // 반 생성
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("1반");
        classRoom.setCapacity(30);
        classRoom.setTeacherName("김선생");
        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        // 학생 생성
        Student student = new Student();
        student.setName("홍길동");
        student.setAge(15);
        student.setClassRoom(savedClassRoom);
        Student savedStudent = studentRepository.save(student);

        // 연관관계 확인
        Student foundStudent = studentRepository.findById(savedStudent.getId())
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다"));
        assertEquals(savedClassRoom.getId(), foundStudent.getClassRoom().getId());

        // 반의 학생 목록 확인
        List<Student> studentsInClass = studentRepository.findByClassRoomId(savedClassRoom.getId());
        assertEquals(1, studentsInClass.size());
        assertEquals("홍길동", studentsInClass.get(0).getName());
    }

    /**
     * JPQL 쿼리 테스트
     */
    @Test
    @Transactional
    void jpqlQueryTest() {
        // 테스트 데이터 생성
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setName("1반");
        classRoom1.setCapacity(20);
        classRoom1.setTeacherName("김선생");
        classRoomRepository.save(classRoom1);

        ClassRoom classRoom2 = new ClassRoom();
        classRoom2.setName("2반");
        classRoom2.setCapacity(40);
        classRoom2.setTeacherName("이선생");
        classRoomRepository.save(classRoom2);

        // 정원이 30명 이상인 반 조회
        List<ClassRoom> largeClassRooms = classRoomRepository.findByCapacityGreaterThan(30);
        assertEquals(1, largeClassRooms.size());
        assertEquals("2반", largeClassRooms.get(0).getName());
    }

    /**
     *
     * 순환 참조 확인 테스트
     */
    @Test
    void testJacksonCircularReference_ShouldThrowException() {
        ObjectMapper mapper = new ObjectMapper();

        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("1반");
        classRoom.setCapacity(30);

        Student student = new Student();
        student.setName("홍길동");
        student.setAge(15);
        student.setClassRoom(classRoom);

        classRoom.getStudents().add(student);

        // JsonProcessingException exception =

        assertThrows(JsonProcessingException.class, () -> {
            mapper.writeValueAsString(student);
        });

        // System.out.println("예외 메시지: " + exception.getMessage());
    }

} 