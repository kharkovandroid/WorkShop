<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8"%>
<%@ page session="true" %>
<% request.setCharacterEncoding("UTF-8");%>

<%@ include file="/WEB-INF/jsp/includeLangSetting.jspf" %>

<!--
    онлайн конвертер из русского в юникод
    https://www.online-toolz.com/langs/ru/tool-ru-text-unicode-entities-convertor.html
-->
<html>
<head>
    <title><fmt:message key="title.indexPage" /></title>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/includeLinkLanguage.jspf" %>
    <form action="controller" method="post">
      <input type="hidden" name="command" value="login"><br>
        <fmt:message key="label.userLogin" />
        <br>
      <input type="text" name="login" value="mehlogin" autocomplete="off"><br>
        <fmt:message key="label.userPassword" />
        <br>
      <input type="password" name="password" value="meh" autocomplete="off"><br>
      <input type="submit" value='<fmt:message key="button.Enter"/>'>
      <input type="reset" value='<fmt:message key="button.Cancel"/>'>
    </form>
    <c:if test="${(isUserExists != null || isUserExists.toString() eq \"no\")
                        && (isUserExists ne \"-1\")}">
        <p style="color:#FF0000; font-size:10pt; face:serif">
            <fmt:message key='massage.wrongLoginOrPassword'/>
        </p>
    </c:if>

</body>
</html>