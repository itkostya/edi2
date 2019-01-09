<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="currentSelectedUserId" type="java.lang.Long"--%>
<%--@elvariable id="infoResult" type="java.lang.String"--%>
<%--@elvariable id="userAccessRightList" type="java.util.List<categories.UserAccessRight>"--%>
<%--@elvariable id="userList" type="java.util.List<categories.User>"--%>

<html>
<head>
    <title>Set user's rights</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="../common/common.jsp"/>
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">

</head>

<body>

<script>

    window.onload = function () {

        refreshChooseOneUser(document.getElementById("table_choose_one_user"));
        refreshCategoryTableRights(document.getElementById("table-tasksList")); // TODO: change name of the table

    };

    //noinspection JSUnusedLocalSymbols
    function refreshChooseOneUser(currentTable) {

        let body, row;
        let row_style;

        row = currentTable.createTHead().insertRow(0);
        row.className = "first_row";

        insertCellInRow(0, row, 'Ф. И. О.');
        insertCellInRow(1, row, 'Должность');

        body = currentTable.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${userList}" varStatus="status">
        row = body.insertRow(${status.index});
        insertCellInRow(0, row, '<a href="javascript:void(0)" class="link-like-text">${cell.fio}</a>');
        insertCellInRow(1, row, '<a href="javascript:void(0)" class="link-like-text">${cell.position.name}</a>');

        row_style = "";
        <c:choose><c:when test="${(status.index % 2) == 0}"> row_style+=" background: rgb(255, 248, 234); ";</c:when></c:choose>
        if (${currentSelectedUserId} === ${cell.id}){
            row_style += "background-color:rgb(26, 219, 8)";
        }
        row.style= row_style;

        row.onclick =  function () { changeStatus("${cell.id}"); };

        </c:forEach>

        currentTable.createTHead().insertRow(1).outerHTML = "<tr class='second_row' style='font-size: 0;'><td class='td_sr'>1</td><td class='td_sr'>2</td></tr>";

        // resizeElementsWhomMenu();
        // window.addEventListener('resize', resizeElementsWhomMenu);

    }

    function changeStatus(userId) {

        const formData = new FormData();
        formData.append("currentSelectedUserId", userId);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}${PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE}", true);
        xhr.timeout = 30000;
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, true);
        };

        let button = document.getElementById("info_result");
        button.innerHTML = "Смена пользователя...";
        //button.innerHTML = "Смена пользователя..." + getHtmlBlackoutAndLoading();
        button.disabled = true;

    }

    //noinspection JSUnusedLocalSymbols
    function refreshCategoryTableRights(currentTable) {

        let body, row;
        let row_style;

        row = currentTable.createTHead().insertRow(0);
        row.className = "first_row";

        insertCellInRow(0, row, 'Метаданные');
        insertCellInRow(1, row, 'Тип метаданных');
        insertCellInRow(2, row, 'Просмотр');
        insertCellInRow(3, row, 'Редактирование');

        body = currentTable.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${userAccessRightList}" varStatus="status">
        row = body.insertRow(${status.index});
        insertCellInRow(0, row, '<a href="#choose_one_user" class="link-like-text">${cell.metadataType.metadata}</a>');
        insertCellInRow(1, row, '<a href="#choose_one_user" class="link-like-text">${cell.metadataType}</a>');
        insertCellInRow(2, row, '<a href="#choose_one_user" class="link-like-text">${cell.view}</a>');
        insertCellInRow(3, row, '<a href="#choose_one_user" class="link-like-text">${cell.edit}</a>');

        row_style = "";
        <c:choose><c:when test="${(status.index % 2) == 0}"> row_style+=" background: rgb(255, 248, 234); ";</c:when></c:choose>
        row.style= row_style;

        <%--row.onclick =  function () { getUserFromPopUpMenu("${cell.fio}", "${cell.id}", "${cell.position.name}"); };--%>

        </c:forEach>

        currentTable.createTHead().insertRow(1).outerHTML = "<tr class='second_row' style='font-size: 0;'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td></tr>";

        // resizeElementsWhomMenu();
        // window.addEventListener('resize', resizeElementsWhomMenu);

    }

</script>

<form method="post" action="${pageContext.request.contextPath}/${PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE}"
      style="overflow:hidden; height:99%" autocomplete="off" name="${PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE}" id="${PageContainer.DATA_PROCESSOR_SET_RIGHTS_PAGE}">

        <div>Выберите сотрудника:</div><div class="horizontal">

        <div style="height:100%;width:17%;vertical-align:top;">
            <div class="menu" style="width:95%">
                <div class="table-wrapper">
                    <div class="table-scroll">
                        <table class="table-whom" id="table_choose_one_user"></table>
                    </div>
                </div>
            </div>
        </div>
        <div style="height:100%;width:0.5%;background-color:rgb(26, 219, 8); vertical-align:top;"></div>
        <div style="height:99%;width:81%;">
        <div style="height:3%">&nbsp;</div>
        <div class="table-wrapper-tasks-list" style="height: 88%;">
            <div class="table-scroll-tasks-list">
                <table class="table-tasks" id="table-tasksList"></table>
            </div>
        </div>
    </div>
    </div>
    <div id="info_result">${infoResult}</div>
 </form>

</body>
</html>
