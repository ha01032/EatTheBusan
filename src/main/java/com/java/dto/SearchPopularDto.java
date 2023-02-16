package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPopularDto {
	
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
	private int reviewCount;

}