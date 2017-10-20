<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="documents.DocumentProperty" %>

<%--@elvariable id="documentTypeId" type="java.lang.Long"--%>
<%--@elvariable id="userList" type="java.util.List<categories.User>"--%>
<%--@elvariable id="userWhomList" type="java.util.List<categories.User>"--%>

<%-- Should be code on page:

  // Choose user with keypress up, down, enter
  <div id="popup-menu">
     <ul>
        <li>Кому: <input type="text" tabindex="1" name="whom" size="80%" onkeyUp = "selectUsers()" onkeypress="selectUsers()" onkeydown="selectUsersOnKeyDown()" id="selectedUser">
           <input type="hidden" name="whomId" value="x" id="whomIdBasic"/>
           <ul id="selectMenuUser"></ul>
        </li>
     </ul>
  </div>


  // Choose 1 user with button (href)

  window.onload = function () {
    refreshChooseOneUser(document.getElementById("table_choose_one_user"));
}

<a href="#choose_one_user" class="overlay_choose_one_user" id="form_choose_one_user"></a>
<form method="post"  action="${pageContext.request.contextPath}/doc_memorandum_create" class="modal_choose_one_user" id="menu_choose_one_user">
    <div>Выберите сотрудника:</div>
    <div id="table-wrapper">
        <div id="table-scroll">
            <table class = "table-whom" id="table_choose_one_user"></table>
        </div>
    </div>
</form>

--%>

<html>

<head>
    <link href="<c:url value="/resources/css/common/whom_menu.css"/>" rel="stylesheet" type="text/css">
     <%--Should be included in documents (memorandum_create, executor_task, etc.) before including this file.--%>
     <%--I don't include common.jsp here because result file (memorandum_create, executor_task, etc.) --%>
     <%--with different common files (add_files, send_document, whom_menu) would include common.jsp many times --%>
    <%--<jsp:include page="../common/common.jsp"/>--%>
</head>

<script>

    const maxCountOfUsers = 10;
    let documentTypeId = ${documentTypeId};

    //noinspection JSUnusedLocalSymbols
    function selectUsers() {

        const selectMenuUser = document.getElementById("selectMenuUser");
        selectMenuUser.innerHTML = "";

        const textForCheck = document.getElementById("selectedUser").value;
        let currentUser = "";
        let currentUserId = -1;
        let currentPositionInCompany = "";
        let count = 0;

        <c:forEach var="cell" items="${userList}">

        if (   ("${cell.lastName}".toLowerCase().indexOf(textForCheck.toLowerCase())) >= 0
            || ("${cell.firstName}".toLowerCase().indexOf(textForCheck.toLowerCase())) >= 0
            || ("${cell.middleName}".toLowerCase().indexOf(textForCheck.toLowerCase())) >= 0
        ){
            count++;
            if (count <= maxCountOfUsers)
                selectMenuUser.innerHTML+='<li tabindex = '+(count+1)+' id = "${cell.id}" onkeydown="selectUsersOnKeyDown()" onclick= getUserFromPopUpMenu("${cell.lastName}&nbsp${cell.firstName}&nbsp${cell.middleName}","${cell.id}","${cell.position.name.replaceAll(" ","&nbsp")}")><div>${cell.lastName} ${cell.firstName} ${cell.middleName}</div><div style="display: none;">${cell.position.name.replaceAll(" ","&nbsp")}</div></li>';
            if (count === 1) {
                currentUser = "${cell.lastName}&nbsp${cell.firstName}&nbsp${cell.middleName}";
                currentUserId = "${cell.id}";
                currentPositionInCompany = "${cell.position.name.replaceAll(" ","&nbsp")}";
            }
        }
        </c:forEach>

        if (count === 1) getUserFromPopUpMenu(currentUser, currentUserId, currentPositionInCompany);
        else if (count > maxCountOfUsers) selectMenuUser.innerHTML+='<li tabindex = '+(count+1)+'>... и еще '+(count - maxCountOfUsers)+' сотрудника(-ов)</li>';

    }

    //noinspection JSUnusedLocalSymbols
    function selectUsersOnKeyDown() {

        let currentChild;
        let currentTabIndex, newPosition, count;

        if (  (event.keyCode === 40)  // Down
            || (event.keyCode === 38)  // Up
            || (event.keyCode === 13)) // Enter
        {

            currentChild = document.getElementById("selectMenuUser").firstElementChild;
            currentTabIndex = document.activeElement.tabIndex;

            newPosition = 0;
            if ((event.keyCode === 40) &&(document.getElementById("selectMenuUser").childElementCount + 1 <= currentTabIndex)) newPosition = 2;
            else if ((event.keyCode === 38) && (currentTabIndex === 2 )) newPosition = document.getElementById("selectMenuUser").childElementCount + 1;
            else if (event.keyCode === 40) newPosition = currentTabIndex + 1;
            else if (event.keyCode === 38) newPosition = currentTabIndex - 1;
            else if (event.keyCode === 13) newPosition = currentTabIndex;

            count = 2;
            while (count++ < newPosition) currentChild = currentChild.nextSibling;
            if (currentChild!== null) currentChild.focus();

            if (event.keyCode === 13) getUserFromPopUpMenu(currentChild.children[0].innerHTML, currentChild.id, currentChild.children[1].innerHTML);
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
        insertCellInRow(0, row, '<a href="#choose_one_user" class="link-like-text">${cell.fio}</a>');
        insertCellInRow(1, row, '<a href="#choose_one_user" class="link-like-text">${cell.position.name}</a>');

        row_style = "";
        <c:choose><c:when test="${(status.index % 2) == 0}"> row_style+=" background: rgb(255, 248, 234); ";</c:when></c:choose>
        row.style= row_style;

        row.onclick =  function () { getUserFromPopUpMenu("${cell.fio}", "${cell.id}", "${cell.position.name}"); };

        </c:forEach>

        currentTable.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td></tr>";

        resizeElementsWhomMenu();
        window.addEventListener('resize', resizeElementsWhomMenu);

    }

    //noinspection JSUnusedLocalSymbols
    function setUsersWhomList() {

        <c:choose>
        <c:when test="${not empty userWhomList}">

            const currentTable = document.getElementById("table-whom-selected");
            let row;
            <c:forEach var="cell" items="${userWhomList}" varStatus="status">
                <c:choose>
                <c:when test="${not empty cell}">
                    row = currentTable.tBodies.item(0).rows[0];
                    insertCellInRow(0, row, '<div class="vR"><div class="vN"><div class="vT">'+getFioAbbreviated("${cell.fio}")+'</div><div class="hidden-id" hidden>${cell.id}</div><div class="vM" onclick="event.currentTarget.offsetParent.outerHTML = \'\'; resizeElements();"></div></div></div>');
                </c:when>
                </c:choose>
            </c:forEach>
            resizeElements();

        </c:when>
        </c:choose>

    }

    function getUserFromPopUpMenu(userFio, id, positionInCompany) {

        if (documentTypeId === <%=DocumentProperty.MESSAGE.getId()%>) {
            const currentTable = document.getElementById("table-whom-selected");
            let row = currentTable.tBodies.item(0).rows[0];
            insertCellInRow(0, row, '<div class="vR"><div class="vN"><div class="vT">'+getFioAbbreviated(userFio)+'</div><div class="hidden-id" hidden>'+id+'</div><div class="vM" onclick="event.currentTarget.offsetParent.outerHTML = \'\'; resizeElements();"></div></div></div>');
            resizeElements();
        }else {
            document.getElementById("selectedUser").value = userFio.replace("&nbsp", " ").replace("&nbsp", " ");
            document.getElementById("selectMenuUser").innerHTML = "";
            document.getElementById("whomIdBasic").value = id;
            document.getElementById("positionToBasic").value = positionInCompany;
        }

    }

    function resizeElementsWhomMenu(){

        const clientHeight = document.firstChild.clientHeight;
        const currentTable = document.getElementById("table_choose_one_user");

        for (let i = 0; i < currentTable.tHead.rows[0].children.length; i++)
            currentTable.tHead.rows[0].children[i].style = "width:"+currentTable.tBodies[0].rows[0].cells[i].clientWidth+";";
        document.getElementsByClassName("second_row")[0].style.cssText = "font-size:" + (clientHeight > 850 ? "0" : clientHeight <= 630 ? "8" : clientHeight <= 700 ? "8" : "6") + "px";

    }

</script>

</html>
