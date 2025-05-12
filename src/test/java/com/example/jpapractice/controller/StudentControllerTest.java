package com.example.jpapractice.controller;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.entity.Student;
import com.example.jpapractice.repository.ClassRoomRepository;
import com.example.jpapractice.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testCircularReferenceResolved() throws Exception {
        // 1. 반 생성
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName("1반");
        classRoom.setCapacity(30);
        classRoom.setTeacherName("김선생");
        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);

        // 2. 학생 생성
        Student student = new Student();
        student.setName("홍길동");
        student.setAge(15);
        student.setClassRoom(savedClassRoom);
        Student savedStudent = studentRepository.save(student);

        // 3. API 호출 - 순환 참조 없이 잘 응답되는지 확인
        mockMvc.perform(get("/api/students/" + student.getId()))
                .andExpect(status().isOk()); // 200 OK → 순환 참조 해결됨

        mockMvc.perform(get("/api/classrooms/" + classRoom.getId()))
                .andExpect(status().isOk()); // 역시 OK면 성공
    }
}
