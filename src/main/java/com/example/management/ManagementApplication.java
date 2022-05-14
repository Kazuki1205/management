package com.example.management;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
@MapperScan("com.example.management.mapper")
public class ManagementApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ManagementApplication.class, args);
//		
//		// ディレクトリを取得・apiバージョンを設定
//		String userDirectory = System.getProperty("user.dir");
//		
//		// JApiDocsの設定
//		DocsConfig config = new DocsConfig();
//		
//		// プロジェクトのrootパスを設定
//		config.setProjectPath(userDirectory);
//		
//		// プロジェクト名を設定
//		config.setProjectName(Paths.get(userDirectory).getFileName().toString());
//		
//		// プロジェクトのバージョンを設定
//		config.setApiVersion("v1.0");
//		
//		// apiドキュメントの出力ディレクトリを設定
//		config.setDocsPath(userDirectory);
//		
//		// 自動出力
//		config.setAutoGenerate(Boolean.TRUE);
//		
//		// htmlファイルを出力
//		config.addPlugin(new MarkdownDocPlugin());
//		Docs.buildHtmlDocs(config);
	}

	// MyBatisの設定
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis-config.xml"));

		return sqlSessionFactoryBean.getObject();
	}
}
