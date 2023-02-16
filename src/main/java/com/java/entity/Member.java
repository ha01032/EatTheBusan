package com.java.entity;

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
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.java.dto.FoodDto;
import com.java.dto.FoodDto2;
import com.java.dto.MemberFavoriteDto;
import com.java.dto.MemberReviewDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;






@NamedNativeQuery(						
name = "find_memberfavoritedto",						
query =						
"	select food.food_code AS foodCode, food.food_area AS foodArea, food.food_name AS foodName, food.food_read AS foodRead,	"+
"	(select image_name from image where refer_code = food.food_code) AS imageName,					"+
"	(select image_path from image where refer_code = food.food_code) AS imagePath,					"+
"	(select count(*) from review where food_code=food.food_code) as count,					"+
"	(select avg(review_score) from review where food_code=food.food_code) as avg					"+
"	from food, favorite					"+
"	where food.food_code = favorite.food_code and favorite.member_code=:memberCode					",
resultSetMapping = "memberfavoritedto"						
)						
@SqlResultSetMapping(						
name = "memberfavoritedto",						
classes = @ConstructorResult(						
targetClass = MemberFavoriteDto.class,						
columns = {						
@ColumnResult(name = "foodCode", type =  String.class),						
@ColumnResult(name = "foodArea", type = String.class),						
@ColumnResult(name = "foodName", type =  String.class),						
@ColumnResult(name = "foodRead", type = int.class),						
@ColumnResult(name = "imageName", type =  String.class),						
@ColumnResult(name = "imagePath", type =  String.class),						
@ColumnResult(name = "count", type =  int .class),						
@ColumnResult(name = "avg", type =  String .class)						
}						
)						
)						

@NamedNativeQuery(	
name = "find_memberreviewdto",
query =	"	SELECT review.review_code AS reviewCode, review.food_code AS foodCode, review.member_code AS memberCode, member.member_name AS memberName, review.review_date AS reviewDate, review.review_cont AS reviewCont, review.review_score AS reviewScore, image.refer_code AS referCode, LISTAGG(image_name, ',')WITHIN GROUP (ORDER BY image_name) AS imageName	"+
		"	FROM review, image, member	"+
		"	WHERE review_code = refer_code(+) AND review.member_code = member.member_code  AND member.member_code= :memberCode	"+
		"	GROUP BY review.review_code,  review.food_code, review.member_code, member.member_name, review.review_date, review.review_cont, review.review_score, image.refer_code	"+
		"	ORDER BY review_code DESC	",
resultSetMapping = "memberreviewdto"
)
@SqlResultSetMapping(
name = "memberreviewdto",
classes = @ConstructorResult(
targetClass = MemberReviewDto.class,
columns = {
@ColumnResult(name = "reviewCode", type =  String.class),
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
name = "find_fooddto",
query =	
"SELECT " +
"food_code AS foodCode,  " +
"food_name AS foodName,  " +
"food_addr AS foodAddr,  " +
"food_area AS foodArea,  " +
"food_phone AS foodPhone,  " +
"food_kind AS foodKind,  " +
"food_menu AS foodMenu,  " +
"food_time AS foodTime,  " +
"food_break AS foodBreak,  " +
"food_intro AS foodIntro,  " +
"food_date AS foodDate,  " +
"food_read AS foodRead,  " +
"food_status AS foodStatus,  " +
"member_code AS memberCode " +
"FROM food WHERE member_code = :memberCode ",
resultSetMapping = "fooddto"
)
@SqlResultSetMapping(
name = "fooddto",
classes = @ConstructorResult(
targetClass = FoodDto2.class,
columns = {
@ColumnResult(name = "foodCode", type =  String.class),
@ColumnResult(name = "foodName", type = String.class),
@ColumnResult(name = "foodAddr", type =  String.class),
@ColumnResult(name = "foodArea", type = String.class),
@ColumnResult(name = "foodPhone", type =  String.class),
@ColumnResult(name = "foodKind", type =  String.class),
@ColumnResult(name = "foodMenu", type =  String.class),
@ColumnResult(name = "foodTime", type =  String.class),
@ColumnResult(name = "foodBreak", type =  String.class),
@ColumnResult(name = "foodIntro", type =  String.class),
@ColumnResult(name = "foodDate", type =  Date.class),
@ColumnResult(name = "foodRead", type =  int.class),
@ColumnResult(name = "foodStatus", type =  String.class),
@ColumnResult(name = "memberCode", type =  String.class)
}
)
)


@Entity
@Table(name="member")
@Getter @Setter
@ToString
@DynamicInsert
public class Member {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="member_code")
	private String memberCode;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "member_date", nullable = false)
	private Date memberDate;
    
    @Column(unique = true)
	private String memberMail;
    
    	
	private String memberPwd;
    
    @Column(name="member_name")
	private String memberName;
    
	private String memberPhone;
    
    //@ColumnDefault("Y")
	private String memberStatus;
    
    private String memberKakao;
    
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Review> reviews = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Food> foods = new ArrayList<>();	
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Purchase> purchases = new ArrayList<>();	
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Favorite> favorties = new ArrayList<>();	
    


}
