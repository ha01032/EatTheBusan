package com.java.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.java.aop.JejuAspect;
import com.java.dto.CouponDto;
import com.java.dto.FoodDto;
import com.java.dto.FoodReviewDto;
import com.java.dto.FoodStarDto;
import com.java.dto.ImageDto;
import com.java.repository.CouponRepository;
import com.java.repository.FavoriteRepository;
import com.java.repository.FoodRepository;
import com.java.repository.ImageRepository;
import com.java.repository.MemberRepository;
import com.java.repository.ReviewRepository;
import com.java.dto.ReviewCountDto;
import com.java.dto.ReviewDto;
import com.java.dto.SearchFoodDto;
import com.java.entity.Coupon;
import com.java.entity.Food;
import com.java.entity.Image;
import com.java.entity.Member;
import com.java.entity.Review;

import lombok.RequiredArgsConstructor;

/**
 * @작성자 : 한문구
 * @작성일 : 2019. 12. 13.
 * @설명 : 음식점 서비스  
 */

@Service
@RequiredArgsConstructor
@Transactional
public class FoodServiceImp implements FoodService {	
	@Autowired
	private final FoodRepository foodRepository;	
	
	@Autowired
	private final ImageRepository imageRepository;
	
	@Autowired
	private final ReviewRepository reviewRepository;
	
	@Autowired
	private final CouponRepository couponRepository;
	
	@Autowired
	private final FavoriteRepository favoriteRepository;
	
	@Autowired
	private final MemberRepository memberRepository;
	

	@Override
	public void foodRead(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String foodCode = request.getParameter("foodCode");
		Food foodDto = new Food();
		Image imageDto = new Image();
		ReviewCountDto reviewCountDto = new ReviewCountDto();

		// 리뷰 리스트 가져오기 위한 Dao
		List<FoodReviewDto> reviewList = null;
		reviewList = foodRepository.reviewList(foodCode);
		//JejuAspect.logger.info(JejuAspect.logMsg+ "reviewList : " +reviewList.toString());
		mav.addObject("reviewList",reviewList);
		String reviewCode = null;
		// 리뷰코드를 통해 리뷰 상세페이지를 가져오기 위한 Dao
		for (int i = 0; i < reviewList.size(); i++) {
			reviewCode = reviewList.get(i).getReviewCode();
			//JejuAspect.logger.info(JejuAspect.logMsg+ "reviewCode : " +reviewCode);
			mav.addObject("reviewCode",reviewCode);
		}
		
		ReviewDto reviewDto = new ReviewDto();
		if (reviewCode != null) {
			Review review = reviewRepository.reviewUpdate(reviewCode);
			reviewDto = ReviewDto.of(review); 
			List<ImageDto> listImage = imageRepository.reviewimgList(reviewCode);
			//JejuAspect.logger.info(JejuAspect.logMsg+listImage.toString());
			mav.addObject("listImage",listImage);
		}
		mav.addObject("reviewDto",reviewDto);
		
		// 별점을 가져오기 위한 Dao
		FoodStarDto searchFoodDto = new FoodStarDto();
		searchFoodDto = foodRepository.getReviewScore(foodCode);
		if (searchFoodDto != null) {
			mav.addObject("searchFoodDto",searchFoodDto);
		}
		// 음식점 정보 가져오기
		foodDto = foodRepository.foodRead(foodCode);
		//JejuAspect.logger.info(JejuAspect.logMsg+foodDto);
		// 음식점 이미지 정보 가져오기
		String referCode = foodCode;
		imageDto = imageRepository.imgRead(referCode);
		
		// 쿠폰정보
		List<CouponDto> couponDtoList = foodRepository.foodCouponList(foodCode); 
		Cookie[] cookies = request.getCookies();
		// 비교하기 위해 새로운 쿠키
        Cookie viewCookie = null; 
        // 쿠키가 있을 경우 
        if (cookies != null && cookies.length > 0) 
        {
            for (int i = 0; i < cookies.length; i++)
            {
                // Cookie의 name이 cookie + foodCode와 일치하는 쿠키를 viewCookie에 넣어줌 
                if (cookies[i].getName().equals("cookie"+foodCode))
                {                   
                    viewCookie = cookies[i];
                }
            }
        }
        // 쿠키가 없을 경우
        if (viewCookie == null) {                       
            // 쿠키 생성(이름, 값)
            Cookie newCookie = new Cookie("cookie"+foodCode, "|" + foodCode + "|");                            
            // 쿠키 추가
            response.addCookie(newCookie);
            // 조회수 증가
            foodRepository.foodReadUpdate(foodCode);
        }		
		// 즐겨찾기 카운트 
        String favoriteCnt = Integer.toString(favoriteRepository.foodFavorite(foodCode));
		// 리뷰 카운트
        reviewCountDto = foodRepository.foodReviewCount(foodCode);
        // JejuAspect.logger.info(JejuAspect.logMsg+"reviewCount"+reviewCountDto.toString());
        // 리뷰 평균 점수
        float reviewAvg = reviewRepository.foodReviewAvg(foodCode);
        // 보낼 데이터
        mav.addObject("favoriteCnt", favoriteCnt);
		mav.addObject("reviewAvg", reviewAvg);
		mav.addObject("reviewCountDto",reviewCountDto);			
		mav.addObject("couponDtoList", couponDtoList);
		//JejuAspect.logger.info(JejuAspect.logMsg+"couponDtoList"+couponDtoList.toString());
		mav.addObject("foodDto", foodDto);	
		mav.addObject("imageDto", imageDto);
		//JejuAspect.logger.info(JejuAspect.logMsg+"couponDtoList"+couponDtoList.toString());
		mav.setViewName("food/read.tiles");		
	}
	
	@Override
	public void foodReviewList(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String foodCode = request.getParameter("foodCode");
		String selScore = request.getParameter("selScore");
		ReviewCountDto reviewCountDto = foodRepository.foodReviewCount(foodCode);
		//JejuAspect.logger.info(JejuAspect.logMsg + reviewCountDto.toString());
		List<FoodReviewDto> foodReviewList = null;
		if (reviewCountDto != null) {		
			if(selScore.equals("0")) {
			foodReviewList = foodRepository.reviewList(foodCode);}
			else {foodReviewList = foodRepository.foodReviewListScore(foodCode, selScore);}
			//JejuAspect.logger.info(JejuAspect.logMsg+foodReviewList.size());
			mav.addObject("foodReviewList",foodReviewList);
			//JejuAspect.logger.info(JejuAspect.logMsg+foodReviewList.toString());
		}		
		mav.addObject("reviewCountDto",reviewCountDto);		
		mav.setViewName("review/list.empty");
		
	}
	@Override
	public void adminFoodList(ModelAndView mav) {		
		List<FoodDto> foodDtoList = foodRepository.allrestaurant();	
		//JejuAspect.logger.info(JejuAspect.logMsg+foodDtoList.toString());
		mav.addObject("foodDtoList",foodDtoList);
		mav.setViewName("admin/food.admin");		
	}
	
	// 관리자 음식점페이지에서 게시글 클릭시 해당 게시글의 정보를 ajax로 가져오기
	@Override
	public void getFood(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");		
		String foodCode = request.getParameter("foodCode");
		Food food = new Food();
		Image imageDto = new Image();
		food = foodRepository.foodRead(foodCode);
		FoodDto foodDto = FoodDto.of(food);
		imageDto = imageRepository.imgRead(foodCode);	
		
		HashMap<String, Object> jsonHashMap = new HashMap<String, Object>();		
		
		if(imageDto != null) {			
			jsonHashMap.put("imageName", imageDto.getImageName());
		}	
		System.out.println("+===============" + foodDto.getFoodCode());
		jsonHashMap.put("foodCode", foodDto.getFoodCode());
		jsonHashMap.put("foodName", foodDto.getFoodName());
		jsonHashMap.put("foodAddr", foodDto.getFoodAddr());
		jsonHashMap.put("foodArea", foodDto.getFoodArea());
		jsonHashMap.put("foodPhone", foodDto.getFoodPhone());
		jsonHashMap.put("foodKind", foodDto.getFoodKind());
		jsonHashMap.put("foodMenu", foodDto.getFoodMenu());
		jsonHashMap.put("foodTime", foodDto.getFoodTime());
		jsonHashMap.put("foodBreak", foodDto.getFoodBreak());
		jsonHashMap.put("foodIntro", foodDto.getFoodIntro());
		jsonHashMap.put("foodStatus", foodDto.getFoodStatus());
		jsonHashMap.put("foodStatus", foodDto.getFoodStatus());
		jsonHashMap.put("memberCode", foodDto.getMemberCode());	
		JSONObject jsonFoodDto = new JSONObject(jsonHashMap);
		//JejuAspect.logger.info(JejuAspect.logMsg+jsonFoodDto.toString());		
		
		String jsonText = jsonFoodDto.toJSONString();
		//sJejuAspect.logger.info(JejuAspect.logMsg+"jsonText: "+jsonText);
		
		response.setContentType("application/x-json;charset=utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(jsonText);
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		
	// 음식점 등록, 대표이미지등록
	@Override
	public void foodInsertOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		FoodDto foodDto = (FoodDto) map.get("foodDto");
		int check = 1;
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");
		// 등록날짜 설정
		foodDto.setFoodDate(new Date());
		// 조회수 0 으로 설정
		foodDto.setFoodRead(0);		
		// 음식점 등록
		 
		String referCode = foodRepository.getcode(); 
		foodDto.setFoodCode(referCode);
		Food food = foodDto.createFood();
		foodRepository.save(food);
		// food_code의 마지막 값 가져옴
		
		//JejuAspect.logger.info(JejuAspect.logMsg+ "str : " +str);
		// 이미지 데이터를 넣을 DTO 
		ImageDto imageDto = new ImageDto();
		// 음식점 등록이 되면 이미지DTO.참조코드에 foodCode의 마지막 값을 넣음		
		imageDto.setReferCode(referCode);  
		// input type file의 name(imgFile)으로 확인
		MultipartFile upFile = request.getFile("imgFile"); 
		long fileSize = upFile.getSize();		
		if (fileSize != 0) {
			// 파일명 = 현재시간을 초단위로 변환한 값 + 올려질때 파일명
			String fileName = Long.toString(System.currentTimeMillis()) + "_" + upFile.getOriginalFilename();
			// 파일 생성위치 
			File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\"); 
			// 만들고자하는 디렉토리의 상위 디렉토리가 존재하지 않을 경우, 생성 불가...
			path.mkdirs();
			// 만들고자하는 디렉토리의 상위 디렉토리가 존재하지 않을 경우, 상위 디렉토리까지 생성
			//path.mkdirs();

			if (path.exists() && path.isDirectory()) {
				File file = new File(path, fileName);
				try {
					upFile.transferTo(file);
				} catch (IOException e) {
					e.printStackTrace();
				}								
				//JejuAspect.logger.info(JejuAspect.logMsg+ "imageDto : " +imageDto);
				check += imageRepository.imgInsert(referCode, fileName, fileSize, file.getAbsolutePath());				
			}					
		}		
		
		mav.addObject("check", check);
		mav.setViewName("food/insertOk.tiles");
		
	}
	
	@Override
	public void foodUpdate(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String referCode = request.getParameter("foodCode");		
		Food foodDto = new Food();		
		Image imageDto = new Image(); 
		// 음식점 정보 가져오기
		foodDto = foodRepository.foodRead(referCode);
		// 이미지 정보 가져오기
		imageDto = imageRepository.imgRead(referCode);
		
		//JejuAspect.logger.info(JejuAspect.logMsg+foodDto);
		//JejuAspect.logger.info(JejuAspect.logMsg+imageDto);
		mav.addObject("foodDto", foodDto);		
		if(imageDto != null) {
			mav.addObject("imageDto", imageDto);
		}
		mav.setViewName("food/update.tiles");
	}
	
	@Override
	public void foodUpdateOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		FoodDto foodDto = (FoodDto) map.get("foodDto");
		
		
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");
		
		// 업로드 등록일자 
		foodDto.setFoodDate(new Date());				
		
		// food 업데이트
		String foodCode = foodDto.getFoodCode();
		Food food = foodRepository.findByFoodCode(foodCode)
	                .orElseThrow(EntityNotFoundException::new);
		food.updateFood(foodDto);
		
		MultipartFile upFile = request.getFile("imgFile"); // input type file의 name으로 확인
		long fileSize = upFile.getSize();		
		if (fileSize != 0) {
			String fileName = Long.toString(System.currentTimeMillis()) + "_" + upFile.getOriginalFilename();

			File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\"); 
			path.mkdirs();	

			if (path.exists() && path.isDirectory()) {
				File file = new File(path, fileName);
				try {
					upFile.transferTo(file);
				} catch (IOException e) {
					e.printStackTrace();
				}				
				//JejuAspect.logger.info(JejuAspect.logMsg+updateImageDto.toString());				
	            
				Image savedItemImg = imageRepository.findimage(foodDto.getFoodCode());
	            if(savedItemImg != null) {
	            savedItemImg.updateImage(fileName, fileSize, file.getAbsolutePath());}
	            else {int check = imageRepository.imgInsert(foodCode, fileName, fileSize, file.getAbsolutePath());}	
			}			
		}
		
		mav.addObject("check", 2);
		mav.setViewName("food/updateOk.tiles");		
	}
	
	// 음식점 정보 삭제
	@Override
	public void foodDelete(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String foodCode = request.getParameter("foodCode");
		int check = 0;		
		
		
		// 가져온 이미지 정보가 있을 경우 해당 경로의 파일을 삭제
		

			// 음식점 코드를 통해 DB에서 이미지 정보 삭제
			check += imageRepository.deleteByReferCode(foodCode);		 
		// 음식점 코드를 통해 DB에서 음식점 정보 삭제
		check += foodRepository.deleteByFoodCode(foodCode);		
		
		mav.addObject("check",check);
		mav.setViewName("food/delete.tiles");		
	}

	

}
