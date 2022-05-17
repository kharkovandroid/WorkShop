<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.gmail.valvol98.data.UnChangeData" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "ui" uri = "/WEB-INF/tld/custom.tld"%>

<%@ include file="/WEB-INF/jsp/includeLangSetting.jspf" %>

<html>
<head>
    <title><fmt:message key = "title.ManagerPage"/></title>
    <script>
        function sortOrderTable(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("allOrdersTable");
            switching = true;
            // Set the sorting direction to ascending:
            dir = "asc";
            /* Make a loop that will continue until
            no switching has been done: */
            while (switching) {
                // Start by saying: no switching is done:
                switching = false;
                rows = table.rows;
                /* Loop through all table rows (except the
                first, which contains table headers): */
                for (i = 1; i < (rows.length - 1); i++) {
                    // Start by saying there should be no switching:
                    shouldSwitch = false;
                    /* Get the two elements you want to compare,
                    one from current row and one from the next: */
                    x = rows[i].getElementsByTagName("TD")[n];
                    y = rows[i + 1].getElementsByTagName("TD")[n];
                    /* Check if the two rows should switch place,
                    based on the direction, asc or desc: */
                    if (dir == "asc") {
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            // If so, mark as a switch and break the loop:
                            shouldSwitch = true;
                            break;
                        }
                    } else if (dir == "desc") {
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                            // If so, mark as a switch and break the loop:
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                if (shouldSwitch) {
                    /* If a switch has been marked, make the switch
                    and mark that a switch has been done: */
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    // Each time a switch is done, increase this count by 1:
                    switchcount ++;
                } else {
                    /* If no switching has been done AND the direction is "asc",
                    set the direction to "desc" and run the while loop again. */
                    if (switchcount == 0 && dir == "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
        }
    </script>

    <script>
        function filterFunction(tableName, inputIdForeman, columnIdForeman, inputIdStatus,
                            columnIdStatus) {
            // Declare variables
            var inputForeman, filterForeman, inputStatus, filterStatus, table, tr, tdForeman,
                tdStatus, i, txtValueForeman, txtValueStatus;
            inputForeman = document.getElementById(inputIdForeman);
            inputStatus = document.getElementById(inputIdStatus);
            filterForeman = inputForeman.value.toUpperCase();
            filterStatus = inputStatus.value.toUpperCase();
            table = document.getElementById(tableName);
            tr = table.getElementsByTagName("tr");

            // Loop through all table rows, and hide those who don't match the search query
            for (i = 0; i < tr.length; i++) {
                tdForeman = tr[i].getElementsByTagName("td")[columnIdForeman];
                tdStatus = tr[i].getElementsByTagName("td")[columnIdStatus];
                if (tdForeman || tdStatus) {
                    txtValueForeman = tdForeman.textContent || tdForeman.innerText;
                    txtValueStatus = tdStatus.textContent || tdStatus.innerText;
                    if (txtValueForeman.toUpperCase().indexOf(filterForeman) > -1 &&
                        txtValueStatus.toUpperCase().indexOf(filterStatus) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }
    </script>
</head>
<body>

    <%@ include file="includeLinkLanguage.jspf" %>

    <c:if test="${UnChangeData.ROLE_MANAGER.toString() eq loggedUser.getRole().toString()}">

        <ui:out userName = "${loggedUser.getName()}" userRole="${loggedUser.getRole()}"/>

        <%@ include file="general.jspf" %>

        <p style="color:#ff0000"><fmt:message key = "label.informationAboutAccounts"/></p>

        <c:if test="${GeneralAcoountsTable != null}">
            <table border='1'>
            <tr>
            <th><fmt:message key = "label.userName"/></th>
            <th><fmt:message key = "label.accountState"/></th>
            </tr>
            <c:forEach var="user" items="${GeneralAcoountsTable}">
                <tr><td>
                <c:out value="${user.getName()}"/>
                </td><td>
                <c:out value="${user.getGerenalActiveMoneyOnAccount()}"/>
                </td></tr>
            </c:forEach>
            </table>
            <br>
            <form action = "controller" method = "post">
                <input type = "hidden" name = "command" value = "addSumToAccount">
                <fmt:message key = "label.choiceUserForAddAccount"/><br>
                <select name="checkedUser">
                    <c:forEach var="user" items="${GeneralAcoountsTable}">
                        <option value="${user.getId()}">${user.getName()}</option>
                    </c:forEach>
                </select><br>
                <fmt:message key = "label.amountOfAddAccount"/><br>
                <input type = "text" name = "sumToAccount"><br>
                <input type = "submit" value = <fmt:message key = "button.addAccount"/>>
            </form>
        </c:if>

        <c:if test="${UnsubmittedOrdersTable != null && UnsubmittedOrdersTable.size() != 0}">
            <hr>
            <p style="color:#ff0000"><fmt:message key = "label.informationAboutSubmittedOrders"/></p>
            <table border='1'>
            <tr>
            <th><fmt:message key = "unsubmittedOrdersTable.userName"/></th>
            <th><fmt:message key = "unsubmittedOrdersTable.shortInformationAboutOrder"/></th>
            <th><fmt:message key = "unsubmittedOrdersTable.factPutForeman"/></th>
            <th><fmt:message key = "unsubmittedOrdersTable.dateTimeInComeOrder"/></th>
            <th><fmt:message key = "unsubmittedOrdersTable.cost"/></th>
            </tr>
            <c:set var="listIdUnSubmittedOrder" value=""/>
            <c:set var="isNeedForeman" value="false"/>
            <c:forEach var="unSubmittedOrder" items="${UnsubmittedOrdersTable}">
                <c:set var="listIdUnSubmittedOrder" value="${listIdUnSubmittedOrder}${unSubmittedOrder.getId()}_"/>
                <tr><td>
                <c:out value="${unSubmittedOrder.getUserName()}"/>
                </td><td>
                <c:out value="${unSubmittedOrder.getDescription()}"/>
                </td><td>
                <c:if test="${unSubmittedOrder.getForemanId() == 0}">
                    <c:out value="-"/>
                    <c:set var="isNeedForeman" value="true"/>
                </c:if>
                <c:if test="${unSubmittedOrder.getForemanId() != 0}">
                    <c:out value="+"/>
                </c:if>
                </td><td>
                <c:out value="${unSubmittedOrder.getDatetime()}"/>
                </td><td>
                <c:if test="${unSubmittedOrder.getCost() == 0}">
                    <form action='controller' method='post'>
                    <input type='hidden' name='command' value='addCostUnSubmittedOrder'>
                    <input type='hidden' name='idUnSubmittedOrder' value='${unSubmittedOrder.getId()}'>
                    <input type='hidden' name='idUser' value='${unSubmittedOrder.getUserId()}'>
                    <input type='text' name='cost'><br>
                    <input type='submit' value=<fmt:message key = "button.add"/>>
                    </form>
                </c:if>
                <c:if test="${unSubmittedOrder.getCost() != 0}">
                    <c:out value="${unSubmittedOrder.getCost()}"/>
                </c:if>
                <c:if test="${idUnSubmittedOrder != null && idUnSubmittedOrder.toString() eq unSubmittedOrder.getId().toString()}">
                    <p style="color:#FF0000; font-size:10pt; face:serif">
                        <fmt:message key = "label.isNotEnoughMoney"/>
                    </p>
                </c:if>
                </td></tr>
            </c:forEach>
            </table>

            <c:if test="${isNeedForeman == true}">
                <br>
                <fmt:message key = "label.choiceForeman"/><br>
                <form action = "controller" method = "post">
                <input type = "hidden" name = "command" value = "addForemanUnSubmittedOrder">
                <input type = 'hidden' name = 'listIdUnSubmittedOrder' value = '${listIdUnSubmittedOrder}'>
                <select name="checkedForeman">
                <c:if test="${ForemenList != null}">
                    <c:forEach var="foremen" items="${ForemenList}">
                        <option value='${foremen.getId()}'>${foremen.getName()}</option>
                    </c:forEach>
                </c:if>
                </select><br>
                <input type = "submit" value = <fmt:message key = "button.putForeman"/>>
            </form>
            </c:if>
        </c:if>

        <c:if test="${AllOrders != null && AllOrders.size() != 0}">
            <hr>

            <p style="color:#ff0000"><fmt:message key = "label.reportForOrders"/></p>

            <input size = '50' type="text" id="filterByForeman" onkeyup="filterFunction('allOrdersTable', 'filterByForeman',
                0, 'filterByStatusOrder', 4)" placeholder='<fmt:message key = "input.filterByForemanName"/>'>
            <br>
            <input size = '50' type="text" id="filterByStatusOrder" onkeyup="filterFunction('allOrdersTable', 'filterByForeman',
                0, 'filterByStatusOrder', 4)" placeholder='<fmt:message key="input.filterByStatusOrder"/>'><br>

            <table id='allOrdersTable' border='1'>
            <tr>
            <th style='cursor:pointer; color:#1152E9' onclick='sortOrderTable(0)' style='color:#0000CD'><fmt:message key = "allOrdersTable.ForemanName"/></th>
            <th><fmt:message key = "allOrdersTable.shortDescriptionOrder"/></th>
            <th style='cursor:pointer; color:#1152E9' onclick='sortOrderTable(2)' style='color:#0000CD'><fmt:message key="allOrdersTable.dateTimeInComeOrder"/></th>
            <th style='cursor:pointer; color:#1152E9' onclick='sortOrderTable(3)' style='color:#0000CD'><fmt:message key = "allOrdersTable.cost"/></th>
            <th style='cursor:pointer; color:#1152E9' onclick='sortOrderTable(4)' style='color:#0000CD'><fmt:message key = "allOrdersTable.OrderState"/></th>
            <th><fmt:message key = "allOrdersTable.chamgeOrderState"/></th>
            </tr>

            <c:forEach var="allOrder" items="${AllOrders}">
                <tr><td>
                <c:if test="${allOrder.getForemanId() != 0}">
                    <c:out value="${allOrder.getForemanName()}"/>
                </c:if>
                </td><td>
                <c:out value="${allOrder.getDescription()}"/>
                </td><td>
                <c:out value="${allOrder.getDatetime()}"/>
                </td><td>
                <c:out value="${allOrder.getCost()}"/>
                </td><td>
                <!--статус заявки-->
                <c:if test="${allOrder.getCost() == 0 && allOrder.getManagerId() == 0}">
                    <fmt:message key = 'allOrdersTable.orderDoesNotConfirmManager'/>
                </c:if>
                <c:if test="${!(allOrder.getCost() == 0 && allOrder.getManagerId() == 0)}">
                    <c:if test="${allOrder.getForemanId() == 0}">
                        <fmt:message key = 'allOrdersTable.managerDoesNotConfirmForeman'/>
                    </c:if>
                    <c:if test="${allOrder.getForemanId() != 0}">
                        <c:if test="${allOrder.getOrderStatus().size() == 0}">
                            <fmt:message key = 'allOrdersTable.foremanDoesNotTakeOrder'/>
                        </c:if>
                        <c:if test="${allOrder.getOrderStatus().size() != 0}">
                            <c:out value="${allOrder.getOrderStatus().get(0).getStatusType()}"/>
                        </c:if>
                    </c:if>
                </c:if>
                </td><td>
                <c:if test="${allOrder.getStatusNext() != null}">
                    <c:if test="${allOrder.getStatusNext().size() != 0 && !(UnChangeData.ORDER_FINAL.toString() eq allOrder.getStatusNext().get(0).getType().toString())}">
                        <c:set var="temp" value=""/>
                        <c:forEach var="allOrderGetI" items="${allOrder.getStatusNext()}">
                            <c:url value="controller?command=changeOrderStatusByManager" var="managerLinkChangeOrderStatus">
                                <c:param name="orderId" value="${allOrder.getId()}"/>
                                <c:param name="statusId" value="${allOrderGetI.getId()}"/>
                                <c:param name="managerId" value="${loggedUser.getId()}"/>
                            </c:url>
                            <a href="${managerLinkChangeOrderStatus}">${allOrderGetI.getType()}</a>
                        </c:forEach>
                    </c:if>
                </c:if>
                </td></tr>
            </c:forEach>
            </table>
        </c:if>
    </c:if>
</body>
</html>