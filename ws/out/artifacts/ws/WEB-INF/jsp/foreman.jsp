<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.gmail.valvol98.data.UnChangeData" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "ui" uri = "/WEB-INF/tld/custom.tld"%>

<%@ include file="includeLangSetting.jspf" %>

<html>
<head>
    <title><fmt:message key = "title.ForemanPage"/></title>
</head>
<body>

<%@ include file="includeLinkLanguage.jspf" %>

<c:if test="${UnChangeData.ROLE_FOREMAN.toString() eq loggedUser.getRole().toString()}">

    <ui:out userName = "${loggedUser.getName()}" userRole="${loggedUser.getRole()}"/>

    <%@ include file="general.jspf" %>

    <p style="color:#ff0000"><fmt:message key = "label.listOrders"/></p>

    <c:if test="${OrdersTable != null}">
        <table border="1">
            <tr>
                <th><fmt:message key = "ordersTable.shortNameOrder"/></th>
                <th><fmt:message key = "ordersTable.dateTimeEnterOrder"/></th>
                <th><fmt:message key = "ordersTable.statusOrder"/></th>
            </tr>
            <c:forEach var="order" items="${OrdersTable}">
                <tr><td>
                    <c:out value="${order.getDescription()}"/>
                </td><td>
                    <c:out value="${order.getDatetime()}"/>
                </td><td>
                    <c:if test="${order.getOrderStatus().size() == 0}">
                        <form id = "formOrderInWork" action="controller" method="post">
                            <input type = "hidden" name = "command" value = "changeOrderStatusByForeman">
                            <input type = "hidden" name = "orderId" value = "${order.getId()}">
                            <input type = "hidden" name = "type" value = "${UnChangeData.ORDER_IN_WORK}">
                            <a href="javascript:{}" onclick="document.getElementById('formOrderInWork').submit(); return false;"><fmt:message key = "ordersTable.takeOrderToWork"/></a>
                        </form>
                    </c:if>
                    <c:if test="${order.getOrderStatus().size() != 0}">
                        <c:if test="${order.isOrderComplited() == false}">
                            <form id = "formOrderCompleted" action="controller" method="post">
                                <input type = "hidden" name = "command" value = "changeOrderStatusByForeman">
                                <input type = "hidden" name = "orderId" value = "${order.getId()}">
                                <input type = "hidden" name = "type" value = "${UnChangeData.ORDER_COMPLETED}">
                                <a href="javascript:{}" onclick="document.getElementById('formOrderCompleted').submit(); return false;"><fmt:message key = "ordersTable.finishWorkOnOrder"/></a>
                            </form>
                        </c:if>
                        <c:if test="${order.isOrderComplited() != false}">
                            <c:out value="${UnChangeData.ORDER_COMPLETED}"/>
                        </c:if>
                    </c:if>
                </td></tr>
            </c:forEach>
        </table>
    </c:if>
</c:if>
</body>
</html>