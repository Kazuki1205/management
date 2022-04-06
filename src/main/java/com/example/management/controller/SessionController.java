package com.example.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * セッションコントローラー
 */
@Controller
public class SessionController {

	/**
	 * ログイン画面を表示する。
	 * 
	 * @return セッションテンプレート
	 */
	@GetMapping("/login")
	public String index () {
		return "sessions/new";
	}
	
	/**
	 * ログインが失敗した際のメソッド
	 * 
	 * @param model　テンプレートから受け取る情報
	 * 
	 * @return　セッションテンプレート
	 */
	@GetMapping("/login-failure")
	public String loginFailure(Model model) {
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-danger");
		model.addAttribute("message", "Eメールまたはパスワードが正しくありません。");

		return "sessions/new";
	}

	/**
	 * ログアウトが完了した際のメソッド
	 * 
	 * @param model　テンプレートから受け取る情報
	 * 
	 * @return　セッションテンプレート
	 */
	@GetMapping("/logout-complete")
	public String logoutComplete(Model model) {
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-info");
		model.addAttribute("message", "ログアウトしました。");

		return "pages/index";
	}
}
