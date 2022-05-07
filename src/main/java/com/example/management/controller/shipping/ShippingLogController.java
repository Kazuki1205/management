package com.example.management.controller.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.management.form.ShippingForm;
import com.example.management.mapper.ShippingMapper;
import com.example.management.model.Shipping;

/**
 * 出荷の履歴画面コントローラー
 */
@Controller
public class ShippingLogController {
	
	@Autowired
	private ShippingMapper shippingMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "出荷履歴";
	}
	
	/**
	 * 出荷履歴一覧を表示
	 * 
	 * @param orderNumber 受注番号
	 * @param detailId 受注明細ID
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param address 住所
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * @param shippingForm 出荷フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 出荷履歴テンプレート
	 */
	@GetMapping("/shipping/log")
	public String index(@RequestParam(name = "orderNumber", defaultValue = "") String orderNumber, 
						@RequestParam(name = "detailId", defaultValue = "") Long detailId, 
						@RequestParam(name = "customerCode", defaultValue = "") String customerCode, 
						@RequestParam(name = "customerName", defaultValue = "") String customerName, 
						@RequestParam(name = "address", defaultValue = "") String address, 
						@RequestParam(name = "itemCode", defaultValue = "") String itemCode, 
						@RequestParam(name = "itemName", defaultValue = "") String itemName, 
						@ModelAttribute("shippingForm") ShippingForm shippingForm, 
						Model model) {
		
		// フォームに入力された情報を基に、受注テーブルから該当レコードの情報を取得する。
		List<Shipping> shippings = shippingMapper.findByConditions(orderNumber, detailId, customerCode, customerName, address, itemCode, itemName);
		
		model.addAttribute("shippings", shippings);
		model.addAttribute("shippingForm", shippingForm);
		
		return "shippings/log";
	}
	
	/**
	 * 出荷詳細情報画面を表示
	 * 
	 * @param id 出荷ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 出荷詳細テンプレート
	 */
	@GetMapping("/shipping/log/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		
		// パス変数の出荷IDを基に、出荷テーブルから該当レコードを取得し、テンプレートへ渡す。
		model.addAttribute("shipping", shippingMapper.findByIdExcludeInvalid(id));
		
		return "shippings/detail";
	}
}
