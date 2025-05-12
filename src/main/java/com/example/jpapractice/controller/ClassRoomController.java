package com.example.jpapractice.controller;

import com.example.jpapractice.entity.ClassRoom;
import com.example.jpapractice.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    /**
     * 새로운 반을 생성합니다.
     * @param classRoom 생성할 반 정보
     * @return 생성된 반 정보
     */
    @PostMapping
    public ResponseEntity<ClassRoom> createClassRoom(@RequestBody ClassRoom classRoom) {
        return ResponseEntity.ok(classRoomService.createClassRoom(classRoom));
    }

    /**
     * 모든 반 정보를 조회합니다.
     * @return 반 목록
     */
    @GetMapping
    public ResponseEntity<List<ClassRoom>> getAllClassRooms() {
        return ResponseEntity.ok(classRoomService.getAllClassRooms());
    }

    /**
     * ID로 반 정보를 조회합니다.
     * @param id 반 ID
     * @return 반 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassRoom> getClassRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(classRoomService.getClassRoomById(id));
    }

    /**
     * 반 정보를 수정합니다.
     * @param id 수정할 반 ID
     * @param updatedClassRoom 수정할 반 정보
     * @return 수정된 반 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassRoom> updateClassRoom(
            @PathVariable Long id,
            @RequestBody ClassRoom updatedClassRoom) {
        return ResponseEntity.ok(classRoomService.updateClassRoom(id, updatedClassRoom));
    }

    /**
     * 반을 삭제합니다.
     * @param id 삭제할 반 ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable Long id) {
        classRoomService.deleteClassRoom(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 정원 이상의 반을 조회합니다.
     * @param minCapacity 최소 정원
     * @return 반 목록
     */
    @GetMapping("/large")
    public ResponseEntity<List<ClassRoom>> findLargeClassRooms(
            @RequestParam Integer minCapacity) {
        return ResponseEntity.ok(classRoomService.findLargeClassRooms(minCapacity));
    }
} 