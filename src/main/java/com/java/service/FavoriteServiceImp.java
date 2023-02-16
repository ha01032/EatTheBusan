package com.java.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.java.dto.CouponDto;
import com.java.entity.Favorite;
import com.java.entity.Food;
import com.java.entity.Member;
import com.java.repository.CouponRepository;
import com.java.repository.FavoriteRepository;
import com.java.repository.FoodRepository;
import com.java.repository.ImageRepository;
//import com.java.aop.JejuAspect;
//import com.java.favorite.dao.FavoriteDao;
import com.java.repository.MemberRepository;
import com.java.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImp implements FavoriteService {

	
	@Autowired
	private final FavoriteRepository favoriteRepository;
	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final FoodRepository foodRepository;

	@Override
	public int favorCheck(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		
		String memberCode = (String) map.get("memberCode");
		String foodCode = (String) map.get("foodCode");
		//JejuAspect.logger.info(JejuAspect.logMsg + memberCode + " || " + foodCode);
		int check = favoriteRepository.countfavorite(memberCode,foodCode);
		//JejuAspect.logger.info(JejuAspect.logMsg + "favorCheck : " + check);

		return check;
	}

	
	@Override
	public int favorSwitch(ModelAndView mav) {
		
		Map<String, Object> map = mav.getModelMap();
	
		String memberCode = (String) map.get("memberCode");
		String foodCode = (String) map.get("foodCode");
		String favorStatus = (String) map.get("favorStatus");
		Member member = memberRepository.findmember(memberCode);
		Food food = foodRepository.foodRead(foodCode);
		Favorite favorite = new Favorite();
		Date favoriteDate = new Date();
		favorite.setFood(food);
		favorite.setMember(member);
		favorite.setFavoriteDate(favoriteDate);
		
		//JejuAspect.logger.info(JejuAspect.logMsg + memberCode + " || " + foodCode+ " || " + favorStatus);
		//int check = FavoriteRepository.favorSwitch(memberCode, foodCode, favorStatus);
		
		int countbefore = favoriteRepository.countfavorite(memberCode,foodCode);
		
//		System.out.println("===========" + memberCode + "----" + foodCode);
	
		if (favorStatus.equals("on") && countbefore > 0) {
			Long id = favoriteRepository.findkey(foodCode, memberCode);
			favoriteRepository.deleteById(id);
			
		} else if (favorStatus.equals("off") && countbefore == 0) {
			favoriteRepository.save(favorite);
		}
		
		int countafter = favoriteRepository.countfavorite(memberCode,foodCode); 
		//int check = FavoriteRepository.favorSwitch(memberCode, foodCode, favorStatus);
		//JejuAspect.logger.info(JejuAspect.logMsg + "favorCheck : " + check);

		return countafter;
	}

	
	
}