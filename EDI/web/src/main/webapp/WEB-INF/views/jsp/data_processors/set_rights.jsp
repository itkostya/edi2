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
    <link href="<c:url value="/resources/css/data_processors/set_rights.css"/>" rel="stylesheet" type="text/css">
</head>

<body>

<script>

    window.onload = function () {

        refreshChooseOneUser(document.getElementById("table_choose_one_user"));
        refreshCategoryTableRights(document.getElementById("table-rightsList"));

        resizeElements();
        window.addEventListener('resize', resizeElements);

    };

    function resizeElements() {

        const clientWidth = document.firstChild.clientWidth;
        const clientHeight = document.firstChild.clientHeight;
        let current_table = document.getElementById("table-rightsList");

        for (i = 0; i < current_table.tHead.rows[0].children.length; i++)
            current_table.tHead.rows[0].children[i].style = "width:" + current_table.tBodies[0].rows[0].cells[i].clientWidth + ";";

        // TODO: check it
        document.getElementsByClassName("first_row_rights_list")[0].style.cssText = "top:" + (clientHeight > 850 ? "-2.5" : clientHeight <= 630 ? "-1" : clientHeight <= 700 ? "-1.5" : "-2") + "%";

        if (clientWidth < 1500) {
            document.getElementsByClassName("first_row_rights_list")[0].innerHTML =
                document.getElementsByClassName("first_row_rights_list")[0].innerHTML.replace("Просмотр", "Пр.").replace("Редактир.", "Ред.");
        }

    }

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

        row.onclick =  function () { changeUser("${cell.id}"); };

        </c:forEach>

        currentTable.createTHead().insertRow(1).outerHTML = "<tr class='second_row' style='font-size: 0;'><td class='td_sr'>1</td><td class='td_sr'>2</td></tr>";

    }

    function changeUser(userId) {

        const formData = new FormData();
        formData.append("param", "changeUser");
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
        button.disabled = true;

    }

    //noinspection JSUnusedLocalSymbols
    function refreshCategoryTableRights(currentTable) {

        let body, row;
        let row_style;

        row = currentTable.createTHead().insertRow(0);
        row.className = "first_row_rights_list";

        insertCellInRow(0, row, 'Метаданные');
        insertCellInRow(1, row, 'Тип метаданных');
        insertCellInRow(2, row, 'Просмотр');
        insertCellInRow(3, row, 'Редактир.');

        body = currentTable.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${userAccessRightList}" varStatus="status">
        row = body.insertRow(${status.index});
        insertCellInRow(0, row, '<a href="#" class="link-like-text">${cell.metadataType.metadata}</a>');
        insertCellInRow(1, row, '<a href="#" class="link-like-text">${cell.metadataType}</a>');
        insertCellInRow(2, row, "<input type=checkbox ".concat(( true === ${cell.view} ? "checked": "")).concat(" onchange = 'changeView(this)'>"));
        insertCellInRow(3, row, "<input type=checkbox ".concat(( true === ${cell.edit} ? "checked": "")).concat(" onchange = 'changeEdit(this)'>"));

        row_style = "";
        <c:choose><c:when test="${(status.index % 2) == 0}"> row_style+=" background: rgb(255, 248, 234); ";</c:when></c:choose>
        row.style= row_style;

        <%--row.onclick =  function () { getUserFromPopUpMenu("${cell.fio}", "${cell.id}", "${cell.position.name}"); };--%>

        </c:forEach>

        currentTable.createTHead().insertRow(1).outerHTML = "<tr class='second_row' style='font-size: 0;'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td></tr>";

    }

    function setNewRights() {

        const tableRights = document.getElementById("table-rightsList");

        const formData = new FormData();
        formData.append("param", "save");

        for (i = 0; i < tableRights.tBodies[0].rows.length; i++) {
            formData.append("metadataStringArray[]", tableRights.tBodies[0].rows[i].cells[0].innerText);
            formData.append("metadataTypeStringArray[]", tableRights.tBodies[0].rows[i].cells[1].innerText);
            formData.append("viewStringArray[]", tableRights.tBodies[0].rows[i].cells[2].children[0].checked);
            formData.append("editStringArray[]", tableRights.tBodies[0].rows[i].cells[3].children[0].checked);
        }

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
        button.innerHTML = "Установка прав пользователя...";
        button.disabled = true;

    }

    function changeView(element) {

        const tableRights = document.getElementById("table-rightsList");
        if (!element.checked && tableRights.tBodies[0].rows[element.parentElement.parentElement.rowIndex-2].cells[3].children[0].checked) {
            tableRights.tBodies[0].rows[element.parentElement.parentElement.rowIndex-2].cells[3].children[0].checked = false;
        }
    }

    function changeEdit(element) {

        const tableRights = document.getElementById("table-rightsList");
        if (element.checked && !tableRights.tBodies[0].rows[element.parentElement.parentElement.rowIndex-2].cells[2].children[0].checked) {
            tableRights.tBodies[0].rows[element.parentElement.parentElement.rowIndex-2].cells[2].children[0].checked = true;
        }
    }

    function setViewToAll(checked) {

        const tableRights = document.getElementById("table-rightsList");

        for (let i = 0; i < tableRights.tBodies[0].rows.length; i++) {
            tableRights.tBodies[0].rows[i].cells[2].children[0].checked = checked;
        }

        if (!checked) {
            for (let i = 0; i < tableRights.tBodies[0].rows.length; i++) {
                if (tableRights.tBodies[0].rows[i].cells[3].children[0].checked) {
                    tableRights.tBodies[0].rows[i].cells[3].children[0].checked = false;
                }
            }
        }
    }

    function setEditToAll(checked) {

        const tableRights = document.getElementById("table-rightsList");

        for (let i = 0; i < tableRights.tBodies[0].rows.length; i++) {
            tableRights.tBodies[0].rows[i].cells[3].children[0].checked = checked;
        }

        if (checked) {
            for (let i = 0; i < tableRights.tBodies[0].rows.length; i++) {
                if (!tableRights.tBodies[0].rows[i].cells[2].children[0].checked) {
                    tableRights.tBodies[0].rows[i].cells[2].children[0].checked = true;
                }
            }
        }
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
        <div style="padding: 3px; height: 5%;" id="div-groupBy">
            <div class="horizontal">
                <div><a href="javascript:void(0)" class="link-like-button" onclick="setNewRights()"><div class="command-bar-save"></div>&nbsp;Записать права&nbsp;</a></div>
                <div>&nbsp;</div>
                <div><a href = "javascript:void(0)" class="link-like-button" onclick="setViewToAll(true)"><div class="command-bar-set-checkbox-items"></div>&nbsp;Просмотр&nbsp;</a></div>
                <div>&nbsp;</div>
                <div><a href = "javascript:void(0)" class="link-like-button" onclick="setViewToAll(false)"><div class="command-bar-clear-checkbox-items"></div>&nbsp;Просмотр&nbsp;</a></div>
                <div>&nbsp;</div>
                <div><a href = "javascript:void(0)" class="link-like-button" onclick="setEditToAll(true)"><div class="command-bar-set-checkbox-items"></div>&nbsp;Редактирование&nbsp;</a></div>
                <div>&nbsp;</div>
                <div><a href = "javascript:void(0)" class="link-like-button" onclick="setEditToAll(false)"><div class="command-bar-clear-checkbox-items"></div>&nbsp;Редактирование&nbsp;</a></div>
            </div>
        </div>
        <div class="table-wrapper-rights-list" style="height: 88%;">
            <div class="table-scroll-rights-list">
                <table class="table-rights" id="table-rightsList"></table>
            </div>
        </div>
    </div>
    </div>
    <div id="info_result">${infoResult}</div>
 </form>

</body>
</html>
