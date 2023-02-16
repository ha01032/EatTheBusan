package com.java.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java.dto.FoodDto;
import com.java.service.FoodService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class FoodController {
	
	 @Autowired private final FoodService foodService;
	 
	 	
		// 음식점 상세페이지로 이동
		@GetMapping("/food/read.go")
		public ModelAndView foodRead(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);	
			mav.addObject("response",response);				
			foodService.foodRead(mav);		
			return mav;
		}
		
		// 평점에 따른 리뷰 읽기 
		@GetMapping("/food/foodReviewList.go")
		public ModelAndView foodReviewList(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);			
			foodService.foodReviewList(mav);				
			return mav;
		}
		
		// 관리자 페이지 ajax로 읽기
		@GetMapping("/admin/getFood.go")
		public void getFood(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request", request);
			mav.addObject("response", response);
			foodService.getFood(mav);
		}
		
		// 음식점 정보 등록 관련
		@GetMapping("/food/insert.go")
		public ModelAndView foodInsert(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("food/insert.tiles");		
			return mav;		
		}
		
		// 음식점 정보 입력시 
		@PostMapping("/food/insertOk.go")
		public ModelAndView foodInsertOk(HttpServletRequest request, HttpServletResponse response, FoodDto foodDto) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);
			mav.addObject("foodDto",foodDto);		
			foodService.foodInsertOk(mav);
			return mav;
		}
		
		// 음식점 정보 수정 시 정보 읽기
		@GetMapping("/food/update.go")
		public ModelAndView foodUpdate(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);
			foodService.foodUpdate(mav);
			return mav;
		}
		
		// 음식점 정보 수정 시 정보 입력
		@PostMapping("/food/updateOk.go")
		public ModelAndView foodUpdateOk(HttpServletRequest request, HttpServletResponse response, FoodDto foodDto) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);
			mav.addObject("foodDto",foodDto);	
			foodService.foodUpdateOk(mav);
			return mav;
		}
		
		// 음식점 정보 삭제 관련
		@GetMapping("/food/delete.go")
		public ModelAndView foodDelete(HttpServletRequest request, HttpServletResponse response) {
			ModelAndView mav = new ModelAndView();
			mav.addObject("request",request);
			foodService.foodDelete(mav);
			return mav;
		}	
		
		
		
		
		
		
		
		

		
		
}
