package com.java.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.dto.CountDto;
import com.java.dto.CouponDto;
import com.java.dto.FoodDto;
import com.java.dto.FoodReviewDto;
import com.java.dto.FoodStarDto;
import com.java.dto.ReviewCountDto;
import com.java.dto.SearchCouponDto;
import com.java.dto.SearchCouponListDto;
import com.java.dto.SearchFoodCodeDto;
import com.java.dto.SearchFoodDto2;
import com.java.dto.SearchPopularDto;
import com.java.entity.Coupon;
import com.java.entity.Food;
import com.java.entity.Purchase;

public interface FoodRepository extends JpaRepository<Food, Long> {
	
	@Query(value = 
			" SELECT count(*) FROM food WHERE food_code IN( " +				
			" (SELECT distinct food_code from ( " +				
			" SELECT food_code from food where food_name like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_menu like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_kind like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_addr like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_tag like ('%'||:keyword||'%')))) ", 				 
			nativeQuery = true)
	int searchCount(String keyword);	
	
	
	@Query(value = 
			" SELECT count(*) FROM food WHERE food_code IN( " +				
			" (SELECT distinct food_code from ( " +				
			" SELECT food_code from food where food_name like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_menu like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_kind like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_addr like ('%'||:keyword||'%') " +				
			" union all " +				
			" SELECT food_code from food where food_tag like ('%'||:keyword||'%')))) " +
			" and (food_kind LIKE (:kindType||'%'))", 				 
			nativeQuery = true)
	int kindsearchCount(String keyword, String kindType);
	
	@Query(name ="find_manysearch", nativeQuery = true)
	List<SearchFoodDto2> manysearchResult(String keyword, int startRow, int endRow);
	
	@Query(name ="find_kindmanysearch", nativeQuery = true)
	List<SearchFoodDto2> kindmanysearchResult(String keyword, String kindType, int startRow, int endRow);
	
	@Query(name ="find_goodsearch", nativeQuery = true)
	List<SearchFoodDto2> goodsearchResult(String keyword, int startRow, int endRow);
	
	@Query(name ="find_kindgoodsearch", nativeQuery = true)
	List<SearchFoodDto2> kindgoodsearchResult(String keyword, String kindType, int startRow, int endRow);
	
	@Query(name = "countByKeywordLike", nativeQuery = true)
	CountDto couponCount(String keyword);
   
	@Query(name = "findByKeywordLike", nativeQuery = true)
	List<SearchCouponListDto> couponList(String keyword);
	
	@Query(name = "find_foodreviewdto", nativeQuery = true)
	List<FoodReviewDto> reviewList(String foodCode);
	
	@Query(name = "find_foodstardto", nativeQuery = true)
	FoodStarDto getReviewScore(String foodCode);
	
	@Query(value = "select * from food where food_code = :foodCode", nativeQuery = true)
	Food foodRead(String foodCode);
	
	@Query(name = "find_coupondto2", nativeQuery = true)
	List<CouponDto> foodCouponList(String foodCode);
	
	@Modifying
	@Transactional
	@Query(value = "update food set food_read=food_read+1 where food_code = :foodCode", nativeQuery = true)
	void foodReadUpdate(String foodCode);
	
	@Query(name = "find_reviewcountdto", nativeQuery = true)
	ReviewCountDto foodReviewCount(String foodCode);
	
	@Query(name = "find_foodreviewdto2", nativeQuery = true)
	List<FoodReviewDto> foodReviewListScore(String foodCode, String selScore); 
	
	@Query(name = "find_allfoodDto", nativeQuery = true)
	List<FoodDto> allrestaurant();
	
	@Query(value = "SELECT 'food'||LPAD(SEQFOOD.nextval,4,0) As foodCode from dual",
			 nativeQuery = true)
	String getcode();
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO food (food_code,food_name,food_addr,food_area,food_phone,"
			+ "food_kind,food_menu,food_time,food_break,food_intro,food_date,food_read,"
			+ "food_status,member_code) values ('food'||LPAD(SEQFOOD.CURRVAL,4,0),"
			+ ":foodName, :foodAddr, :foodArea, :foodPhone, :foodKind,"
			+ ":foodMenu, :foodTime, :foodBreak, :foodIntro, :foodDate, :foodRead, :foodStatus, :memberCode)"
			, nativeQuery = true)
	public int foodinsert
	(@Param("foodName") String foodName, @Param("foodAddr") String foodAddr, @Param("foodArea") String foodArea, @Param("foodPhone") String foodPhone,
			@Param("foodKind") String foodKind, @Param("foodMenu") String foodMenu, @Param("foodTime") String foodTime, @Param("foodBreak") String foodBreak,
			@Param("foodIntro") String foodIntro, @Param("foodDate") Date foodDate, @Param("foodRead") int foodRead,
			@Param("foodStatus") String foodStatus, @Param("memberCode") String memberCode);
	
	
	Optional<Food> findByFoodCode(String foodCode);
	
	   @Modifying
	   @Transactional
	   @Query(value = "DELETE FROM food WHERE food_code = :foodCode ", nativeQuery = true)
	   public int deleteByFoodCode(@Param("foodCode") String foodCode);
	
	@Query(name = "find_searchpopulardto", nativeQuery = true)
	List<SearchPopularDto> popularList();
	
	@Modifying
	@Transactional
	@Query(value = " insert into food(food_code, member_code, food_name, food_addr, food_kind, food_status, food_date, food_read, food_phone) " +
	"values('food'||LPAD(seqFood.nextval,4,0), :memberCode, :foodName, :foodAddr, :foodKind,'N', sysdate, '0' ,'0')"
	, nativeQuery = true)
	public int myfoodinsert
	(@Param("memberCode") String memberCode, @Param("foodName") String foodName, @Param("foodAddr") String foodAddr, @Param("foodKind") String foodKind);
}

