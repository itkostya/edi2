<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="categoryElement" type="abstract_entity.AbstractCategory"--%>
<%--@elvariable id="columnSet" type="Set<? extends SingularAttribute<? extends AbstractCategory, ?>>"--%>
<%--@elvariable id="ruPluralShortName" type="java.lang.String"--%>

<html>
<head>
    <title>${categoryElement.name}(${ruPluralShortName})</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
</head>

<body>
<script>

    window.onload = function () {
        refreshAttributes(document.getElementById("table-attributes"));
    };

    function refreshAttributes(current_table) {

        let body, row;
        let row_style;

        row = current_table.createTHead().insertRow(0);
        insertCellInRow(0, row, 'Field', "");
        insertCellInRow(0, row, 'Value', "");

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">

            row = body.insertRow(${statusCol.index});
            insertCellInRow(0, row, '${cellCol.name}', "");
            insertCellInRow(1, row, '<div contenteditable>${((cellCol.getType().getPersistenceType() == "BASIC")? categoryElement[cellCol.name]: categoryElement[cellCol.name].name)}</div>', "");

            row_style = "";
            <c:choose><c:when test="${(statusCol.index % 2) == 0}">
            row_style += " background: rgb(255, 248, 234); ";
            </c:when></c:choose>
            row.style = row_style;

            <%--row.ondblclick = function () {--%>
                <%--onClickOpenElement(${cellCol.id});--%>
            <%--};--%>

        </c:forEach>

    }

</script>
</body>

<div style="height:90%;" id="div-for-table-attributes">
    <div class="table-wrapper">
        <div class="table-scroll">
            <table class="table-attributes" id="table-attributes"></table>
        </div>
    </div>
</div>

</html>
