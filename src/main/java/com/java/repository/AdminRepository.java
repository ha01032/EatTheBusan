package com.java.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.java.dto.AdminCouponDto;
import com.java.dto.AdminCouponReadDto;
import com.java.dto.AdminFoodDto;
import com.java.dto.AdminFoodReadDto;
import com.java.entity.Admin;

//Query에 name속성에 해당하는 쿼리/Dto매핑을 해당 Entity에서 @SqlResultSetMapping로 수행

public interface AdminRepository extends JpaRepository<Admin, Long>,
        QuerydslPredicateExecutor<Admin> {	
	@Query(name = "find_adminfooddto", nativeQuery = true)			
	AdminFoodDto foodCnt();
	
	@Query(name = "find_adminfoodreaddto", nativeQuery = true)
	List<AdminFoodReadDto> foodReadRank();
	
	@Query(name = "find_admincouponreaddto", nativeQuery = true)
	List<AdminCouponReadDto> couponReadRank();
	
	@Query(name = "find_admincoupondto", nativeQuery = true)
	AdminCouponDto couponCount();
	
	int countByAdminIdAndAdminPwd(String adminId, String adminPwd);
	
	Admin findByAdminId(String adminId);
	

}