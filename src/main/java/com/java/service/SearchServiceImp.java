package com.java.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.java.dto.CountDto;
//import com.java.aop.JejuAspect;
import com.java.dto.SearchCouponDto;
import com.java.dto.SearchCouponListDto;
import com.java.dto.SearchFoodDto;
import com.java.dto.SearchFoodDto2;
import com.java.dto.SearchPopularDto;
import com.java.repository.CouponRepository;
import com.java.repository.FoodRepository;
import com.java.repository.ImageRepository;
import com.java.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImp implements SearchService {
	
	@Autowired
	private final FoodRepository foodRepository;
	private final ReviewRepository reviewRepository;

	@Override

	//키워드 검색
	public void searchKeyword(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		String keyword =  (String) map.get("keyword");
		//JejuAspect.logger.info(JejuAspect.logMsg + keyword);
		if (keyword == null) {
			keyword = "";
		}
		// 쿠폰 수
		CountDto countdto = foodRepository.couponCount(keyword);
		int couponCount = countdto.getCount(); 
		//int couponCount = searchDao.couponCount(keyword);
		//JejuAspect.logger.info(JejuAspect.logMsg + couponCount);
		
		List<SearchCouponListDto> couponList = new ArrayList<SearchCouponListDto>();
		if (couponCount > 0) {
			couponList = foodRepository.couponList(keyword);
			//couponList = searchDao.couponList(keyword);
		}
		//JejuAspect.logger.info(JejuAspect.logMsg + couponList.size());
		mav.addObject("couponCount", couponCount);
		mav.addObject("couponList", couponList);
		
		mav.setViewName("search/search.tiles");
	}

	@Override
	public int searchCount(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		String keyword = (String) map.get("keyword");
		String kindType = (String) map.get("kindType");
		if (keyword == null) {
			keyword = "";
		}
		kindType = kindType.replaceAll(",", "");
		
		//int searchCount = searchDao.searchCount(keyword, addrArr, kindArr);
		int searchCount = 0;
		if(kindType != "") {
		searchCount = foodRepository.kindsearchCount(keyword, kindType);	
		}
		else {
		searchCount = foodRepository.searchCount(keyword);
		}
		
		return searchCount;
	}

	@Override
	public String searchResult(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		String keyword = (String) map.get("keyword");
		String currentPage = (String) map.get("currentPage");
		String orderType = (String) map.get("orderType");
		String kindType = (String) map.get("kindType");
		if (keyword == null) {
			keyword = "";
		}
		kindType = kindType.replaceAll(",", "");
		
		int pageNumber = Integer.parseInt(currentPage); 		// 요청페이지 번호 (def.1)
		int boardSize = 10; 									// 페이지당 출력할 게시물 수
		int startRow = boardSize * (pageNumber - 1) + 1;		// 시작번호
		int endRow = boardSize * pageNumber;
		
		//int searchCount = searchDao.searchCount(keyword, addrArr, kindArr);
		int searchCount = 0;
		if(kindType != "") {
		searchCount = foodRepository.kindsearchCount(keyword, kindType);	
		}
		else {
		searchCount = foodRepository.searchCount(keyword);
		}
		
		List<SearchFoodDto2> searchResultList = new ArrayList<SearchFoodDto2>();
		if(orderType == null) {orderType = "";}
		if (searchCount > 0) {
			if(orderType.equals("평점")){
				if(kindType.equals("")) {searchResultList = foodRepository.goodsearchResult(keyword, startRow, endRow);} 
				else
					{searchResultList = foodRepository.kindgoodsearchResult(keyword, kindType, startRow, endRow);}
				
			}
			else {
				if(kindType.equals("")){searchResultList = foodRepository.manysearchResult(keyword, startRow, endRow);} 
				else 
				{searchResultList = foodRepository.kindmanysearchResult(keyword, kindType, startRow, endRow);}
			}
			
		}
		
		//JejuAspect.logger.info(JejuAspect.logMsg + searchCount + " / " + searchResultList.size());

		JSONArray arr = new JSONArray();
		for(SearchFoodDto2 sFoodDto : searchResultList) {
			HashMap<String, Object> jMap = new HashMap<String, Object>();
			jMap.put("foodCode", sFoodDto.getFoodCode());
			jMap.put("foodName", sFoodDto.getFoodName());
			jMap.put("foodMenu", sFoodDto.getFoodMenu());
			jMap.put("foodKind", sFoodDto.getFoodKind());
			jMap.put("foodAddr", sFoodDto.getFoodAddr());
			jMap.put("foodArea", sFoodDto.getFoodArea());
			jMap.put("foodRead", sFoodDto.getFoodRead());
			jMap.put("reviewCount", sFoodDto.getReviewCount());
			jMap.put("reviewScore", sFoodDto.getReviewScore());
			jMap.put("imageName", sFoodDto.getImageName());
			jMap.put("imagePath", sFoodDto.getImagePath());
			arr.add(jMap);
//			JejuAspect.logger.info(JejuAspect.logMsg + jMap.toString());
		}
		String jsonText = JSONValue.toJSONString(arr);
		//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);
		
		return jsonText;
	}
	
	@Override
	public String popularList(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		List<SearchPopularDto> popularList = new ArrayList<SearchPopularDto>();
		popularList = foodRepository.popularList();
		
		//JejuAspect.logger.info(JejuAspect.logMsg + "popularList : " + popularList.size());

		JSONArray arr = new JSONArray();
		for(SearchPopularDto sFoodDto : popularList) {
			HashMap<String, Object> jMap = new HashMap<String, Object>();
			jMap.put("foodCode", sFoodDto.getFoodCode());
			jMap.put("foodName", sFoodDto.getFoodName());
			jMap.put("foodMenu", sFoodDto.getFoodMenu());
			jMap.put("foodKind", sFoodDto.getFoodKind());
			jMap.put("foodAddr", sFoodDto.getFoodAddr());
			jMap.put("foodArea", sFoodDto.getFoodArea());
			jMap.put("foodRead", sFoodDto.getFoodRead());
			jMap.put("reviewCount", sFoodDto.getReviewCount());
			jMap.put("reviewScore", sFoodDto.getReviewScore());
			jMap.put("imageName", sFoodDto.getImageName());
			jMap.put("imagePath", sFoodDto.getImagePath());
			arr.add(jMap);
//			JejuAspect.logger.info(JejuAspect.logMsg + jMap.toString());
		}
		String jsonText = JSONValue.toJSONString(arr);
		//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);
		
		return jsonText;
	}

	@Override
	public String countCont(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		String countFood = Long.toString(foodRepository.count());
		String countReview = Long.toString(reviewRepository.count());
		
		//JejuAspect.logger.info(JejuAspect.logMsg + "countFood : " + countFood + "countReview : " + countReview);

		HashMap<String, String> jMap = new HashMap<String, String>();
		jMap.put("countFood", countFood);
		jMap.put("countReview", countReview);

		String jsonText = JSONValue.toJSONString(jMap);
		//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);
		
		return jsonText;
	}


}
