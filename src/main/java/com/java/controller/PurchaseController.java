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

//import com.java.aop.JejuAspect;
import com.java.dto.PurchaseDto;
import com.java.service.PurchaseService;
import com.java.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/purchase/*")
public class PurchaseController {

	@Autowired
	private final PurchaseService purchaseService;

	// 구매 페이지 연결
	@GetMapping("purchaseInsert.go")
	public ModelAndView purchaseInsert(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		purchaseService.purchaseInsert(mav);

		return mav;
	}
	// 구매하기
	@GetMapping(value = "purchaseInsertOk.go")
	public ModelAndView purchaseInsertOk(HttpServletRequest request, HttpServletResponse response,
			PurchaseDto purchaseDto) {
		//String memberMail = request.getParameter("memberMail");

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("purchaseDto", purchaseDto);
		//mav.addObject("memberMail", memberMail);

		purchaseService.purchaseInsertOk(mav);

		return mav;
	}
	
	// 구매내역
	@GetMapping("purchaseList.go")
	public ModelAndView purchaseList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		purchaseService.purchaseList(mav);

		return mav;
	}
	
	@PostMapping(value = "kakaoPay.go")
	public ModelAndView kakaoPay(HttpServletRequest request, HttpServletResponse response, PurchaseDto purchaseDto) {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("purchaseDto", purchaseDto);
		purchaseService.kakaoPay(mav);
		
		return mav;
	}
	
	@GetMapping("purchaseDelete.go")
	public ModelAndView purchaseDelete(HttpServletRequest request, HttpServletResponse response) {
		String couponCode = request.getParameter("couponCode");
		String couponName = request.getParameter("couponName");
		String purchaseCode = request.getParameter("purchaseCode");

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("couponCode", couponCode);
		mav.addObject("couponName", couponName);
		mav.addObject("purchaseCode", purchaseCode);
		
		mav.setViewName("purchase/purchaseDelete.empty");
		
		return mav;
	}
	
	// 구매 상세 불러오기(관리자)
	@GetMapping("purchaseDeleteAdmin.go")
	public void purchaseDeleteAdmin(HttpServletRequest request, HttpServletResponse response) {
		String couponCode = request.getParameter("couponCode");
		String couponName = request.getParameter("couponName");

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("couponCode", couponCode);
		mav.addObject("couponName", couponName);
		
		
		String jsonText = purchaseService.purchaseDelete(mav);
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
	
	// 구매 취소(관리자)
	@PostMapping("purchaseDeleteOk.go")
	@ResponseBody
	public void purchaseDeleteOk(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		String jsonText =  purchaseService.purchaseDeleteOk(mav);
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
