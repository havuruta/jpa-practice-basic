package com.example.jpapractice.service;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.repository.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @RequiredArgsContructor
 * 롬복에서 제공
 * final이나 notnull 필수적으로 필요한 것들을 포함해서 생성
 * @Autowired를 사용해 생성자로 주입하던 것과 똑같은 기능을 해줌
 *
 * readOnly
 * 기본적으로 트랜잭셔널이 일어나게 되면 엔티티의 변화를 확인하고 수정사항에 맞추어 수정을함
 * readOnly=false가 기본값으로 적용이 되며, 변경감지와 그에 대한 flush가 일어나게됨
 * 조회 메서드에서는 readOnly=true를 명시함으로써 최적화된 기능을 적용시킴
 */


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