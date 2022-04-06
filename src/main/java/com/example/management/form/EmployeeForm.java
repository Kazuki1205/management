package com.example.management.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.management.form.EmployeeForm;
import com.example.management.validation.ValidGroup1;
import com.example.management.validation.ValidGroup2;

import lombok.Data;

/**
 * 社員マスタ登録画面の入力フォームバリデーション用クラス
 */
@Data
public class EmployeeForm {
	
	@NotBlank(groups = ValidGroup1.class)
	@Size(groups = ValidGroup2.class, min = 1, max = 32)
	private String username; // 社員ID
	
	@NotBlank(groups = ValidGroup1.class)
	@Size(groups = ValidGroup2.class, min = 1, max = 32)
	private String name; // 社員名
	
	@NotNull(groups = ValidGroup1.class, message = "部署を選択して下さい")
	private Long departmentId; // 部署ID
	
	@NotBlank(groups = ValidGroup1.class)
	@Size(groups = ValidGroup2.class, min = 5, max = 32)
	private String password; // パスワード
	
	private String passwordConfirmation; // パスワード確認用
	
	/**
	 * パスワード入力確認用バリデーション
	 * password と passwordConfirmation が一致すると true を返す
	 * 
	 * @return バリデーションチェックの真偽値
	 */
	@AssertTrue(groups = ValidGroup2.class, message = "パスワードと確認用パスワードが一致しません")
	public boolean isPasswordValid () {
		if (password == null || password.isEmpty()) {
			return true;
		}
		return password.equals(passwordConfirmation);
	}
	
}
