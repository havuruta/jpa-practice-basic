package com.example.jpapractice.controller;

import com.example.jpapractice.entity.Student;
import com.example.jpapractice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 새로운 학생을 생성합니다.
     * @param student 생성할 학생 정보
     * @return 생성된 학생 정보
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    /**
     * 모든 학생 정보를 조회합니다.
     * @return 학생 목록
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /**
     * ID로 학생 정보를 조회합니다.
     * @param id 학생 ID
     * @return 학생 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    /**
     * 학생 정보를 수정합니다.
     * @param id 수정할 학생 ID
     * @param updatedStudent 수정할 학생 정보
     * @return 수정된 학생 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {
        return ResponseEntity.ok(studentService.updateStudent(id, updatedStudent));
    }

    /**
     * 학생을 삭제합니다.
     * @param id 삭제할 학생 ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 반의 모든 학생을 조회합니다.
     * @param classRoomId 반 ID
     * @return 학생 목록
     */
    @GetMapping("/classroom/{classRoomId}")
    public ResponseEntity<List<Student>> getStudentsByClassRoom(
            @PathVariable Long classRoomId) {
        return ResponseEntity.ok(studentService.getStudentsByClassRoom(classRoomId));
    }

    /**
     * 특정 나이 이상의 학생을 조회합니다.
     * @param minAge 최소 나이
     * @return 학생 목록
     */
    @GetMapping("/age")
    public ResponseEntity<List<Student>> getStudentsByAge(
            @RequestParam Integer minAge) {
        return ResponseEntity.ok(studentService.getStudentsByAge(minAge));
    }
} 