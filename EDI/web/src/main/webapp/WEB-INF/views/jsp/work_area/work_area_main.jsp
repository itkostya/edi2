<%--@elvariable id="TimeModule" type="enumerations"--%>
<%@ page import="app_info.TimeModule" %>

<%--@elvariable id="DocumentProperty" type="enumerations"--%>
<%@ page import="documents.DocumentProperty" %>

<%--@elvariable id="CommonModule" type="enumerations"--%>
<%@ page import="tools.CommonModule" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="bookMark" type="java.lang.String"--%>
<%--@elvariable id="controlledTasksList" type="java.util.List<business_processes.ExecutorTask>"--%>
<%--@elvariable id="coworkersList" type="java.util.List<categories.User>"--%>
<%--@elvariable id="filterString" type="java.lang.String"--%>
<%--@elvariable id="mapSortValue" type="java.lang.String"--%>
<%--@elvariable id="markedTasksList" type="java.util.List<business_processes.ExecutorTaskFolderStructure>"--%>
<%--@elvariable id="memorandumCount" type="java.lang.Integer"--%>
<%--@elvariable id="messageCount" type="java.lang.Integer"--%>
<%--@elvariable id="reviewTasksList" type="java.util.List<business_processes.ExecutorTask> "--%>
<%--@elvariable id="userAccessRightList" type="java.util.List<categories.UserAccessRight>"--%>
<%--@elvariable id="userPresentation" type="java.lang.String"--%>

<html>

<head>
    <title>User workspace</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/work_area/work_area_main.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
</head>

<body>

<script>

    window.onload = function () {

        <c:choose>
        <c:when test="${bookMark == 'reviewTasksList'}">
        refreshReviewTasks(document.getElementById("table-review-tasks"));
        </c:when>
        <c:when test="${bookMark == 'controlledTasksList'}">
        refreshControlledTasks(document.getElementById("table-controlled-tasks"));
        </c:when>
        <c:when test="${bookMark == 'markedTasksList'}">
        refreshMarkedTasks(document.getElementById("table-marked-tasks"));
        </c:when>
        <c:when test="${bookMark == 'rightsList'}">
        refreshCategoryTableRights(document.getElementById("table-rights-list"));
        </c:when>
        <c:when test="${bookMark == 'coworkersList'}">
        refreshCoworkers(document.getElementById("table-coworkers"));
        </c:when>
        </c:choose>

        <c:set value="${bookMark}" var="currentBookMark"/>

        resizeElements();
        window.addEventListener('resize', resizeElements);

        <c:set value="${messageCount!=null && messageCount!=0}" var="isExistsMessageCount"/>
        <c:set value="${memorandumCount!=null && memorandumCount!=0}" var="isExistsMemorandumCount"/>

        setSizeJournalButtons();

    };

    //  -----  Element's size BEGIN -----

    function resizeElements() {

        const clientWidth = document.firstChild.clientWidth;
        const clientHeight = document.firstChild.clientHeight;
        let current_table = document.getElementById(
            "${ bookMark=='reviewTasksList' ? 'table-review-tasks':
                bookMark == 'controlledTasksList' ? 'table-controlled-tasks' :
                bookMark == 'markedTasksList' ? 'table-marked-tasks' :
                bookMark == 'rightsList' ? 'table-rights-list':
                bookMark == 'coworkersList' ? 'table-coworkers':
                ''}");
        let i;

        for (i = 0; i < current_table.tHead.rows[0].children.length; i++)
            current_table.tHead.rows[0].children[i].style = current_table.tBodies[0].rows.length > 0 ? "width:" + current_table.tBodies[0].rows[0].cells[i].clientWidth + ";" : "";

        <c:choose>
        <c:when test="${bookMark == 'reviewTasksList'}">
        if (current_table.tHead.rows[0].children[0].clientHeight > 18) {
            current_table.tHead.rows[0].children[0].children[0].innerHTML = current_table.tHead.rows[0].children[0].children[0].innerHTML.replace("Тип процесса", "Тип");
            current_table.tHead.rows[0].children[4].children[0].innerHTML = current_table.tHead.rows[0].children[4].children[0].innerHTML.replace("Дата отправки", "Дата");
        }
        </c:when>
        <c:when test="${bookMark == 'controlledTasksList'}">
        if (current_table.tHead.rows[0].children[0].clientHeight > 18) {
            current_table.tHead.rows[0].children[0].children[0].innerHTML = current_table.tHead.rows[0].children[0].children[0].innerHTML.replace("Тип процесса", "Тип");
            current_table.tHead.rows[0].children[4].children[0].innerHTML = current_table.tHead.rows[0].children[4].children[0].innerHTML.replace("Исполнить до", "Исп. до");
        }
        </c:when>
        <c:when test="${bookMark == 'coworkersList'}">
        if (current_table.tBodies[0].rows[0].cells[2].clientWidth < 105) {
            current_table.tHead.rows[0].children[2].innerHTML = (current_table.tBodies[0].rows[0].cells[2].clientWidth < 40 ? "" :
                current_table.tBodies[0].rows[0].cells[2].clientWidth < 70 ? "Подр." : "Подразд.");
        }

        if (current_table.tBodies[0].rows[0].cells[1].clientWidth < 80) {
            current_table.tHead.rows[0].children[1].innerHTML = (current_table.tBodies[0].rows[0].cells[1].clientWidth < 40 ? "" : "Должн.");
        }

        </c:when>
        </c:choose>

        document.getElementById("div-for-main-table").style.cssText = "height:" + (clientHeight > 850 ? "75.5" : clientHeight <= 630 ? "64" : clientHeight <= 700 ? "69" : "72") + "%";
        document.getElementsByClassName("first_row_task_table")[0].style.cssText = "top:" + (clientHeight > 850 ? "-3" : clientHeight <= 630 ? "0" : clientHeight <= 700 ? "-1" : "-2") + "%";
        document.getElementsByClassName("second_row")[0].style.cssText = "font-size:" + (clientHeight > 850 ? "0" : clientHeight <= 630 ? "22" : clientHeight <= 700 ? "15" : "8") + "px";

        if (clientWidth < 850) {
            let textInTablesHeadlines = document.getElementsByClassName("text-in-table-headline");
            for (i = 0; i < textInTablesHeadlines.length; i++) {
                textInTablesHeadlines[i].innerHTML = textInTablesHeadlines[i].innerHTML.replace("На рассмотрении", "").replace("На контроле", "").replace("Отмеченные", "").replace("Сотрудники", "").replace("Доступные объекты", "");
            }
        }

    }

    function setSizeJournalButtons() {

        let i;
        let ratio = document.firstChild.clientHeight / (document.getElementById("page-switcher").offsetTop - (document.getElementById("user-presentation").offsetTop + document.getElementById("user-presentation").offsetHeight));
        const percentRatio = 7;
        let size = 72;
        if (ratio < percentRatio)
            size = document.getElementById("page-switcher").offsetTop - (document.getElementById("user-presentation").offsetTop + document.getElementById("user-presentation").offsetHeight);
        if (size > 72) size = 72;

        document.getElementById("div-documents").style.cssText = "height:7%; height:" + size + "px; width:" + size + "px";

        let docImage = document.getElementsByName("doc-image");
        for (i = 0; i < docImage.length; i++) docImage[i].style.cssText = "height:" + size + "px; width:" + size + "px";

        let numberInCircle = document.getElementsByClassName("number_in_circle");
        for (i = 0; i < numberInCircle.length; i++)
            numberInCircle[i].style.cssText = "" + (numberInCircle[i].style.cssText.indexOf("background: none;") >= 0 ? "background: none;" : "")
                + "height:" + Math.round(size / 3) + "px; width:" + Math.round(size / 3) + "px; top:-" + (size - 1) + "px; left:-1px;border-radius:" + Math.round(size / 3) + "px; font-size: " + (size > 50 ? "medium;" : "x-small;");

    }

    //  -----  Element's size END -----

    //  -----  Work with tables BEGIN -----

    function refreshReviewTasks(current_table) {

        let body, row;
        let row_style, img_marked;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'}? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";
        insertCellInRow(0, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, '+') + '">' + getColSymbol("0", colNum, colOrd, defSort, '&darr;') + 'Тип процесса</button>'); // Down (asc)
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="1.' + getColOrder("1", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("1", colNum, colOrd, defSort, '') + 'Кто</button>');
        insertCellInRow(2, row, '<button name="sortColumn" class="btn-link2" value="2.' + getColOrder("2", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("2", colNum, colOrd, defSort, '') + 'Документ</button>');
        insertCellInRow(3, row, '<button name="sortColumn" class="btn-link2" value="3.' + getColOrder("3", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("3", colNum, colOrd, defSort, '') + 'Тема</button>');
        insertCellInRow(4, row, '<button name="sortColumn" class="btn-link2" value="4.' + getColOrder("4", colNum, colOrd, defSort, '-') + '">' + getColSymbol("4", colNum, colOrd, defSort, '&uarr;') + 'Дата отправки</button>'); // Up (desc)
        insertCellInRow(5, row, '<button name="sortColumn" class="btn-link2" value="5.' + getColOrder("5", colNum, colOrd, defSort, '+') + '">' + getColSymbol("5", colNum, colOrd, defSort, '&darr;') + 'Срок</button>');  // Down (asc)

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${reviewTasksList}" varStatus="status">
        row = body.insertRow(${status.index});
        img_marked = '<img class="image-for-table" src="/resources/images/enumerators/ProcessType/${cell.processType.enName.toLowerCase()}/accept.png">';
        insertCellInRow(0, row, img_marked + getHighlightedText('  ${cell.processType.ruName}', filterString));
        insertCellInRow(1, row, getHighlightedText('${cell.author.fio}', filterString));
        insertCellInRow(2, row, getHighlightedText('${cell.document.getDocumentView("dd.MM.yyyy HH:mm:ss")}', filterString));

        img_marked = '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/documents/${cell.document.documentProperty.enName.toLowerCase()}/document_type.png">';
        insertCellInRow(3, row, img_marked + getHighlightedText(' ${CommonModule.getCorrectStringForWeb(cell.document.theme)}', filterString));

        insertCellInRow(4, row, getHighlightedText('${TimeModule.getDate(cell.date, 'dd.MM.yyyy HH:mm:ss')}', filterString));
        insertCellInRow(5, row, getHighlightedText('${TimeModule.getDate(cell.finalDate, 'dd.MM.yyyy')}', filterString));

        <c:choose>
        <c:when test="${TimeModule.startOfCurrentDay().equals(TimeModule.startOfDay(cell.finalDate))}">
        row.cells[2].style = "font: bold 100% serif;";
        row.cells[5].style = "font: bold 100% serif;";
        </c:when>
        </c:choose>

        row_style = "";
        <c:choose><c:when test="${TimeModule.startOfDay(cell.finalDate) < TimeModule.startOfCurrentDay()}">
        row_style += "color:red;";
        </c:when></c:choose>
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += " background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style = row_style;

        row.ondblclick = function () {
            onClickOpenTask("formOpenTask", ${cell.document.id}, ${cell.id}, false, "${cell.document.documentProperty}");
            <c:choose><c:when test="${cell.document.documentProperty == DocumentProperty.MESSAGE}">
                    setTimeout(function() {window.location.reload(true);}, 5000);
            </c:when></c:choose>
        };

        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td><td class='td_sr'>5</td><td class='td_sr'>6</td></tr>";

    }

    function refreshControlledTasks(current_table) {

        let body, row;
        let row_style, img_marked;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'}? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";
        insertCellInRow(0, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, '+') + '">' + getColSymbol("0", colNum, colOrd, defSort, '&darr;') + 'Тип процесса</button>'); // Down
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="1.' + getColOrder("1", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("1", colNum, colOrd, defSort, '') + 'Кому</button>');
        insertCellInRow(2, row, '<button name="sortColumn" class="btn-link2" value="2.' + getColOrder("2", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("2", colNum, colOrd, defSort, '') + 'Документ</button>');
        insertCellInRow(3, row, '<button name="sortColumn" class="btn-link2" value="3.' + getColOrder("3", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("3", colNum, colOrd, defSort, '') + 'Тема</button>');
        insertCellInRow(4, row, '<button name="sortColumn" class="btn-link2" value="4.' + getColOrder("4", colNum, colOrd, defSort, '+') + '">' + getColSymbol("4", colNum, colOrd, defSort, '&darr;') + 'Исполнить до</button>'); // Down

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${controlledTasksList}" varStatus="status">
        row = body.insertRow(${status.index});
        img_marked = '<img class="image-for-table" src="/resources/images/enumerators/ProcessType/${cell.processType.enName.toLowerCase()}/accept.png">  ';
        insertCellInRow(0, row, img_marked + getHighlightedText('${cell.processType.ruName}', filterString));
        insertCellInRow(1, row, getHighlightedText('${cell.executor.fio}', filterString));
        insertCellInRow(2, row, getHighlightedText('${cell.document.getDocumentView("dd.MM.yyyy HH:mm:ss")}', filterString));

        img_marked = '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/documents/${cell.document.documentProperty.enName.toLowerCase()}/document_type.png">';
        insertCellInRow(3, row, img_marked + getHighlightedText('  ${CommonModule.getCorrectStringForWeb(cell.document.theme)}', filterString));

        insertCellInRow(4, row, getHighlightedText('${TimeModule.getDate(cell.finalDate, 'dd.MM.yyyy')}', filterString));

        <c:choose>
        <c:when test="${TimeModule.startOfCurrentDay().equals(TimeModule.startOfDay(cell.finalDate))}">
        row.cells[2].style = "font: bold 100% serif;";
        row.cells[4].style = "font: bold 100% serif;";
        </c:when>
        </c:choose>

        row_style = "";
        <c:choose><c:when test="${TimeModule.startOfDay(cell.finalDate) < TimeModule.startOfCurrentDay()}">
        row_style += "color:red;";
        </c:when></c:choose>
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += " background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style = row_style;

        row.ondblclick = function () {
            onClickOpenTask("formOpenTask", ${cell.document.id}, null, false, "${cell.document.documentProperty}")
        };
        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td><td class='td_sr'>5</td></tr>";

    }

    function refreshMarkedTasks(current_table) {

        let body, row;
        let row_style, img_marked;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'}? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";
        insertCellInRow(0, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("0", colNum, colOrd, defSort, '') + 'Номер');
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="1.' + getColOrder("1", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("1", colNum, colOrd, defSort, '') + 'Документ');
        insertCellInRow(2, row, '<button name="sortColumn" class="btn-link2" value="2.' + getColOrder("2", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("2", colNum, colOrd, defSort, '') + 'Тема');
        insertCellInRow(3, row, '<button name="sortColumn" class="btn-link2" value="3.' + getColOrder("3", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("3", colNum, colOrd, defSort, '') + 'Автор');
        insertCellInRow(4, row, '<button name="sortColumn" class="btn-link2" value="4.' + getColOrder("4", colNum, colOrd, defSort, '-') + '">' + getColSymbol("4", colNum, colOrd, defSort, '&uarr;') + 'Дата'); // Up (desc)

        body = current_table.appendChild(document.createElement('tbody'));
        //markedTasksList: List<ExecutorTaskFolderStructure> getExecutorList(User currentUser, FolderStructure.MARKED)
        <c:forEach var="cell" items="${markedTasksList}" varStatus="status">
        row = body.insertRow(${status.index});
        insertCellInRow(0, row, getHighlightedText('${cell.executorTask.document.number}', filterString));
        insertCellInRow(1, row, getHighlightedText('${cell.executorTask.document.getDocumentView("dd.MM.yyyy HH:mm:ss")}', filterString));

        img_marked = '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/documents/memorandum/document_type.png">';
        insertCellInRow(2, row, img_marked + getHighlightedText('  ${CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)}', filterString));

        insertCellInRow(3, row, getHighlightedText('${cell.executorTask.author.fio}', filterString));
        insertCellInRow(4, row, getHighlightedText('${TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')}', filterString));

        row_style = "";
        <c:choose><c:when test="${!cell.executorTask.completed}">row_style += "color:red; font: bold 100% serif;";
        </c:when></c:choose>
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += " background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style = row_style;

        row.ondblclick = function () {
            onClickOpenTask("formOpenTask", ${cell.executorTask.document.id}, null, ${cell.executorTask.draft}, "${cell.executorTask.document.documentProperty}")
        };

        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td><td class='td_sr'>5</td></tr>";

    }

    function refreshCategoryTableRights(current_table) {

        let body, row;
        let row_style;

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";  // Better have name "first_row_work_area" for every table

        insertCellInRow(0, row, 'Метаданные');
        insertCellInRow(1, row, 'Тип метаданных');

        body = current_table.appendChild(document.createElement('tbody'));

        let statusIndex = 0;
        <c:forEach var="cell" items="${userAccessRightList}" varStatus="status">
            <c:choose><c:when test="${true == (cell.view || cell.edit)}">
                row = body.insertRow(statusIndex);
                insertCellInRow(0, row, '<a href="${pageContext.request.contextPath}${PageContainer.getJournalPage(cell.metadataType)}" target="_blank" class="link-like-text">${cell.metadataType.metadata}</a>');
                insertCellInRow(1, row, '<a href="${pageContext.request.contextPath}${PageContainer.getJournalPage(cell.metadataType)}" target="_blank" class="link-like-text">' +
                    '<img class="image-for-table" src="/resources/images/data_processors/'.concat((true === ${cell.edit} ? "edit" : "view")).concat('.png">${cell.metadataType}</a>'));
                row.style.cssText += ((statusIndex % 2) === 0 ? " background: rgb(255, 248, 234); " : "");
                statusIndex++;
            </c:when></c:choose>
        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row' style='font-size: 0;'><td class='td_sr'>1</td><td class='td_sr'>2</td></tr>";

    }

    function refreshCoworkers(current_table) {

        let body, row;
        let row_style;
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_task_table";
        insertCellInRow(0, row, 'Ф. И. О.');
        insertCellInRow(1, row, 'Должность');
        insertCellInRow(2, row, 'Подразделение');

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${coworkersList}" varStatus="status">
        row = body.insertRow(${status.index});
        insertCellInRow(0, row, getHighlightedText('${cell.fio}', filterString));
        insertCellInRow(1, row, getHighlightedText('${cell.position.name}', filterString));
        insertCellInRow(2, row, getHighlightedText('${cell.department.name}', filterString));

        row_style = "";
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += " background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style.cssText = row_style;

        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td></tr>";

    }

    //  -----  Work with tables END -----

</script>

<div style="height:3%">
    <div class="horizontal">
        <div><h2 class="left_up_panel">Main Panel</h2></div>
        <div><a href=${PageContainer.USER_PAGE}>
            <img src="${pageContext.request.contextPath}/resources/images/black-logout-16.png"></a></div>
    </div>
</div>

<form method="post" action="${pageContext.request.contextPath}/work_area" style="overflow:hidden; height:96%"
      autocomplete="off" name="work_area" id="work_area">

    <h1 class="user_presentation" id="user-presentation">${userPresentation}</h1>

    <div style="height:7%" id="div-documents">
        <table>
            <tr>
                <td>
                    <div class="relative-with-border" id="div-document-message">
                        <div>
                            <a href='${pageContext.request.contextPath}/doc_message_journal'>
                                <img src="${pageContext.request.contextPath}/resources/images/documents/message/work_area.png"
                                     alt="Сообщение" class="button_blue" name="doc-image"></a>
                        </div>
                        <div class="number_in_circle" ${isExistsMessageCount ? '' : 'style="background: none;"'}
                             onclick="window.alert('Количество входящих сообщений');">
                            ${isExistsMessageCount ? messageCount : ''} </div>
                    </div>
                </td>
                <td>
                    <div class="relative-with-border" id="div-document-memorandum">
                        <div>
                            <a href='${pageContext.request.contextPath}/doc_memorandum_journal'>
                                <img src="${pageContext.request.contextPath}/resources/images/documents/memorandum/work_area.png"
                                     alt="Служебная записка" class="button_lime" name="doc-image"></a>
                        </div>
                        <div class="number_in_circle" ${isExistsMemorandumCount ? '' : 'style="background: none;"'}
                             onclick="window.alert('Количество входящих служебных записок');">
                            ${isExistsMemorandumCount ? memorandumCount : ''}</div>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <div style="height:2%" id="separator"><label>&nbsp;</label></div>

    <div class="tabs" id="page-switcher">
        <c:choose>
            <c:when test="${bookMark=='reviewTasksList'}">
                <div class="open">
                    <img class="image-for-table"
                         src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/information/accept.png">
                    <span class="text-in-table-headline">На рассмотрении</span>
                </div>
            </c:when>
            <c:otherwise>
                <div><img class="image-for-table"
                          src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/information/accept.png"
                          onclick="document.getElementById('1').click();">
                    <button name="bookMark" value='reviewTasksList' class="btn-link2" id='1'>
                        <span class="text-in-table-headline">На рассмотрении</span>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${bookMark=='controlledTasksList'}">
                <div class="open"><img class="image-for-table"
                                       src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/affirmation/accept.png">
                    <span class="text-in-table-headline">На контроле</span>
                </div>
            </c:when>
            <c:otherwise>
                <div><img class="image-for-table"
                          src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/affirmation/accept.png"
                          onclick="document.getElementById('2').click();">
                    <button name="bookMark" value='controlledTasksList' class="btn-link2" id='2'>
                        <span class="text-in-table-headline">На контроле</span>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${bookMark=='markedTasksList'}">
                <div class="open"><img class="image-for-table"
                                       src="${pageContext.request.contextPath}/resources/images/command-bar/mark.png">
                    <span class="text-in-table-headline">Отмеченные</span>
                </div>
            </c:when>
            <c:otherwise>
                <div><img class="image-for-table"
                          src="${pageContext.request.contextPath}/resources/images/command-bar/mark.png"
                          onclick="document.getElementById('3').click();">
                    <button name="bookMark" value='markedTasksList' class="btn-link2" id='3'>
                        <span class="text-in-table-headline">Отмеченные</span>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${bookMark=='rightsList'}">
                <div class="open"><img class="image-for-table"
                                       src="${pageContext.request.contextPath}/resources/images/data_processors/edit.png">
                    <span class="text-in-table-headline">Доступные объекты</span>
                </div>
            </c:when>
            <c:otherwise>
                <div><img class="image-for-table"
                          src="${pageContext.request.contextPath}/resources/images/data_processors/edit.png"
                          onclick="document.getElementById('4').click();">
                    <button name="bookMark" value='rightsList' class="btn-link2" id='4'>
                        <span class="text-in-table-headline">Доступные объекты</span>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${bookMark=='coworkersList'}">
                <div class="open" style="border-right: 1px solid black">
                    <img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/user.png">
                    <span class="text-in-table-headline">Сотрудники</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="last-element">
                    <img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/user.png"
                         onclick="document.getElementById('coworkersList').click();">
                    <button name="bookMark" value='coworkersList' class="btn-link2"
                            id='coworkersList'>
                        <span class="text-in-table-headline">Сотрудники</span></button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:choose>
        <c:when test="${bookMark=='reviewTasksList'}">
            <div style="height:6%" class="horizontal">
                <div>
                    <table>
                        <tr>
                            <td>
                                <button name="bookMark" value='reviewTasksList'>
                                    <img class="command-bar-refresh"
                                         src="${pageContext.request.contextPath}/resources/images/refresh.png"/>
                                    Обновить
                                </button>
                            </td>
                            <td>
                                <ul id=menu>
                                    <li>
                                        <div class="div-like-button">
                                            <div class="command-bar-add-document"></div>
                                            Создать документ ▼
                                        </div>
                                        <ul>
                                            <c:forEach var="cell" items="${DocumentProperty.values()}">
                                                <li><a href="javascript:void(0)"
                                                       onclick="createNewDocument('create', '${cell.enName}','')" target="_blank">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Создать ${cell.ruName}</a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </li>
                                </ul>
                            </td>
                    </table>
                </div>
                <div class="search-string"><input name="reviewTasksListFilterString" placeholder="Поиск"
                                                  onkeyup="onKeyupSearchString(arguments[0],'work_area')"
                                                  value="${filterString}"/></div>
            </div>
            <div style="height:75.5%;" id="div-for-main-table">
                <div class="table-wrapper">
                    <div class="table-scroll-review-tasks">
                        <table class="table-tasks" id="table-review-tasks"></table>
                    </div>
                </div>
            </div>
        </c:when>

        <c:when test="${bookMark=='controlledTasksList'}">
            <div style="height:6%" class="horizontal">
                <div>
                    <table>
                        <tr>
                            <td>
                                <button name="bookMark" value='controlledTasksList'>
                                    <img class="command-bar-refresh"
                                         src="${pageContext.request.contextPath}/resources/images/refresh.png">
                                    Обновить
                                </button>
                            </td>
                            <td>Отменить</td>
                        </tr>
                    </table>
                </div>
                <div class="search-string"><input name="controlledTasksListFilterString" placeholder="Поиск"
                                                  onkeyup="onKeyupSearchString(arguments[0],'work_area')"
                                                  value="${filterString}"/></div>
            </div>

            <div style="height:75.5%;" id="div-for-main-table">
                <div class="table-wrapper">
                    <div class="table-scroll-review-tasks">
                        <table class="table-tasks" id="table-controlled-tasks"></table>
                    </div>
                </div>
            </div>
        </c:when>

        <c:when test="${bookMark=='markedTasksList'}">
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
                        <table class="table-tasks" id="table-marked-tasks"></table>
                    </div>
                </div>
            </div>
        </c:when>

        <c:when test="${bookMark=='rightsList'}">
            <div style="height:6%" class="horizontal">
                <div>
                    <table>
                        <tr>
                            <td>
                                <button name="bookMark" value='rightsList'>
                                    <img class="command-bar-refresh"
                                         src="${pageContext.request.contextPath}/resources/images/refresh.png">
                                    Обновить
                                </button>
                            </td>
                            <td></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div style="height:75.5%;" id="div-for-main-table">
                <div class="table-wrapper">
                    <div class="table-scroll-review-tasks">
                        <table class="table-rights" id="table-rights-list"></table>
                    </div>
                </div>
            </div>
        </c:when>

        <c:when test="${bookMark=='coworkersList'}">
            <div style="height:6%" class="horizontal">
                <div>
                    <table>
                        <tr>
                            <td>Подразделение</td>
                            <td>День Рождения</td>
                        </tr>
                    </table>
                </div>
                <div class="search-string"><input name="coworkersListFilterString" placeholder="Поиск"
                                                  onkeyup="onKeyupSearchString(arguments[0],'work_area')"
                                                  value="${filterString}"/></div>
            </div>
            <div style="height:75.5%;" id="div-for-main-table">
                <div class="table-wrapper">
                    <div class="table-scroll-review-tasks">
                        <table class="table-coworkers" id="table-coworkers"></table>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>

    <input type="hidden" name="tempId" value="${param.tempId}" id="tempId"/>

</form>

<form hidden action="" id="createDocument" target="_blank">
    <input type="hidden" name="tempId"/>
    <input type="hidden" name="documentCopyId"/>
    <input type="hidden" name="operationType">
</form>

<form hidden action="${pageContext.request.contextPath}/executor_task" id="formOpenTask" target="_blank">
    <input type="hidden" name="documentId"/>
    <input type="hidden" name="executorTaskId"/>
    <input type="hidden" name="tempId"/>
</form>

<form hidden action="${pageContext.request.contextPath}/work_area">
    <input type="hidden" name="mapSortValue"/>
</form>

</body>

</html>
