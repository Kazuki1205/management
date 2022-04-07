package com.example.management.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * メニューコントローラー
 */
@Controller
public class MenuController {
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "メニュー画面";
	}

	/**
	 * ログイン後に表示されるメニュー画面
	 * 
	 * @return メニューテンプレート
	 */
	@GetMapping("/menu")
	public String index() {

		return "menus/index";
	}
}
