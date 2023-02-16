package com.java.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.java.dto.AdminCouponDto;
import com.java.dto.AdminCouponReadDto;
import com.java.dto.AdminFoodDto;
import com.java.dto.AdminFoodReadDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity

//카테고리별 count 조회쿼리 Dto매핑
@NamedNativeQuery(				
name = "find_adminfooddto",				
query =				
"select * from" +				
"(select count(food_code) as totalFoodCnt from food)," +				
"(select count(food_kind) as korFood from food where food_kind like '한식%')," +				
"(select count(food_kind) as chnFood from food where food_kind like '중식%')," +				
"(select count(food_kind) as jpFood from food where food_kind like '일식%')," +				
"(select count(food_kind) as wtFood from food where food_kind like '양식%')," +				
"(select count(food_kind) as cafe from food where food_kind like '카페%')," +				
"(select count(food_kind) as etc from food where food_kind like '기타%')",				
resultSetMapping = "adminfooddto"				
)								
@SqlResultSetMapping(				
name = "adminfooddto",				
classes = @ConstructorResult(				
targetClass = AdminFoodDto.class,				
columns = {				
@ColumnResult(name = "totalFoodCnt ", type =  int.class),				
@ColumnResult(name = "korFood ", type = int.class),				
@ColumnResult(name = "chnFood ", type =  int.class),				
@ColumnResult(name = "jpFood ", type = int.class),				
@ColumnResult(name = "wtFood ", type =  int.class),				
@ColumnResult(name = "cafe ", type =  int.class),				
@ColumnResult(name = "etc ", type =  int.class)				
}				
)				
)

//음식점 top10 조회쿼리 Dto 매핑
@NamedNativeQuery(
name = "find_adminfoodreaddto",
query = "select * FROM ( select food_code AS foodCode, food_name AS foodName, food_read AS foodRead, ROW_NUMBER() OVER (ORDER BY food_read desc) rank FROM food) WHERE rownum <=10",
resultSetMapping = "adminfoodreaddto"
)
@SqlResultSetMapping(
name = "adminfoodreaddto",
classes = @ConstructorResult(
targetClass = AdminFoodReadDto.class,
columns = {
@ColumnResult(name = "foodCode", type =  String.class),
@ColumnResult(name = "foodName", type = String.class),
@ColumnResult(name = "foodRead", type =  int.class),
@ColumnResult(name = "rank", type = int.class)
}
)
)

//구매 top10 조회쿼리 Dto 매핑
@NamedNativeQuery(
name = "find_admincouponreaddto",
query = "SELECT couponCode, coupon_name AS couponName, food_code AS foodCode, purchaseCnt, rank FROM (SELECT ROWNUM AS rank, B.* FROM (SELECT * FROM (select count(purchase.coupon_code) AS purchaseCnt, purchase.coupon_code AS couponCode from purchase group by purchase.coupon_code) A JOIN coupon ON A.couponCode = coupon.coupon_code order by purchaseCnt DESC)B) where rank <= 10",
resultSetMapping = "admincouponreaddto"
)
@SqlResultSetMapping(
name = "admincouponreaddto",
classes = @ConstructorResult(
targetClass = AdminCouponReadDto.class,
columns = {
@ColumnResult(name = "couponCode", type =  String.class),
@ColumnResult(name = "couponName", type = String.class),
@ColumnResult(name = "foodCode", type =  String.class),
@ColumnResult(name = "purchaseCnt", type = int.class),
@ColumnResult(name = "rank", type = int.class)
}
)
)

//coupon 통계 조회쿼리 dto 매핑
@NamedNativeQuery(
name = "find_admincoupondto",
query =
"SELECT * FROM"+
"(select count(coupon_code) as totalCouponCnt from purchase WHERE purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS korCoupon from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '한식%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS chnCoupon from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '중식%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS jpCoupon from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '일식%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS wtCoupon from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '양식%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS cafe from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '카페%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate)), "+
"(select count(purchase.coupon_code) AS etc from purchase INNER JOIN coupon ON purchase.coupon_code = coupon.coupon_code "+
"INNER JOIN food ON coupon.food_code = food.food_code WHERE food_kind like '기타%' AND purchase_status = 'Y' AND purchase_date BETWEEN trunc(sysdate,'mm') AND last_day(sysdate))",
resultSetMapping = "admincoupondto"
)
@SqlResultSetMapping(
name = "admincoupondto",
classes = @ConstructorResult(
targetClass = AdminCouponDto.class,
columns = {
@ColumnResult(name = "totalCouponCnt", type =  int.class),
@ColumnResult(name = "korCoupon", type = int.class),
@ColumnResult(name = "chnCoupon", type =  int.class),
@ColumnResult(name = "jpCoupon", type = int.class),
@ColumnResult(name = "wtCoupon", type =  int.class),
@ColumnResult(name = "cafe", type =  int.class),
@ColumnResult(name = "etc", type =  int.class)
}
)
)














@Table(name="admin")
@Getter @Setter
@ToString
public class Admin {

    @Id
    @Column(name="admin_id")
	private String adminId;
    @Column(name="admin_pwd")
	private String adminPwd;
    

}
