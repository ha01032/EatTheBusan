package com.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFavoriteDto {	// 내가 등록한 가고싶다
	private String foodCode;
	private String foodArea;
	private String foodName;
	private int foodRead;
	private String imageName;
	private String imagePath;
	private int count;
	private String avg;


	


	
	
}
