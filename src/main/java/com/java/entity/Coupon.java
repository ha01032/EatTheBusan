package com.java.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ConstructorResult;
import javax.persistence.ColumnResult;

import org.hibernate.annotations.CreationTimestamp;

import com.java.dto.CouponAdminDto;
import com.java.dto.CouponDto;
import com.java.dto.CouponPurDto;
import com.java.dto.CouponReadDto;
import com.java.dto.SearchFoodCodeDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity

//Eat딜 리스트 조회쿼리 Dto매핑
@NamedNativeQuery(			
name = "find_coupondto",			
query =			
"SELECT B.coupon_code AS couponCode, B.food_code AS foodCode, B.coupon_name AS couponName, B.coupon_costori AS couponCostori, B.coupon_salerate AS couponSalerate, B.coupon_costsale AS couponCostsale, B.coupon_intro AS couponIntro, TO_CHAR(B.coupon_startdate, 'YYYY-MM-DD') AS couponStartdate, TO_CHAR(B.coupon_enddate, 'YYYY-MM-DD') AS couponEnddate, image_name AS imageName, image_path AS imagePath FROM (SELECT ROWNUM as num, A.* FROM (select * from coupon, image WHERE coupon_code = refer_code AND coupon_enddate >= :today AND coupon_status = 'Y' ORDER BY coupon_code DESC) A) B WHERE B.num >= :startRow AND B.num <= :endRow",			
resultSetMapping = "coupondto"			
)			
@SqlResultSetMapping(			
name = "coupondto",			
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

//쿠폰 상세페이지 조회쿼리 Dto매핑
@NamedNativeQuery(
name = "find_couponreaddto",
query =
"SELECT coupon.coupon_code AS couponCode, coupon.food_code AS foodCode, coupon.coupon_status AS couponStatus, coupon.coupon_name AS couponName, coupon.coupon_costori AS couponCostori, coupon.coupon_salerate AS couponSalerate, coupon.coupon_costsale AS couponCostsale, coupon.coupon_intro AS couponIntro, TO_CHAR(coupon.coupon_startdate, 'YYYY-MM-DD') AS couponStartdate, TO_CHAR(coupon.coupon_enddate, 'YYYY-MM-DD') AS couponEnddate, (select image_code from image where refer_code = coupon.coupon_code) AS imageCode, (select image_name from image where refer_code = coupon.coupon_code) AS imageName, (select image_path from image where refer_code = coupon.coupon_code) AS imagePath, (select food_name from food Where food_code = coupon.food_code) AS foodName from coupon where coupon_code = :couponCode",
resultSetMapping = "couponreaddto"
)
@SqlResultSetMapping(
name = "couponreaddto",
classes = @ConstructorResult(
targetClass = CouponReadDto.class,
columns = {
@ColumnResult(name = "couponCode", type =  String.class),
@ColumnResult(name = "foodCode", type = String.class),
@ColumnResult(name = "couponStatus", type =  String.class),
@ColumnResult(name = "couponName", type = String.class),
@ColumnResult(name = "couponCostori", type =  int.class),
@ColumnResult(name = "couponSalerate", type =  int.class),
@ColumnResult(name = "couponCostsale", type =  int.class),
@ColumnResult(name = "couponIntro", type =  String.class),
@ColumnResult(name = "couponStartdate", type =  String.class),
@ColumnResult(name = "couponEnddate", type =  String.class),
@ColumnResult(name = "imageCode", type =  String.class),
@ColumnResult(name = "imageName", type =  String.class),
@ColumnResult(name = "imagePath", type =  String.class),
@ColumnResult(name = "foodName ", type =  String.class)
}
)
)

//쿠폰 관리자 조회쿼리 Dto매핑
@NamedNativeQuery(
name = "find_couponadmindto",
query =
"SELECT "+
"B.coupon_code AS couponCode," +
"B.food_code AS foodCode," +
"B.coupon_name AS couponName," +
"B.coupon_intro AS couponIntro," +
"B.coupon_costori AS couponCostori," +
"B.coupon_costsale AS couponCostsale," +
"B.coupon_salerate AS couponSalerate," +
"B.coupon_status AS couponStatus," +
"B.image_code AS imageCode," +
"B.image_name AS imageName," +
"B.image_size AS imageSize," +
"B.image_path AS imagePath," +
"TO_CHAR(B.coupon_startdate, 'YYYY-MM-DD') AS couponStartdate, TO_CHAR(B.coupon_enddate, 'YYYY-MM-DD') AS couponEnddate " +
"FROM (select * from coupon, image WHERE coupon_code = refer_code) B",
resultSetMapping = "couponadmindto"
)
@SqlResultSetMapping(
name = "couponadmindto",
classes = @ConstructorResult(
targetClass = CouponAdminDto.class,
columns = {
@ColumnResult(name = "couponCode", type =  String.class),
@ColumnResult(name = "foodCode", type = String.class),
@ColumnResult(name = "couponName", type = String.class),
@ColumnResult(name = "couponIntro", type =  String.class),
@ColumnResult(name = "couponCostori", type =  int.class),
@ColumnResult(name = "couponCostsale", type =  int.class),
@ColumnResult(name = "couponSalerate", type =  int.class),
@ColumnResult(name = "couponStatus", type =  String.class),
@ColumnResult(name = "imageCode", type =  String.class),
@ColumnResult(name = "imageName", type =  String.class),
@ColumnResult(name = "imageSize", type =  long.class),
@ColumnResult(name = "imagePath", type =  String.class),
@ColumnResult(name = "couponStartdate", type =  String.class),
@ColumnResult(name = "couponEnddate", type =  String.class)
}
)
)

//쿠폰 관리자 조회쿼리 Dto매핑
@NamedNativeQuery(
name = "find_searchfoodcodedto",
query =
"SELECT food_code AS foodCode, food_name AS foodName, food_addr AS foodAddr FROM food WHERE food_name LIKE '%'||:foodName||'%'",
resultSetMapping = "searchfoodcodedto"
)
@SqlResultSetMapping(
name = "searchfoodcodedto",
classes = @ConstructorResult(
targetClass = SearchFoodCodeDto.class,
columns = {
@ColumnResult(name = "foodCode", type =  String.class),
@ColumnResult(name = "foodName", type = String.class),
@ColumnResult(name = "foodAddr", type = String.class)
}
)
)

// 쿠폰 구매창
@NamedNativeQuery(
name = "find_couponpurdto",
query =
"select coupon.coupon_code AS couponCode, coupon.food_code AS foodCode, coupon.coupon_status AS couponStatus, coupon.coupon_name AS couponName, coupon.coupon_costori AS couponCostori, coupon.coupon_salerate AS couponSalerate, coupon.coupon_costsale AS couponCostsale, coupon.coupon_intro AS couponIntro, " +
"TO_CHAR(coupon.coupon_startdate, 'YYYY-MM-DD') AS couponStartdate, TO_CHAR(coupon.coupon_enddate, 'YYYY-MM-DD') AS couponEnddate, " +
"(select image_code from image where refer_code = coupon.coupon_code) AS imageCode, " +
"(select image_name from image where refer_code = coupon.coupon_code) AS imageName, " +
"(select image_path from image where refer_code = coupon.coupon_code) AS imagePath, " +
"(select food_name from food Where food_code = coupon.food_code) AS foodName " +
"from coupon where coupon_code = :couponCode " ,
resultSetMapping = "couponpurdto"
)
@SqlResultSetMapping(
name = "couponpurdto",
classes = @ConstructorResult(
targetClass = CouponPurDto.class,
columns = {
@ColumnResult(name = "couponCode", type =  String.class),
@ColumnResult(name = "foodCode", type = String.class),
@ColumnResult(name = "couponStatus", type = String.class),
@ColumnResult(name = "couponName", type =  String.class),
@ColumnResult(name = "couponCostori", type =  int.class),
@ColumnResult(name = "couponSalerate", type =  int.class),
@ColumnResult(name = "couponCostsale", type =  int.class),
@ColumnResult(name = "couponIntro", type =  String.class),
@ColumnResult(name = "couponStartdate", type =  String.class),
@ColumnResult(name = "couponEnddate", type =  String.class),
@ColumnResult(name = "imageCode", type =  String.class),
@ColumnResult(name = "imageName", type =  String.class),
@ColumnResult(name = "imagePath", type =  String.class),
@ColumnResult(name = "foodName", type =  String.class)
}
)
)




@Table(name="coupon")
 
@Getter
@Setter
@ToString

public class Coupon {

	// 기본키 pk = not null + unique
    @Id 
    @Column(name="coupon_code")
    private String couponCode;       //상품 코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_code")
    private Food food;    
    
    @Column(name = "coupon_name", nullable = false, length = 500)
    private String couponName;
    
    @CreationTimestamp
	@Temporal(TemporalType.DATE)
    @Column(name = "coupon_startdate", nullable = false)
    private Date couponStartdate;
    
    @CreationTimestamp
	@Temporal(TemporalType.DATE)
    @Column(name = "coupon_enddate", nullable = false)
    private Date couponEnddate;

    @Column(name = "coupon_costori", nullable = false, length = 20)
    private int couponCostori;	
    
    @Column(name = "coupon_costsale", nullable = false, length = 3)
    private int couponCostsale;
    
   
    @Column(name = "coupon_salerate", nullable = false, length = 20)
	private int couponSalerate;
    
   
    @Column(name = "coupon_intro", length = 4000)
    private String couponIntro;
    
    @Column(name = "coupon_status", length = 20)
	private String couponStatus;
	 
	@OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Purchase> purchases = new ArrayList<>();	
    
	
    public void updateCoupon(CouponReadDto couponDto){
        
    	
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	
    	this.couponName = couponDto.getCouponName();
        try {
			this.couponStartdate = format.parse(couponDto.getCouponStartdate());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			this.couponEnddate = format.parse(couponDto.getCouponEnddate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.couponCostori = couponDto.getCouponCostori();
        this.couponCostsale = couponDto.getCouponCostsale();
        this.couponSalerate = couponDto.getCouponSalerate();
        this.couponIntro = couponDto.getCouponIntro();
        this.couponStatus = couponDto.getCouponStatus();
        
    }
	
}





