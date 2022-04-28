// 各ページの一覧表示テーブルに使用するライブラリ
$(function($){
	
	$.extend( $.fn.dataTable.defaults, {
		
		language: { url: "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json" }
	});

	$("#DataTables").DataTable({
		
        // 件数切替機能 有効
        lengthChange: true,
        
        // 検索機能 無効
        searching: false,
        
        // ソート機能 有効
        ordering: true,
        
        // 情報表示 有効
        info: true,
        
        // ページング機能 有効
        paging: true, 
        
        // 件数切替の値を10～50の10刻みにする
	    lengthMenu: [ 10, 20, 30, 40, 50 ],
	    
	    // 件数のデフォルトの値を20にする
	    displayLength: 20,  
        
        // スクロールバー
        scrollX: true,
        scrollY: 700, 
        
        // 状態を保存する機能をつける
    	stateSave: true
    });
});

// 登録成功時・失敗時等に表示するメッセージの消える間隔
$(function() {
	
	$(".alert").fadeOut(3000);
});

// 新規登録・更新のフォーム送信時のメッセージ
$(function(){
	
    $("#form").on("click", function(){
	
        if(window.confirm('入力内容を確認して問題なければOKを押してください')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

// 削除のフォーム送信時のメッセージ
$(function(){
	
    $("#form-delete").on("click", function(){
	
        if(window.confirm('この情報を削除しますか？')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

// 社員パスワード変更のフォーム送信時メッセージ
$(function(){
	
    $("#form-change").on("click", function(){
	
        if(window.confirm('入力したパスワードに変更しますか？')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

// 受注明細フォーム削除時メッセージ
$(function(){
	
    $(".form-remove").on("click", function(){
	
        if(window.confirm('選択した明細行を削除しますか？')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

// セレクトボックスに検索機能を追加するライブラリ
$(document).ready(function() {
	
    $('.select').select2({
	
        // コンテナ幅のカスタマイズをサポートします。
        width: '300px',
	});
});

// 日報入力時に製作番号を選択した際、それに紐づく商品コード・商品名・製作数・部署毎の完了数計・不良数計を非同期で取得
$(function() {

	// 日報入力画面の製作番号セレクトボックスの値変更時
	$('#ajax-productionId').change(function() {
		
		// セレクトボックスのvalueが「""」(選択して下さい)の場合、非同期通信は行わない。
		if ($('[name=productionId] option:selected').val()) {
		
			// urlのコントローラーにPOSTデータで、製作番号IDとトークンを渡す。
			$.ajax({
				url: '/ajax/production', 
				type: 'POST', 
				data: {
					productionId: $('[name=productionId] option:selected').val(), 
					_csrf: $('*[name=_csrf]').val()
				}, 
				dataType: 'json' // レスポンスをJsonデータとして受け取る。
			})
			
			// コントローラーから返された製作手配クラスのJsonデータから、商品コード・商品名・製作数・部署毎の完了数計・不良数計をそれぞれ取り出し、各フォームに入力する。
			.done(function(data) {
				
				$('#ajax-itemCode').val(data.item.code);
				$('#ajax-itemName').val(data.item.name);
				$('#ajax-lotQuantity').val(data.lotQuantity);
				$('#ajax-departmentCompletionQuantityTotal').val(data.departmentCompletionQuantityTotal);
				$('#ajax-failureQuantityTotal').val(data.failureQuantityTotal);
				$('#ajax-storingQuantityTotal').val(data.storingQuantityTotal);
			})	
			
			// 「選択して下さい」を選ぶと、各入力欄に空白が入力される。
		} else {
			
			$('#ajax-itemCode').val("");
			$('#ajax-itemName').val("");
			$('#ajax-lotQuantity').val("");
			$('#ajax-departmentCompletionQuantityTotal').val("");
			$('#ajax-failureQuantityTotal').val("");
			$('#ajax-storingQuantityTotal').val("");
		}
	});
});

// 受注明細入力時に商品を選択した際、それに紐づく単価を非同期で取得、数量が入力されていれば小計を計算する。
$(function() {

	// 受注明細入力画面の商品セレクトボックスの値変更時
	$('.ajax-itemId').change(function() {
		
		// 選択されたオプション要素を変数に代入する。
		let itemIdElement = $('option:selected', this);
		
		// 選択されたオプション要素の親要素のセレクトボックスのインデックスを取得する。
		let index = itemIdElement.parent().attr('data-item-id');
		
		// 受注数を変数に代入
		let orderQuantity = $('[data-order-quantity=' + index + ']').val();
		
		// セレクトボックスのvalueが「""」(選択して下さい)の場合、非同期通信は行わない。
		if (itemIdElement.val()) {
		
			// urlのコントローラーにPOSTデータで、商品IDとトークンを渡す。
			$.ajax({
				url: '/ajax/item', 
				type: 'POST', 
				data: {
					itemId: itemIdElement.val(), 
					_csrf: $('*[name=_csrf]').val()
				}, 
				dataType: 'json' // レスポンスをJsonデータとして受け取る。
			})
			
			// コントローラーから返された商品クラスのJsonデータから、単価を取り出し、フォームに入力する。
			.done(function(data) {
				
				// フォームに単価を入力。
				$('[data-unit-price=' + index + ']').val(data.unitPrice);
				
				// 受注数が入力されていれば、小計を計算しフォームにセットする。無ければ空をセットする。
				if (orderQuantity) {
					
					$("[data-order-amount=" + index + "]").val(data.unitPrice * orderQuantity);
				} else {
					
					$("[data-order-amount=" + index + "]").val('');
				}
			})	
			
		// 「選択して下さい」を選ぶと、単価・小計に空白が入力される。
		} else {
			
			$("[data-unit-price=" + index + "]").val("");
			$("[data-order-amount=" + index + "]").val("");
		}
	});
});

// 受注明細入力時に受注数を入力した際、単価が入力されていれば小計を計算する。
$(function() {
	
	// 受注明細入力画面の受注数の値変更時
	$('.dom-orderQuantity').change(function () {
		
		// 入力された受注数フォームのインデックスを取得し変数へ代入する。
		let index = $(this).attr('data-order-quantity')
		
		// 入力フォームのvalueが「""」の場合、フォームの値書き換えは行わない。
		if ($(this).val()) {
			
			// 受注数・単価を変数に代入
			let orderQuantity = $(this).val();
			let unitPrice = $('[data-unit-price=' + index + ']').val();
			
			// 単価が入力されていれば、小計を計算しフォームにセットする。無ければ空をセットする。
			if (unitPrice) {
				
				$('[data-order-amount=' + index + ']').val(unitPrice * orderQuantity);
			} else {
				
				$('[data-order-amount=' + index + ']').val('');		
			}
			
		// 入力フォームが空だった場合、計算は行わず、小計には空を入力する。
		} else {
			
			$('[data-order-amount=' + index + ']').val('');		
		}
	});
});

// 出荷明細の出荷数を入力した際に、出荷金額小計を計算して入力する。
$(function() {
	
	$('.ajax-shippingQuantity').change(function() {
		
		// 変更のあった要素の出荷数を取得する。
		let shippingQuantity = $(this).val();
		
		// 変更のあった要素のインデックスを取得する。
		let index = $(this).attr('data-shipping-quantity');
		
		// 商品単価を取得する。
		let unitPrice = $('[data-unit-price=' + index + ']').val();
		
		// 入力があれば出荷金額小計を計算し、フォームに入力する。
		if (shippingQuantity) {
			
			// 商品単価 * 出荷数 = 出荷金額小計を計算し、フォームに入力する。
			$('[data-shipping-amount=' + index + ']').val(unitPrice * shippingQuantity);
		} else {
			
			$('[data-shipping-amount=' + index + ']').val();
		}
	});
});