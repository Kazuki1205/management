jQuery(function($){
	
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


$(function() {
	
	$(".alert").fadeOut(3000);
});

$(function(){
	
    $("#form").on("click", function(){
	
        if(window.confirm('入力内容を確認して問題なければOKを押してください')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

$(function(){
	
    $("#form-delete").on("click", function(){
	
        if(window.confirm('この情報を削除しますか？')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

$(function(){
	
    $("#form-change").on("click", function(){
	
        if(window.confirm('入力したパスワードに変更しますか？')) {
	
            return true;
        } else {
	
            return false;
        }
    });
});

$(document).ready(function() {
	
    $('.select').select2({
	
        // コンテナ幅のカスタマイズをサポートします。
        width: '350px',
	
        // クリア可能な選択をサポートします。
        allowClear: true
	});
});