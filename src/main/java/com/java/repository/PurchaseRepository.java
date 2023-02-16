package com.java.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.dto.PurCouponDto;
import com.java.dto.PurchaseListDto2;
import com.java.dto.PurchaseListDto3;
import com.java.dto.PurchaseListDto4;
import com.java.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	
	
	@Query(value = "select count(*) FROM purchase WHERE member_code = :memberCode",
			 nativeQuery = true)
	int countpurchase(String memberCode);
	
	@Query(name= "find_purchaselistdto2",  nativeQuery = true)
	List<PurchaseListDto2> purchaseSelectAll(String memberCode);
	
	@Query(value = "SELECT 'pc'||LPAD(seqpurchase.nextval,4,0) As purchase_code from dual",
			 nativeQuery = true)
	String getcode();	
		
	
	@Modifying
	@Transactional
	@Query(value =
			"INSERT INTO purchase(purchase_code, coupon_code, member_code, purchase_phone, purchase_date, purchase_cost, purchase_status) " +
			"VALUES('pc'||LPAD(seqpurchase.CURRVAL,4,0),:couponCode, :memberCode, :purchasePhone, SYSDATE, :purchaseCost, 'Y')"
			, nativeQuery = true)
	public int purchaseInsertOk
	(@Param("couponCode") String couponCode, @Param("memberCode") String memberCode, @Param("purchasePhone") String purchasePhone, @Param("purchaseCost") int purchaseCost);
	
	
	@Query(value = "SELECT NVL(COUNT(*),0) FROM purchase WHERE  member_code = :memberCode"
			, nativeQuery = true)
	public int getCount(String memberCode);
	
	@Query(name = "find_purcoupondto", nativeQuery = true)
	PurCouponDto purchaseCouponSelect(String purchaseCode);
	
	@Query(name = "find_purchaselistdto3", nativeQuery = true)
	PurchaseListDto3 purchaseSelect(String purchaseCode);
	
	@Query(name= "find_purchaselistdto4",  nativeQuery = true)
	List<PurchaseListDto4> purchaseListAll();	
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE purchase SET purchase_status='N' WHERE purchase_code = :purchaseCode" 
		,nativeQuery = true)
	public int purchaseDelete(String purchaseCode);

	
}

