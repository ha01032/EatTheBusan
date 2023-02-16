package com.java.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


public interface FavoriteService {

	int favorCheck(ModelAndView mav);

	int favorSwitch(ModelAndView mav);

}