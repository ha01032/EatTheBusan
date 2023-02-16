package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReviewDto {	// 내가 등록한 리뷰
	private String reviewCode;
	private String foodCode;
	private String memberCode;
	private String memberName;
	private Date reviewDate;
	private String reviewCont;
	private int reviewScore;
	private String referCode;
	private String imageName;

	


	
	
}
