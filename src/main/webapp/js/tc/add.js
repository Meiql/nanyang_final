//jQuery页面加载
jQuery(document).ready(function(){
	//当公司选项发生改变时
//	selectListener("company",function(data){
		jQuery("#company").change(function(){
		clearStatType();
	});
	
	//添加统筹信息
	jQuery("#add_statChip").click(function(){
		statcount++;
		//生成一个copy的副本对象
		var t=jQuery("#pStapTemp").clone();
		jQuery(t).attr("id",statcount+"pStapTemp");
		jQuery(t).find("#stapTemp").attr("id",statcount+"allDiv");
		jQuery(t).find("#checkbox").val(statcount);
		jQuery(t).find("#checkbox").attr("id",statcount+"checkbox");
		jQuery(t).find("#statType").change(function (){
			return changeValidate(jQuery(t).find("#statType"));
		});
		jQuery(t).find("#statType").attr("id",statcount+"statType");
		jQuery(t).find("#DeliverDate").focus(function(){
			WdatePicker({dateFmt:'yyyy-MM', maxDate:'#F{$dp.$D(\'stopProtectMonth\')}'});
		});
		jQuery(t).find("#ChargedDate").focus(function(){
			WdatePicker({minDate:"#F{$dp.$D(\'"+statcount+"DeliverDate\')}", dateFmt:'yyyy-MM'});
		});
		jQuery(t).find("#DeliverDate").attr("id",statcount+"DeliverDate");
		jQuery(t).find("#ChargedDate").attr("id",statcount+"ChargedDate");
		jQuery(t).find("#base").attr("id",statcount+"base");
		jQuery(t).find("#InsurancePersonal").attr("id",statcount+"InsurancePersonal");
		jQuery(t).find("#InsuranceCompany").attr("id",statcount+"InsuranceCompany");
		jQuery("#validForm").append(t);
	});
	//删除统筹信息
	jQuery("#delete_statChip").click(function(){
		jQuery.each(jQuery(":checkbox"), function(i,n){
			if(!n.checked)
				return true;
			if(n.value=="")
				return true;
			if(jQuery(":checkbox").length==2){
				layer.alert("只剩下一个统筹信息不许删除！");
				return false;
			}
			jQuery("#"+n.value+"allDiv").empty();
		});
	});
	//others
	 //监听提交
//	selectListener("company",function(data){
//		alert(data.value());
//	});
});
//清空保险类型
function clearStatType(){
	jQuery.each(jQuery(":checkbox"), function(i,n){
		if(n.value=="")
			return true;
		var tempSel = document.getElementById((n.value)+"statType");
		tempSel.options.selectedIndex=0;
		cleanOneStat(n.value)
	});
}
//下拉框选择时的验证
function changeValidate(selcon){
	var tbefore = jQuery(selcon).parent().find(":checkbox").val();
	cleanOneStat(tbefore);
	var arrayStat = "@";
	//遍历所有的统筹类型下拉框
	jQuery.each(jQuery("select[name='statType']"), function(i,n){
		var selTempVal = jQuery(n).val();
		if(selTempVal==""){
			return true;
		}
		if(arrayStat.indexOf("["+selTempVal+"]")>0){
			layer.alert("此统筹类型已经选择过！");
			selcon.options.selectedIndex=0;
			cleanOneStat(tbefore)
			return false;
		}else{
			arrayStat = arrayStat + "[" + selTempVal + "]";
		}
	});
}
function cleanOneStat(tbefore){
	jQuery("#"+tbefore+"DeliverDate").val("");
	jQuery("#"+tbefore+"ChargedDate").val("");
	jQuery("#"+tbefore+"base").val("");
	jQuery("#"+tbefore+"InsurancePersonal").val("");
	jQuery("#"+tbefore+"InsuranceCompany").val("");
}
//用jQuery给交费基数注入浮点型的验证事件
function validateDouble(con){
	jQuery(con).blur(function(){
		var oValueA = jQuery(con).val();
		reg = /^[0-9]+([.]\d{1,2})?$/;
		if(oValueA!=""){
			if(!reg.test(oValueA)){
				layer.alert("数值数据格式不正确");
				jQuery(con).val("");
			}else{
			}
		 }
	});
}

//良四舍五入的方法
function sswr(sum){
	return Math.round((Math.floor(sum*1000)/10))/100;
}
//清除一个保险的相应标准信息
function clearOtherData(idBefore){
	jQuery("#"+idBefore+"iStapValidate").val("");
	jQuery("#"+idBefore+"DeliverDate").val("");
	jQuery("#"+idBefore+"ChargedDate").val("");
	jQuery("#"+idBefore+"person").val("");
	jQuery("#"+idBefore+"perNum").val("");
	jQuery("#"+idBefore+"base").val("");
	jQuery("#"+idBefore+"InsurancePersonal").val("");
	jQuery("#"+idBefore+"InsuranceCompany").val("");
}
function valited() {
	var standValidae = true;
 	if(jQuery("#userId").val()==""){
 		layer.alert("请选择用户");
 		standValidae=false;
 		jQuery("#userId").focus();
 		return false;
 	}
 	if(jQuery("#gongjijinaccount").val()==""){
 		layer.alert("请填写公积金账号");
 		standValidae=false;
 		jQuery("#gongjijinaccount").focus();
 		return false;
 	}
 	if(jQuery("#baoxianaccount").val()==""){
 		layer.alert("请填写社保账号");
 		standValidae=false;
 		jQuery("#baoxianaccount").focus();
 		return false;
 	}
 	//公司非空验证
 	if(jQuery("#company").val()==""){
 		layer.alert("请选择公司名称！");
 		jQuery("#company").focus();
 		standValidae=false;
 		return false;
 	}
	//公司非空验证
 	if(jQuery("#tcjiaonadi").val()==""){
 		layer.alert("请选择统筹缴纳地！");
 		jQuery("#tcjiaonadi").focus();
 		standValidae=false;
 		return false;
 	}
 	//保险信息验证
 	
 	jQuery.each(jQuery(":checkbox"), function(i,n){
 		if(n.value=="")
 			return true;
 		var before = n.value;
 		if(jQuery("#"+before+"statType").val()==""){
 			standValidae=false;
 			layer.alert("请选择选择统筹类型！");
 			return false;
 		}
 		//起始交费月份非空验证
 		if(jQuery("#"+before+"DeliverDate").val()==""){
 			layer.alert("请选择缴费开始时间！");
 			jQuery("#"+before+"DeliverDate").focus();
 			standValidae=false;
 			return false;
 		}
 		//交费基数非空验证
 		if(jQuery("#"+before+"base").val()==""){
 			layer.alert("请填写交费基数！");
 			jQuery("#"+before+"base").focus();
 			standValidae=false;
 			return false;
 		}
 		//个人缴纳非空验证
 		if(jQuery("#"+before+"InsurancePersonal").val()==""){
 			layer.alert("请填写个人缴纳！");
 			jQuery("#"+before+"InsurancePersonal").focus();
 			standValidae=false;
 			return false;
 		}
 		//公司缴纳非空验证
 		if(jQuery("#"+before+"InsuranceCompany").val()==""){
 			layer.alert("请填公司缴纳！");
 			jQuery("#"+before+"InsuranceCompany").focus();
 			standValidae=false;
 			return false;
 		}
 	});
	if(jQuery("#gongjijinaccount").val()!=null){
		debugger
		var typeStr = "";
	 	jQuery.each(jQuery(":checkbox"), function(i,n){
	 		if(n.value=="")
	 			return true;
	 		var before = n.value;
	 		typeStr+=jQuery("#"+before+"statType").val()+",";
	 	});
	 	if(typeStr.indexOf("N000007")<0){
	 		standValidae=false;
 			layer.alert("请选择选择公积金类型！");
 			return false;
	 		}
 	}
 	if(standValidae){
 		return true;
 	}
 };