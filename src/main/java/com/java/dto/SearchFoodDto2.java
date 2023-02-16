package com.java.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @작성자 : 한문구
 * @작성일 : 2019. 12. 12.
 * @설명 : 음식점 DTO(VO) 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFoodDto2 {
	
	private int rnum;	
	private String foodCode;	
	private String foodName;	
	private String foodAddr;	
	private String foodArea;	
	private String foodPhone;	
	private String foodKind;	
	private int foodRead;	
	private String foodMenu;	
	private String imageName;	
	private String imagePath;		
	private float reviewScore;	
	private String reviewCount;	
	
	
	
}
