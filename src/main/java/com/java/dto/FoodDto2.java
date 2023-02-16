package com.java.dto;

import java.util.Date;

import org.modelmapper.ModelMapper;

import com.java.entity.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FoodDto2 {
	private String foodCode;	// 음식점코드
	private String foodName;	// 음식점 명
	private String foodAddr;	// 음식점 주소
	private String foodArea;	// 음식점 지역
	private String foodPhone;	// 음식점 전화번호
	private String foodKind;	// 음식 분류  ex) 한식, 중식, 
	private String foodMenu;	// 음식 대표메뉴
	private String foodTime;	// 음식점 영업시간
	private String foodBreak;	// 음식점 휴일
	private String foodIntro;	// 음식점 소개
	private Date foodDate;	// 음식점 등록일
	private int foodRead;	// 음식점 조회 카운트
	private String foodStatus;	//	음식점 상태  ex) y : 완료, n : 검토중	
	private String memberCode;	// 등록자 
	
	

    }
	

