<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="columnSet" type="Set<? extends SingularAttribute<? extends AbstractCategory, ?>>"--%>
<%--@elvariable id="ruPluralShortName" type="java.lang.String"--%>
<%--@elvariable id="ruPluralFullName" type="java.lang.String"--%>

<html>
<head>
    <title>${ruPluralShortName}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
</head>
<body>
<script>

    window.onload = function () {
        refreshCategoryTable(document.getElementById("table-category"));
    };

    function refreshCategoryTable(current_table) {

        let body, row;
        let row_style, img_marked;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'}? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";
        <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">
            insertCellInRow(${statusCol.index}, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("0", colNum, colOrd, defSort, '') + '${cellCol.name}</button>'); // Down
        </c:forEach>

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${categoryTable}" varStatus="status">
            row = body.insertRow(${status.index});
            <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">
                insertCellInRow(${statusCol.index}, row, '${((cellCol.getType().getPersistenceType() == "BASIC")? cell[cellCol.name]: cell[cellCol.name].name)}', filterString);
            </c:forEach>

            row_style = "";
            <c:choose><c:when test="${(status.index % 2) == 0}">
            row_style += " background: rgb(255, 248, 234); ";
            </c:when></c:choose>
            row.style = row_style;

        </c:forEach>

    }

</script>

<div style="height:3%"><h2 class="left_up_panel">${ruPluralFullName}</h2></div>
<form method="post" action="${pageContext.request.contextPath}/smth" style="overflow:hidden; height:96%"
      autocomplete="off" name="work_area" id="work_area">

    <div style="height:6%" class="horizontal">
        <div>
            <table>
                <tr>
                    <td>
                        <button name="bookMark" value='markedTasksList'>
                            <img class="command-bar-refresh"
                                 src="${pageContext.request.contextPath}/resources/images/refresh.png">
                            Обновить
                        </button>
                    </td>
                    <td>Снять</td>
                </tr>
            </table>
        </div>
        <div class="search-string"><input name="markedTasksListFilterString" placeholder="Поиск"
                                          onkeyup="onKeyupSearchString(arguments[0],'work_area')"
                                          value="${filterString}"/></div>
    </div>
    <div style="height:75.5%;" id="div-for-main-table">
        <div class="table-wrapper">
            <div class="table-scroll-review-tasks">
                <table class="table-category" id="table-category"></table>
            </div>
        </div>
    </div>

</form>
</body>
</html>
