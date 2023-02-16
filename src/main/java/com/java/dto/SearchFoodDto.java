package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 한문구
 * @작성일 : 2019. 12. 12.
 * @설명 : 음식점 DTO(VO) 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFoodDto {
	
	private String member_code;	// 멤버 코드

	private String foodCode;	// 음식점 코드
	private String foodName;	// 음식점 명
	private String foodAddr;	// 음식점 주소
	private String foodArea;	// 음식점 지역
	private String foodPhone;	// 음식점 전화번호
	private String foodKind;	// 음식 분류  ex) 한식, 중식, 
	private String foodMenu;	// 음식 대표메뉴
	private String foodTime;	// 음식점 영업시간
	private String foodBreak;	// 음식점 휴일
	private String foodIntro;	// 음식점 소개
	private Date foodDate;		// 음식점 등록일
	private int foodRead;		// 음식점 조회 카운트
	private String foodStatus;	//	음식점 상태  ex) 검토중, 완료
	
	private String reviewCode;
	private String reviewDate;
	private String reviewCont;
	private float reviewScore;
	private String reviewCount;
	
	private String imageCode;	//	이미지 코드
	private String referCode;
	private String imageName;	//	이미지 이름
	private long imageSize;		//	이미지 사이즈		
	private String imagePath;	//	이미지 경로
	
	
	
}
