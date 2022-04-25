package com.example.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.management.service.CommonService;

/**
 * エラーページコントローラー
 */
@ControllerAdvice
@Component
public class GlobalControllerAdvice {
	
	@Autowired
	private CommonService commonService;
	
	// ログ出力
	protected static Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	/**
	 * 例外が発生した際に呼び出されるメソッド
	 * 
	 * @param e　例外
	 * @param model　テンプレートへ渡す情報
	 * 
	 * @return　エラーページテンプレート
	 */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		
		log.error("Exception", e);
		
		model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "ただいま利用できません。"));
		
		return "error";
	}

}
