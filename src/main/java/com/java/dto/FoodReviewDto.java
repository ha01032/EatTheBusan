package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 한문구
 * @작성일 : 2019. 12. 17.
 * @설명 : 음식점상세페이지에서 사용될 리뷰 리스트 DTO 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodReviewDto {
	private String reviewCode;	// 리뷰 코드
	private String foodCode;	// 음식점 코드
	private String memberCode;	// 멤버 코드	
	private String memberName;	// 멤버 이름
	private Date reviewDate;	// 리뷰 날짜
	private String reviewCont;	// 리뷰 내용
	private int reviewScore;	// 리뷰 점수 5,3,1
	private String referCode; 	// 참조 코드
	private	String imageName; // 리뷰 이미지 리스트
	
	
}
