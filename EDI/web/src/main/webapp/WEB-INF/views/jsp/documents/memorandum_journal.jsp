<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="bookMark1" type="java.lang.String"--%>
<%--@elvariable id="filterString" type="java.lang.String"--%>
<%--@elvariable id="groupBy" type="java.lang.String"--%>

<html>

<head>
    <title>Журнал служебных записок</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/documents/memo_journal.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
    <jsp:include page="../common/document_journal.jsp"/>
</head>

<body>

<script>
    window.onload = function () {

        <c:choose>
        <c:when test="${bookMark1=='tasksListByGroup'}">
        refreshFolderStructure();
        refreshTasksListByGroup(document.getElementById("table-tasksList"), "memorandum");
        </c:when>
        <c:when test="${bookMark1=='fullTasksList'}">
        refreshFullTasksList(document.getElementById("table-tasksList-all"), "memorandum");
        </c:when>
        </c:choose>

        resizeElements();
        window.addEventListener('resize', resizeElements);

        <c:set value="${groupBy=='sender'}" var="isGroupBySender"/>

    };

    function resizeElements() {

        const clientWidth = document.firstChild.clientWidth;
        const clientHeight = document.firstChild.clientHeight;
        let current_table = document.getElementById("${bookMark1=='tasksListByGroup' ? 'table-tasksList': 'table-tasksList-all'}");
        let i;

        <c:choose>
        <c:when test="${bookMark1=='tasksListByGroup'}">

        document.getElementById("div-groupBy").style.cssText = "padding: 3px; height:" + (clientHeight > 850 ? "5" : clientHeight <= 630 ? "9" : clientHeight <= 700 ? "7" : "6") + "%";
        document.getElementsByClassName("table-wrapper-tasks-list")[0].style.cssText = "height:" + (clientHeight > 850 ? "88" : clientHeight <= 630 ? "77" : clientHeight <= 700 ? "83" : "85") + "%";
        document.getElementById("folder_structure").style.cssText =
            "font-size:" + (clientWidth > 1600 ? "15" : clientWidth <= 1200 ? "9" : clientWidth <= 1280 ? "10" : clientWidth <= 1450 ? "11" : "13") + "px";

        if (clientWidth < 1200) {
            let folderTables = document.getElementsByClassName("folder_name");
            for (i = 0; i < folderTables.length; i++)
                if (clientWidth < 700) {
                    folderTables[i].innerHTML = ""
                }
                else if (folderTables[i].innerHTML.length > 11) {
                    folderTables[i].innerHTML = ( folderTables[i].innerHTML.indexOf("Отмеченные") > 0 ? "&nbsp;Отм." : folderTables[i].innerHTML.substr(0, 10) + ".")
                }
        }
        </c:when>

        <c:when test="${bookMark1=='fullTasksList'}">
            document.getElementsByClassName("table-wrapper-tasks-list")[0].style.cssText = "height:" + (clientHeight > 850 ? "88" : clientHeight <= 630 ? "81" : clientHeight <= 700 ? "84" : "86") + "%";
        </c:when>
        </c:choose>

        for (i = 0; i < current_table.tHead.rows[0].children.length; i++)
            current_table.tHead.rows[0].children[i].style = "width:" + current_table.tBodies[0].rows[0].cells[i].clientWidth + ";";

        document.getElementById("div-journal-headline").style.cssText = "height:" + (clientHeight > 850 ? "3" : clientHeight <= 630 ? "7" : clientHeight <= 700 ? "5" : "4") + "%";
        document.getElementsByClassName("first_row_tasks_list")[0].style.cssText = "top:" + (clientHeight > 850 ? "-2.5" : clientHeight <= 630 ? "-1" : clientHeight <= 700 ? "-1.5" : "-2") + "%";
        document.getElementsByClassName("second_row")[0].style.cssText = "font-size:" + (clientHeight > 850 ? "0" : clientHeight <= 630 ? "22" : clientHeight <= 700 ? "15" : "8") + "px";

    }

</script>

<form method="post" action="${pageContext.request.contextPath}/doc_memorandum_journal"
      style="overflow:hidden; height:99%" autocomplete="off" name="doc_memorandum_journal">

    <div style="height:3%" id="div-journal-headline">
        <div class="horizontal">
            <div><a href=${PageContainer.WORK_AREA_PAGE}><img
                    src="${pageContext.request.contextPath}/resources/images/back_on_main_page.png"></a></div>
            <div><h2 class="left_up_panel">Журнал служебных записок</h2></div>
        </div>
    </div>

    <div style="height:2%">
        <div><label>&nbsp;</label></div>
    </div>

    <div class="tabs">
        <c:choose>
            <c:when test="${bookMark1=='tasksListByGroup'}">
                <div class="open">
                    По папкам
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <button name="bookMark1" value='tasksListByGroup' class="btn-link2" id='a1'>
                        По папкам
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${bookMark1=='fullTasksList'}">
                <div class="open last-element">
                    Общий список
                </div>
            </c:when>
            <c:otherwise>
                <div class="last-element">
                    <button name="bookMark1" value='fullTasksList' class="btn-link2" id='a2'>
                        Общий список
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="horizontal">
        <c:choose>
            <c:when test="${bookMark1=='tasksListByGroup'}">
                <div style="height:100%;width:11%;vertical-align:top;">
                    <div class="menu" style="width:95%">
                        <table id="folder_structure" class="folder_structure">
                            <tbody></tbody>
                        </table>
                    </div>
                </div>

                <div style="height:100%;width:0.5%;
                    background-color:rgb(26, 219, 8); vertical-align:top;">&nbsp;
                </div>

                <div style="height:99%;width:87%;">
                    <div style="height:3%">&nbsp;</div>
                    <div style="height:5%" id="div-groupBy">
                        <div class="horizontal">
                            <div>
                                <button name="groupBy" value="${groupBy=='sender' ? 'author':'sender'}">
                                    <img ${isGroupBySender==true ? 'class="image-group-switcher"' : ''}
                                            src="${pageContext.request.contextPath}/resources/images/user.png"></button>
                            </div>
                            <div class="search-string-journal"><input name="tasksListByGroupFilterString"
                                                                      placeholder="Поиск"
                                                                      onkeyup="onKeyupSearchString(arguments[0],'doc_memorandum_journal')"
                                                                      value="${filterString}"/></div>
                        </div>
                    </div>
                    <div class="table-wrapper-tasks-list">
                        <div class="table-scroll-tasks-list">
                            <table class="table-tasks" id="table-tasksList"></table>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div style="height:100%;width:100%;">
                    <div style="height:3%">&nbsp;</div>
                    <div style="height:5%">&nbsp;</div>
                    <div class="search-string-journal"><input name="fullTasksListFilterString" placeholder="Поиск"
                                                              onkeyup="onKeyupSearchString(arguments[0],'doc_memorandum_journal')"
                                                              value="${filterString}"/></div>
                    <div class="table-wrapper-tasks-list">
                        <div class="table-scroll-tasks-list">
                            <table class="table-tasks" id="table-tasksList-all"></table>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</form>

<form hidden id="myForm" target="_blank">
    <input type="hidden" name="documentId"/>
    <input type="hidden" name="executorTaskId"/>
    <input type="hidden" name="tempId"/>
</form>

</body>

</html>
