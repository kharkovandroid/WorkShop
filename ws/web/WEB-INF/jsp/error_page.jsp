<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/includeLangSetting.jspf" %>

<html>
<head>
	<title><fmt:message key = "title.ErrorPage"/></title>
</head>
<body>
	<h2>
		<fmt:message key = "label.informationAboutError"/>
	</h2>
	<hr>
	<c:if test="${not empty errorSessionMessage}">
		<h3><fmt:message key = "label.errorDescription"/></h3>
		${errorSessionMessage}</h3>
	</c:if>
	<%--<hr>
	<c:if test="${not empty errorSessionCause}">
		<h3><fmt:message key = "label.stackTrace"/></h3>
		<c:forEach var="stackTraceElement" items="${errorSessionCause}">
			${stackTraceElement}
		</c:forEach>
	</c:if>
	--%>
</body>
</html>