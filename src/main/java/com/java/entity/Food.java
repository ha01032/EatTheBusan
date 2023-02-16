package com.java.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.java.dto.CountDto;
import com.java.dto.CouponDto;
import com.java.dto.CouponReadDto;
import com.java.dto.FoodDto;
import com.java.dto.FoodDto2;
import com.java.dto.FoodReviewDto;
import com.java.dto.FoodStarDto;
import com.java.dto.ReviewCountDto;
import com.java.dto.SearchCouponListDto;
import com.java.dto.SearchFoodDto2;
import com.java.dto.SearchPopularDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;




@Entity


@NamedNativeQuery(				
		name = "find_searchpopulardto",			
		query =			
		"	select  "	+
		"	rnum as rnum,		"  +
		"	food_code as foodCode,		"  +
		"	food_name as foodName,		"  +
		"	food_addr as foodAddr,		"  +
		"	food_area as foodArea,		"  +
		"	food_phone as foodPhone,		"  +
		"	food_kind as foodKind,		"  +
		"	food_read as foodRead,		"  +
		"	food_menu as foodMenu,		"  +
		"	image_name as imageName,		"  +
		"	image_path as imagePath,		"  +
		
		"	review_score as reviewScore,	"  +
		"	review_count as reviewCount	from " +			
		"	(SELECT ROWNUM rnum, A.* FROM	"	+ 
		"	(SELECT food_code, food_name, food_addr, food_area, food_phone, "	+ 
		"	food_kind, food_read, food_menu, image_name, image_path, review_food ,nvl(review_score,0)  "	+ 
		"	review_score, nvl(review_count,0) review_count FROM 	"	+ 
		"	food, image,  	"	+ 
		"	(SELECT food_code review_food, NVL(AVG(review_score),0) review_score, nvl(COUNT(review_code),0) review_count 	"	+ 
		"	FROM review GROUP BY food_code) rv	"	+ 
		"	WHERE food.food_code = image.refer_code(+)	"	+ 
		"	AND food.food_code = rv.review_food(+)	"	+ 
		"	ORDER BY food_read DESC ) 	A	) 	"	+ 
		"	WHERE rnum >= 1 and rnum <= 4		",				
	resultSetMapping = "searchpopulardto"				
	)
@SqlResultSetMapping(				
		name = "searchpopulardto",				
		classes = @ConstructorResult(				
		targetClass = SearchPopularDto.class,				
		columns = {				
		@ColumnResult(name = "rnum", type = int.class),
		@ColumnResult(name = "foodCode", type = String.class),
		@ColumnResult(name = "foodName", type = String.class),
		@ColumnResult(name = "foodAddr", type = String.class),
		@ColumnResult(name = "foodArea", type = String.class),
		@ColumnResult(name = "foodPhone", type = String.class),
		@ColumnResult(name = "foodKind", type = String.class),
		@ColumnResult(name = "foodRead", type = int.class),
		@ColumnResult(name = "foodMenu", type = String.class),
		@ColumnResult(name = "imageName", type = String.class),
		@ColumnResult(name = "imagePath", type = String.class),
		
		@ColumnResult(name = "reviewScore", type = float.class),
		@ColumnResult(name = "reviewCount", type = int.class)
		}				
		)				
		)	




@NamedNativeQuery(			
name = "find_allfoodDto",			
query =			
"	Select	 " +	
"	food_code AS foodCode,	 " +	
"	food_name AS foodName,	 " +	
"	food_addr AS foodAddr,	 " +	
"	food_area AS foodArea,	 " +	
"	food_phone AS foodPhone,	 " +	
"	food_kind AS foodKind,	 " +	
"	food_menu AS foodMenu,	 " +	
"	food_time AS foodTime,	 " +	
"	food_break AS foodBreak,	 " +	
"	food_intro AS foodIntro,	 " +	
"	food_date AS foodDate,	 " +	
"	food_read AS foodRead,	 " +	
"	food_status AS foodStatus,	 " +	
"	member_code AS memberCode,	 " +	
"	food_tag AS foodTag	 " +	
"	FROM food	",	
resultSetMapping = "fooddto2"			
)
@SqlResultSetMapping(
name = "fooddto2",
classes = @ConstructorResult(
targetClass = FoodDto.class,
columns = {
@ColumnResult(name = "foodCode", type = String .class),
@ColumnResult(name = "foodName", type = String.class),
@ColumnResult(name = "foodAddr", type = String.class),
@ColumnResult(name = "foodArea", type = String.class),
@ColumnResult(name = "foodPhone", type = String.class),
@ColumnResult(name = "foodKind", type = String.class),
@ColumnResult(name = "foodMenu", type = String.class),
@ColumnResult(name = "foodTime", type = String.class),
@ColumnResult(name = "foodBreak", type = String.class),
@ColumnResult(name = "foodIntro", type = String.class),
@ColumnResult(name = "foodDate", type = Date.class),
@ColumnResult(name = "foodRead", type = int.class),
@ColumnResult(name = "foodStatus", type = String.class),
@ColumnResult(name = "memberCode", type = String.class),
@ColumnResult(name = "foodTag", type = String.class)
}
)
)
		

@NamedNativeQuery(			
name = "find_coupondto2",			
query =			
"	SELECT B.coupon_code AS couponCode, B.food_code AS foodCode, B.coupon_name AS couponName, B.coupon_costori AS couponCostori, B.coupon_salerate AS couponSalerate, B.coupon_costsale AS couponCostsale, B.coupon_intro AS couponIntro,	 " +	
"	TO_CHAR(B.coupon_startdate, 'YYYY-MM-DD') AS couponStartdate, TO_CHAR(B.coupon_enddate, 'YYYY-MM-DD') AS couponEnddate, image_name AS imageName, image_path	AS imagePath " +	
"	FROM (SELECT ROWNUM as num, A.* FROM (select * from coupon, image WHERE coupon_code = refer_code AND coupon_enddate >= sysdate AND coupon_status = 'Y' AND food_code = :foodCode ORDER BY coupon_code DESC) A) B	 " ,	
resultSetMapping = "coupondto2"			
)			
@SqlResultSetMapping(			
name = "coupondto2",			
classes = @ConstructorResult(			
targetClass = CouponDto.class,			
columns = {			
@ColumnResult(name = "couponCode", type =  String.class),			
@ColumnResult(name = "foodCode", type = String.class),			
@ColumnResult(name = "couponName", type =  String.class),			
@ColumnResult(name = "couponCostori", type = int.class),			
@ColumnResult(name = "couponSalerate", type =  int.class),			
@ColumnResult(name = "couponCostsale", type =  int.class),			
@ColumnResult(name = "couponIntro", type =  String.class),			
@ColumnResult(name = "couponStartdate", type =  String.class),			
@ColumnResult(name = "couponEnddate", type =  String.class),			
@ColumnResult(name = "imageName", type =  String.class),			
@ColumnResult(name = "imagePath ", type =  String.class)			
}			
)			
)			


@NamedNativeQuery(			
name = "find_foodreviewdto",			
query =			
"	SELECT REVIEW.review_code AS reviewCode, REVIEW.food_code AS foodCode, REVIEW.member_code AS memberCode, MEMBER.member_name AS memberName, REVIEW.review_date AS reviewDate, REVIEW.review_cont AS reviewCont, REVIEW.review_score AS reviewScore, IMAGE.refer_code AS referCode, LISTAGG(image_name, ',') WITHIN GROUP (ORDER BY image_name) AS imageName	 " +	
"	FROM REVIEW, IMAGE, MEMBER	 " +	
"	WHERE REVIEW.review_code = IMAGE.refer_code(+) AND review.member_code = member.member_code  AND food_code = :foodCode	 " +	
"	GROUP BY REVIEW.review_code,  REVIEW.food_code, review.member_code, MEMBER.member_name, REVIEW.review_date, REVIEW.review_cont, REVIEW.review_score, IMAGE.refer_code	 " +	
"	ORDER BY review_code DESC	 ",	
resultSetMapping = "foodreviewdto")

@NamedNativeQuery(		
name = "find_foodreviewdto2",		
query =		
"	SELECT REVIEW.review_code AS reviewCode, REVIEW.food_code AS foodCode, REVIEW.member_code AS memberCode, MEMBER.member_name AS memberName, REVIEW.review_date AS reviewDate, REVIEW.review_cont AS reviewCont, REVIEW.review_score AS reviewScore, IMAGE.refer_code AS referCode, LISTAGG(image_name, ',') WITHIN GROUP (ORDER BY image_name) AS imageName	 " +
"	FROM REVIEW, IMAGE, MEMBER	 " +
"	WHERE review_code = refer_code(+) AND review.member_code = member.member_code  AND food_code = :foodCode AND REVIEW.review_score = :selScore	 " +
"	GROUP BY REVIEW.review_code,  REVIEW.food_code, review.member_code, MEMBER.member_name, REVIEW.review_date, REVIEW.review_cont, REVIEW.review_score, IMAGE.refer_code	 " +
"	ORDER BY review_code DESC	",
resultSetMapping = "foodreviewdto")		

@SqlResultSetMapping(			
name = "foodreviewdto",			
classes = @ConstructorResult(			
targetClass = FoodReviewDto.class,			
columns = {			
@ColumnResult(name = "reviewCode", type =  String.class ),			
@ColumnResult(name = "foodCode", type = String.class),			
@ColumnResult(name = "memberCode", type =  String.class),			
@ColumnResult(name = "memberName", type = String.class),			
@ColumnResult(name = "reviewDate", type =  Date.class),			
@ColumnResult(name = "reviewCont", type =  String.class),			
@ColumnResult(name = "reviewScore", type =  int.class),			
@ColumnResult(name = "referCode", type =  String.class),			
@ColumnResult(name = "imageName", type =  String.class)			
}			
)			
)	








@NamedNativeQuery(
name = "find_manysearch",
query =
"SELECT " +
"rnum AS rnum, " +
"food_code AS foodCode, " +
"food_name AS foodName, " +
"food_addr AS foodAddr, " +
"food_area AS foodArea, " +
"food_phone AS foodPhone, " +
"food_kind AS foodKind, " +
"food_read AS foodRead, " +
"food_menu AS foodMenu, " +
"image_name AS imageName, " +
"image_path AS imagePath, " + 
"review_score AS reviewScore, " +
"review_count AS reviewCount " +
"FROM " +
"(SELECT ROWNUM rnum, A.* FROM  " +
"(SELECT food_code, food_name, food_addr, food_area, food_phone, food_kind, food_read, food_menu, image_name, image_path, nvl(review_score,0) review_score, nvl(review_count,0) review_count FROM  " +
"(SELECT * FROM food WHERE food_code in(  " +
"(SELECT distinct food_code from (  " +
"SELECT food_code from food where food_name like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_menu like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_kind like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_addr like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_tag like ('%'||:keyword||'%')  " +
")))  " +
"order by case  " +
"when food_name like ('%'||:keyword||'%') then 0  " +
"when food_menu like ('%'||:keyword||'%') then 1  " +
"when food_kind like ('%'||:keyword||'%') then 2  " +
"when food_tag like ('%'||:keyword||'%') then 3  " +
"else 4 end asc) fo, image, (SELECT food_code review_food, NVL(AVG(review_score),0) review_score, nvl(COUNT(review_code),0) review_count FROM review GROUP BY food_code) rv  " +
"WHERE fo.food_code = image.refer_code(+)  " +
"AND fo.food_code = rv.review_food(+)  " +
"ORDER BY food_read DESC " +
")A) " +

"WHERE rnum >= :startRow and rnum <= :endRow ",
resultSetMapping = "searchfooddto2"
)

@NamedNativeQuery(
name = "find_kindmanysearch",
query =
"SELECT " +
"rnum AS rnum, " +
"food_code AS foodCode, " +
"food_name AS foodName, " +
"food_addr AS foodAddr, " +
"food_area AS foodArea, " +
"food_phone AS foodPhone, " +
"food_kind AS foodKind, " +
"food_read AS foodRead, " +
"food_menu AS foodMenu, " +
"image_name AS imageName, " +
"image_path AS imagePath, " + 
"review_score AS reviewScore, " +
"review_count AS reviewCount " +
"FROM " +
"(SELECT ROWNUM rnum, A.* FROM  " +
"(SELECT food_code, food_name, food_addr, food_area, food_phone, food_kind, food_read, food_menu, image_name, image_path, nvl(review_score,0) review_score, nvl(review_count,0) review_count FROM  " +
"(SELECT * FROM food WHERE food_code in(  " +
"(SELECT distinct food_code from (  " +
"SELECT food_code from food where food_name like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_menu like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_kind like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_addr like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_tag like ('%'||:keyword||'%')  " +
")))  " +
"order by case  " +
"when food_name like ('%'||:keyword||'%') then 0  " +
"when food_menu like ('%'||:keyword||'%') then 1  " +
"when food_kind like ('%'||:keyword||'%') then 2  " +
"when food_tag like ('%'||:keyword||'%') then 3  " +
"else 4 end asc) fo, image, (SELECT food_code review_food, NVL(AVG(review_score),0) review_score, nvl(COUNT(review_code),0) review_count FROM review GROUP BY food_code) rv  " +
"WHERE fo.food_code = image.refer_code(+)  " +
"AND fo.food_code = rv.review_food(+)  " +
"and (food_kind LIKE (:kindType||'%')) " +
"ORDER BY food_read DESC " +
")A) " +

"WHERE rnum >= :startRow and rnum <= :endRow ",
resultSetMapping = "searchfooddto2"
)


@NamedNativeQuery(
name = "find_kindgoodsearch",
query =
"SELECT " +
"rnum AS rnum, " +
"food_code AS foodCode, " +
"food_name AS foodName, " +
"food_addr AS foodAddr, " +
"food_area AS foodArea, " +
"food_phone AS foodPhone, " +
"food_kind AS foodKind, " +
"food_read AS foodRead, " +
"food_menu AS foodMenu, " +
"image_name AS imageName, " +
"image_path AS imagePath, " + 
"review_score AS reviewScore, " +
"review_count AS reviewCount " +
"FROM " +
"(SELECT ROWNUM rnum, A.* FROM  " +
"(SELECT food_code, food_name, food_addr, food_area, food_phone, food_kind, food_read, food_menu, image_name, image_path, nvl(review_score,0) review_score, nvl(review_count,0) review_count FROM  " +
"(SELECT * FROM food WHERE food_code in(  " +
"(SELECT distinct food_code from (  " +
"SELECT food_code from food where food_name like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_menu like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_kind like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_addr like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_tag like ('%'||:keyword||'%')  " +
")))  " +
"order by case  " +
"when food_name like ('%'||:keyword||'%') then 0  " +
"when food_menu like ('%'||:keyword||'%') then 1  " +
"when food_kind like ('%'||:keyword||'%') then 2  " +
"when food_tag like ('%'||:keyword||'%') then 3  " +
"else 4 end asc) fo, image, (SELECT food_code review_food, NVL(AVG(review_score),0) review_score, nvl(COUNT(review_code),0) review_count FROM review GROUP BY food_code) rv  " +
"WHERE fo.food_code = image.refer_code(+)  " +
"AND fo.food_code = rv.review_food(+)  " +
"and (food_kind LIKE (:kindType||'%')) " +
"ORDER BY review_score DESC	" +
")A) " +
"WHERE rnum >= :startRow and rnum <= :endRow " ,
resultSetMapping = "searchfooddto2"
)
@NamedNativeQuery(
name = "find_goodsearch",
query =
"SELECT " +
"rnum AS rnum, " +
"food_code AS foodCode, " +
"food_name AS foodName, " +
"food_addr AS foodAddr, " +
"food_area AS foodArea, " +
"food_phone AS foodPhone, " +
"food_kind AS foodKind, " +
"food_read AS foodRead, " +
"food_menu AS foodMenu, " +
"image_name AS imageName, " +
"image_path AS imagePath, " + 
"review_score AS reviewScore, " +
"review_count AS reviewCount " +
"FROM " +
"(SELECT ROWNUM rnum, A.* FROM  " +
"(SELECT food_code, food_name, food_addr, food_area, food_phone, food_kind, food_read, food_menu, image_name, image_path, nvl(review_score,0) review_score, nvl(review_count,0) review_count FROM  " +
"(SELECT * FROM food WHERE food_code in(  " +
"(SELECT distinct food_code from (  " +
"SELECT food_code from food where food_name like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_menu like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_kind like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_addr like ('%'||:keyword||'%')  " +
"union all  " +
"SELECT food_code from food where food_tag like ('%'||:keyword||'%')  " +
")))  " +
"order by case  " +
"when food_name like ('%'||:keyword||'%') then 0  " +
"when food_menu like ('%'||:keyword||'%') then 1  " +
"when food_kind like ('%'||:keyword||'%') then 2  " +
"when food_tag like ('%'||:keyword||'%') then 3  " +
"else 4 end asc) fo, image, (SELECT food_code review_food, NVL(AVG(review_score),0) review_score, nvl(COUNT(review_code),0) review_count FROM review GROUP BY food_code) rv  " +
"WHERE fo.food_code = image.refer_code(+)  " +
"AND fo.food_code = rv.review_food(+)  " +
"ORDER BY review_score DESC	" +
")A) " +
"WHERE rnum >= :startRow and rnum <= :endRow " ,
resultSetMapping = "searchfooddto2"
)
@SqlResultSetMapping(
name = "searchfooddto2",
classes = @ConstructorResult(
targetClass = SearchFoodDto2.class,
columns = {
@ColumnResult(name = "rnum", type =  int.class ),
@ColumnResult(name = "foodCode", type = String.class),
@ColumnResult(name = "foodName", type =  String.class),
@ColumnResult(name = "foodAddr", type = String.class),
@ColumnResult(name = "foodArea", type =  String.class),
@ColumnResult(name = "foodPhone", type =  String.class),
@ColumnResult(name = "foodKind", type =  String.class),
@ColumnResult(name = "foodRead", type =  int.class),
@ColumnResult(name = "foodMenu", type =  String.class),
@ColumnResult(name = "imageName", type =  String.class),
@ColumnResult(name = "imagePath", type =  String.class),
@ColumnResult(name = "reviewScore", type =  float.class),
@ColumnResult(name = "reviewCount", type =  String.class)
}
)
)

@NamedNativeQuery(				
name = "countByKeywordLike",				
query =				
"	SELECT count(*) AS count FROM coupon, food			 " +
"	WHERE coupon.food_code = food.food_code			 " +
"	AND (			 " +
"	coupon_name like ('%'||:keyword||'%')			 " +
"	or			 " +
"	food_name like ('%'||:keyword||'%')			 " +
"	or			 " +
"	food_kind like ('%'||:keyword||'%')			 " +
"	or			 " +
"	food_menu like ('%'||:keyword||'%')			 " +
"	or			 " +
"	food_tag like ('%'||:keyword||'%'))			 ",
	resultSetMapping = "countdto"			
)				
@SqlResultSetMapping(				
name = "countdto",				
classes = @ConstructorResult(				
targetClass = CountDto.class,				
columns = {				
@ColumnResult(name = "count", type =  int.class)				
}				
)				
)				

@NamedNativeQuery(				
		name = "findByKeywordLike",			
		query =			
				"	select  "	+ 			
				"  	coupon_code as  couponCode, 	" +
				"	coupon_costori as couponCostori, 	" +
				"	coupon_costsale as  couponCostsale, 	" +
				"	coupon_enddate as  couponEnddate, 	" +
				"	coupon_intro as  couponIntro, 	" +
				"	coupon_name as  couponName, 	" +
				"	coupon_salerate as  couponSalerate, 	" +
				"	coupon_startdate as  couponStartdate, 	" +
				"	coupon_status as  couponStatus, " +
				"	food.food_code as foodCode,	" +
				"	food_addr as foodAddr, 	" +
				"	food_area as foodArea, 	" +
				"	food_date as  foodDate, 	" +
				"	food_intro as foodIntro, 	" +
				"	food_kind as foodKind, 	" +
				"	food_menu as foodMenu, 	" +
				"	food_name as foodName, 	" +
				"	food_phone as foodPhone, 	" +
				"	food_read as foodRead, 	" +
				"	food_time as foodTime, 	" +
				"	member_code as memberCode, 	" +
				"	image_code as imageCode, 	 	" +
				"	image_name as imageName, 	" +
				"	image_path as imagePath,  	" +
				"	image_size as imageSize, 	" +
				"	refer_code as referCode	 	" +
				"	from coupon, food, image	"+ 
				"	WHERE coupon.food_code = food.food_code	" + 
				"	AND coupon.coupon_code = image.refer_code(+)	"+ 
				"	and ( coupon_name like ('%'||:keyword||'%')	"	+ 
				"	or food_name like ('%'||:keyword||'%')	"	+ 
				"	or food_kind like ('%'||:keyword||'%')	"	+ 
				"	or food_menu like ('%'||:keyword||'%')	"	+ 
				"	) order by coupon_salerate desc",				
	resultSetMapping = "searchcouponlistdto"				
	)	

@SqlResultSetMapping(				
name = "searchcouponlistdto",				
classes = @ConstructorResult(				
targetClass = SearchCouponListDto.class,				
columns = {				
@ColumnResult(name = "couponCode", type = String .class),				
@ColumnResult(name = "couponCostori", type = int.class),				
@ColumnResult(name = "couponCostsale", type = int.class),				
@ColumnResult(name = "couponEnddate", type = String.class),				
@ColumnResult(name = "couponIntro", type = String.class),				
@ColumnResult(name = "couponName", type = String.class),				
@ColumnResult(name = "couponSalerate", type = int.class),				
@ColumnResult(name = "couponStartdate", type = String.class),				
@ColumnResult(name = "couponStatus", type = String.class),				
@ColumnResult(name = "foodCode", type = String.class),	
@ColumnResult(name = "foodAddr", type = String.class),
@ColumnResult(name = "foodArea", type = String.class),
@ColumnResult(name = "foodDate", type = Date.class),
@ColumnResult(name = "foodIntro", type = String.class),
@ColumnResult(name = "foodKind", type = String.class),
@ColumnResult(name = "foodMenu", type = String.class),
@ColumnResult(name = "foodName", type = String.class),
@ColumnResult(name = "foodPhone", type = String.class),				
@ColumnResult(name = "foodRead", type = int .class),								
@ColumnResult(name = "foodTime", type = String.class),				
@ColumnResult(name = "memberCode", type = String.class),				
@ColumnResult(name = "imageCode", type = String.class),				
@ColumnResult(name = "imageName", type = String.class),				
@ColumnResult(name = "imagePath", type = String.class),				
@ColumnResult(name = "imageSize", type = long.class),				
@ColumnResult(name = "referCode", type = String.class)				
}				
)				
)

@NamedNativeQuery(
name = "find_foodstardto",
query =
"SELECT AVG(review_score) as reviewScore FROM review WHERE FOOD_CODE = :foodCode GROUP BY FOOD_CODE",
resultSetMapping = "foodstardto"
)
@SqlResultSetMapping(
name = "foodstardto",
classes = @ConstructorResult(
targetClass = FoodStarDto.class,
columns = {
@ColumnResult(name = "reviewScore", type =  float.class)
}
)
)

@NamedNativeQuery(		
name = "find_reviewcountdto",		
query =		
"	select a.good, b.soso, c.bad, d.whole	 " +
"	FROM (select count(*) as good from review where  food_code=:foodCode AND review_score = 5) a,	 " +
"	(select count(*) as soso from review where food_code=:foodCode AND review_score = 3) b,	 " +
"	(select count(*) as bad from review where food_code=:foodCode AND review_score = 1) c,	 " +
"	(select count(*) as whole from review where food_code=:foodCode) d	 ",
resultSetMapping = "reviewcountdto")		
@SqlResultSetMapping(		
name = "reviewcountdto",		
classes = @ConstructorResult(		
targetClass = ReviewCountDto.class,		
columns = {		
@ColumnResult(name = "good", type =  int.class ),		
@ColumnResult(name = "soso", type = int.class),		
@ColumnResult(name = "bad", type =  int.class),		
@ColumnResult(name = "whole", type = int.class),		
}		
)		
)		






@Table(name = "food")
@ToString
@Getter
@Setter
public class Food {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_Code", nullable = false)
	private String foodCode;	// 음식점코드
	
	/*
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "member_id") private Member member;
	 */
	
	@Column(name = "food_name", nullable = false)
	private String foodName;	// 음식점 명
	
	@Column(name = "food_addr", nullable = false)
	private String foodAddr;	// 음식점 주소
	
	private String foodArea;	// 음식점 지역
	
	@Column(name = "food_phone")
	private String foodPhone;	// 음식점 전화번호
	
	@Column(name = "food_kind", nullable = false)
	private String foodKind;	// 음식 분류  ex) 한식, 중식, 
	
	@Column(name = "food_menu")
	private String foodMenu;	// 음식 대표메뉴
	
	@Column(name = "food_time")
	private String foodTime;	// 음식점 영업시간
	
	@Column(name = "food_break")
	private String foodBreak;	// 음식점 휴일
	
	
	@Column(name = "food_intro")
	private String foodIntro;	// 음식점 소개
	
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name = "food_date", nullable = false)
	private Date foodDate;	// 음식점 등록일
	
	@Column(name = "food_read", nullable = false)
	private int foodRead;	// 음식점 조회 카운트
	
	//@Enumerated(EnumType.STRING)
	@Column(name = "food_status")
	private String foodStatus;	//	음식점 상태  ex) y : 완료, n : 검토중	
	// private FoodStatus foodStatus;
	
	private String foodTag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_code")
	private Member member;	// 등록자
	
	@OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Coupon> coupons = new ArrayList<>();	
	
	@OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Review> reviews = new ArrayList<>();	
	
	@OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Favorite> favorites = new ArrayList<>();	
	

	
    public void updateFood(FoodDto foodDto){	
    	this.foodName = foodDto.getFoodName();	
    	this.foodAddr = foodDto.getFoodAddr();	
    	this.foodArea = foodDto.getFoodArea();	
    	this.foodPhone = foodDto.getFoodPhone();	
    	this.foodKind = foodDto.getFoodKind();	 
    	this.foodMenu = foodDto.getFoodMenu();	
    	this.foodTime = foodDto.getFoodTime();	
    	this.foodBreak = foodDto.getFoodBreak();	
    	this.foodIntro = foodDto.getFoodIntro();	
    	this.foodDate = foodDto.getFoodDate();	
    	this.foodRead = foodDto.getFoodRead();	
    	this.foodStatus = foodDto.getFoodStatus();			
    }
    	
    	
    	

    	
    	
}	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	

