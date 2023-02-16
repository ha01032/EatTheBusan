package com.java.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.dto.CouponDto;
import com.java.dto.CouponReadDto;
import com.java.service.CouponService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import com.java.dto.ImageDto;

/**
 * @작성자 : 최현찬
 * @작업일 : 2023. 02. 07.
 * @작업 내용 : CouponController 생성
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon/*")
public class CouponController {
	@Autowired
	private final CouponService couponService;

	// 쿠폰리스트
	@GetMapping("couponList.go") 
	@ResponseBody
	public ModelAndView couponList(HttpServletRequest request, HttpServletResponse response, CouponDto couponDto) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		couponService.couponList(mav);
		mav.setViewName("coupon/couponList.tiles");
		return mav;
	}
	
	// 쿠폰 리스트(Ajax 새로고침)
	@GetMapping("couponListAjax.go")
	public @ResponseBody void couponListAjax(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		String jsonText = couponService.couponListAjax(mav);

		if (jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			try {
				PrintWriter out;
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 쿠폰 상세페이지
	@GetMapping("couponRead.go")
	public ModelAndView couponRead(HttpServletRequest request, HttpServletResponse response, CouponDto couponDto) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("response", response);

		couponService.couponRead(mav);

		return mav;
	}
	
	// 쿠폰등록창 식당코드 검색
	@GetMapping("searchFoodCode.go")
	public ModelAndView readFoodCode(HttpServletRequest request, HttpServletResponse response, CouponDto couponDto) {
		String foodName = request.getParameter("foodName");
		String cInsert = request.getParameter("cInsert");

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("foodName", foodName);
		mav.addObject("cInsert", cInsert);
		couponService.searchFoodCode(mav);

		mav.setViewName("coupon/searchFoodCode.empty");

		return mav;
	}
	
	// 쿠폰상품 등록
	@RequestMapping(value = "/coupon/couponInsertOk.go", method = RequestMethod.POST)
	public ModelAndView couponInsertOk(HttpServletRequest request, HttpServletResponse response, CouponDto couponDto) {
		String imageFile = request.getParameter("imageFile");
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("couponDto", couponDto);
		mav.addObject("imageFile", imageFile);

		couponService.couponInsertOk(mav);

		return mav;
	}
	
	// 쿠폰상품 불러오기[쿠폰 상품 수정 기능]
	@GetMapping("couponUpdate.go")
	@ResponseBody
	public void couponUpdate(HttpServletRequest request, HttpServletResponse response, CouponDto couponDto) {

		String couponCode = request.getParameter("couponCode");
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("response", response);
		mav.addObject("couponCode", couponCode);
		
		String jsonText = couponService.couponUpdate(mav);
		
		if(jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 쿠폰상품 수정
	@PostMapping("couponUpdateOk.go")
	public ModelAndView couponUpdateOk(HttpServletRequest request, HttpServletResponse response, CouponReadDto couponDto, ImageDto imageDto) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("couponDto", couponDto);
		mav.addObject("imageDto", imageDto);

		couponService.couponUpdateOk(mav);

		return mav;
	}
	
	
	// 쿠폰상품 삭제
	@PostMapping("couponDeleteOk.go")
	public void couponDeleteOk(HttpServletRequest request, HttpServletResponse response) {
		String couponCode = request.getParameter("couponCode");
		System.out.println("couponCode/Ok: "+couponCode);
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("couponCode", couponCode);

		String jsonText = couponService.couponDeleteOk(mav);

		if(jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	



	



}
