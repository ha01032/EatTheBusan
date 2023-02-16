<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그아웃</title>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
</head>
<body>
	<c:remove var="memberCode" scope="session"/>
	<c:set var="root" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
		alert("로그아웃 되었습니다.");
		Kakao.init('fd1cbb0f62181ab5ff1688b8db098e0c');
		Kakao.Auth.logout();
		location.href="${root}/";
	</script>
</body>
</html>