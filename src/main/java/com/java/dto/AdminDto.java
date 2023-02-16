package com.java.dto;

import org.modelmapper.ModelMapper;

import com.java.entity.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

	private String adminId;
	private String adminPwd;
	
	
	private static ModelMapper modelMapper = new ModelMapper();
    public static AdminDto of(Admin admin){
        return modelMapper.map(admin,AdminDto.class);
    }
	
	
}
