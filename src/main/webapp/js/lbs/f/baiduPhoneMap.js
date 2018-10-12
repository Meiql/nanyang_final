var winWidth = 0;
var winHeight = 0;
var map=null;
var infoWin=null;
var markers = []; //标记点集合
function findDimensions(){ 
	//函数：获取尺寸
	//获取窗口宽度
	if (window.innerWidth)
		winWidth = window.innerWidth;
	else if ((document.body) && (document.body.clientWidth))
		winWidth = document.body.clientWidth;
			//获取窗口高度
	if (window.innerHeight)
		winHeight = window.innerHeight;
	else if ((document.body) && (document.body.clientHeight))
		winHeight = document.body.clientHeight;
		//通过深入Document内部对body进行检测，获取窗口大小
	if (document.documentElement  && document.documentElement.clientHeight && document.documentElement.clientWidth){
		winHeight = document.documentElement.clientHeight;
		winWidth = document.documentElement.clientWidth;
	}
		 
}
function init(list) {
	try{
		//适应手机屏幕，地图画到同样大的div中
		findDimensions();
		jQuery("#container").css('height',winHeight+"px");
	}catch(e){
		
	}
    buildMap();
    if(list!=null&&list!=""&&list!=undefined){
    	var _length = list.length;
    	for(var i = 0;i < _length; i++) {
    		var point = new BMap.Point(list[i].lng,list[i].lat);
    		var content='<div ><p>地址:' +
            list[i].poiaddress+ '</p><p> 电话:'+list[i].tel+'</p></div>';
    		addMarker(point,content,list[i].name);
            /*(function(i){
                	marker = new Marker({
                    position: new qq.maps.LatLng(list[i].lat,list[i].lng),
                    map: map
                });
                qq.maps.event.addListener(marker, 'click', function() {
                	map.setCenter(new qq.maps.LatLng(list[i].lat,list[i].lng));
                	infoWin.close();
                    infoWin.open();
                    infoWin.setContent('<div ><p>地址:' +
                            list[i].poiaddress+ '</p><p> 电话:'+list[i].tel+'</p></div>'+'<div style="text-align:right;"><input type="button" value="去这里" style="width:45px;background:#46a3ff;color:#ffffff;border:1px;" onclick=daohang(\''+list[i].poiaddress+'\',\''+list[i].lat+'\',\''+list[i].lng+'\')></div>');
                    infoWin.setPosition(new qq.maps.LatLng(list[i].lat,list[i].lng));
                });
            })(i);*/
            
        }
    	map.centerAndZoom(new BMap.Point(list[_length-1].lng, list[_length-1].lat), 11);
    	var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markers});  //创建聚合
    }else{
    	alert("暂未提供相关位置");
    }
}

function addMarker(point,content,title){
	//创建标记点和标记点聚合
	var marker = new BMap.Marker(point);
	buildInfoWindow(marker, content, title)
	map.addOverlay(marker);
	markers.push(marker);
}

function buildInfoWindow(marker,content,_title){
	var searchInfoWindow = new BMapLib.SearchInfoWindow(map,content,{
		title: _title,   //设置信息框标题
		width:100,  //设置宽
		hight:100,  //高
		enableSendToPhone:false,//设置允许信息窗发送短息
		//panel:"panel",  //如果开启检索功能，这个是设置显示检索结果的id
		//enableAutoPan : true,     //如果让标记点能移动则需要开启自动平移
		searchTypes   :[     //信息框开启的功能
						//BMAPLIB_TAB_SEARCH,   //周边检索
						BMAPLIB_TAB_TO_HERE,  //到这里去
						BMAPLIB_TAB_FROM_HERE //从这里出发
					]
	});
	searchInfoWindow.enableAutoPan();
	marker.addEventListener("click", function(e){
	    searchInfoWindow.open(marker);
    })
}

function buildMap(){
	// 创建一个地图
	  map = new BMap.Map("container");
	  map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
	  map.enableScrollWheelZoom(); //启动滚轮放大缩小地图
	  // 创建平移缩放控件
	  var navigationControl = new BMap.NavigationControl({
	    // 靠左上角位置
	    anchor: BMAP_ANCHOR_TOP_LEFT,
	    // LARGE类型
	    type: BMAP_NAVIGATION_CONTROL_LARGE,
	    // 启用显示定位
	    enableGeolocation: true
	  });
	  map.addControl(navigationControl);
	  // 添加定位控件
	  var geolocationControl = new BMap.GeolocationControl({
		  // 靠左上角位置
		    anchor: BMAP_ANCHOR_BOTTOM_RIGHT,
		    // LARGE类型
		    type: BMAP_NAVIGATION_CONTROL_LARGE,
		    // 启用显示定位
		    enableGeolocation: true
	  });
	  geolocationControl.addEventListener("locationSuccess", function(e){
	    // 定位成功事件
	    var address = '';
	    address += e.addressComponent.province;
	    address += e.addressComponent.city;
	    address += e.addressComponent.district;
	    address += e.addressComponent.street;
	    address += e.addressComponent.streetNumber;
	    alert("当前定位地址为：" + address);
	  });
	  geolocationControl.addEventListener("locationError",function(e){
	    // 定位失败事件
	    alert(e.message);
	  });
	  map.addControl(geolocationControl);
	  
	  //创建比例尺控件
	  var scaleControl = new BMap.ScaleControl({
		  anchor: BMAP_ANCHOR_BOTTOM_LEFT,
	  });
	  scaleControl.setUnit(BMAP_UNIT_METRIC);
	  map.addControl(scaleControl);
	  
	  //创建地图类型切换控件
	  var mapTypeControl = new BMap.MapTypeControl({
		  anchor: BMAP_ANCHOR_TOP_RIGHT,
		  type: BMAP_MAPTYPE_CONTROL_HORIZONTAL,
	  });
	  map.addControl(mapTypeControl);
}

