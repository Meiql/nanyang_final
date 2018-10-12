// 1.填写用户信息提交、2.车主信息——填写车牌号信息提交、3.驾驶人用户按钮、 4.发送验证码、5.手机验证码验证
function registerSubmit(_formId) {
	$.ajax({
		url : _ctx+"/f/mp/122/"+host+"/"+siteId+"/ajax/registerSubmit",
		data : $("#" + _formId).serialize(),
		type:"post",
		success : function(data) {
			if (data.status == "success") {
				$(".message122").html("");
				if (_formId == "form1") {
					$(".steps122").hide();
					$("#steps_122-2").show();
					$("#yzmsjhm").val($("#sjhm").val())
					return;
				} else if(_formId == "form3"){
					alert("注册成功，正在跳转...");
					return;
				}
				$(".steps122").hide();
                $("#steps_122-3").show();
			} else {
				if (data.data != null && data.data.status == "error") {
					var i = 0;
					for ( var s in data.data) {
						if (s == "status")
							continue;
						++i;
						if (_formId == "form0-2") {
							$("#errorMesssage122").html(data.data[s]);
							return;
						}
						$("#" + s).parent().find("span").html(
								data.data[s])
					}
					if (i == 0) {
						alert(data.message)
					}
					return;
				}
				alert(data.message)
			}
		}
	})
}

// 发送验证码
function getIdentifyingcode() {
	$.ajax({
		url : _ctx+"/f/mp/122/"+host+"/"+siteId+"/ajax/registerSubmit",
		data : {
			submitType : "4"
		},
		type:"post",
		success : function(data) {
			if (data.status == "success" && data.data != null && data.data.status == "success") {
				$(".returnMessage122").html(data.data.returnMessage122);
			} else {
				alert(data.message)
			}
		}
	})
}
// 上一步
function actPrev(_showid, _hideid) {
	$("#" + _showid).show();
	$("#" + _hideid).hide();
	$("#errorMesssage122").html("")
}