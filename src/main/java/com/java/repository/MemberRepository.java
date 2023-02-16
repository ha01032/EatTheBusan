package com.java.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.dto.FoodDto2;
import com.java.dto.MemberFavoriteDto;
import com.java.dto.MemberReviewDto;
import com.java.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	int countByMemberMailAndMemberPwd(String memberMail, String memberPwd);
	
	Member findByMemberMail(String memberMail);
		
	// 회원가입
	@Modifying
	@Transactional
	@Query(value =
	"insert into member(member_code, member_date, member_mail, member_pwd, member_name, member_status) " + 
	"values('member'||LPAD(seqMember.nextval,4,0), :memberDate, :memberMail, :memberPwd, :memberName, 'Y')",
	nativeQuery = true)
	public int memberInsert
	(Date memberDate, String memberMail, String memberPwd, String memberName);
	
	
	// email 중복체크
	@Query(value = "select count(*) from member where member_mail= :memberMail" ,
			 nativeQuery = true)	
	int emailCheck(String memberMail);
	
	
	//회원정보수정
	@Modifying
	@Transactional
	@Query(value = " update member set member_pwd=:memberPwd, member_name=:memberName, member_phone=:memberPhone where member_code=:memberCode",
			nativeQuery = true)	
	public int memberUpdateOk
	(@Param("memberPwd") String memberPwd, @Param("memberName") String memberName, @Param("memberPhone") String memberPhone, @Param("memberCode") String memberCode);
	
	@Query(value = "select * FROM member WHERE member_code = :memberCode",
			 nativeQuery = true)	
	Member findmember(String memberCode);
	
	@Query(value = "select count(*) FROM member WHERE member_code = :memberCode",
			 nativeQuery = true)
	int countmember(String memberCode);
	
	// memberdto
	//@Query(name = "find_memberdto" ,nativeQuery = true)
	//sList<MemberDto> findAll();

	@Query(value = "select * FROM member WHERE member_code = :memberCode",
			 nativeQuery = true)	
	Member memberInfo(String memberCode);
	

	
	@Modifying
	@Transactional
	@Query(value = "update member set member_status=:memberStatus, member_name=:memberName, member_phone=:memberPhone where member_code=:memberCode"
			,nativeQuery = true)	
	int adminUpdateOk(@Param("memberStatus") String memberStatus, @Param("memberName") String memberName, @Param("memberPhone") String memberPhone, @Param("memberCode") String memberCode);
	
	
	//카카오
	@Modifying
	@Transactional
	@Query(value = "insert into member(member_code, member_mail, member_name, member_date, member_status) " +
					" values('MEMBER'||LPAD(seqMember.nextval,4,0),	:memberMail, :memberName, sysdate, 'Y') "
			,nativeQuery = true)
					public int insertKakao
					(@Param("memberMail") String memberMail, @Param("memberName") String memberName);
					
	
	@Query(value = "select * FROM member WHERE member_mail = :memberMail",
			 nativeQuery = true)	
	Member getMemberCode(String memberMail);
	
	@Query(name = "find_fooddto", nativeQuery = true)
	List<FoodDto2> getMyFood(String memberCode);

	
	@Query(name= "find_memberfavoritedto",  nativeQuery = true)
	List<MemberFavoriteDto> getMyFavorite(String memberCode);
	
	
	@Query(name= "find_memberreviewdto",  nativeQuery = true)
	List<MemberReviewDto> getMyReview(String memberCode);
	
	//네이버
	@Modifying
	@Transactional
	@Query(value = "insert into member(member_code, member_mail, member_name, member_date, member_status)" + 
					" values('member'||LPAD(seqMember.nextval,4,0), :memberMail, :memberName, sysdate, 'Y') "
					,nativeQuery = true)
	public int insertNaver
	(@Param("memberMail") String memberMail, @Param("memberName") String memberName);
	

}