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
public class PurchaseListDto3 {
	private String couponCode;	
	private String couponName;	
	private int couponCostori;	
	private int couponCostsale;	
	private String purchaseCode;	
	private String purchaseStatus;	
	private String memberCode;	
	private Date purchaseDate;	
	private String purchasePhone;	
	private String imageName;	
	private String imagePath;	
	private String imageCode;	
	

}
