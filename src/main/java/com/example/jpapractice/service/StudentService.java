package com.example.jpapractice.service;

import com.example.jpapractice.dto.StudentDto;
import com.example.jpapractice.entity.Student;
import com.example.jpapractice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    /**
     * 새로운 학생을 생성합니다.
     * @param student 생성할 학생 정보
     * @return 저장된 학생 정보
     */
    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    /**
     * 모든 학생 정보를 조회합니다.
     * @return 학생 목록
     */
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * ID로 학생 정보를 조회합니다.
     * @param id 학생 ID
     * @return 학생 정보
     */
    public StudentDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
    
    /**
     * 학생 정보를 수정합니다.
     * @param id 수정할 학생 ID
     * @param updatedStudent 수정할 학생 정보
     * @return 수정된 학생 정보
     */
    @Transactional
    public StudentDto updateStudent(Long id, Student updatedStudent) {
        StudentDto existingStudent = getStudentById(id);
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setAge(updatedStudent.getAge());
        existingStudent.setClassRoomName(updatedStudent.getClassRoom().getName());
        existingStudent.setClassRoomId(updatedStudent.getClassRoom().getId());
        return existingStudent;
    }
    
    /**
     * 학생을 삭제합니다.
     * @param id 삭제할 학생 ID
     */
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    /**
     * 특정 반의 모든 학생을 조회합니다.
     * @param classRoomId 반 ID
     * @return 학생 목록
     */
    public List<Student> getStudentsByClassRoom(Long classRoomId) {
        return studentRepository.findByClassRoomId(classRoomId);
    }
    
    /**
     * 특정 나이 이상의 학생을 조회합니다.
     * @param minAge 최소 나이
     * @return 학생 목록
     */
    public List<Student> getStudentsByAge(Integer minAge) {
        return studentRepository.findStudentsByAge(minAge);
    }

    private StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setAge(student.getAge());
        if (student.getClassRoom() != null) {
            dto.setClassRoomId(student.getClassRoom().getId());
            dto.setClassRoomName(student.getClassRoom().getName());
        }
        return dto;
    }
} 