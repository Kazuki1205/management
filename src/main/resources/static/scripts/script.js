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

// セレクトボックスに検索機能を追加するライブラリ
$(document).ready(function() {
	
    $('.select').select2({
	
        // コンテナ幅のカスタマイズをサポートします。
        width: '300px',
	});
});

// 日報入力時に製作番号を選択した際、それに紐づく商品名・製作数を非同期で取得
$(function() {

	// 日報入力画面の製作番号セレクトボックスの値変更時
	$('#ajax-productionId').change(function() {
		
		// セレクトボックスのValueが「""」(選択して下さい)の場合、非同期通信は行わない。
		if ($('[name=productionId] option:selected').val()) {
		
			// urlのコントローラーにPOSTデータで、製作番号IDとトークンを渡す。
			$.ajax({
				url: '/report/register/ajax', 
				type: 'POST', 
				data: {
					id: $('[name=productionId] option:selected').val(), 
					_csrf: $('*[name=_csrf]').val()
				}, 
				dataType: 'json' // レスポンスをJsonデータとして受け取る。
			})
			
			// コントローラーから返された製作手配クラスのJsonデータから、商品名・製作数をそれぞれ取り出し、各フォームに入力する。
			.done(function(data) {
	
				$('#ajax-itemName').val(data.item.name);
				$('#ajax-lotQuantity').val(data.lotQuantity);
			})	
			
			// 「選択して下さい」を選ぶと、各入力欄に空白が入力される。
		} else {
			
			$('#ajax-itemName').val("");
			$('#ajax-lotQuantity').val("");
		}
	});
});