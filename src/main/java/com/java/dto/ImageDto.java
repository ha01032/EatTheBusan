package com.java.dto;


import org.modelmapper.ModelMapper;

import com.java.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
	private String imageCode;
	private String referCode;
	private String imageName;
	private long imageSize;
	private String imagePath;
	
	private static ModelMapper modelMapper = new ModelMapper();
    public static ImageDto of(Image image){
        return modelMapper.map(image,ImageDto.class);
    }
	
}
