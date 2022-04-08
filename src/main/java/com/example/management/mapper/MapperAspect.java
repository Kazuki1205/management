package com.example.management.mapper;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * AOPクラス
 * 指定したクラスの共通した処理を抜き出して処理する。
 */
@Aspect
@Component
public class MapperAspect {
	
	/**
	 * com.example.management.mapperパッケージの
	 * update/deleteメソッドの実行前に処理。各メソッドへ引数を渡す。
	 * 
	 * @throws Throwable 例外のスーパークラス 
	 */
	@Before("execution(* com.example.management.mapper.*Mapper.update(..)) || " + 
			"execution(* com.example.management.mapper.*Mapper.delete(..))") 
	public void setUpdateAt(JoinPoint joinPoint) throws Throwable {
		
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		
		// Mapperの第一引数(Employee等のモデルクラス）を取得
		Object[] args = joinPoint.getArgs();
		Object dto = args[0];
		
		// ↑で取得したdto(Employee等のモデルクラス)のメンバ変数のsetterを指定し、現在時刻をセットする。
		Method setUpdatedAt = ReflectionUtils.findMethod(dto.getClass(), "setUpdatedAt", LocalDateTime.class);
		
		setUpdatedAt.invoke(dto, now);		
	}
}


