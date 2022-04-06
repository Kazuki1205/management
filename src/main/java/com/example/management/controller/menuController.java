package com.example.management.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.management.model.Employee;

/**
 * メニューコントローラー
 */
@Controller
public class menuController {

	/**
	 * ログイン後に表示されるメニュー画面
	 * 
	 * @param employee　ログイン後に画面に表示される「部署」「社員」の情報を取得する。
	 * @return メニューテンプレート
	 */
	@GetMapping("/menu")
	public String index(@AuthenticationPrincipal Employee employee) {

		return "menus/index";
	}
}
