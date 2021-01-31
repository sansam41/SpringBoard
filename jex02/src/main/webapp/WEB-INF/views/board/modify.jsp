<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="../includes/header.jsp" %>

<style>
.uploadResult{
	
	width:100%;
	background-color:gray;
}
.uploadResult ul{
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}
.uploadResult ul li {
	list-style: none;
	padding: 100px;
	align-content:center;
	text-align:center;
}
.uploadResult ul li img{
	width: 100px;
	}
.uploadResult ul li span{
	color:white;
}	
.bigPictureWrapper{
	position:absolute;
	display: none;
	justify-content: center;
	align-items:center;
	top:0%;
	width:100%;
	height:100%;
	background-color:gray;
	z-index: 100;
	background:rgba(255,255,255,0.5);
	}
.bigPicture{
	position: relative;
	display: flex;
	justify-content:center;
	align-items:center;
	}
.bigPicture img {
	width:600px;
	}
</style>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->


<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Register</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
			<form role="form" action="/board/modify" method="post">
				<input type='hidden' id='pageNum' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>	
				<input type='hidden' id='amount' name='amount' value='<c:out value="${cri.amount }"/>'>	
				<input type='hidden' id='type' name='type' value='<c:out value="${cri.type }"/>'>	
				<input type='hidden' id='keyword' name='keyword' value='<c:out value="${cri.keyword }"/>'>	
				<div class="form-group">
				<label>Bno</label>
				<input class="form-control" name='bno' value='<c:out value="${board.bno}"/>' readonly="readonly">
				</div>
				<div class="form-group">
				<label>Title</label>
				<input class="form-control" name='title' value='<c:out value="${board.title}"/>'>
				</div>
				<div class="form-group">
				<label>Text-area</label>
				<textarea class="form-control" rows="3" name='content'>
				<c:out value="${board.content}"/>
				</textarea>
				</div>
				<div>
				<label>Writer</label>
				<input class="form-control" name='writer' value='<c:out value="${board.writer}"/>' readonly="readonly">
				</div>
				<button type="submit" data-oper='modify' class="btn btn-default">
				Modify
				</button>
				<button type="submit" data-oper='remove' class="btn btn-danger">
				Remove
				</button>				
				<button type="submit" data-oper='list' class="btn btn-info">
				List
				</button>
			</form>												
			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Files</div>
			<div class="panel -body">
				<div class="form-group uploadDiv">
					<input type='file' name='uploadFile' multiple="multiple">
				</div>
				<div class='uploadResult'>
					<ul>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	
	
	var formObj=$("form");
	
	$('button').on("click",function(e){
		e.preventDefault();
		var operation=$(this).data("oper");
		console.log(operation);
		
		if(operation==='remove'){
			formObj.attr("action","/board/remove");
		}
		else if(operation==='list'){
			formObj.attr("action","/board/list").attr("method","get");
			
			var pageNumTag=$("input[name='pageNum']").clone();
			var amountTag=$("input[name='amount']").clone();
			var keywordTag=$("input[name='keyword']").clone();
			var typeTag=$("input[name='type']").clone();
			formObj.empty();
			formObj.append(pageNumTag);
			formObj.append(amountTag);
			formObj.append(keywordTag);
			formObj.append(typeTag);
		}else if(operation==='modify'){
				console.log("submit clicked");
					
				var str="";
				
				$(".uploadResult ul li").each(function(i,obj){
					var jobj=$(obj);
					console.dir(jobj);
						
					str+="<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
					str+="<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
					str+="<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
					str+="<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
						
				});
				formObj.append(str).submit();
			}
		formObj.submit();
		});

	var bno='<c:out value="${board.bno}"/>';
	
	$.getJSON("/board/getAttachList",{bno:bno},function(arr){
		console.log(arr);
		var str="";
		$(arr).each(function(i,attach){
			if(attach.fileType){
				var fileCallPath=encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
				str+="<li data-path='"+attach.uploadPath;
				str+="' data-uuid='"+attach.uuid;
				str+="' data-filename='"+attach.fileName;
				str+="' data-type='"+attach.fileType+"'><div>";
				str+="<span>"+attach.fileName+"</span>";
				str+="<button type='button' data-file=\'"+fileCallPath+"\'";
				str+=" data-type='file' class='btn btn-warning btn-circle'>";
				str+="<i class='fa fa-times'></i></button><br>";
				str+="<img src='/display?fileName="+fileCallPath+"'>";
				str+="</div></li>";
			}else{
				var fileCallPath=encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
				str+="<li data-path='"+attach.uploadPath;
				str+="' data-uuid='"+attach.uuid;
				str+="' data-filename='"+attach.fileName;
				str+="' data-type='false'><div>";
				str+="<span>"+attach.fileName+"</span>";
				str+="<button type='button' data-file=\'"+fileCallPath+"\'";
				str+=" data-type='file' class='btn btn-warning btn-circle'>";
				str+="<i class='fa fa-times'></i></button><br>";
				str+="<img src='/resources/img/attach.png'>";
				str+="</div></li>";
			}
		});
		$(".uploadResult ul").html(str);
	});
	$(".uploadResult").on("click","button",function(e){
		console.log("delete file");
		
		if(confirm("Remove this file? ")){
		var targetLi=$(this).closest("li");
		targetLi.remove();
		}
		/*var targetFile=$(this).data("file");
		var type=$(this).data("type");
		var targetLi=$(this).closest("li");
		
		$.ajax({
			url:'/deleteFile',
			data:{fileName: targetFile, type:type},
			dataType:'text',
			type:'POST',
				success: function(result){
					alert(result);
					targetLi.remove();
				}
		});*/
	});
	
	
	var regex=new RegExp("(.*?)\.(exe|sh|zip|alz)$"); //불가능한 파일명 및 확장자
	var maxSize=5242880;	//5MB
	
	//파일명 및 파일 사이즈 검사 
	function checkExtension(fileName,fileSize){
		
		if(fileSize>=maxSize){
			alert("파일 사이즈 초과");
			return false;
		}
		
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		return true;
	}
	
	//file 업로드
	$("input[type='file']").change(function(e){
		var formData=new FormData();
		var inputFile=$("input[name='uploadFile']");
		var files=inputFile[0].files;
		for(var i =0;i<files.length;i++){
			if(!checkExtension(files[i].name,files[i].size)){
				return false;
			}
			formData.append("uploadFile",files[i]);
		}
	$.ajax({
		url: '/uploadAjaxAction',
		processData:false,
		contentType:false,
		data:formData,
		type:'POST',
		dataType:'json',
			success: function(result){
				console.log(result);
				showUploadResult(result);
			}
	});
	});
	
	
	//upload 파일 섬네일 생성 및 출력
	function showUploadResult(uploadResultArr){
		if(!uploadResultArr||uploadResultArr.length==0){return;}
		var uploadUL=$(".uploadResult ul");
		var str="";
			$(uploadResultArr).each(function(i,obj){
				
				if(!obj.image){
					var fileCallPath=encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
					str+="<li data-path='"+obj.uploadPath;
					str+="' data-uuid='"+obj.uuid;
					str+="' data-filename='"+obj.fileName;
					str+="' data-type='"+obj.image+"'><div>";
					str+="<span>"+obj.fileName+"</span>";
					str+="<button type='button' data-file=\'"+fileCallPath+"\'";
					str+=" data-type='file' class='btn btn-warning btn-circle'>";
					str+="<i class='fa fa-times'></i></button><br>";
					str+="<img src='/resources/img/attach.png'>";
					str+="</div></li>";
				}
				else{
				//str+="<li>"+obj.fileName+"</li>";
				var fileCallPath=encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
				var originPath=obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName;
				originPath=originPath.replace(new RegExp(/\\/g),"/");
				str+="<li data-path='"+obj.uploadPath;
				str+="' data-uuid='"+obj.uuid;
				str+="' data-filename='"+obj.fileName;
				str+="' data-type='"+obj.image+"'><div>";
				str+="<span>"+obj.fileName+"</span>";
				str+="<button type='button' data-file=\'"+fileCallPath+"\'";
				str+=" data-type='image' class='btn btn-warning btn-circle'>";
				str+="<i class='fa fa-times'></i></button><br>";
				str+="<img src='/display?fileName="+fileCallPath+"'>";
				str+="</div></li>";
				}
				});
		uploadUL.append(str);
		};
});
</script>
<%@ include file="../includes/footer.jsp"%>