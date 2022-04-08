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