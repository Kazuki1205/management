package com.example.management.form;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.management.form.EmployeeForm;

import lombok.Data;

@Data
public class EmployeeForm {
	
	@NotBlank
	@Size(max = 32)
	private String username;
	
	@NotBlank
	@Size(max = 32)
	private String name;

	private List<DepartmentForm> departments; // HTMLでセレクトボックスに表示させる為のリスト。フォーム送信時は departmentId にString型として受け取る。
	
	@NotBlank
	private String departmentId;
	
	@NotBlank
	@Size(max = 32)
	private String password;
	
	@NotBlank
	@Size(max = 32)
	private String passwordConfirmation;
	
	@AssertTrue(message = "PasswordとPassword confirmationは同一にしてください。")
	public boolean isPasswordValid () {
		if (password == null || password.isEmpty()) {
			return true;
		}
		return password.equals(passwordConfirmation);
	}
	
}
