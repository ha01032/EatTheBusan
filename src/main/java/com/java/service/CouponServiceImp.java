package com.java.service;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.java.aop.JejuAspect;
import com.java.repository.CouponRepository;
import com.java.repository.ImageRepository;
import com.java.dto.CouponAdminDto;
import com.java.dto.CouponDto;
import com.java.dto.CouponReadDto;
import com.java.dto.SearchFoodCodeDto;
import com.java.entity.Coupon;
import com.java.entity.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

//import com.java.dao.ImageDao;
import com.java.dto.ImageDto;

/**
 * @작성자 : 전지원
 * @작업일 : 2019. 12. 14.
 * @작업 내용 : insert & 식당코드 검색 작성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImp implements CouponService {

	@Autowired
	private final CouponRepository couponRepository;
	@Autowired
	private final ImageRepository imageRepository;
	//@Autowired
	//private final PurchaseRepository purchaseRepository;

	//@Autowired
	//private ImageDao imageDao;
	
	
	// 쿠폰리스트
	@Override
	public void couponList(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");

		String pageNumber = request.getParameter("pageNumber");

		if (pageNumber == null)
			pageNumber = "1";
		//JejuAspect.logger.info(JejuAspect.logMsg + "pageNumber: " + pageNumber);
		int currentPage = Integer.parseInt(pageNumber);

		// 쿠폰 리스트 카운트
		Long count = couponRepository.countBy();
		//JejuAspect.logger.info(JejuAspect.logMsg + "count: " + count);

		int scrollSize = 15;
		int startRow = (currentPage - 1) * scrollSize + 1;
		int endRow = currentPage * scrollSize;

		List<CouponDto> couponList = null;

		// 현재 날짜 출력
		Date today = new Date();
		//JejuAspect.logger.info(JejuAspect.logMsg + "date: " + today);

		if (count > 0) {
			// 쿠폰 리스트 가져오기

			couponList = couponRepository.couponList(startRow, endRow, today);
			

			//JejuAspect.logger.info(JejuAspect.logMsg + "couponList 사이즈: " + couponList.size());
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponList : " + couponList.toString());
			mav.addObject("couponList", couponList);
		}
		mav.addObject("count", count);
		mav.addObject("pageNumber", pageNumber);
	}
	
	
	
	
	
	// 쿠폰 리스트(Ajax 새로고침)
		@Override
		@ResponseBody
		public String couponListAjax(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");

			String pageNumber = request.getParameter("pageNumber");
			//JejuAspect.logger.info(JejuAspect.logMsg + "pageNumberAjax: " + pageNumber);

			int currentPage = Integer.parseInt(pageNumber);

			// 쿠폰 리스트 카운트
			Long count = couponRepository.countBy();
			//JejuAspect.logger.info(JejuAspect.logMsg + "count: " + count);

			int scrollSize = 15;
			int startRow = (currentPage - 1) * scrollSize + 1;
			int endRow = currentPage * scrollSize;
			//JejuAspect.logger.info(JejuAspect.logMsg + "startRow: " + startRow + " endRow:" + endRow);
			List<CouponDto> couponList = null;

			Date today = new Date();
			//JejuAspect.logger.info(JejuAspect.logMsg + "date: " + today);

			if (count > 0) {
				// 쿠폰 리스트 가져오기
				couponList = couponRepository.couponList(startRow, endRow, today);
				//JejuAspect.logger.info(JejuAspect.logMsg + "couponList 사이즈: " + couponList.size());
			}

			JSONArray arr = new JSONArray();
			for (CouponDto couponDto : couponList) {
				HashMap<String, Object> CommonMap = new HashMap<String, Object>();
				CommonMap.put("couponCode", couponDto.getCouponCode());
				CommonMap.put("foodCode", couponDto.getFoodCode());
				CommonMap.put("couponName", couponDto.getCouponName());
				CommonMap.put("couponStartdate", couponDto.getCouponStartdate());
				CommonMap.put("couponEnddate", couponDto.getCouponEnddate());
				CommonMap.put("couponCostori", couponDto.getCouponCostori());
				CommonMap.put("couponCostsale", couponDto.getCouponCostsale());
				CommonMap.put("imageName", couponDto.getImageName());
				CommonMap.put("couponSalerate", couponDto.getCouponSalerate());
				arr.add(CommonMap);
				//JejuAspect.logger.info(JejuAspect.logMsg + CommonMap.toString());
			}
			String jsonText = JSONValue.toJSONString(arr);
			//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);

			return jsonText;
		}
		
		// 쿠폰상세페이지
		@Override
		public void couponRead(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");

			//int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			String couponCode = request.getParameter("couponCode");
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponCode : " + couponCode);

			CouponReadDto couponReadDto = couponRepository.couponRead(couponCode);
			String couponIntro = couponReadDto.getCouponIntro();
			if(couponIntro != null) {
				couponReadDto.setCouponIntro(couponReadDto.getCouponIntro().replace("\r\n", "<br/>"));
			}
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponDto : " + couponDto.toString());

			mav.addObject("couponDto", couponReadDto);
			mav.setViewName("coupon/couponDetail.tiles");
		}

		// 쿠폰리스트[관리자]
		@Override
		public void couponListAdmin(ModelAndView mav) {
			
			// 쿠폰 리스트 가져오기
			List<CouponAdminDto> couponList = couponRepository.couponListAdmin();
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponList 사이즈: " + couponList.size());
			mav.addObject("couponList", couponList);
		}
		
		// 식당 코드 검색
		@Override
		public void searchFoodCode(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");

			String foodName = request.getParameter("foodName");
			//JejuAspect.logger.info(JejuAspect.logMsg + "검색 내용: " + foodName);

			String cInsert = request.getParameter("cInsert");
			request.setAttribute("cInsert", cInsert);

			if (foodName != null) {
				// 검색어로 식당코드 찾기
				List<SearchFoodCodeDto> searchFoodCodeList = couponRepository.searchFoodCode(foodName);

				//JejuAspect.logger.info(JejuAspect.logMsg + "searchFoodCodeList 사이즈: " + searchFoodCodeList.size());
				mav.addObject("foodCodeList", searchFoodCodeList);
			}
		}
		
		
		// 쿠폰상품 등록
		@Override
		public void couponInsertOk(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			CouponDto couponDto = (CouponDto) map.get("couponDto");
			String foodCode = couponDto.getFoodCode();
			String couponName = couponDto.getCouponName();
			String couponStartdate = couponDto.getCouponStartdate();
			String couponEnddate = couponDto.getCouponEnddate();
			int couponCostori = couponDto.getCouponCostori();
			int couponSalerate = couponDto.getCouponSalerate();
			int couponCostsale = couponDto.getCouponCostsale();
			String couponIntro = couponDto.getCouponIntro();
			
			String referCode  = couponRepository.getcode();
			int couponInsert = couponRepository.couponInsert(foodCode, couponName, couponStartdate, couponEnddate, couponCostori, couponSalerate, couponCostsale, couponIntro);
			if (referCode != null) {
				MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");
				MultipartFile upImage = request.getFile("imageFile");

				long imageSize = upImage.getSize();

				//String rootPath = request.getSession().getServletContext().getRealPath("/");
				//String attachPath = "static/ftp/";
				String imageName = Long.toString(System.currentTimeMillis()) + "_" + upImage.getOriginalFilename();

				File path = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\");
				path.mkdir();

				//JejuAspect.logger.info(JejuAspect.logMsg + "imageSize: " + imageSize);
				//JejuAspect.logger.info(JejuAspect.logMsg + "imageName: " + imageName);
				//JejuAspect.logger.info(JejuAspect.logMsg + "이미지 실제 저장경로: " + imagePath);

				if (path.exists() && path.isDirectory()) {
					File file = new File(path, imageName);
					System.out.println("CHECK");
					try {
						upImage.transferTo(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String imagePath = path.getAbsolutePath() + imageName;
					ImageDto imageDto = new ImageDto();
					int check = imageRepository.imgInsert(referCode, imageName, imageSize, imagePath);
					System.out.println("==========check값:" + check);
					//JejuAspect.logger.info(JejuAspect.logMsg + "imageDto: " + imageDto.toString());
					//JejuAspect.logger.info(JejuAspect.logMsg + "check: " + check);
					mav.addObject("check", check);
				}
			}
			mav.setViewName("coupon/couponInsertOk.tiles");
		}
		
		// 쿠폰상품수정페이지
		@Override
		public String couponUpdate(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");

			String couponCode = request.getParameter("couponCode");
			// int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponCode : " + couponCode);

			CouponReadDto couponDto = couponRepository.couponRead(couponCode);
			// request.setAttribute("pageNumber", pageNumber);

			String couponStatus = couponDto.getCouponStatus();
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponStatus : " + couponStatus);

			Map<String, Object> upMap = new HashMap<String, Object>();
			upMap.put("couponCode", couponDto.getCouponCode());
			upMap.put("foodCode", couponDto.getFoodCode());
			upMap.put("foodName", couponDto.getFoodName());
			upMap.put("couponName", couponDto.getCouponName());
			upMap.put("couponStartdate", couponDto.getCouponStartdate());
			upMap.put("couponEnddate", couponDto.getCouponEnddate());
			upMap.put("couponCostori", couponDto.getCouponCostori());
			upMap.put("couponCostsale", couponDto.getCouponCostsale());
			upMap.put("imageName", couponDto.getImageName());
			upMap.put("couponSalerate", couponDto.getCouponSalerate());
			upMap.put("couponIntro", couponDto.getCouponIntro());
			upMap.put("couponStatus", couponDto.getCouponStatus());
			//JejuAspect.logger.info(JejuAspect.logMsg + upMap.toString());

			String jsonText = JSONValue.toJSONString(upMap);
			//JejuAspect.logger.info(JejuAspect.logMsg + "JSONtext : " + jsonText);

			mav.addObject("couponDto", couponDto);
			mav.setViewName("coupon/couponUpdate.tiles");

			return jsonText;
		}
		
		// 쿠폰상품 수정
		@Override
		public void couponUpdateOk(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			MultipartHttpServletRequest request = (MultipartHttpServletRequest) map.get("request");
			MultipartFile upImage = request.getFile("imageFile");

			CouponReadDto couponDto = (CouponReadDto) map.get("couponDto");
			String couponStatus = couponDto.getCouponStatus();
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponStatus: " + couponStatus);
			ImageDto imageDto = (ImageDto) map.get("imageDto");

			String couponCode = couponDto.getCouponCode();

			//JejuAspect.logger.info(JejuAspect.logMsg + "couponDto: " + couponDto.toString());
			//JejuAspect.logger.info(JejuAspect.logMsg + "imageDto: " + imageDto.toString());

			Iterator<String> iter = request.getFileNames();

			while (iter.hasNext()) {
				MultipartFile upFile = request.getFile(iter.next());
				long fileSize = upFile.getSize();

				if (upFile.isEmpty() == false) {
					if (fileSize != 0) {
						long imageSize = upImage.getSize();
						String imageName = Long.toString(System.currentTimeMillis()) + "_" + upImage.getOriginalFilename();

						//String dir = "/ftp/";
						//ServletContext context = request.getSession().getServletContext();
						//String realFolder = context.getRealPath(dir);
						//File imagePath = new File(realFolder);
						File imagePath = new File("C:\\spring\\eatthebusan\\src\\main\\resources\\static\\ftp\\");
						imagePath.mkdir();
						//JejuAspect.logger.info(JejuAspect.logMsg + "imagePath: " + imagePath);

						if (imagePath.exists() && imagePath.isDirectory()) {
							File file = new File(imagePath, imageName);
							try {
								upImage.transferTo(file);
							} catch (IOException e) {
								e.printStackTrace();
							}

							imageDto.setImageName(imageName);
							imageDto.setReferCode(couponCode);
							imageDto.setImageSize(imageSize);
							imageDto.setImagePath(file.getAbsolutePath());
							System.out.println("=================" + imageDto.toString());
							//JejuAspect.logger.info(JejuAspect.logMsg + "imageDto: " + imageDto.toString());

							// 쿠폰 이미지 수정
				            Image savedItemImg = imageRepository.findByReferCode(couponCode)
				                    .orElseThrow(EntityNotFoundException::new);
				            savedItemImg.updateImage(imageName, imageSize, file.getAbsolutePath());
				            
					       
					        
							//JejuAspect.logger.info(JejuAspect.logMsg + "쿠폰이미지 수정: " + check);
						}
					}
				}
				//check = .couponUpdateOk(couponDto);
				 Coupon coupon = couponRepository.findByCouponCode(couponCode)
			                .orElseThrow(EntityNotFoundException::new);
				 coupon.updateCoupon(couponDto);
				 //음식점 코드 변경기능은 못넣음 - 외래키라.. 추후에 더 연구하고 넣기
	
				//JejuAspect.logger.info(JejuAspect.logMsg + "쿠폰내용 수정: " + check);

				mav.addObject("check", 1);
			}

			mav.addObject("couponCode", couponCode);
			mav.setViewName("coupon/couponUpdateOk.tiles");
		}
		
		// 쿠폰 삭제
		@Override
		public String couponDeleteOk(ModelAndView mav) {
			Map<String, Object> map = mav.getModelMap();
			HttpServletRequest request = (HttpServletRequest) map.get("request");
			String couponCode = request.getParameter("couponCode");
			//JejuAspect.logger.info(JejuAspect.logMsg + "couponCode: " + couponCode);
			
			// 삭제할 이미지 정보를 가져옴
			Image image = imageRepository.imgRead(couponCode);
			ImageDto imageDto = ImageDto.of(image);
			// 가져온 이미지 정보가 있을 경우 해당 경로의 파일을 삭제
			
			if(imageDto != null) {
				if (imageDto.getImageName() != null) {
					File checkFile = new File(imageDto.getImagePath());
					if (checkFile.exists() && checkFile.isFile()) {
						checkFile.delete();
					}
				}
			}
			// 음식점 코드를 통해 DB에서 이미지 정보 삭제
			int check1 = imageRepository.deleteByReferCode(couponCode);	
			int check = couponRepository.deleteByCouponCode(couponCode);
			System.out.println("=====check====" + check);
			
			
			
			//int check = couponRepository.
				//	.couponDeleteOk(couponCode);
			//JejuAspect.logger.info(JejuAspect.logMsg + "check_3: " + check);

			Map<String, Integer> cDelMap = new HashMap<String, Integer>();
			cDelMap.put("check", check);

			String jsonText = JSONValue.toJSONString(cDelMap);
			//JejuAspect.logger.info(JejuAspect.logMsg + "jsonText: " + jsonText);

			mav.addObject("check", check);

			return jsonText;
		}
		
				


	

}
