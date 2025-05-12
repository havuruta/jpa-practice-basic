package com.example.jpapractice.service;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.repository.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassRoomService {
    
    private final ClassRoomRepository classRoomRepository;
    
    /**
     * 새로운 반을 생성합니다.
     * @param classRoom 생성할 반 정보
     * @return 저장된 반 정보
     */
    @Transactional
    public ClassRoom createClassRoom(ClassRoom classRoom) {
        return classRoomRepository.save(classRoom);
    }
    
    /**
     * 모든 반 정보를 조회합니다.
     * @return 반 목록
     */
    public List<ClassRoom> getAllClassRooms() {
        return classRoomRepository.findAll();
    }
    
    /**
     * ID로 반 정보를 조회합니다.
     * @param id 반 ID
     * @return 반 정보
     */
    public ClassRoom getClassRoomById(Long id) {
        return classRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("반을 찾을 수 없습니다: " + id));
    }
    
    /**
     * 반 정보를 수정합니다.
     * @param id 수정할 반 ID
     * @param updatedClassRoom 수정할 반 정보
     * @return 수정된 반 정보
     */
    @Transactional
    public ClassRoom updateClassRoom(Long id, ClassRoom updatedClassRoom) {
        ClassRoom existingClassRoom = getClassRoomById(id);
        existingClassRoom.setName(updatedClassRoom.getName());
        existingClassRoom.setCapacity(updatedClassRoom.getCapacity());
        existingClassRoom.setTeacherName(updatedClassRoom.getTeacherName());
        return existingClassRoom;
    }
    
    /**
     * 반을 삭제합니다.
     * @param id 삭제할 반 ID
     */
    @Transactional
    public void deleteClassRoom(Long id) {
        classRoomRepository.deleteById(id);
    }
    
    /**
     * 특정 정원 이상의 반을 조회합니다.
     * @param minCapacity 최소 정원
     * @return 반 목록
     */
    public List<ClassRoom> findLargeClassRooms(Integer minCapacity) {
        return classRoomRepository.findByCapacityGreaterThan(minCapacity);
    }
} 