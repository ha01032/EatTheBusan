package com.java.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.java.repository.AdminRepository;
import com.java.repository.CouponRepository;
import com.java.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

import com.java.dto.AdminCouponDto;
import com.java.dto.AdminCouponReadDto;
import com.java.dto.AdminDto;
import com.java.dto.AdminFoodDto;
import com.java.dto.AdminFoodReadDto;
import com.java.dto.MemberDto;
import com.java.entity.Admin;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImp implements AdminService {
	
	@Autowired
	private final AdminRepository adminRepository;
	
	@Override
	public void getMainData(ModelAndView mav) {
		AdminFoodDto adminFoodDto = new AdminFoodDto();
		adminFoodDto = adminRepository.foodCnt();			
		List<AdminFoodReadDto> adminFoodRank = adminRepository.foodReadRank();		
		if(adminFoodDto!=null) {
			mav.addObject("adminFoodDto", adminFoodDto);
		}
		if(adminFoodRank!=null) {
			mav.addObject("adminFoodRank",adminFoodRank);
		}
		
		List<AdminCouponReadDto> adminCouponRank = adminRepository.couponReadRank();
		//JejuAspect.logger.info(JejuAspect.logMsg + "adminCouponRank:" +adminCouponRank.toString());
		mav.addObject("adminCouponRank", adminCouponRank);
		
		AdminCouponDto adminCouponDto = adminRepository.couponCount();
		//JejuAspect.logger.info(JejuAspect.logMsg + "adminCouponDto:" +adminCouponDto.toString());
		if(adminCouponDto != null) {
			mav.addObject("adminCouponDto", adminCouponDto);
		}
		
		
		mav.setViewName("admin/main.admin");	
	}
	
	
	
	@Override	
	public void adminLogin(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request=(HttpServletRequest) map.get("request");
		String adminId = request.getParameter("adminId");
		String adminPwd = request.getParameter("adminPwd");
		
		
		int check = adminRepository.countByAdminIdAndAdminPwd(adminId, adminPwd);
		Admin admin = null;
		AdminDto adminDto = null;
		if (check == 1) {
			admin = adminRepository.findByAdminId(adminId);
			adminDto = AdminDto.of(admin);
		}
		
		mav.addObject("check", check);
		mav.addObject("adminDto", adminDto);
		mav.setViewName("admin/adminLoginOk.empty");
	}


}
