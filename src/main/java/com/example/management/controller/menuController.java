package com.example.management.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.management.entity.Employee;

@Controller
public class menuController {

	@GetMapping("/menu")
	public String index(@AuthenticationPrincipal Employee employee) {
		return "menus/index";
	}
}
