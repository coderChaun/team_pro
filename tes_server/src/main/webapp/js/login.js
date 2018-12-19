$(function(){
	$("form #emp_no").val(getCookie("loginName"));
	$("form").submit(function(){
		return login();
		alert("1");
	});
});

/**
 * @returns {fuchun}
 */
function login(){
	alert("login");
	//获取页面中的数据
	var loginName=$("form #emp_no").val();
	var password=$("form #password").val();
	//获取记住密码
	var remember=$("form input[type=checkbox]").get(0).checked;
	//alert(remember);
	//alert(loginName +"   "+password);
	//把获取的数据异步提交给服务端Controller
	$.ajax({
		url:basePath+"oaUser/login/name/"+loginName+"/password/"+password,
		type:"get",		
		dataType:"json",
		success:function(result){
			if(result.status==1){
				//重定向？
				window.location.href="index.html";
				//判断记住账号是否勾选
				if(remember){
					//记住账号添加到cookie中
					addCookie("loginName",loginName,5);
				}
			}
			alert(result.message);
		},
		error:function(){
			alert("请求失败!!!");
		}		 
	});
	
	return false;
}