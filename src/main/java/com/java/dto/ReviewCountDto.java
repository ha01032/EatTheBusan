package com.java.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCountDto {
	private int good;	// 맛있어요, 5점 의 개수 
	private int soso;	// 괜찮아요, 3점 의 개수
	private int bad;	// 별로에요, 1점 의 개수
	private int whole;	// 리뷰 전체 개수 
	
	
}
