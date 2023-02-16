package com.java.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
//import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.dto.FoodDto;
import com.java.dto.FoodDto2;
import com.java.dto.MemberDto;
import com.java.dto.MemberFavoriteDto;
import com.java.dto.MemberReviewDto;
import com.java.dto.PurchaseListDto2;
import com.java.entity.Member;
import com.java.repository.FavoriteRepository;
import com.java.repository.FoodRepository;
//import com.java.member.dto.MemberFavoriteDto;
//import com.java.purchase.dao.PurchaseDao;
//import com.java.purchase.dto.PurchaseListDto;
//import com.java.review.dto.ReviewDto;
//import com.java.aop.JejuAspect;
//import com.java.coupon.dto.CouponDto;
//import com.java.food.dao.FoodDao;
//import com.java.food.dto.FoodDto;
//import com.java.food.dto.FoodReviewDto;
import com.java.repository.MemberRepository;
import com.java.repository.PurchaseRepository;
import com.java.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImp implements MemberService {

	@Autowired
	private final MemberRepository memberRepository;
	@Autowired
	private final PurchaseRepository purchaseRepository;

	@Autowired
	private final ReviewRepository reviewRepository;
	@Autowired
	private final FavoriteRepository favoriteRepository;

	@Autowired
	private final FoodRepository foodRepository;

	// @Autowired
	// private PurchaseDao purchaseDao;

	// 카카오 로그인시 카카오닉네임 이메일 입력
	@Override
	public void insertKakao(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");

		String memberName = request.getParameter("nickname");
		String memberMail = request.getParameter("mail");
		// JejuAspect.logger.info(JejuAspect.logMsg + mail);
		// DB 저장하기전에 카카오에서 주는 mail값으로 현재 DB에 있는지 체크한다.
		int emailCheck = memberRepository.emailCheck(memberMail);
		int check = 0;
		// 만약 emailCheck 0이면 DB에 존재하지 않으므로 DB에 추가하는 과정을 하고
		if (emailCheck == 0) {
			check = memberRepository.insertKakao(memberMail, memberName);
			// emailCheck 이미 존재해서 체크값이 1이면 그 값을 체크값에 주어서 넘어간다
		} else {
			check = emailCheck;
		}
		Member memberDto = null;
		if (check == 1) {
			memberDto = memberRepository.getMemberCode(memberMail);
		}

		// JejuAspect.logger.info(JejuAspect.logMsg + check);
		// JejuAspect.logger.info(JejuAspect.logMsg + mail);
		mav.addObject("check", check);
		mav.addObject("memberDto", memberDto);
		mav.setViewName("member/mailLoginOk.tiles");

	}

	// 네이버 로그인
	@Override
	public void insertNaver(String json, ModelAndView mav)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(json, Map.class);
		Map<String, Object> data = (Map<String, Object>) map.get("response");

		String memberMail = (String) data.get("email");
		String memberName = (String) data.get("name");

		// JejuAspect.logger.info(JejuAspect.logMsg + email);

		int emailCheck = memberRepository.emailCheck(memberMail);
		int check = 0;

		if (emailCheck == 0)
			check = memberRepository.insertNaver(memberMail, memberName);
		else
			check = emailCheck;

		Member memberDto = null;
		if (check == 1) {
			memberDto = memberRepository.getMemberCode(memberMail);
		}

		mav.addObject("check", check);
		mav.addObject("memberDto", memberDto);
		mav.setViewName("member/mailLoginOk.tiles");

	}

	// 회원가입 과정
	@Override
	public void memberSignInOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		MemberDto memberDto = (MemberDto) map.get("memberDto");
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		Date memberDate = new Date();
		String memberMail = request.getParameter("mail");
		String memberName = request.getParameter("name");
		String memberPwd = request.getParameter("pwd");

//		memberDto.setMemberMail(request.getParameter("mail"));
//		memberDto.setMemberName(request.getParameter("name"));
//		memberDto.setMemberPwd(request.getParameter("pwd"));
//		memberDto.setMemberDate(new Date());
//		memberDto.setMemberPhone(request.getParameter("phone"));
//		JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());
		int check = memberRepository.memberInsert(memberDate, memberMail, memberPwd, memberName);
//		JejuAspect.logger.info(JejuAspect.logMsg + check);
		mav.addObject("check", check);
		mav.setViewName("member/signInOk.tiles");
	}

	// 이메일 중복 체크
	@Override
	public void mailCheck(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String mail = request.getParameter("mail");
		int check = memberRepository.emailCheck(mail);
		// JejuAspect.logger.info(JejuAspect.logMsg + check);

		response.setContentType("application/text;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(check);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// mav.addObject("check", check);
		// mav.setViewName("member/emailCheckOk.empty");
	}

	// 이메일 로그인 성공 과정
	@Override
	public void memberMailLoginOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String memberPwd = request.getParameter("pwd");
		String memberMail = request.getParameter("mail");

		int check = memberRepository.countByMemberMailAndMemberPwd(memberMail, memberPwd);
		Member member = null;
		MemberDto memberDto = null;
		if (check == 1) {
			member = memberRepository.findByMemberMail(memberMail);
			memberDto = MemberDto.of(member);
			// JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());
		}

		// JejuAspect.logger.info(JejuAspect.logMsg + check);
		mav.addObject("check", check);
		mav.addObject("memberDto", memberDto);
		mav.setViewName("member/mailLoginOk.tiles");
	}

	// 마이페이지
	@Override
	public void memberMypage(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpSession session = request.getSession();
		String memberCode = (String) session.getAttribute("memberCode");

		int couponCount = purchaseRepository.countpurchase(memberCode);

		int reviewCount = reviewRepository.countreview(memberCode);
		int favoriteCount = favoriteRepository.countfavorite(memberCode);
		mav.addObject("couponCount", couponCount);
		mav.addObject("reviewCount", reviewCount);
		mav.addObject("favoriteCount", favoriteCount);

//			JejuAspect.logger.info(JejuAspect.logMsg + memberCode);
		// 개인정보를 보기 위한

		Member member = memberRepository.findmember(memberCode);
		MemberDto memberDto = MemberDto.of(member);
//			JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());
		mav.addObject("memberDto", memberDto);

		// 내가 등록한 식당 리스트를 보기 위한
		// List<FoodDto> foodList = memberRepository.getMyFood(memberCode);

		List<FoodDto2> foodList = memberRepository.getMyFood(memberCode);

//			JejuAspect.logger.info(JejuAspect.logMsg + foodList.size());
//			JejuAspect.logger.info(JejuAspect.logMsg + foodList.toString());
		mav.addObject("foodList", foodList);

		// 가고싶다
		// List<MemberFavoriteDto> favoriteList = memberDao.getMyFavorite(memberCode);
		List<MemberFavoriteDto> favoriteList = memberRepository.getMyFavorite(memberCode);

		// JejuAspect.logger.info(JejuAspect.logMsg + favoriteList.size());
		// JejuAspect.logger.info(JejuAspect.logMsg + favoriteList.toString());
		mav.addObject("favoriteList", favoriteList);

		// 리뷰
		List<MemberReviewDto> reviewList = memberRepository.getMyReview(memberCode);
		// JejuAspect.logger.info(JejuAspect.logMsg + reviewList.size());
		// JejuAspect.logger.info(JejuAspect.logMsg + reviewList.toString());
		mav.addObject("reviewList", reviewList);

		// EAT딜
		List<PurchaseListDto2> couponList = purchaseRepository.purchaseSelectAll(memberCode);
		// List<CouponDto> couponList = memberDao.getMyCoupon(memberCode);
		// JejuAspect.logger.info(JejuAspect.logMsg + couponList.size());
		// JejuAspect.logger.info(JejuAspect.logMsg + couponList.toString());
		mav.addObject("couponList", couponList);
		mav.setViewName("member/myPage.tiles");

	}

	// 개인정보 수정
	@Override
	public void memberUpdateOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		MemberDto memberDto = (MemberDto) map.get("memberDto");
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String memberPwd = memberDto.getMemberPwd();
		String memberName = memberDto.getMemberName();
		String memberPhone = memberDto.getMemberPhone();
		String memberCode = memberDto.getMemberCode();

		// JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());
		int check = memberRepository.memberUpdateOk(memberPwd, memberName, memberPhone, memberCode);

		// JejuAspect.logger.info(JejuAspect.logMsg + check);
		mav.addObject("check", check);
		mav.setViewName("member/memberUpdateOk.tiles");
	}

	// 내가 등록한 식당
	@Override
	public void myFoodWrite(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		FoodDto foodDto = (FoodDto) map.get("foodDto");
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpSession session = request.getSession();
		String memberCode1 = (String) session.getAttribute("memberCode");
		foodDto.setMemberCode(memberCode1);
		// JejuAspect.logger.info(JejuAspect.logMsg + foodDto.toString());
		String memberCode = foodDto.getMemberCode();
		String foodName = foodDto.getFoodName();
		String foodAddr = foodDto.getFoodAddr();
		String foodKind = foodDto.getFoodKind();
//			 String foodStatus = foodDto.getFoodStatus();	
//			 Date foodDate = foodDto.getFoodDate();	
//			 int foodRead = foodDto.getFoodRead();	
//			 String foodPhone = foodDto.getFoodPhone();	
		// String referCode = foodRepository.getcode();
		int check = foodRepository.myfoodinsert(memberCode, foodName, foodAddr, foodKind);
		// JejuAspect.logger.info(JejuAspect.logMsg + check);
		mav.addObject("check", check);
		mav.setViewName("member/myFoodOk.tiles");
	}

	// 내가 등록한 식당 삭제
	@Override
	public void myFoodDel(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String foodCode = request.getParameter("foodCode");

		// JejuAspect.logger.info(JejuAspect.logMsg + "foodCode : " + foodCode);
		int check = foodRepository.deleteByFoodCode(foodCode);
		// JejuAspect.logger.info(JejuAspect.logMsg + check);

		response.setContentType("application/text;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(foodCode);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 관리자
	@Override
	public void adminMember(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
//			MemberDto memberDto = (MemberDto) map.get("memberDto");

		List<Member> memberList = memberRepository.findAll();
		mav.addObject("memberList", memberList);
	}

	@Override
	public void getMember(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String memberCode = request.getParameter("memberCode");
		// JejuAspect.logger.info(JejuAspect.logMsg + memberCode);

		Member memberDto = new Member();
		memberDto = memberRepository.memberInfo(memberCode);

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		String memberDate = date.format(memberDto.getMemberDate());
		// JejuAspect.logger.info(JejuAspect.logMsg + "memberDate : " + memberDate);

		JSONObject jsonMemberDto = new JSONObject();
		// JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());

		jsonMemberDto.put("memberCode", memberDto.getMemberCode());
		jsonMemberDto.put("memberDate", memberDate);
		jsonMemberDto.put("memberMail", memberDto.getMemberMail());
		jsonMemberDto.put("memberName", memberDto.getMemberName());
		jsonMemberDto.put("memberPhone", memberDto.getMemberPhone());
		jsonMemberDto.put("memberStatus", memberDto.getMemberStatus());
		String jsonText = jsonMemberDto.toJSONString();
		// JejuAspect.logger.info(JejuAspect.logMsg + "jsontext" + jsonText);

		response.setContentType("application/x-json;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(jsonText);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void adminUpdateOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		MemberDto memberDto = (MemberDto) map.get("memberDto");
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		// JejuAspect.logger.info(JejuAspect.logMsg + memberDto.toString());
		String memberStatus = memberDto.getMemberStatus();
		String memberName = memberDto.getMemberName();
		String memberPhone = memberDto.getMemberPhone();
		String memberCode = memberDto.getMemberCode();
		int check = memberRepository.adminUpdateOk(memberStatus, memberName, memberPhone, memberCode);
		// JejuAspect.logger.info(JejuAspect.logMsg + check);
		mav.addObject("check", check);
		mav.setViewName("member/adminUpdateOk.tiles");
	}

}