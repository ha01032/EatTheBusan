package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCouponListDto {


	private String couponCode;
	private int couponCostori;
	private int couponCostsale;
	private String couponEnddate;
	private String couponIntro;
	private String couponName;
	private int couponSalerate;
	private String couponStartdate;
	private String couponStatus;
	private String foodCode;
	private String foodAddr;
	private String foodArea;
	private Date foodDate;
	private String foodIntro;
	private String foodKind;
	private String foodMenu;
	private String foodName;
	private String foodPhone;
	private int foodRead;
	private String foodTime;
	private String member_code;
	private String imageCode;
	private String imageName;
	private String imagePath;
	private long imageSize;
	private String referCode;
	
	
}


