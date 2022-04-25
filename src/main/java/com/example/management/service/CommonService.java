package com.example.management.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 各コントローラーの共通ロジッククラス
 */
@Service
public class CommonService {
	
	/**
	 * 各コントローラーで発生するメッセージ表示の
	 * 情報を編集しmapで返すメソッド。
	 * 
	 * @param alert アラートの表示色を変えるclassの名前
	 * @param message 表示するメッセージ
	 * 
	 * @return Map<String, Object> アラートメッセージを表示する為の情報をセットしたmap
	 */
	public Map<String, Object> getMessageMap(String alert, String message) {
		
		// アラートメッセージ表示に必要な情報を詰めるMapを用意する。
		Map<String, Object> map = new HashMap<>();
		
		// "alert-info", "alert-danger"を引数に取り、表示色を変える。
		map.put("class", alert);
		
		// 表示するメッセージ。
		map.put("message", message);
		
		return map;
	}
}
