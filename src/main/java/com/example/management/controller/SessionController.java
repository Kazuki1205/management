package com.example.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.management.model.Employee;
import com.example.management.service.CommonService;

/**
 * セッションコントローラー
 */
@Controller
public class SessionController {
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "ログイン画面";
	}

	/**
	 * ログイン画面を表示する。
	 * 既にログイン状態ならメニュー画面へ遷移する。
	 * 
	 * @return セッションテンプレート/メニューへリダイレクト
	 */
	@GetMapping("/login")
	public String index (@AuthenticationPrincipal Employee employee) {
		
		// 既にログイン状態であれば、ログイン画面を表示せずメニュー画面へ遷移する。
		if (employee == null) {
		
			return "sessions/new";
		} else {
			
			return "redirect:/menu";
		}
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
		
		model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "Eメールまたはパスワードが正しくありません。"));

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
		
		model.addAttribute("messageMap", commonService.getMessageMap("alert-info", "ログアウトしました。"));
		model.addAttribute("navTitle", "生産管理システム");

		return "pages/index";
	}
}
