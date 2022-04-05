package com.example.management.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SessionController {

	@GetMapping("/login")
	public String index () {
		return "sessions/new";
	}
	@GetMapping("/login-failure")
	public String loginFailure(Model model, Locale locale) {
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-danger");
		model.addAttribute("message", "Eメールまたはパスワードが正しくありません。");

		return "sessions/new";
	}

	@GetMapping("/logout-complete")
	public String logoutComplete(Model model, Locale locale) {
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", "ログアウトしました。");

		return "pages/index";
	}
}
