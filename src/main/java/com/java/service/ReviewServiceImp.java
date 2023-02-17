package com.java.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.java.aop.JejuAspect;
import com.java.dto.ImageDto;
import com.java.dto.ReviewDto;
import com.java.entity.Member;
import com.java.entity.Food;
import com.java.entity.Image;
import com.java.entity.Review;
import com.java.repository.CouponRepository;
import com.java.repository.FavoriteRepository;
import com.java.repository.FoodRepository;
import com.java.repository.ImageRepository;
import com.java.repository.MemberRepository;
import com.java.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImp implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ImageRepository imageRepository;

	// 리뷰 정보 등록시 페이지로 이동
	@Override
	public void reviewInsert(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String foodCode = request.getParameter("foodCode");
		String foodName = reviewRepository.getFoodName(foodCode);
		mav.addObject("foodName", foodName);
		mav.addObject("foodCode", foodCode);
		mav.setViewName("review/insert.tiles");
	}

	// 리뷰 정보 등록시 정보 입력
	@Override
	public void reviewInsertOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest requestSession = (HttpServletRequest) map.get("request");
		HttpSession session = requestSession.getSession();
		String memberCode = (String) session.getAttribute("memberCode");
		int check = 1;
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");
		List<MultipartFile> files = request.getFiles("imgFile");

		ReviewDto reviewDto = (ReviewDto) map.get("reviewDto");
		// JejuAspect.logger.info(JejuAspect.logMsg + "files : " +files.toString());
		reviewDto.setReviewDate(new Date());
		reviewDto.setMemberCode(memberCode);
		// JejuAspect.logger.info(JejuAspect.logMsg + reviewDto);
		String referCode = reviewRepository.getcode();
		reviewDto.setReviewCode(referCode);
		Review review = reviewDto.createReview();
		reviewRepository.save(review);
		for (MultipartFile f : files) {
			if (!f.isEmpty()) {
				ImageDto imageDto = new ImageDto();
				imageDto.setReferCode(referCode);
				long fileSize = f.getSize();
				if (fileSize != 0) {
					String fileName = Long.toString(System.currentTimeMillis()) + "_" + f.getOriginalFilename();

					File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\");
					// C:\\Spring\\workspace\\image
					// C://Spring//workspace//springProject//static//ftp
					// C:\\ftp\\
					path.mkdirs();

					if (path.exists() && path.isDirectory()) {
						File file = new File(path, fileName);
						try {
							f.transferTo(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						imageDto.setImagePath(file.getAbsolutePath());
						imageDto.setImageSize(fileSize);
						imageDto.setImageName(fileName);
						// JejuAspect.logger.info(JejuAspect.logMsg+check);
						check += imageRepository.imgInsert(referCode, fileName, fileSize, file.getAbsolutePath());
						// JejuAspect.logger.info(JejuAspect.logMsg + "imageDto : "+
						// imageDto.toString());
					}
				}
			}
		}
		mav.addObject("foodCode", reviewDto.getFoodCode());
		mav.addObject("check", check);
		mav.setViewName("review/insertOk.tiles");
	}

	// 리뷰 정보 수정시 해당 페이지로 이동 및 리뷰 정보를 가져오기

	@Override
	public void reviewUpdate(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String reviewCode = request.getParameter("reviewCode");
		Review reviewDto = new Review();
		reviewDto = reviewRepository.reviewUpdate(reviewCode);
		Food food = reviewDto.getFood();
		String foodCode = food.getFoodCode();
		String foodName = food.getFoodName();
		Member member = reviewDto.getMember();
		String memberCode = member.getMemberCode();

		List<Image> listImage = imageRepository.imgList(reviewCode);
		// JejuAspect.logger.info(JejuAspect.logMsg+listImage.toString());
		mav.addObject("foodName", foodName);
		mav.addObject("reviewDto", reviewDto);
		mav.addObject("listImage", listImage);
		mav.addObject("food", foodCode);
		mav.addObject("member", memberCode);
		mav.setViewName("review/update.tiles");

	}
	
	@Override
	public void reviewMyUpdate(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String reviewCode = request.getParameter("reviewCode");
		Review reviewDto = new Review();
		reviewDto = reviewRepository.reviewUpdate(reviewCode);
		Food food = reviewDto.getFood();
		String foodCode = food.getFoodCode();
		String foodName = food.getFoodName();
		Member member = reviewDto.getMember();
		String memberCode = member.getMemberCode();
		List<Image> listImage = imageRepository.imgList(reviewCode);		
		//JejuAspect.logger.info(JejuAspect.logMsg+listImage.toString());
		mav.addObject("foodName",foodName);
		mav.addObject("reviewDto",reviewDto);
		mav.addObject("listImage",listImage);
		mav.addObject("food", foodCode);
		mav.addObject("member", memberCode);
		mav.setViewName("review/myUpdate.tiles");	
	}
	
	// 관리자 페이지로 이동시 리스트 가져오기
	@Override
	public void adminReviewList(ModelAndView mav) {
		List<ReviewDto> reviewDtoList = reviewRepository.allreview();

		// JejuAspect.logger.info(JejuAspect.logMsg+reviewDtoList.toString());
		mav.addObject("reviewDtoList", reviewDtoList);
		mav.setViewName("admin/review.admin");
	}
	
	// 리뷰 정보 수정시 입력된 리뷰 정보 보내기 
	@Override
	public void reviewUpdateOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");		
		List<MultipartFile> files = request.getFiles("imgFile");
		int check = 1;
		ReviewDto reviewDto = (ReviewDto) map.get("reviewDto");
		String foodCode = request.getParameter("foodCode");
		reviewDto.setReviewDate(new Date());
		//JejuAspect.logger.info(JejuAspect.logMsg + reviewDto);
		String reviewCode = reviewDto.getReviewCode();
		Review review = reviewRepository.findByReviewCode(reviewCode)
                .orElseThrow(EntityNotFoundException::new);
		review.updateReview(reviewDto);
		// 삭제할 이미지의 이름을 받음 (ex: image.jpg,image1.jpg)
		String delStr = request.getParameter("deleteImg");
		//JejuAspect.logger.info(JejuAspect.logMsg + delStr.toString());
		if(delStr != null && !delStr.equals("")) {
			// split하여 delList라는 문자열배열에 입력
			String[] delList = delStr.split(",");			 
			for(int i = 0; i < delList.length; i++) {
				// 돌면서 해당 리뷰 코드의 값과 이름을 삭제할 Dto에 넣고 해당 값의 이미지DTO를 불러옴
				Image imageDto = imageRepository.findreviewimage(reviewCode, delList[i]);		
				// 불러온 이미지 DTO에서 이름이 있으면 해당 파일의 주소를 가지고 삭제
				if (imageDto.getImageName() != null) {
					File checkFile = new File(imageDto.getImagePath());
					if (checkFile.exists() && checkFile.isFile()) {
						checkFile.delete();
					}
				}
				// 삭제할 이미지 DTO를 가지고 table로 부터 삭제
				imageRepository.imgSelectDelete(reviewCode, delList[i]);
			}		
		}		
		// 입력받은 이미지 파일들을 체크하여 새로 입력
		for (MultipartFile f : files) {			
			if (!f.isEmpty()) {
				ImageDto imageDto = new ImageDto();
				imageDto.setReferCode(reviewCode);
				long fileSize = f.getSize();
				if (fileSize != 0) {
					String fileName = Long.toString(System.currentTimeMillis()) + "_" + f.getOriginalFilename();

					File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\"); 
					path.mkdirs();					

					if (path.exists() && path.isDirectory()) {
						File file = new File(path, fileName);
						try {
							f.transferTo(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						imageDto.setImagePath(file.getAbsolutePath());
						imageDto.setImageSize(fileSize);
						imageDto.setImageName(fileName);
						//JejuAspect.logger.info(JejuAspect.logMsg + imageDto.toString());
						//JejuAspect.logger.info(JejuAspect.logMsg+check);
						check += imageRepository.imgInsert(reviewCode, fileName, fileSize, file.getAbsolutePath());
					}
				}
			}
		}
		mav.addObject("check", check);
		mav.addObject("foodCode",foodCode);
		mav.setViewName("review/updateOk.tiles");		
	}
	
	@Override
	public void reviewMyUpdateOk(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");		
		List<MultipartFile> files = request.getFiles("imgFile");
		int check = 1;
		ReviewDto reviewDto = (ReviewDto) map.get("reviewDto");
		String foodCode = request.getParameter("foodCode");
		reviewDto.setReviewDate(new Date());
		//JejuAspect.logger.info(JejuAspect.logMsg + reviewDto);
		String reviewCode = reviewDto.getReviewCode();
		Review review = reviewRepository.findByReviewCode(reviewCode)
                .orElseThrow(EntityNotFoundException::new);
		review.updateReview(reviewDto);
		// 삭제할 이미지의 이름을 받음 (ex: image.jpg,image1.jpg)
		String delStr = request.getParameter("deleteImg");
		//JejuAspect.logger.info(JejuAspect.logMsg + delStr.toString());
		if(delStr != null && !delStr.equals("")) {
			// split하여 delList라는 문자열배열에 입력
			String[] delList = delStr.split(",");			 
			for(int i = 0; i < delList.length; i++) {
				// 돌면서 해당 리뷰 코드의 값과 이름을 삭제할 Dto에 넣고 해당 값의 이미지DTO를 불러옴
				Image imageDto = imageRepository.findreviewimage(reviewCode, delList[i]);		
				// 불러온 이미지 DTO에서 이름이 있으면 해당 파일의 주소를 가지고 삭제
				if (imageDto.getImageName() != null) {
					File checkFile = new File(imageDto.getImagePath());
					if (checkFile.exists() && checkFile.isFile()) {
						checkFile.delete();
					}
				}
				// 삭제할 이미지 DTO를 가지고 table로 부터 삭제
				imageRepository.imgSelectDelete(reviewCode, delList[i]);
			}		
		}		
		// 입력받은 이미지 파일들을 체크하여 새로 입력
		for (MultipartFile f : files) {			
			if (!f.isEmpty()) {
				ImageDto imageDto = new ImageDto();
				imageDto.setReferCode(reviewCode);
				long fileSize = f.getSize();
				if (fileSize != 0) {
					String fileName = Long.toString(System.currentTimeMillis()) + "_" + f.getOriginalFilename();

					File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\"); 
					path.mkdirs();					

					if (path.exists() && path.isDirectory()) {
						File file = new File(path, fileName);
						try {
							f.transferTo(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						imageDto.setImagePath(file.getAbsolutePath());
						imageDto.setImageSize(fileSize);
						imageDto.setImageName(fileName);
						//JejuAspect.logger.info(JejuAspect.logMsg + imageDto.toString());
						//JejuAspect.logger.info(JejuAspect.logMsg+check);
						check += imageRepository.imgInsert(reviewCode, fileName, fileSize, file.getAbsolutePath());
					}
				}
			}
		}
		mav.addObject("check", check);
		mav.addObject("foodCode",foodCode);
		mav.setViewName("review/myUpdateOk.tiles");		
	}
	
	// 유저가 리뷰 삭제
	@Override
	public void userReviewDelete(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String reviewCode = request.getParameter("reviewCode");
		String foodCode = request.getParameter("foodCode");
		//JejuAspect.logger.info(JejuAspect.logMsg+reviewCode);
		int check = 0;
		List<Image> imageList = imageRepository.imgList(reviewCode);
		for(int i = 0; i< imageList.size(); i++) {
			if (imageList.get(i).getImageName() != null) {
				File checkFile = new File(imageList.get(i).getImagePath());
				if (checkFile.exists() && checkFile.isFile()) {
					checkFile.delete();
				}
			}
		}		
		check = reviewRepository.deleteByReviewCode(reviewCode);
		check += imageRepository.deleteByReferCode(reviewCode);
		mav.addObject("check",check);
		mav.addObject("foodCode",foodCode);
		mav.setViewName("review/userDelete.tiles");		
	}
	
	// 관리자 페이지에서 게시글 클릭시 해당 리뷰 정보 가져오기
	@Override
	public void getReview(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String reviewCode = request.getParameter("reviewCode");
		Review reviewDto = reviewRepository.reviewUpdate(reviewCode);
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Food food = reviewDto.getFood();
		String foodCode = food.getFoodCode();
		String foodName = food.getFoodName();	
		Member member = reviewDto.getMember();
		String memberCode = member.getMemberCode();
		List<Image> listImage = imageRepository.imgList(reviewCode);
		System.out.println(listImage);
		JSONArray jsonArr = new JSONArray();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		for(Image imgDto : listImage) {			
			hashMap.put("imageName", imgDto.getImageName());
			JSONObject jsonImgDto = new JSONObject(hashMap);
			//jsonImgDto.put("imageName", imgDto.getImageName());
			jsonArr.add(jsonImgDto);
		}
		
		
		//JejuAspect.logger.info(JejuAspect.logMsg+reviewDto.toString()+","+listImage.toString());
		
		hashMap.put("reviewCode", reviewDto.getReviewCode());
		hashMap.put("foodCode",	foodCode); 
		hashMap.put("memberCode", memberCode); 
		hashMap.put("reviewDate", date.format(reviewDto.getReviewDate())); 
		hashMap.put("reviewCont", reviewDto.getReviewCont());
		hashMap.put("reviewScore", reviewDto.getReviewScore());
		hashMap.put("listImage", jsonArr);		 
		
		JSONObject jsonReviewDto = new JSONObject(hashMap);		
		
		String jsonText = jsonReviewDto.toJSONString();
		//JejuAspect.logger.info(JejuAspect.logMsg+"jsonText: "+jsonText);
		
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
	
	// 관리자가 리뷰 삭제
	@Override
	public void reviewDelete(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String reviewCode = request.getParameter("reviewCode");
		//JejuAspect.logger.info(JejuAspect.logMsg+reviewCode);
		int check = 0;
		List<Image> imageList = imageRepository.imgList(reviewCode);
		for(int i = 0; i< imageList.size(); i++) {
			if (imageList.get(i).getImageName() != null) {
				File checkFile = new File(imageList.get(i).getImagePath());
				if (checkFile.exists() && checkFile.isFile()) {
					checkFile.delete();
				}
			}
		}		
		check = reviewRepository.deleteByReviewCode(reviewCode);
		check += imageRepository.deleteByReferCode(reviewCode);
		mav.addObject("check",check);
		mav.setViewName("review/delete.tiles");
	}
	
	
	// 리뷰 정보 읽기
	@Override
	public void reviewRead(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		String reviewCode = (String)  request.getParameter("reviewCode");
		//JejuAspect.logger.info(JejuAspect.logMsg+" reviewCode : "+reviewCode);
		Review reviewDto = reviewRepository.reviewUpdate(reviewCode);
		
		  JSONObject jo = new JSONObject();
		  jo.put("reviewCont", reviewDto.getReviewCont()); 
		  jo.put("reviewScore", reviewDto.getReviewScore()); 
		  
		 String foodCode = request.getParameter("foodCode");
		String foodName = reviewRepository.getFoodName(foodCode);
		List<Image> listImage = imageRepository.imgList(reviewCode);
//	
		JSONArray arr = new JSONArray();
		for(Image imageDto : listImage) {
			HashMap<String, Object> hmap = new HashMap<String, Object>();
			hmap.put("imageCode", imageDto.getImageCode());
			hmap.put("referCode", imageDto.getReferCode());
			hmap.put("imageName", imageDto.getImageName());
			hmap.put("imageSize", imageDto.getImageSize());
			hmap.put("imagePath", imageDto.getImagePath());
			arr.add(hmap);
		}
		jo.put("imgList",arr);
		
		String jsonText = jo.toJSONString();
		System.out.println(jsonText);
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
	
	
	@Override
	public void myUserReviewDelete(ModelAndView mav) {
		Map<String, Object> map = mav.getModel();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String reviewCode = request.getParameter("reviewCode");
		String foodCode = request.getParameter("foodCode");
		//JejuAspect.logger.info(JejuAspect.logMsg+reviewCode);
		int check = 0;
		List<Image> imageList = imageRepository.imgList(reviewCode);
		for(int i = 0; i< imageList.size(); i++) {
			if (imageList.get(i).getImageName() != null) {
				File checkFile = new File(imageList.get(i).getImagePath());
				if (checkFile.exists() && checkFile.isFile()) {
					checkFile.delete();
				}
			}
		}		
		check = reviewRepository.deleteByReviewCode(reviewCode);
		check += imageRepository.deleteByReferCode(reviewCode);
		mav.addObject("check",check);
		mav.addObject("foodCode",foodCode);
		mav.setViewName("review/myUserDelete.tiles");		
		
	}
	


}
