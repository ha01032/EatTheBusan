package com.java.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.service.CouponService;
import com.java.service.SearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequiredArgsConstructor
public class SearchController {
	
	@Autowired
	private final SearchService searchService;

	
	
	// 음식점 리스트
	@RequestMapping(value="/food/list.go", method = RequestMethod.GET)
	public String foodList(HttpServletRequest request, HttpServletResponse response) {
		return "food/list.tiles";
	}	
	
	
	
	@GetMapping(value="/search.go")
	public ModelAndView searchKeyword(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		
		String keyword = request.getParameter("keyword");
		String foodKind = request.getParameter("foodKind");
		
		mav.addObject("request", request);
		mav.addObject("keyword", keyword);
		mav.addObject("foodKind", foodKind);
		
		searchService.searchKeyword(mav);
		return mav;
	}
	
	
	
	
	// 검색 결과 카운트 AJAX
	@PostMapping("searchCountAjax.do")
	@ResponseBody
	public String searchCount(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("keyword", request.getParameter("keyword"));
		mav.addObject("kindType", request.getParameter("kindType"));
		
		return Integer.toString(searchService.searchCount(mav));
	}

	
	// 검색 결과 리스트 AJAX
	@PostMapping("searchResultAjax.do")
	@ResponseBody
	public void searchResult(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView();
		
		String keyword = request.getParameter("keyword");
		String currentPage = request.getParameter("currentPage");
		String orderType = request.getParameter("orderType");
		String addrType = request.getParameter("addrType");
		String kindType = request.getParameter("kindType");

		mav.addObject("keyword", keyword);
		mav.addObject("currentPage", currentPage);
		mav.addObject("orderType", orderType);
		mav.addObject("addrType", addrType);
		mav.addObject("kindType", kindType);
		
		String jsonText = searchService.searchResult(mav);
		
		if (jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			try {
				PrintWriter out;
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	// 인기 음식점 리스트 AJAX
	@RequestMapping(value="/searchPopularAjax.do")
	@ResponseBody
	public void searchPopularAjax(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		String jsonText = searchService.popularList(mav);
		
		if (jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			try {
				PrintWriter out;
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 컨텐츠 수 AJAX
	@RequestMapping(value="/countContAjax.do")
	@ResponseBody
	public void countContentsAjax(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		String jsonText = searchService.countCont(mav);
		
		if (jsonText != null) {
			response.setContentType("application/x-json;charset=utf-8");
			try {
				PrintWriter out;
				out = response.getWriter();
				out.print(jsonText);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
