package com.java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.java.dto.ReviewDto;
import com.java.entity.Purchase;
import com.java.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	
	@Query(value = "select count(*) FROM review WHERE member_code = :memberCode",
			 nativeQuery = true)
	int countreview(String memberCode);
	
	@Query(value= "select * from review where review_code = :reviewCode",
			nativeQuery = true)
	Review reviewUpdate(String reviewCode);
	
	@Query(value = "select ROUND(nvl(AVG(review_score),0),2) from review where food_code = :foodCode",
			nativeQuery = true)
	float foodReviewAvg(String foodCode);
	
	@Query(value = "select food_name from food where food_code=:foodCode", nativeQuery = true)
	String getFoodName(String foodCode);
	
	@Query(value = "SELECT 'REVIEW'||LPAD(SEQREVIEW.nextval,4,0) As reviewCode from dual",
			 nativeQuery = true)
	String getcode();
	
	@Query(name = "find_allreviewdto", nativeQuery = true)
	List<ReviewDto> allreview();
	
	Optional<Review> findByReviewCode(String reviewCode);
	
	int deleteByReviewCode(String reviewCode);
	

}

