package com.java.dto;


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
public class CouponAdminDto {
	private String couponCode;	
	private String foodCode;	
	private String couponName;	
	private String couponIntro;	
	private int couponCostori;	
	private int couponCostsale;	
	private int couponSalerate;	
	private String couponStatus;	
	private String imageCode;	
	private String imageName;	
	private long imageSize;	
	private String imagePath;	
	private String couponStartdate;	
	private String couponEnddate;		

}
