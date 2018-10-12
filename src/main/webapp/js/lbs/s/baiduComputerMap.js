$(function(){
		newMap();  //进入页面创建地图
	});
	var map;
	function newMap(){
		//先创建地图
		map = new BMap.Map("container");  // 创建Map实例
		var point=new BMap.Point(116.331398,39.897445);
		map.centerAndZoom(point, 12);  // 初始化地图,设置中心点坐标和地图级别
		map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		getpoint(map);
	}
	
	//更换地图的中心点
	function initMap(){  
		var myCity = new BMap.LocalCity();
	    myCity.get(myFun);
	}
	function myFun(result){
		var cityName = result.name;
		map.setCenter(cityName);
	}
	function search(){
		var local = new BMap.LocalSearch(map, {
			renderOptions:{map: map}
		});
		var _address=$("#address").val();
		if(_address!=null&&_address!=""&&_address!=undefined){
			local.search(_address);
		}else{
			alert("搜索地址不能为空！")
		}
		
	}
	function getpoint(t){
		t.addEventListener("click",function(e){
			var pt=e.point;
			$("#weidu").val(pt.lat);
			$("#jingdu").val(pt.lng);
			var geoc = new BMap.Geocoder(); //逆地址解析
			geoc.getLocation(pt, function(rs){
				var addComp = rs.addressComponents;
				//将逆地址解析得到的城市放入search_city  
				//addComp.province：获取省  addComp.city：获取市 addComp.district：获取区  addComp.street：获取街道  addComp.streetNumber：获取门牌号
				$("#search_city").val(addComp.city);
			}); 
			var xinxi = map.getInfoWindow(); //获取地图上处于打开状态的信息窗的实例。当地图没有打开的信息窗口时，此方法返回null
			if(xinxi!=null){
				var content=xinxi.getContent();  //地图上显示信息窗口时，获取信息窗口中的内容
				$("#addressName").val($(content).find("td:first").next().text());  //获取地图上显示信息窗口中的地址，并放入地址栏
			}
			
		});
	}