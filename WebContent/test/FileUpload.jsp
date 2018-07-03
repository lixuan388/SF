<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
    
	<script type="text/javascript" src="/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
</head>
<body>    

<div>
	<form name="itemForm"  target="_self" id="itemForm" method="post"  action="/SF/FileUpload" enctype="multipart/form-data" >
   			<input name = "fileName" id="uploaderInput" accept="*" multiple="" type="file">
    </form>	
<input type="button" value="上传" onclick="UploadImage();">
</div>
<div id="msg">

</div>

<script type="text/javascript">

	function propertychange(id)
	{
		console.log('propertychange:'+id);
		UploadImage();
	}
	
	function UploadImage()
	{
		var form =$("#itemForm");
		form.ajaxSubmit({
			contentType:"multipart/form-data",
		     dataType: "html",
		     success: function(msg){
					var result=msg;
		     }
		 });
		getProgress();
	}
	function getProgress()
	{
		$.ajax({
	        url:"/SF/GetUploadProgress",
	        type:'get',
	        dataType:'Json',
	        success:function(data){
	        	if (data.type=='Temp')
	        	{
	        		$('#msg').html('正在上传：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='Loacl')
	        	{
	        		$('#msg').html('正在保存：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='success')
	        	{
	        		$('#msg').html('上传成功！');
	        	}
				if (data.type!='success' && data.type!='null')
				{
					setTimeout(getProgress,500);
				}
	        }
		});	
	}
	
	
	
</script>
</body>
</html>