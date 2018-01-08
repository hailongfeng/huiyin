<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("contextPath",path);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树形菜单演示</title>
		<link href="${requestScope.contextPath}/static/assets/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/font-awesome.min.css" />
		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/colorbox.css" />
		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />

		<!-- ace styles -->

		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/ace.min.css" />
		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/ace-rtl.min.css" />
		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/ace-skins.min.css" />
		<link rel="stylesheet" href="${requestScope.contextPath}/static/assets/css/bootstrap-treeview.min.css" />
	<style type="text/css">
	@font-face {
	font-family: 'Glyphicons Halflings';
	src: url('${requestScope.contextPath}/static/assets/font/glyphicons-halflings-regular.eot');
	src: url('${requestScope.contextPath}/static/assets/font/glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'), 
	url('${requestScope.contextPath}/static/assets/font/glyphicons-halflings-regular.woff') format('woff'), 
	url('${requestScope.contextPath}/static/assets/font/glyphicons-halflings-regular.ttf') format('truetype'), 
	url('${requestScope.contextPath}/static/assets/font/glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
	}
	</style>
</head>
<body>
<div class="container">
  <form role="form" method="post" action="${requestScope.contextPath}/menu/add">
   <h4>${result }</h4>
   <input type="text" name="name" class="form-control" >
  <input type="text" id="txt_departmentname" name="txt_departmentname" class="form-control" value="" placeholder="分类名称">
  <input type="hidden" name="pid" id="pid">
  <input type="submit" value="提交" class="form-control" >
  <!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
		<div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
            </div>
            <div class="modal-body">
				<div id="tree"></div>
			</div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
  </form>
</div>

<!-- body内容在上面  -->
		<!-- basic scripts -->

		<script src="${requestScope.contextPath}/static/assets/js/jquery-2.0.3.min.js"></script>
		<script src="${requestScope.contextPath}/static/assets/js/bootstrap.min.js"></script>
		<script src="${requestScope.contextPath}/static/assets/js/typeahead-bs2.min.js"></script>

		<!-- page specific plugin scripts -->

		<script src="${requestScope.contextPath}/static/assets/js/jquery.colorbox-min.js"></script>

		<!-- ace scripts -->
		<script src="${requestScope.contextPath}/static/assets/js/ace-extra.min.js"></script>
		<script src="${requestScope.contextPath}/static/assets/js/bootstrap-treeview.min.js"></script>
		<script src="${requestScope.contextPath}/static/assets/js/ace-elements.min.js"></script>
		<script src="${requestScope.contextPath}/static/assets/js/ace.min.js"></script>
		
		<script type="text/javascript" language="javascript">
		var tree =${menu};
		//var tree =[{"id":1,"nodes":[{"id":2,"parentId":1,"text":"郑州市"},{"id":3,"nodes":[{"id":4,"parentId":3,"text":"长葛县"},{"id":5,"parentId":3,"text":"禹州县"}],"parentId":1,"text":"许昌市"}],"parentId":0,"text":"河南省"},{"id":6,"parentId":0,"text":"北京"}];

		function getTree() {
			// Some logic to retrieve, or generate tree structure
			return tree;
		}

		$(function(){

			$("#txt_departmentname").click(function() {
				var options = {  
					bootstrap2 : false,  
					showTags : true,  
					levels : 5,  
					showCheckbox : true,  
					checkedIcon : "glyphicon glyphicon-check",  
					data : getTree(),  
					onNodeSelected : function(event, data) {  
						$("#txt_departmentname").val(data.text);
						$("#pid").val(data.id);
						$('#myModal').modal('hide')
						
					} 
				};
				$('#tree').treeview(options);
				$('#myModal').modal('show')
			 }); 
		});
			
		</script>
</body>
</html>