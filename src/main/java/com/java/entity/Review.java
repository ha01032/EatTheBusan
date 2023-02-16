package com.java.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.java.dto.FoodDto;
import com.java.dto.ReviewDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "review")
@ToString
@Entity
@Getter
@Setter


@NamedNativeQuery(			
name = "find_allreviewdto",			
query =			
"	Select	 " +	
"	review_code AS reviewCode,	 " +	
"	food_code AS foodCode,	 " +	
"	member_code AS memberCode,	 " +	
"	review_date AS reviewDate,	 " +	
"	review_cont AS reviewCont,	 " +	
"	review_score AS reviewScore	 " +		
"	FROM review	",	
resultSetMapping = "reviewdto"			
)
@SqlResultSetMapping(
name = "reviewdto",
classes = @ConstructorResult(
targetClass = ReviewDto.class,
columns = {
@ColumnResult(name = "reviewCode", type = String .class),
@ColumnResult(name = "foodCode", type = String.class),
@ColumnResult(name = "memberCode", type = String.class),
@ColumnResult(name = "reviewDate", type = Date.class),
@ColumnResult(name = "reviewCont", type = String.class),
@ColumnResult(name = "reviewScore", type = int.class)
}
)
)










public class Review {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_code", nullable = false)
	private String reviewCode;	// 리뷰 코드
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_code")
	private Food food;	// 음식점 코드
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_code")
	private Member member;	// 멤버 코드
	
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name = "review_date" ,nullable = false)
	private Date reviewDate;	// 리뷰 등록일
	
	
	@Column(name = "review_cont" ,nullable = false)
	private String reviewCont;	// 리뷰 내용
	
	@Column(name = "review_score" ,nullable = false)
	private int reviewScore;	// 리뷰 점수	
	
	
	
	
	
    public void updateReview(ReviewDto reviewDto){	
    	this.reviewDate = reviewDto.getReviewDate();	
    	this.reviewCont = reviewDto.getReviewCont();	
    	this.reviewScore = reviewDto.getReviewScore();	
	
    }
	
	
	
	
	
	
	

}
