package com.example.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページコントローラー
 */
@Controller
public class PagesController {
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "生産管理アプリ";
	}

	/**
	 * 最初に表示されるページ。
	 * 
	 * @param テンプレートから受け取る情報
	 * 
	 * @return トップページテンプレート
	 */
	@RequestMapping("/")
	public String index() {
		
		return "pages/index";
	}
}
