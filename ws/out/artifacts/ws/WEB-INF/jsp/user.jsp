<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.gmail.valvol98.data.UnChangeData" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "ui" uri = "/WEB-INF/tld/custom.tld"%>

<%@ include file="includeLangSetting.jspf" %>

<html>
<head>
    <title><fmt:message key = "title.UserPage"/></title>
</head>
<body>

    <%@ include file="includeLinkLanguage.jspf" %>

    <c:if test="${UnChangeData.ROLE_USER.toString() eq loggedUser.getRole().toString()}">

        <ui:out userName = "${loggedUser.getName()}" userRole="${loggedUser.getRole()}"/>

        <%@ include file="general.jspf" %>

        <p style="color:#ff0000"><fmt:message key = "label.newOrderToRepair"/></p>
        <form action = "controller" method = "post">
            <input type = "hidden" name = "command" value = "addOrder">
            <fmt:message key = "label.shortOrderDescription"/><br>
            <input type = "text" name = "description"><br>
            <input type = "submit" value = <fmt:message key = "button.addUserPage"/>>
        </form>
        <c:if test="${isDescriptionExists != null && isDescriptionExists eq \"no\"}">
            <p style="color:#FF0000">
                <fmt:message key = "label.shortOrderDescriptionDoesNotExist"/>
            </p>
        </c:if>

        <hr>
        <p style="color:#ff0000"><fmt:message key = "label.accountInformation"/></p>
        <c:if test="${AcoountsTable != null}">
            <table border='1'>
                <tr>
                    <th><fmt:message key = "acoountsTable.incomePayment"/></th>
                    <th><fmt:message key = "acoountsTable.datePayment"/></th>
                </tr>
                <c:set var="indexRow" value="1"/>
                <c:forEach var="account" items="${AcoountsTable}">
                    <tr><td>
                        <c:out value="${account.getPayment()}"/>
                    </td><td>
                        <c:out value="${account.getDatetime()}"/>
                    </td></tr>
                </c:forEach>
            </table>

            <c:forEach var = "row" items = "${rowList}" varStatus="localIndex">
                <c:url value="controller?command=nextAccountPage" var="linkToNextAccountPage">
                    <c:param name="startRow" value="${UnChangeData.NUMBER_ACCOUNTS_PER_PAGE * localIndex.index}"/>
                </c:url>
                <a href="${linkToNextAccountPage}"><c:out value="${localIndex.index + 1}"/></a>
                &nbsp &nbsp &nbsp
            </c:forEach>
            <br>
            <fmt:message key = "label.generalSummaIncomingPayment"/> &nbsp &nbsp &nbsp
            <c:out value="${SumAccount}"/>
            <br>
            <fmt:message key = "label.generalSummaPaid"/> &nbsp &nbsp &nbsp
            <c:out value="${SumReservePaid}"/>
            <br>
            <fmt:message key = "label.generalSummaRest"/> &nbsp &nbsp &nbsp
            <c:out value="${SumAccountMinusSumReservePaid}"/>
        </c:if>

        <hr>
        <p style="color:#ff0000"><fmt:message key = "label.orderInformation"/></p>
        <c:if test="${OrdersTable != null}">
            <table border='1'>
                <tr>
                    <th><fmt:message key = "ordersTable.shortDescriptionOrder"/></th>
                    <th><fmt:message key = "ordersTable.Price"/></th>
                    <th><fmt:message key = "ordersTable.dateTimeIncomeOrder"/></th>
                    <th><fmt:message key = "ordersTable.statusOrderUserPage"/></th>
                    <th><fmt:message key = "ordersTable.comment"/></th>
                </tr>
                <c:forEach var="order" items="${OrdersTable}">
                    <tr><td>
                        <c:out value="${order.getDescription()}"/>
                    </td><td>
                        <c:out value="${order.getCost()}"/>
                    </td><td>
                        <c:out value="${order.getDatetime()}"/>
                    </td><td>
                        <c:if test="${order.getOrderStatus().size() != 0}">
                            <c:forEach var="os" items="${order.getOrderStatus()}">
                                <c:out value="${os.getStatusType()}"/>
                                <br/>
                                <c:out value="${os.getDatetime()}"/>
                                <br/>
                                <br/>
                            </c:forEach>
                        </c:if>
                        <c:if test="${!(order.getOrderStatus().size() != 0)}">
                            <c:if test="${order.getManagerId() == 0 && order.getCost() == 0}">
                                <c:out
                                        value="${UnChangeData.MESSAGE_ORDER_STATUS_NOT_SUMBIT_COST_BY_MANAGER}"/>
                            </c:if>
                            <c:if test="${!(order.getManagerId() == 0 && order.getCost() == 0)}">
                                <c:out
                                        value="${UnChangeData.MESSAGE_ORDER_STATUS_NOT_SUBMIT_FOREMANE_BY_MANAGER}"/>
                            </c:if>
                        </c:if>
                    </td><td>
                        <c:if test="${order.getComment() != null}">
                            <c:out value="${order.getComment()}"/>
                        </c:if>
                        <c:if test="${order.getComment() == null}">
                            <c:if test="${order.isOrderComplited()}">
                                <form action='controller' method='get'>
                                    <input type='hidden' name='command' value='addComment'>
                                    <input type='hidden' name='orderId' value='${order.getId()}'>
                                    <input type='text' name='comment'><br>
                                    <input type='submit' value=<fmt:message key =
                                                                                    "button.addComment"/>>
                                </form>
                            </c:if>
                        </c:if>
                    </td></tr>
                </c:forEach>
            </table>
        </c:if>
    </c:if>
</body>
</html>