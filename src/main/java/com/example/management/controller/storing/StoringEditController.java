package com.example.management.controller.storing;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.StoringForm;
import com.example.management.mapper.ProductionMapper;
import com.example.management.mapper.ReportMapper;
import com.example.management.mapper.StoringMapper;
import com.example.management.model.Production;
import com.example.management.model.Storing;
import com.example.management.service.StoringService;

/**
 * 入庫の履歴画面コントローラー
 */
@Controller
public class StoringEditController {
	
	@Autowired
	private StoringMapper storingMapper;
	
	@Autowired
	private StoringService storingService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ReportMapper reportMapper;
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {

		return "入庫入力(更新)";
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * 社員マスタ登録画面のセレクトボックスの部署一覧を取得する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 製作型のリスト
	 */
	@ModelAttribute(name = "productions")
	public List<Production> getProductions() {
		
		List<Production> productions = productionMapper.findAllExcludeInvalid();
		
		return productions;
	}
	
	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * storings/common.htmlの入力フォームを、
	 * edit画面ではreadonlyにする。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return 編集フラグ
	 */
	@ModelAttribute(name = "editFlag")
	public Integer setEditFlag() {
		
		Integer editFlag = 1;
		
		return editFlag;
	}

	/**
	 * 
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 入庫編集画面テンプレート
	 */
	@GetMapping("/storing/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// 入庫テーブルからIDを基にレコードを取得
		Storing storing = storingMapper.findById(id);
		
		// 入庫フォームへ値の詰め替え
		StoringForm storingForm = modelMapper.map(storing, StoringForm.class);
		
		// 入庫テーブルの製作IDを基に、製作クラスの情報を取得する。
		Production production = productionMapper.findById(storing.getProduction().getId());
		
		// それぞれ製作ID・製作番号・商品コード・商品名・製作数をセットする。
		storingForm.setProductionId(production.getId());
		storingForm.setLotNumber(production.getLotNumber());
		storingForm.setItemCode(production.getItem().getCode());
		storingForm.setItemName(production.getItem().getName());
		storingForm.setLotQuantity(production.getLotQuantity());
		
		// 日報テーブルの完了数計(製作IDと部署IDでグループ化したものの集計)を取得する。nullなら0とする。
		storingForm.setStoringQuantityTotal(storingMapper.sumOfStoringQuantity(production.getId()).orElse(0));
		
		// 日報テーブルの不良数計(製作IDでグループ化したものの集計)を取得する。nullなら0とする。
		storingForm.setFailureQuantityTotal(reportMapper.sumOfFailureQuantity(production.getId()).orElse(0));
		
		model.addAttribute("storingForm", storingForm);
		
		return "storings/edit";
	}
	
	
	@PostMapping("/storing/edit/update")
	public String update(@ModelAttribute("storingForm") StoringForm storingForm, RedirectAttributes redirectAttributes) {
		
		// フォームで入力された完了区分で更新する。フォームの製作IDで取得した製作クラスを引数で渡す。
		storingService.update(storingForm, productionMapper.findByIdExcludeInvalid(storingForm.getProductionId()));
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "更新に成功しました。");
		
		return "redirect:/storing/log";
	}
}
