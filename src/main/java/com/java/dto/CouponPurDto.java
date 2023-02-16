package com.java.dto;

import org.modelmapper.ModelMapper;

import com.java.entity.Coupon;
import com.java.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 전지원
 * @작업일 : 2019. 12. 13.
 * @작업 내용 :  couponStartdate, couponEnddate 데이터타입 String으로 변환
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponPurDto {
	private String couponCode;	
	private String foodCode;	
	private String couponStatus;	
	private String couponName;	
	private int couponCostori;	
	private int couponSalerate;	
	private int couponCostsale;	
	private String couponIntro;	
	private String couponStartdate;	
	private String couponEnddate;	
	private String imageCode;	
	private String imageName;	
	private String imagePath;	
	private String foodName;


}