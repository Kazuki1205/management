package com.example.management.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Table(name = "employees")
@Data
public class Employee extends AbstractEntity implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	public enum Authority {
		ROLE_USER, ROLE_ADMIN
	};
	
	public Employee() {
		super();
	}
	
	public Employee(String username, String password, String name, Long departmentId) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.departmentId = departmentId;
		this.authority = Authority.ROLE_USER;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Long departmentId;
	
	@ManyToOne
    @JoinColumn(name = "departmentId", insertable = false, updatable = false)
	private Department department;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Authority authority;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority.toString()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
