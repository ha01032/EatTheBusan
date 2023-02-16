package com.java.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.dto.ReviewCountDto;
import com.java.entity.Favorite;
import com.java.entity.Purchase;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	
	
	@Query(value = "select count(*) FROM favorite WHERE member_code = :memberCode",
			 nativeQuery = true)
	int countfavorite(String memberCode);
	
	@Query(value = "select * FROM favorite WHERE member_code = :memberCode",
			 nativeQuery = true)
	List<Favorite> findfavorite(String memberCode);
	
	@Query(value = "select count(food_code) from favorite where food_code=:foodCode", nativeQuery = true)
	int foodFavorite(String foodCode);
	
	@Query(value = "select count(*) FROM favorite WHERE member_code = :memberCode and food_code = :foodCode",
			 nativeQuery = true)
	int countfavorite(@Param("memberCode")String memberCode, @Param("foodCode") String foodCode);
	
	@Transactional
    @Modifying
	@Query(value = "insert into favorite values (seqfavorite.nextval, sysdate, :foodCode, :memberCode)", nativeQuery = true)
	int makefavorite(@Param("foodCode") String foodCode, @Param("memberCode") String memberCode);
	

	@Query(value = "SELECT favorite_id FROM favorite WHERE food_code = :foodCode AND member_code = :memberCode", nativeQuery = true)
	Long findkey(String foodCode, String memberCode);

}

