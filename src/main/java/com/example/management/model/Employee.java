package com.example.management.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

/**
 * UserDetailsを実装した社員クラス。
 * ユーザー認証はこのクラスで行う。
 */
@Data
public class Employee implements UserDetails {

	private static final long serialVersionUID = 1L; // シリアルID
	
	public Employee () {} // コンストラクタ(MyBatisで使用)
	
	private Long id; // ID
	
	private String username; // 社員ID
	
	private String password; // パスワード
	
	private String name; // 社員名
	
	private Department department; // 部署クラス
	
	private String authority; // 権限
	
	private LocalDateTime createdAt; // 作成日時
	
	private LocalDateTime updatedAt; // 更新日時
	
	private Short invalid; // 削除フラグ

	/**
	 * 社員の所持する権限を返すメソッド
	 * 
	 * @return Collection<GrantedAuthority> コレクション型の権限
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority.toString()));
		return authorities;
	}

	// UserDetails実装のオーバーライド
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// UserDetails実装のオーバーライド
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// UserDetails実装のオーバーライド
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// UserDetails実装のオーバーライド
	@Override
	public boolean isEnabled() {
		return true;
	}

}
