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
public class PurchaseListDto2 {
	private int numb;	
	private String couponCode;	
	private String couponName;	
	private String couponStartdate;	
	private String couponEnddate;	
	private int couponCostori;	
	private int couponCostsale;	
	private Date purchaseDate;	
	private String purchasePhone;	
	private String purchaseCode;	
	private String purchaseStatus;	
	private String imageName;	
	private String imagePath;	
	private String imageCode;	

}
