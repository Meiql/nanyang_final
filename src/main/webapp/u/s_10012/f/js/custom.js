function whinit(){
				//top   640/88
				var _height=getScreenWindwoHeight();
				var _width=getScreenWindwoWidth();
				$("html").css("font-size",(_width*0.15625)+"px");
				if ((_width*0.15625) > 100) {
					jQuery("html").css("font-size","100px");
				};
				if ((_width*0.15625) < 50) {
					jQuery("html").css("font-size","50px");
				};
				$(".main").css("height", _height + "px");
				$(".regain_all").css("height", _height +"px");
				$(".qrcode_all").css("height", _height +"px");
				//$(".shopcart_all").css("height", _height +"px");
				$(".record_all").css("height", _height +"px");


}
$(document).ready(function() {
	whinit();
	var Util = Util || {};
	Util.jTab = function(tab, pan, evt) {
	    $(pan).find('.jPanel').hide();
	    $(tab).find('.jTab:first').addClass('current').show();
	    $(pan).find('.jPanel:first').show();
	    $(tab).find('.jTab').bind(evt,
	    function() {
	        $(this).addClass('current').siblings('.jTab').removeClass('current');
	        var index = $(tab).find('.jTab').index(this);
	        $(pan).children().eq(index).show().siblings().hide();
	        var tool = $('#ToolTip');
	        if (typeof(tool[0]) != 'undefined') {
	            tool.remove()
	        };
	        return false
	    })
	};
	Util.jTab('#investTabs','#investPanel','click'); 
	$('.order-meut').bind('click',function(){
		$('.menu_all').toggle();
	});
});
window.onresize=function(){
	whinit();
}

// 底部导航
function initNavStyle(currentNav) {
	$('#footer a').removeClass("nav_on");
	switch (currentNav) {
		case 'lc'://社区
			$('#lc').addClass("nav_on");
			break;
		case 'jk'://类别
			$('#jk').addClass("nav_on");
			break;
		case 'cf'://购物车
			$('#cf').addClass("nav_on");
			break;
		case 'wd'://我的
			$('#wd').addClass("nav_on");
			break;
		default://默认商城
			$('#jx').addClass("nav_on");
			break;
	}
}

// 屏幕高度
function getScreenWindwoHeight(){
	var swh=0;
	if(window.innerHeight){
		swh=window.innerHeight;
	}else if(document.body&&document.body.clientHeight){
		swh=document.body.clientHeight;
	}
	if(document.documentElement&&document.documentElement.clientHeight){
		swh=document.documentElement.clientHeight;
	}
	return swh;
}
// 屏幕宽度
function getScreenWindwoWidth(){
	var swh=0;
	if(window.innerWidth){
		swh=window.innerWidth;
	}else if(document.body&&document.body.clientWidth){
		swh=document.body.clientWidth;
	}
	if(document.documentElement&&document.documentElement.clientWidth){
		swh=document.documentElement.clientWidth;
	}
	return swh;
}