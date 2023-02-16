package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCouponDto {
	private int totalCouponCnt; // 전체 음식점 수
	private int	korCoupon;	//한식
	private int chnCoupon;	//중국식
	private int jpCoupon;		//일식
	private int wtCoupon;		//양식	
	private int cafe;		//카페
	private int etc;		//기타	
	
	
	


}
