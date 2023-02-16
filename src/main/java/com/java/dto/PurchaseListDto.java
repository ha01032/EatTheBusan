package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 전지원
 * @작업일 : 2019. 12. 17.
 * @작업 내용 : PurchaseDto
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseListDto {
	private String purchaseCode;
	private String couponCode;
	private String memberCode;
	private String purchasePhone;
	private Date purchaseDate;
	private String purchaseStatus;
	
	private String couponName;
	private String couponStartdate;	
	private String couponEnddate;
	private String couponIntro;	
	private int couponCostori;
	private int couponCostsale;	
	private int couponSalerate;	
	
	private String imageCode;
	private String imageName;
	private String imagePath;
	

}
