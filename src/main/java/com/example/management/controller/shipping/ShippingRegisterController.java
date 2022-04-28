package com.example.management.controller.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.ShippingDetailForm;
import com.example.management.form.ShippingForm;
import com.example.management.mapper.OrderDetailMapper;
import com.example.management.mapper.OrderMapper;
import com.example.management.mapper.ShippingMapper;
import com.example.management.mapper.StockMapper;
import com.example.management.model.Order;
import com.example.management.service.CommonService;
import com.example.management.service.ShippingService;

/**
 * 出荷の新規登録コントローラー
 */
@Controller
public class ShippingRegisterController {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
		
		return "出荷入力(新規登録)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 出荷登録画面のセレクトボックスの受注一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 受注型のリスト
	 */
	@ModelAttribute(name = "orders")
	public List<Order> getOrders() {
		
		List<Order> orders = orderMapper.findAllExcludeInvalidAndCompletion();
		
		return orders;
	}
	
	/**
	 * 出荷登録画面を表示
	 * 
	 * @param shippingForm 出荷フォーム
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 出荷登録テンプレート
	 */
	@GetMapping("/shipping/register")
	public String register(@ModelAttribute("shippingForm") ShippingForm shippingForm, 
						   RedirectAttributes redirectAttributes, 
						   Model model) {
		
		return "shippings/register";
	}
	
	/**
	 * 出荷登録画面で、受注番号を選択後、「検索」ボタンを押した際に呼ばれるメソッド。
	 * 必要な情報を出荷フォームに詰める。
	 * 
	 * @param shippingForm　出荷フォーム
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 出荷登録テンプレート
	 */
	@PostMapping(value = "/shipping/register", params = "search")
	public String searchOrder(@ModelAttribute("shippingForm") ShippingForm shippingForm, Model model) {
		
		// 検索の際、「選択して下さい」が選ばれている場合、受注番号がnullの為、検索処理は行わない。
		if (!shippingForm.getSelectOrderNumber().equals("")) {
			
			// 出荷フォームに情報を詰めて、テンプレートへ渡す。
			model.addAttribute("shippingForm", shippingService.setShippingForm(shippingForm));
		}
		
		return "shippings/register";
	}
	
	/**
	 * 出荷フォームに入力された情報を基に、DBをupdateする。
	 * 出荷テーブルにinsert、受注テーブル・受注明細テーブルの出荷完了日をupdate、
	 * 在庫テーブルの在庫数をupdate。
	 * 
	 * @param shippingForm 出荷フォーム
	 * @param bindingResult 出荷フォームのバリデーション結果
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 出荷登録テンプレート/出荷履歴へリダイレクト
	 */
	@PostMapping("/shipping/register/new")
	public String create(@Validated @ModelAttribute("shippingForm") ShippingForm shippingForm, 
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes, 
						 Model model) {
		
		// 出荷フォームにバリデーションエラーが合った場合、もしくは受注番号が選択されていない場合は、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", "登録に失敗しました。"));
		} else {
			
			// 出荷フォームから出荷明細フォームのリストを取り出す。
			List<ShippingDetailForm> shippingDetailForms = shippingForm.getShippingDetailForms();
			
			// 出荷フォームから受注番号を取り出す。
			String orderNumber = shippingForm.getOrderNumber();
			
			// エラーフラグを用意。
			Short errorFlag = 0;
			
			// 出荷数の入力チェック用フラグを用意。
			Short inputFlag = 0;
			
			// メッセージを用意。
			String msg = "いずれかの明細行に出荷数を入力して下さい。";
			
			// for文で出荷明細行毎に評価を行う。「受注数 - 出荷済計 >= 出荷数」 and 「実在庫 >= 出荷数」若しくは、「いずれかの行が出荷数1以上」に該当しない場合は、エラーメッセージを表示する。
			for (ShippingDetailForm shippingDetailForm : shippingDetailForms) {
				
				// 受注明細IDを取り出す。
				Integer detailId = shippingDetailForm.getDetailId();
				
				// 受注数を取得する。
				Integer orderQuantity = orderDetailMapper.getOrderQuantity(orderNumber, detailId);
				
				// 出荷済計を取得する。
				Integer shippingQuantityTotal = shippingMapper.sumOfShippingQuantityTotal(orderNumber, detailId).orElse(0);
				
				// 実在庫を取得する。
				Integer actualQuantity = stockMapper.getActualQuantity(shippingDetailForm.getItemId());
				
				// 出荷フォームの出荷数を取得する。nullだった場合は「0」とする。
				Integer shippingQuantity = shippingDetailForm.getShippingQuantity() != null ? shippingDetailForm.getShippingQuantity() : 0;
				
				// 「受注数 - 出荷済計 >= 出荷数」がfalseになった場合、処理を中断。メッセージを格納する。
				if (!(orderQuantity - shippingQuantityTotal >= shippingQuantity)) {

					msg = ("未出荷の数量を超えた出荷数が入力されています。");
					errorFlag = 1;				
					break;
				}
				
				// 「実在庫 >= 出荷数」がfalseになった場合、処理を中断。メッセージを格納する。
				if (!(actualQuantity >= shippingQuantity)) {
					
					msg = ("実在庫の数量を超えた出荷数が入力されています。");
					errorFlag = 1;			
					break;								
				}
				
				// 各行の出荷数入力チェック。for内で出荷数「1」以上にならなかった場合、フラグが立たず繰り返しを終了する。
				if (shippingQuantity >= 1) {
					
					inputFlag = 1;
				}
				
			}
			
			// for内でエラーが発生した場合、エラーメッセージを表示し、テンプレートを返す。
			if (errorFlag == 1 || inputFlag == 0) {
				
				model.addAttribute("messageMap", commonService.getMessageMap("alert-danger", msg));
			} else {
				
				// DBへinsert処理。
				shippingService.create(shippingForm);
				
				redirectAttributes.addFlashAttribute("messageMap", commonService.getMessageMap("alert-info", "登録に成功しました。"));
				
				return "redirect:/shipping/log";
			}
		}
		
		model.addAttribute("shippingForm", shippingForm);
		
		return "shippings/register";
	}
}
