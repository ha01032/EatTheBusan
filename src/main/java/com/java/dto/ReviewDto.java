package com.java.dto;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.java.entity.Admin;
import com.java.entity.Food;
import com.java.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 한문구
 * @작성일 : 2019. 12. 19.
 * @설명 : 리뷰 용 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
	private String reviewCode;	// 리뷰 코드
	private String foodCode;	// 음식점 코드
	private String memberCode;	// 멤버 코드
	private Date reviewDate;	// 리뷰 등록일
	private String reviewCont;	// 리뷰 내용
	private int reviewScore;	// 리뷰 점수	
	
	
	private static ModelMapper modelMapper = new ModelMapper();
    public static ReviewDto of(Review review){
        return modelMapper.map(review,ReviewDto.class);
    }
    
    public Review createReview(){
        return modelMapper.map(this, Review.class);
    }
    

}
