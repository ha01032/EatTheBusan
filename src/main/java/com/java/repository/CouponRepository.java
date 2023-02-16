package com.java.repository;

import com.java.dto.CouponAdminDto;
import com.java.dto.CouponDto;
import com.java.dto.CouponPurDto;
import com.java.dto.CouponReadDto;
import com.java.dto.PurCouponDto;
import com.java.dto.SearchFoodCodeDto;
import com.java.entity.Coupon;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

//Query에 name속성에 해당하는 쿼리/Dto매핑을 해당 Entity에서 @SqlResultSetMapping로 수행

public interface CouponRepository extends JpaRepository<Coupon, Long>,
        QuerydslPredicateExecutor<Coupon> {
	
	
	Long countBy();
	
	@Query(name = "find_coupondto", nativeQuery = true)
	List<CouponDto> couponList(@Param("startRow") int startRow, @Param("endRow") int endRow, @Param("today") Date today);
	
	@Query(name = "find_couponreaddto", nativeQuery = true)
	CouponReadDto couponRead(@Param("couponCode") String couponCode);
	
	@Query(name = "find_couponadmindto", nativeQuery = true)
	List<CouponAdminDto> couponListAdmin();	
	
	@Query(name = "find_searchfoodcodedto", nativeQuery = true)
	List<SearchFoodCodeDto> searchFoodCode(String foodName);	
	
	@Query(value = "SELECT 'coupon'||LPAD(seqcoupon.nextval,4,0) As couponCode from dual",
			 nativeQuery = true)
	String getcode();	
	
	@Modifying
	@Transactional
	@Query(value =
			"INSERT INTO coupon(coupon_code, food_code, coupon_name, coupon_startdate, coupon_enddate, coupon_costori, coupon_salerate, coupon_costsale, coupon_intro, coupon_status) " +
			"VALUES('coupon'||LPAD(seqcoupon.CURRVAL,4,0), :foodCode, :couponName, TO_DATE(:couponStartdate, 'YYYY-MM-DD'), TO_DATE(:couponEnddate,'YYYY-MM-DD'), :couponCostori, :couponSalerate, :couponCostsale, :couponIntro, 'Y')"
			, nativeQuery = true)
	public int couponInsert
	(@Param("foodCode") String foodCode, @Param("couponName") String couponName, @Param("couponStartdate") String couponStartdate, @Param("couponEnddate") String couponEnddate, @Param("couponCostori") int couponCostori, @Param("couponSalerate") int couponSalerate, @Param("couponCostsale") int couponCostsale, @Param("couponIntro") String couponIntro);
	
	Coupon findByCouponName(String couponName);
		
	Optional<Coupon> findByCouponCode(String couponCode);
	
	String findCouponNameByCouponCode(String couponCode);
	
	public int deleteByCouponCode(String couponCode);
	
	@Query(name = "find_couponpurdto", nativeQuery = true)
	CouponPurDto purchaseSelect(String couponCode);

}
