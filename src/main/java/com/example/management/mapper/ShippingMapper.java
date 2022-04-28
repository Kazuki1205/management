package com.example.management.mapper;

import java.util.List;
import java.util.Optional;

import com.example.management.model.Shipping;

/**
 * MyBatisで使用するMapperクラス(@Mapperアノテーションは設定にて省略)
 */
public interface ShippingMapper {

	/**
	 * 出荷テーブルのレコードを、引数のあいまい検索で取得する。
	 * 
	 * @param orderNumber 受注番号
	 * @param detailId 受注明細ID
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param address 住所
	 * @param itemCode 商品コード
	 * @param itemName 商品名
	 * 
	 * @return List<Shipping> リスト型の出荷クラス
	 */
	public List<Shipping> findByConditions(String orderNumber, Long detailId, String customerCode,String customerName,String address,String itemCode,String itemName);
	
	/**
	 * 引数の出荷IDを基に該当レコードを取得する。
	 * 
	 * @param id 出荷ID
	 * 
	 * @return 出荷クラス
	 */
	public Shipping findByIdExcludeInvalid(Long id);
	
	/**
	 * 引数の受注番号と受注明細IDと合致したレコードの出荷数を集計する。
	 * 
	 * @param orderNumber　受注番号
	 * @param detailId 受注ID
	 * 
	 * @return Optional<Integer> 出荷数合計
	 */
	public Optional<Integer> sumOfShippingQuantityTotal(String orderNumber, Integer detailId);
	
	/**
	 * 引数の出荷クラスを基に、DBへinsertする。
	 * 
	 * @param shipping 出荷クラス
	 */
	public void create(Shipping shipping);
}
