package com.java.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "favorite")
@Getter 
@Setter
@ToString
public class Favorite {
	
	
    @Id
    
    @Column(name = "Favorite_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_code")
    private Member member;
		
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_code")
    private Food food;  

    @CreationTimestamp
	@Temporal(TemporalType.DATE)
    @Column(name = "favorite_date" , nullable = false)
    private Date favoriteDate;


    }

