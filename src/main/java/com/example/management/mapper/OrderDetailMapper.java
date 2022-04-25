package com.example.management.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.management.model.OrderDetail;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface OrderDetailMapper {
	
	/**
	 * 引数の受注番号と明細IDで該当レコードを取得する。
	 * 
	 * @param orderNumber 受注番号
	 * @param detailId 受注明細ID
	 * 
	 * @return OrderDetail 受注明細クラス
	 */
	public OrderDetail findByPrimaryKey(String orderNumber, Long detailId);

	/**
	 * 引数の受注明細クラスのリストをDBへinsertする。
	 * 
	 * @param orderDetails 受注明細クラスのリスト
	 */
	public void createAll(@Param("orderDetails") List<OrderDetail> orderDetails);
	
	/**
	 * 引数の受注明細クラスを基に、該当レコードをupdateする。
	 * 
	 * @param orderDetail 受注明細クラス
	 */
	public void update(OrderDetail orderDetail);
}
