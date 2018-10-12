//绑定
function bangding(_form) {
	if(_form=="bindVehicleForm"){
		var abbreviation=$("#hm").val();
		$("#hphm").val(abbreviation+$("#hp").val())
		if($("#hp").val()==""){
			layer.msg("请输入车牌号码！", {
				shade : 0.1,
				icon:0,
				shadeClose : true,
				time:1500
			});
			return;
		}
	}else if(_form=="bindDrvForm"){
		if($("#dabh").val()==""){
			layer.msg("请输入驾驶证档案编号！", {
				shade : 0.1,
				icon:0,
				shadeClose : true,
				time:1500
			});
			return;
		}
	}
	$.ajax({
		url : _ctx+"/f/mp/122/"+host+"/"+siteId+"/ajax/bang122",
		data : $("#" + _form).serialize(),
		type:"post",
		async:false,
		success : function(data) {
			if (data.status == "success") {
				if(_form=="bindVehicleForm"){
					sessionStorage.setItem("hphm", $("#hphm").val());
                    sessionStorage.setItem("hpzl", $("#hpzl").val());
				}
				layer.msg(data.message, {
					shade : 0.1,
					icon:1,
					shadeClose : true,
					time:1500,
					shadeClose : false,
					end:function(){
						if(returnUrl=="/vehssurisQuery"){
							returnUrl=returnUrl+"?hphm="+$("#hphm").val()+"&hpzl="+$("#hpzl").val();
						}
						location.href=_ctx+"/f/mp/122/"+host+"/"+siteId+returnUrl;
					}
				});
			} else {
				layer.msg(data.message, {
					shade : 0.1,
					icon:2,
					shadeClose : true,
					shadeClose : false,
					time:1500
				});
			}
		}
	})
}