<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h1>All</h1>
<sec:authorize access="isAnonymous()">
	<a href="/customLogin">�α���</a>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
	<a href="/customLogout">�α׾ƿ�</a>
</sec:authorize>
</body>
</html>