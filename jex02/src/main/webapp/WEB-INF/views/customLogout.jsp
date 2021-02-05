<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<!-- Bootstrap Core Css -->
<link href="/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- MetisMenu CSS -->
<link href="/resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="/resources/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom Font -->
<link href="/resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<%@include file="../views/includes/header.jsp" %>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Logout Page</h3>
					</div>
					<div class="panel-body">
						<form role="form" method='post' action="/customLogout">
							<fieldset>
								<a href="index.html" class="btn btn-lg btn-success btn-block">Logout</a>
							</fieldset>
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						</form>

					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="/resources/vendor/jquery/jquery.min.js"></script>
	<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="/resources/vendor/metisMenu/metisMenu.min.js"></script>
	<script src="/resources/dist/js/sb-admin-2.js"></script>
	<script>
	$(".btn-success").on("click",function(e){
		e.preventDefault();
		$("form").submit();
	});		
	</script>
	<c:if test="${param.logout!=null}">
	<script>
	$(document).ready(function(){
		alert("로그아웃하였습니다.");
	});
	</script>
	</c:if>
<%@ include file="../views/includes/footer.jsp"%>