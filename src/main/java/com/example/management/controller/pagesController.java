package com.example.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * トップページコントローラー
 */
@Controller
public class pagesController {

	/**
	 * 最初に表示されるページ。
	 * 
	 * @return トップページテンプレート
	 */
	@RequestMapping("/")
	public String index() {
		return "pages/index";
	}
}
