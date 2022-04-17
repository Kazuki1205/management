package com.example.management.controller.department;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.management.form.DepartmentForm;
import com.example.management.mapper.DepartmentMapper;
import com.example.management.service.DepartmentService;

@Controller
public class DepartmentEditController {
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 各ハンドラメソッド実行前に呼び出されるメソッド
	 * ナビゲーションバーの名前を設定する。
	 * ※modelに自動的にaddAttributeされる。
	 * 
	 * @return ナビゲーションバータイトル
	 */
	@ModelAttribute(name = "navTitle")
	public String setNavTitle() {
			
		return "部署マスタ(更新/削除)";
	}

	/**
	 * 部署マスタ一覧から選んだID情報を基に
	 * departmentFormに詰め直して編集画面を表示する
	 * 
	 * @param id ID
	 * @param model テンプレートへ渡す情報
	 * 
	 * @return 部署編集テンプレート
	 */
	@GetMapping("/department/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		
		// ID情報を基にDBから部署テーブルを検索し部署クラスとして取得、部署フォームへ詰め替える。
		DepartmentForm departmentForm = modelMapper.map(departmentMapper.findByIdExcludeInvalid(id), DepartmentForm.class);
		
		model.addAttribute("departmentForm", departmentForm);
		
		return "departments/edit";
	}
	
	/**
	 * 部署情報を受け取り、DBを更新するメソッド
	 * 更新できたらリストにリダイレクト。
	 * 
	 * @param departmentForm　部署フォーム
	 * @param bindingResult 部署フォームのバリデーション結果
	 * @param model テンプレートへ渡す情報
	 * @param redirectAttributes リダイレクト先へ渡す情報
	 * 
	 * @return　部署編集テンプレート/部署リストへリダイレクト
	 */
	@PostMapping("/department/edit/update")
	public String update(@Validated @ModelAttribute("departmentForm") DepartmentForm departmentForm, 
						 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		// 部署フォームのバリデーションチェックに引っかかった場合、エラーメッセージを表示。
		if (bindingResult.hasErrors()) {
			
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "更新に失敗しました。");
			
			return "departments/edit";
		} else {
			
			// DBへINSERT処理、正常処理メッセージを表示。
			departmentService.update(departmentForm);
			
			redirectAttributes.addFlashAttribute("hasMessage", true);
			redirectAttributes.addFlashAttribute("class", "alert-info");
			redirectAttributes.addFlashAttribute("message", "更新に成功しました。");
			
			return "redirect:/department/list";
		}
	}
	
	
	@PostMapping("/department/edit/delete")
	public String delete(@ModelAttribute("departmentForm") DepartmentForm departmentForm, RedirectAttributes redirectAttributes) {
		
		// DBへDELETE処理
		departmentService.delete(departmentForm);
		
		redirectAttributes.addFlashAttribute("hasMessage", true);
		redirectAttributes.addFlashAttribute("class", "alert-info");
		redirectAttributes.addFlashAttribute("message", "削除に成功しました。");
		
		return "redirect:/department/list";
	}
}
