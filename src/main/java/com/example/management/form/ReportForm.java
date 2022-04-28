package com.example.management.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;

/**
 * 日報画面の入力フォーム用クラス
 */
@Data
public class ReportForm {

	private Long id; // ID
	
	private Long departmentId; // 部署名
	
	private String employeeName; // 社員名
	
	private String lotNumber; // 製作番号
	
	private Integer lotQuantity; // 製作数
	
	private Integer departmentCompletionQuantityTotal; // 入力者の所属する部署毎の、該当製番に対する完了数計
	
	private Integer failureQuantityTotal; // 該当製番に対する不良数計
	
	@NotNull(message = "製作番号を選択して下さい。")
	private Long productionId; // 製作ID
	
	@NotBlank(message = "商品コードが入力されていません。")
	private String itemCode; // 商品コード
	
	@NotBlank(message = "商品名が入力されていません。")
	private String itemName; // 商品名
	
	@PositiveOrZero(message = "「0」以上の値を入力して下さい。")
	private Integer completionQuantity = 0; // 完了数
	
	@PositiveOrZero(message = "「0」以上の値を入力して下さい。")
	private Integer failureQuantity = 0; // 不良数
	
	/**
	 * 完了数・不良数ともに「0」の場合、バリデーションエラー
	 * 
	 * @return boolean 真偽値
	 */
	@AssertTrue(message = "完了数と不良数のどちらかは「1」以上の値を入力して下さい。")
	public boolean isQuantityValid() {
		
		// 空の状態でフォーム送信されている場合、「0」をセットする。
		this.completionQuantity = completionQuantity == null ? 0 : completionQuantity;
		this.failureQuantity = failureQuantity == null ? 0 : failureQuantity;
		
		// 完了数・不良数ともに「0」の場合、バリデーションエラー。falseを返す
		if (completionQuantity == 0 && failureQuantity == 0) {
			
			return false;
		}
		return true;
	}
}
