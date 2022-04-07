package com.example.management;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * DIコンテナに格納しておくクラスを設定
 */
@Configuration
public class AppConfig {
	
	/**
	 * フォーム→モデル・モデル→フォームの値詰替に使用
	 * 
	 * @return ModelMapper
	 */
	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
	
	/**
	 * データベースから取得したLocalDateTime型の日時を
	 * Thymeleafで繰り返し表示する為に必要。
	 * 
	 * @return　Java8TimeDialect
	 */
	@Bean
	public Java8TimeDialect java8TimeDialect() {
		
		return new Java8TimeDialect();
	}
}