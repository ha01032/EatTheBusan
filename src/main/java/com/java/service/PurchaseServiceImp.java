package com.java.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.java.dto.CouponDto;
import com.java.dto.CouponPurDto;
import com.java.dto.MemberDto;
import com.java.dto.PurCouponDto;
import com.java.dto.PurchaseDto;
import com.java.dto.PurchaseListDto;
import com.java.dto.PurchaseListDto2;
import com.java.dto.PurchaseListDto3;
import com.java.dto.PurchaseListDto4;
import com.java.entity.Coupon;
import com.java.entity.Member;
import com.java.repository.CouponRepository;

//import com.java.aop.JejuAspect;

import com.java.repository.FoodRepository;
import com.java.repository.MemberRepository;
import com.java.repository.PurchaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImp implements PurchaseService {
	
	@Autowired
	private final CouponRepository couponRepository;
	private final PurchaseRepository purchaseRepository;
	private final MemberRepository memberRepository;
	
	//@Inject
	//JavaMailSender mailSender;
	
	// 구매 페이지 연결
	@Override
	public void purchaseInsert(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String couponCode = request.getParameter("couponCode");
		String purchasePhone = request.getParameter("purchasePhone");
		//JejuAspect.logger.info(JejuAspect.logMsg + "purchasePhone: "+ purchasePhone);
		//JejuAspect.logger.info(JejuAspect.logMsg + "couponCode: "+ couponCode);
		
		CouponPurDto couponDto = couponRepository.purchaseSelect(couponCode);
		//JejuAspect.logger.info(JejuAspect.logMsg + "couponDto: "+ couponDto.toString());
		
		
		//세션으로 회원정보 가져오기
		HttpSession session =  request.getSession(false);
		String memberCode = (String) session.getAttribute("memberCode");
		if(memberCode != null) {
			//JejuAspect.logger.info(JejuAspect.logMsg + "memberCode: "+ memberCode);
			Member member = memberRepository.findmember(memberCode);
			MemberDto memberDto = MemberDto.of(member);
			
			mav.addObject("memberDto", memberDto);
		}
		
		mav.addObject("purchasePhone", purchasePhone);
		mav.addObject("couponDto", couponDto);
		mav.setViewName("purchase/purchaseDetail.tiles");
	}
	
	//구매하기
		@Override
		public void purchaseInsertOk(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			PurchaseDto purchaseDto = (PurchaseDto) map.get("purchaseDto");
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			
			//HttpSession session =  request.getSession(false);
			String memberMail = request.getParameter("memberMail");
			String memberCode = request.getParameter("memberCode");
			String couponCode = request.getParameter("couponCode");
			String purchasePhone = request.getParameter("purchasePhone");
			int purchaseCost = Integer.parseInt(request.getParameter("purchaseCost"));
			//JejuAspect.logger.info(JejuAspect.logMsg + "memberMail: "+ memberMail);
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponCode: "+ couponCode);
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseCost: "+ purchaseCost);
			
			
			String purchaseCode = purchaseRepository.getcode();
			int check  = purchaseRepository.purchaseInsertOk(couponCode, memberCode, purchasePhone, purchaseCost);
			
			PurCouponDto couponDto = null;
			if(purchaseCode != null) {
			couponDto = purchaseRepository.purchaseCouponSelect(purchaseCode);
				//JejuAspect.logger.info(JejuAspect.logMsg + "couponDto: "+ couponDto.toString());
				mav.addObject("couponDto", couponDto);
			}
			
			//String couponName = couponDto.getCouponName();	
			//int couponCost = couponDto.getCouponCostsale();	
			
			//String subject = "구매해주셔서 감사합니다.";
			//String mailContent = "결제가 완료되었습니다."+"\n\n" + "구매하신 상품: "+ couponName + "\n" + "결제된 금액: "+ couponCost+ "원";
			
			/*
			 * if(check > 0) { try { MimeMessage msg = mailSender.createMimeMessage();
			 * MailDto mailDto = new MailDto(); msg.addRecipient(RecipientType.TO, new
			 * InternetAddress(memberMail)); msg.addFrom(new InternetAddress[] {new
			 * InternetAddress("labelle410@gmail.com", "잇더제주 eatthejeju")});
			 * msg.setSubject(subject, "utf-8"); msg.setText(mailContent, "utf-8");
			 * 
			 * mailSender.send(msg);
			 * 
			 * } catch(Exception e) { e.printStackTrace(); } }
			 */
			
			mav.addObject("check",check);
			mav.setViewName("purchase/purchaseInsertOk.tiles");
		}
		
		//구매내역
		@Override
		public void purchaseList(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			
			HttpSession session =  request.getSession(false);
			String memberCode = (String) session.getAttribute("memberCode");
			
			String pageNumber = request.getParameter("pageNumber");
			if(pageNumber == null) pageNumber = "1";
			int currentPage = Integer.parseInt(pageNumber);
			//JejuAspect.logger.info(JejuAspect.logMsg + "pageNumber/currentPage: "+ pageNumber +"/"+ currentPage);

			int count = purchaseRepository.getCount(memberCode);
			//JejuAspect.logger.info(JejuAspect.logMsg + "count: "+ count);
			
			List<PurchaseListDto2> purchaseList = purchaseRepository.purchaseSelectAll(memberCode);
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseList: "+ purchaseList.size());
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseList: "+ purchaseList.toString());
			
			//mav.addObject("pageNumber", pageNumber);
			//mav.addObject("boardSize", boardSize);
			mav.addObject("count", count);
			mav.addObject("purchaseList", purchaseList);
			mav.setViewName("purchase/purchaseList.tiles");
		}
		
		@Override
		public void kakaoPay(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			
			String couponCode = request.getParameter("couponCode");
			String memberCode = request.getParameter("memberCode");
			String purchasePhone = request.getParameter("purchasePhone");
			String purchaseCost = request.getParameter("purchaseCost");
			String memberName = request.getParameter("memberName");
			String memberMail = request.getParameter("memberMail");
			String couponName = request.getParameter("couponName");
			String foodName = request.getParameter("foodName");
			String couponCostsale = request.getParameter("couponCostsale");
			
			/*
			 * JejuAspect.logger.info(JejuAspect.logMsg + "couponCode : " + couponCode);
			 * JejuAspect.logger.info(JejuAspect.logMsg + "memberCode : " + memberCode);
			 * JejuAspect.logger.info(JejuAspect.logMsg + "purchasePhone : " +
			 * purchasePhone); JejuAspect.logger.info(JejuAspect.logMsg + "purchaseCost : "
			 * + purchaseCost); JejuAspect.logger.info(JejuAspect.logMsg + "memberName : " +
			 * memberName); JejuAspect.logger.info(JejuAspect.logMsg + "memberMail : " +
			 * memberMail); JejuAspect.logger.info(JejuAspect.logMsg + "couponName : " +
			 * couponName); JejuAspect.logger.info(JejuAspect.logMsg + "foodName : " +
			 * foodName); JejuAspect.logger.info(JejuAspect.logMsg + "couponCostsale : " +
			 * couponCostsale);
			 */
			
			mav.addObject("couponCode", couponCode);
			mav.addObject("memberCode", memberCode);
			mav.addObject("purchasePhone", purchasePhone);
			mav.addObject("purchaseCost", purchaseCost);
			mav.addObject("memberName", memberName);
			mav.addObject("memberMail", memberMail);
			mav.addObject("couponName", couponName);
			mav.addObject("foodName", foodName);
			mav.addObject("couponCostsale", couponCostsale);
			
			mav.setViewName("purchase/kakaoPay.empty");
		}
		
		//구매 상세 불러오기
		@Override
		public String purchaseDelete(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			String purchaseCode = request.getParameter("purchaseCode");
			
			PurchaseListDto3 purchaseListDto = purchaseRepository.purchaseSelect(purchaseCode);
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
			String purchaseDate = date.format(purchaseListDto.getPurchaseDate());
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseDate: "+ purchaseDate);
			
			Map<String, Object> pDeleteMap = new HashMap<String, Object>();
			pDeleteMap.put("purchaseCode", purchaseListDto.getPurchaseCode());
			pDeleteMap.put("couponCode", purchaseListDto.getCouponCode());
			pDeleteMap.put("memberCode", purchaseListDto.getMemberCode());
			pDeleteMap.put("purchasePhone", purchaseListDto.getPurchasePhone());
			pDeleteMap.put("purchaseDate", purchaseDate);
			pDeleteMap.put("couponName", purchaseListDto.getCouponName());
			pDeleteMap.put("couponCostsale", purchaseListDto.getCouponCostsale());
			pDeleteMap.put("purchaseStatus", purchaseListDto.getPurchaseStatus());
			
			String jsonText = JSONValue.toJSONString(pDeleteMap);
			//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);
			
			return jsonText;
		}
		
		//구매내역전체(관리자)
		@Override
		public void purchaseListAll(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			
			List<PurchaseListDto4> purchaseList = purchaseRepository.purchaseListAll();
			
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseList: "+ purchaseList.toString());
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseList: "+ purchaseList.size());
			
			mav.addObject("purchaseList", purchaseList);
		}
		
		// 구매 취소
		@Override
		public String purchaseDeleteOk(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");

			String purchaseCode = request.getParameter("purchaseCode");
			//JejuAspect.logger.info(JejuAspect.logMsg + "purchaseCode: "+ purchaseCode);

			int check = purchaseRepository.purchaseDelete(purchaseCode);
			//JejuAspect.logger.info(JejuAspect.logMsg + "check: "+ check);
			
			mav.addObject("check", check);
			
			Map<String, Integer> delMap = new HashMap<String, Integer>();
			delMap.put("check", check);
			
			String jsonText = JSONValue.toJSONString(delMap);		
			//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);
			
			return jsonText;
		}
		
		
		
	
}
