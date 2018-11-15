<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="categoryTable" type="List<AbstractCategory>"--%>
<%--@elvariable id="columnSet" type="Set<? extends SingularAttribute<? extends AbstractCategory, ?>>"--%>
<%--@elvariable id="elementPageName" type="java.lang.String"--%>
<%--@elvariable id="filterString" type="java.lang.String"--%>
<%--@elvariable id="mapSortValue" type="java.lang.String"--%>
<%--@elvariable id="ruPluralShortName" type="java.lang.String"--%>
<%--@elvariable id="ruPluralFullName" type="java.lang.String"--%>

<html>

<head>
    <title>${ruPluralShortName}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
    <%--IT'S COPY FROM CATEGORY_JOURNAL.JSP, BUT IT HAS SPECIAL THINGS AVAILABLE ONLY FOR CATEGORY USER:
      I DON'T WANTED TO USE WEIRD CONDITIONS IN COMMON JSP FILE SO I CREATED A NEW ONE
      IT'S NOT INHERITAGE FOR JSP, BUT IT'S CLOSE FOR THIS--%>
</head>

<body>
<script>

    window.onload = function () {
        refreshCategoryTable(document.getElementById("table-category"));
    };

    // CHANGEABLE
    function refreshCategoryTable(current_table) {

        let body, row;
        let row_style;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.substring(0, ((mapSortValue.contains('.') ? mapSortValue.indexOf('.') : 1 )))}");
        <%--let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");--%>
        let colOrd = (true === ${mapSortValue == 'default'}? null : "${mapSortValue.charAt(mapSortValue.length()-1)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        let itsId, strOrd, strView;
        <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">
            itsId = (true === ${'id' == cellCol.name});
            strOrd = getColOrder("${statusCol.index}", colNum, colOrd, defSort, (itsId ? "+" : "n"));
            strView = getColSymbol("${statusCol.index}", colNum, colOrd, defSort, (itsId ?  "&darr;" : ""));
            insertCellInRow(${statusCol.index}, row, '<button name="sortColumn" class="btn-link2" value="${statusCol.index}.'+ strOrd + '">' + strView + '${cellCol.name}</button>');
        </c:forEach>

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${categoryTable}" varStatus="status">
            row = body.insertRow(${status.index});
            <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">
                insertCellInRow(${statusCol.index}, row, getHighlightedText('${((cellCol.getType().getPersistenceType() == "BASIC")? cell[cellCol.name]: cell[cellCol.name].name)}', filterString));
            </c:forEach>

            row_style = "";
            <c:choose><c:when test="${(status.index % 2) == 0}">
            row_style += " background: rgb(255, 248, 234); ";
            </c:when></c:choose>

            // CHANGEABLE BEGIN
            if (  true === ${cell['role']== null} )
            row_style += " background: rgb(255, 178, 179); ";
            // CHANGEABLE END

            row.style = row_style;

            row.ondblclick = function () {
                onClickOpenElement(${cell.id});
            };

        </c:forEach>
    }

    function onClickOpenElement(elementId) {
        const myForm = document.forms["formOpenElement"];
        myForm.action = "${pageContext.request.contextPath}${elementPageName}";
        myForm.elements["elementId"].value = elementId;
        myForm.elements["tempId"].value = getRandomInt();
        myForm.submit();
    }

</script>

<div style="height:3%"><h2 class="left_up_panel">${ruPluralFullName}</h2></div>
<form method="post" action="${pageContext.request.contextPath}" style="overflow:hidden; height:96%"
      autocomplete="off" name="category_journal" id="category_journal">

    <div style="height:6%" class="horizontal">
        <div>
            <table>
                <tr>
                    <td>
                        <button>
                            <img class="command-bar-refresh"
                                 src="${pageContext.request.contextPath}/resources/images/refresh.png">
                            Обновить
                        </button>
                    </td>
                    <td></td>
                </tr>
            </table>
        </div>
        <div class="search-string"><input name="categoryJournalFilterString" placeholder="Поиск"
                                          onkeyup="onKeyupSearchString(arguments[0],'category_journal')"
                                          value="${filterString}"/></div>
    </div>
    <div style="height:90%;" id="div-for-main-table">
        <div class="table-wrapper">
            <div class="table-scroll-review-tasks">
                <table class="table-category" id="table-category"></table>
            </div>
        </div>
    </div>

</form>

<form hidden id="formOpenElement" target="_blank">
    <input type="hidden" name="elementId"/>
    <input type="hidden" name="tempId"/>
</form>

</body>
</html>
