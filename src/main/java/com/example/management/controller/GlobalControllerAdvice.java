package com.example.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * エラーページコントローラー
 */
@ControllerAdvice
@Component
public class GlobalControllerAdvice {
	
	// ログ出力
	protected static Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	/**
	 * 例外が発生した際に呼び出されるメソッド
	 * 
	 * @param e　例外
	 * @param model　テンプレートへ渡す情報
	 * @return　
	 */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		
		log.error("Exception", e);
		
		model.addAttribute("hasMessage", true);
		model.addAttribute("class", "alert-danger");
		model.addAttribute("message", "ただいま利用できません。");
		
		return "error";
	}

}
