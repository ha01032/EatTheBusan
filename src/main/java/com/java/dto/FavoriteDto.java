package com.java.dto;

import java.sql.Date;

import org.modelmapper.ModelMapper;

import com.java.entity.Favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
	private String memberCode;
	private String foodCode;
	private Date favoriteDate;
	

	private static ModelMapper modelMapper = new ModelMapper();
    public static FavoriteDto of(Favorite favorite){
        return modelMapper.map(favorite,FavoriteDto.class);
    }
	
}
