package com.example.jpapractice.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * DTO(Data Transfer Object)로 해결
 * Entity파일들은 DB에 들어가는 데이터 선언 기능만
 * 유저에게 받아온 json형식의 요청이나 객체를 반환 해주는 역할은 DTO로 분리
 * Entity와 Dto를 활용하여 책임 분리가 명확함
 *
 * 사용방법
 * 하나의 값이 아닌 객체를 반환해줄 때 사용하는 방법으로 ResponseDto RequestDto등으로 구분할 수 도 있음
 * 사용할 때는 @Getter,@Setter,@AllArgsConstructor,@NoArgsConstructor를 사용
 *
 * 왜 해결이 되나?
 * 원래 문제가 생겼던 이유는 Entity를 반환하기 때문에 서로 FK관계에 대하여 명시하였기 때문에 JACKSON에서 연속적으로 탐색하기 때문
 * Dto로 분리를 해놓으면, 단순하게 가서 정보를 가져오는 것이기 때문에 필드에 있는 것만 직렬화하면 되니까 문제가 없음
 */

/**
 * 지금은 순환참조 해결을 위한 dto 사용이기 때문에 간단하게 dto를 선언하여 entity를 사용하지 않고 해결
 *
 * From/ToEntity를 선언하여 repository에서는 엔티티를
 * service에서는 dto를 반환하게 정해놓을 수 있음
 * 현재는 service 단에서 convertToDto가 그 기능을 해주고 있음
 *
 * 기능별로 객체안의 내용이 다를 수 있다면 dto별로 선언해 관리하는 것도 좋은 방법
 * request, response, create, update 등 여러가지 형태로 만들어 둘 수도 있음
 */

@Getter
@Setter
public class StudentDto {
    private Long id;
    private String name;
    private Integer age;
    // 엔티티에서는 해당 내용을 ClassRoom 객체로 넣어놨었음
    // 문자형태로 정보를 남겨놔야 순환참조가 해결됨
    private Long classRoomId;
    private String classRoomName;
} 