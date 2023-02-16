package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCouponReadDto {
	private String couponCode; 	 // 쿠폰 코드
	private String couponName; 	 // 쿠폰명 
	private String foodCode;	 // 음식점 코드 
	private int	purchaseCnt;	 // 판매수
	private int rank;	// 순위		
	



}
