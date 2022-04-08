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
        
        // 件数切替の値を15～75の15刻みにする
	    lengthMenu: [ 15, 30, 45, 60, 75 ],
	    
	    // 件数のデフォルトの値を15にする
	    displayLength: 15,  
        
        // スクロールバー
        scrollX: true,
        scrollY: 700, 
        
        // 状態を保存する機能をつける
    	stateSave: true,
        
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
        if(window.confirm('この社員情報を削除しますか？')) {
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