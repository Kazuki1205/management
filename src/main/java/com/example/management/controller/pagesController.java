package com.example.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pagesController {

	@RequestMapping("/")
	public String index() {
		return "pages/index";
	}
}
