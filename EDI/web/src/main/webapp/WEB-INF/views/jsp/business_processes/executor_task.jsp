<%--@elvariable id="Constant" type="enumerations"--%>
<%@ page import="app_info.Constant" %>

<%--@elvariable id="TimeModule" type="enumerations"--%>
<%@ page import="app_info.TimeModule" %>

<%--@elvariable id="ProcessResult" type="enumerations"--%>
<%@ page import="enumerations.ProcessResult" %>

<%--@elvariable id="ProcessType" type="enumerations"--%>
<%@ page import="enumerations.ProcessType" %>

<%--@elvariable id="ElementStatus" type="enumerations"--%>
<%@ page import="model.ElementStatus" %>

<%--@elvariable id="CommonModule" type="enumerations"--%>
<%@ page import="tools.CommonModule" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="commentForRecipients" type="java.lang.String"--%>
<%--@elvariable id="currentUser" type="categories.User"--%>
<%--@elvariable id="documentEdi" type="abstract_entity.AbstractDocumentEdi"--%>
<%--@elvariable id="executorTask" type="business_processes.ExecutorTask"--%>
<%--@elvariable id="infoResult" type="java.lang.String"--%>
<%--@elvariable id="isMarkedExecutorTask" type="boolean"--%>
<%--@elvariable id="markedAvailable" type="boolean"--%>
<%--@elvariable id="mapHistory" type="java.util.Map<business_processes.BusinessProcess, java.util.List<business_processes.BusinessProcessSequence>>"--%>
<%--@elvariable id="mapSignatures" type="java.util.Map<business_processes.ExecutorTask, java.util.List<categories.UploadedFile>>"--%>
<%--@elvariable id="mapStop" type="java.util.Map<enumerations.ProcessType, java.util.List<business_processes.BusinessProcessSequence>> "--%>
<%--@elvariable id="processType" type="enumerations.ProcessType"--%>
<%--@elvariable id="sessionDataElement" type="model.SessionDataElement"--%>
<%--@elvariable id="tempId" type="java.lang.Long"--%>
<%--@elvariable id="uploadedFiles" type="java.util.List<categories.UploadedFile>"--%>
<%--@elvariable id="withdrawAvailable" type="boolean"--%>

<html>

<head>
    <link href="<c:url value="/resources/css/business_processes/executor_task.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/business_processes/tree.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/common/modal_forms.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
    <jsp:include page="../common/send_document.jsp"/>
    <jsp:include page="../common/add_files.jsp"/>
</head>

<body lang=RU style='tab-interval:35.4pt;background:#ddd'>

<script>

    let table_users;
    let table_recipients;
    let process_type;
    let recipients_row_index;
    const fileList = [];
    let uploadedFileList;   // hasn't been used (yet?) - add for common work with add_files.jsp
    let rowMarkedTableIndex;  // works only with 1-st table (in add_files.jsp 2 tables - 0, 1 )
    let rowMarkedIndex;

    window.onload = function () {

//        window.alert("onLoad");
        <%--window.alert("executorTask.result=${executorTask.result}");--%>
        <%--window.alert("sessionDataElement.elementStatus: ${sessionDataElement.elementStatus}");--%>
        table_users = null;         // Filled in send_document.jsp
        table_recipients = null;    // Filled in send_document.jsp
        process_type = -1;
        recipients_row_index = -1;
        uploadedFileList = null;
        rowMarkedTableIndex = -1;
        rowMarkedIndex = -1;

        <c:choose>
        <c:when test="${sessionDataElement.elementStatus == ElementStatus.CLOSE}">window.close();
        </c:when>
        <c:when test="${sessionDataElement.elementStatus == ElementStatus.UPDATE}">window.opener.location.reload(true); // Reload parent's page
        </c:when>
        </c:choose>

        <c:set value="${(isMarkedExecutorTask == true)}" var="isMarkedElement"/>

        setCommandBarMenu();
        setMainView();
        setFiles();
        setSignatures();
        setHistory();
        setStop();

        document.getElementById("menu_send_to_users").innerHTML = createMenuSendToUsers();
        document.getElementById("menu_choose_users").innerHTML = createMenuChooseUsers();
        setUsersOnPage();

        resizeElements();
        window.addEventListener('resize', resizeElements);

    };

    function resizeElements() {

        const clientWidth = document.firstChild.clientWidth-(${processType == ProcessType.INFORMATION} ? 0 : 200);
        const clientHeight = window.innerHeight;

        if (clientWidth < 1275) {
            replaceTextInHtmlElement(document.getElementById("command-bar-stop"), "Отменить", "");
            if (clientWidth < 1140) {
                replaceTextInHtmlElement(document.getElementById("command-bar-trash"), "Корзина", "");
                replaceTextInHtmlElement(document.getElementById("command-bar-restore"), "Восстановить", "");
                if (clientWidth < 1040) {
                    replaceTextInHtmlElement(document.getElementById("command-bar-history"), "История", "");
                    if (clientWidth < 925) {
                        replaceTextInHtmlElement(document.getElementById("command-bar-send"), "Отправить", "");
                        if (clientWidth < 785) {
                            if (document.getElementById("command-bar-accept") !== null) {
                                document.getElementById("command-bar-accept").style.cssText = "display: none;";
                            }
                            if (document.getElementById("command-bar-decline") !== null) {
                                document.getElementById("command-bar-decline").style.cssText = "display: none;";
                            }
                            replaceTextInHtmlElement(document.getElementById("text-send-forward"), "Переслать", "");
                            replaceTextInHtmlElement(document.getElementById("text-send-reply"), "Ответ", "");
                        }
                    }
                }
            }
        }

        document.getElementById("page0_textarea").style.cssText = "width:95%; height:" + (clientHeight > 930 ? "70" : clientHeight <= 630 ? "46" : clientHeight <= 780 ? "58" : "65") + "%";
        document.getElementById("div-table-wrapper").style.cssText = "width:95%; height:" + (clientHeight > 930 ? "57" : clientHeight <= 630 ? "28" : clientHeight <= 780 ? "40" : "50") + "%";
        resizeElementsSendDocument();

    }

    function setCommandBarMenu() {

        const command_bar_trash = document.getElementById("command_bar_trash");
        <c:set value="${(executorTask.deletedByAuthor && executorTask.author == currentUser)||(executorTask.deletedByExecutor && executorTask.executor == currentUser)}" var="isTrash"/>

        <c:choose>
        <c:when test="${(executorTask != null)}">
            <c:choose>
            <c:when test="${isTrash}">
                command_bar_trash.innerHTML = '<div>' +
                    '<a href="#" class="link-like-button" onclick="changeCommandBarParameter(\'restore-trash\',\'Восстановить\')">'+
                    '<div class="command-bar-restore"></div>' +
                    '<span id="command-bar-restore">Восстановить</span></a></div>';
            </c:when>
            <c:otherwise>
                command_bar_trash.innerHTML = '<div>' +
                    '<a href="#" class="link-like-button" onclick="changeCommandBarParameter(\'in-trash\',\'Корзина\')">'+
                    '<div class="command-bar-trash"></div>' +
                    '<span id="command-bar-trash">Корзина</span></a></div>';
            </c:otherwise>
            </c:choose>
        </c:when>
        </c:choose>

    }

    function setMainView() {

        <c:choose>
        <c:when test="${not empty documentEdi}">

            <c:import var="documentView" url="../documents/${documentEdi.documentProperty.enName.toLowerCase()}_view.jsp" scope="session"/>
            document.getElementById("review_document").innerHTML = "${CommonModule.getCorrectStringJspPage(documentView)}";

            <c:choose>
            <c:when test="${((documentView != null)&&(documentView.contains('command-bar-buttons-accept-decline-'.concat(documentEdi.documentProperty.enName.toLowerCase()))))}">
                document.getElementById("command-bar-buttons-accept-decline").innerHTML = document.getElementById("command-bar-buttons-accept-decline-${documentEdi.documentProperty.enName.toLowerCase()}").innerHTML;
                document.getElementById("command-bar-buttons-accept-decline-${documentEdi.documentProperty.enName.toLowerCase()}").outerHTML = "";
            </c:when>
            </c:choose>

            <c:choose>
            <c:when test="${((documentView != null)&&(documentView.contains('command-bar-buttons-send-'.concat(documentEdi.documentProperty.enName.toLowerCase()))))}">
                document.getElementById("command-bar-buttons-send").innerHTML = document.getElementById("command-bar-buttons-send-${documentEdi.documentProperty.enName.toLowerCase()}").innerHTML;
                document.getElementById("command-bar-buttons-send-${documentEdi.documentProperty.enName.toLowerCase()}").outerHTML = "";
            </c:when>
            </c:choose>

        </c:when>
        </c:choose>

    }

    function setProcessType(text_of_button, index) {
        const chosenAction = document.getElementById("chosenAction");
        chosenAction.value = text_of_button;
        const chosenActionId = document.getElementById("chosenActionId");
        chosenActionId.value = index;
        resizeMenuWidth();
    }

    function setSignatures() {

        if (document.getElementById("signatures") !== null) {

            let fragment = "";
            <c:forEach var="entry" items="${mapSignatures}" varStatus="status">
            fragment += "<hr>";
            fragment += "<span style='font-size:12.0pt;font-family: 'Times New Roman',serif'><b>" + "${entry.key.result.ruName}" + "." + "</b>&nbsp;&nbsp;${TimeModule.getDate(entry.key.dateCompleted, 'dd.MM.yyyy')}&nbsp;&nbsp;" + "${entry.key.executor.position.name}" + "&nbsp;" + "${entry.key.executor.fio}" + "</span>";
            <c:choose>
            <c:when test="${entry.key.comment != ''}">
            fragment += "</br><span style='font-size:12.0pt;font-family:'Times New Roman',serif'><i>" +
                '${(entry.key.comment.contains(PageContainer.EXECUTOR_TASK_PAGE) ? CommonModule.getDocumentLinkView(entry.key.comment) : entry.key.comment)}' +
                "</i></span>";
            </c:when>
            </c:choose>

            <c:forEach items="${entry.value}" var="item" varStatus="loop">
            fragment += "<p style='margin:5px'><a href= 'javascript:void(0)' onClick=\"downloadFile('${item.fileName}','${item.name}','${entry.key.id}')\"><img src='${pageContext.request.contextPath}/resources/images/files/" + getExtensionImageByFilename("${item.name}") + "'  width='32' height='32' align='middle'/>${item.name}</a></p>";
            </c:forEach>

            </c:forEach>

            if (fragment !== "") fragment = "<div style='text-align:left;background:#fff'>" + fragment + "</div>";

            document.getElementById("signatures").innerHTML = fragment;
        }

    }

    function setFiles() {

        let filesString = "";

        <%--// fileName - md5 hash sum, name - name of file (iTunes.exe) ${cell.fileName} ${cell.name}--%>
        <c:forEach var="cell" items="${uploadedFiles}" varStatus="status">
        filesString += "<p style='margin:5px'><a href= 'javascript:void(0)' onClick=\"downloadFile('${cell.fileName}','${cell.name}', null)\"><img src='${pageContext.request.contextPath}/resources/images/files/" + getExtensionImageByFilename("${cell.name}") + "'  width='32' height='32' align='middle'/>${cell.name}</a></p>";
        </c:forEach>

        document.getElementById("uploadedFiles").innerHTML = filesString;

    }

    function resizeMenuWidth() {
        // Set width of<input class="agent-hide-file" id="input_add_files"> depends on parent <div id="div_add_files">
        const div_add_files = document.getElementById("div_add_files");
        if (div_add_files !== null)
            document.getElementById("input_add_files").style.cssText = "width: " + div_add_files.clientWidth + "; height: " + div_add_files.clientHeight;

    }

    //noinspection JSUnusedLocalSymbols
    function downloadFile(uploadedFileName, uploadedName, executorTaskId) {

        const downloadFileForm = document.forms["downloadFileForm"];
        downloadFileForm.action = "${pageContext.request.contextPath}${PageContainer.DOWNLOAD_PAGE}";
        downloadFileForm.elements["uploadedFileName"].value = uploadedFileName;
        downloadFileForm.elements["uploadedName"].value = uploadedName;
        downloadFileForm.elements["tempId"].value = ${tempId};
        downloadFileForm.elements["executorTaskId"].value = executorTaskId;
        downloadFileForm.submit();

    }

    function setHistory() {

        <c:choose>
        <c:when test="${((mapHistory!= null) && (mapHistory != ''))}">

        let historyParam = "";
        historyParam += "<span class='head-hidden-table'>История документа</span>";
        historyParam += "<div class='doc_name'>";
        historyParam += '${documentEdi.getDocumentView("dd.MM.yyyy HH:mm:ss")}';
        historyParam += "</div>";

        <c:forEach var="entry" items="${mapHistory}">

        historyParam += "<div class='bp_head'>";
        historyParam += "Инициатор ${entry.key.author.fio}, процесс начат ";

        <c:choose><c:when test="${entry.key.date != null}">historyParam += "${TimeModule.getDate(entry.key.date, 'dd.MM.yyyy HH:mm')}";
        </c:when>
        <c:otherwise>historyParam += " Ошибка - Дата не установлена";
        </c:otherwise></c:choose>

        <c:choose><c:when test="${entry.key.completed == true}">historyParam += ", завершен";
        </c:when>
        <c:otherwise>historyParam += ", не завершен";
        </c:otherwise></c:choose>

        historyParam += "</div>";

        historyParam += "<table class='table_history'>";
        <c:forEach items="${entry.value}" var="item" varStatus="loop">
        historyParam += "<tr class='table_history'>";
        historyParam += "<td class='text-${item.processType}'>${item.processType.ruName}</td>";
        historyParam += "<td>${item.executor.fio}</td>";
        historyParam += "<td>${TimeModule.getDate(item.date, 'dd.MM.yyyy HH:mm:ss')}</td>";

        <c:choose>
        <c:when test="${item.result != null && item.executorTask !=null}">
        historyParam += "<td>${TimeModule.getDate(item.executorTask.dateCompleted, 'dd.MM.yyyy HH:mm:ss')}</td>";
        </c:when>
        <c:when test="${item.executorTask == null}">historyParam += "<td class='waited'>Ожидание очереди</td>";
        </c:when>
        <c:otherwise>historyParam += "<td class ='delivered'>Доставлено</td>";
        </c:otherwise>
        </c:choose>

        <c:choose>
        <c:when test="${item.result != null}">historyParam += "<td class='background-${item.result}'>${item.result.ruName}</td>";
        </c:when>
        <c:when test="${item.viewed == true}">
        historyParam += "<td class='viewed'>";
        historyParam += "Просмотрел, еще не ${item.processType.processResult.ruName.toLowerCase()}";
        historyParam += "</td>";
        </c:when>
        <c:otherwise>historyParam += "<td class='waited'>Еще не просмотрел</td>";
        </c:otherwise>
        </c:choose>

        <c:choose>
        <c:when test="${item.result == ProcessResult.CANCELED}">historyParam += "<td class='canceled'>Стоп</td>";
        </c:when>
        <c:when test="${item.result != null}">historyParam += "<td class='completed'>Пройден</td>";
        </c:when>
        <c:otherwise>historyParam += "<td class='${item.orderBp.enName}'>${item.orderBp.ruDescription}</td>";
        </c:otherwise>
        </c:choose>

        <c:choose>
        <c:when test="${item.executorTask != null}">
            historyParam += "<td class='comment'>";
            historyParam += "${item.executorTask.comment}";
            historyParam += "</td>";
        </c:when>
        <c:otherwise>historyParam += "<td></td>";
        </c:otherwise>
        </c:choose>

        historyParam += "</tr>";
        </c:forEach>

        historyParam += "</table>";
        </c:forEach>

        document.getElementById("history").innerHTML = historyParam;
        </c:when>
        </c:choose>
    }

    //  ***** Tree begin *****

    function setStop() {

        <c:choose>
        <c:when test="${((mapStop!=null) && (mapStop.size() > 0))}">

        let mapParam = "";
        mapParam += "<span class='head-hidden-table'>" + "Отменить процессы" + "</span>";
        mapParam += "<div class='horizontal'>" +
            "<div><a class='link-like-button' onclick='return stopBusinessProcesses()'><div class='command-bar-stop16'></div><span>Отменить</span></a></div>" +
            "<div>&nbsp;</div>" +
            "<div><a href='#current_document' class='link-like-button'><div class='command-bar-close-blue'></div><span>Закрыть</span></a></div>" +
            "<div>&nbsp;</div>" +
            "<div><a class='link-like-button'><div class='command-bar-set-checkbox-items' onclick='setItems(true)'></div></a></div>" +
            "<div>&nbsp;</div>" +
            "<div><a class='link-like-button'><div class='command-bar-clear-checkbox-items' onclick='setItems(false)'></div></a></div>" +
            "</div>";

        mapParam += "<div onclick='treeToggle(arguments[0])'>";
        mapParam += "<ul class='Container' id = 'mainTreeContainer'>";
        <c:forEach var="entry" items="${mapStop}" varStatus="status1">
        mapParam += (true === ${status1.last} ? "<li class='Node IsRoot ExpandOpen IsLast'>" : "<li class='Node IsRoot ExpandOpen '>" );
        mapParam += "<div class='Expand'></div>";
        mapParam += "<input type='checkbox' onchange='setChildItems(arguments[0])'/>";
        mapParam += "<div class='Content'>";
        mapParam += "${entry.key.ruName}";
        mapParam += "</div>";
        mapParam += "<ul class='Container'>";
        <c:forEach items="${entry.value}" var="item" varStatus="status2">
        mapParam += (true === ${status2.last} ? "<li class='Node ExpandLeaf IsLast'>" : "<li class='Node ExpandLeaf'>");
        mapParam += "<div class='Expand'></div>";
        mapParam += "<input type='checkbox' value='${item.id}'/>";
        mapParam += "<div class='Content'>";
        mapParam += "${item.executor.fio}";
        mapParam += "</div>";
        mapParam += "</li>";
        </c:forEach>
        mapParam += "</ul>";
        mapParam += "</li>";
        </c:forEach>
        mapParam += "</ul>";
        mapParam += "</div>";
        mapParam += "<div><input name='closeDocument' placeholder='Закрыть документ' type='checkbox' checked/>Закрыть документ</div>";
        document.getElementById("stop").innerHTML = mapParam;
        </c:when>
        </c:choose>

    }

    //noinspection JSUnusedLocalSymbols
    function setChildItems(event) {

        const list = event.srcElement.parentElement.lastElementChild.children;
        for (let i = 0; i < list.length; i++) {
            //noinspection JSUndefinedPropertyAssignment
            list[i].children[1].checked = event.srcElement.checked;
        }

    }

    //noinspection JSUnusedLocalSymbols
    function treeToggle(event) {
        event = event || window.event;
        const clickedElem = event.target || event.srcElement;

        if (!hasClass(clickedElem, 'Expand')) {
            return // клик не там
        }

        // Node, на который кликнули
        const node = clickedElem.parentNode;
        if (hasClass(node, 'ExpandLeaf')) {
            return // клик на листе
        }

        // определить новый класс для узла
        const newClass = hasClass(node, 'ExpandOpen') ? 'ExpandClosed' : 'ExpandOpen';
        // заменить текущий класс на newClass
        // регексп находит отдельно стоящий open|close и меняет на newClass
        const re = /(^|\s)(ExpandOpen|ExpandClosed)(\s|$)/;
        node.className = node.className.replace(re, '$1' + newClass + '$3');
    }

    function hasClass(elem, className) {
        return new RegExp("(^|\\s)" + className + "(\\s|$)").test(elem.className)
    }

    //noinspection JSUnusedLocalSymbols
    function setItems(checked) {

        const list1 = document.getElementById("mainTreeContainer").children;
        for (let i = 0; i < list1.length; i++) {
            //noinspection JSUndefinedPropertyAssignment
            list1[i].children[1].checked = checked;
            const list2 = list1[i].lastElementChild.children;
            for (let j = 0; j < list2.length; j++) {
                //noinspection JSUndefinedPropertyAssignment
                list2[j].children[1].checked = checked;
            }
        }
    }

    //noinspection JSUnusedLocalSymbols
    function stopBusinessProcesses() {

        const businessProcessSequenceId = [];

        const list1 = document.getElementById("mainTreeContainer").children;
        for (let i = 0; i < list1.length; i++) {
            const list2 = list1[i].lastElementChild.children;
            for (let j = 0; j < list2.length; j++) if (list2[j].children[1].checked) businessProcessSequenceId.push(list2[j].children[1].value);
        }
        document.getElementById("businessProcessSequenceId").value = businessProcessSequenceId;

        location.replace(location.href.replace("#form_stop", "#"));

        const formData = new FormData(document.forms["modal_stop"]);
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}", true);
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, true);
        };

        document.getElementById("info_result").innerHTML = "Отмена процессов...";

    }

    //  ***** Tree end *****

    //noinspection JSUnusedLocalSymbols
    function changePageOnPanelCompletedTask(pageNumber) {

        const page0 = document.getElementById("page0");
        const page1 = document.getElementById("page1");
        const page0_a = document.getElementById("page0_a");
        const page1_a = document.getElementById("page1_a");
        const page0_textarea = document.getElementById("page0_textarea");
        const page1_add_files = document.getElementById("page1_add_files");

        switch (pageNumber) {
            case 0:
                page0.className = "open";
                page1.className = "last-element";
                page0_a.className = "link-like-text no-events";
                page1_a.className = "link-like-text";
                page0_textarea.hidden = false;
                page1_add_files.hidden = true;
                break;
            case 1:
                page0.className = "";
                page1.className = "open last-element";
                page0_a.className = "link-like-text";
                page1_a.className = "link-like-text no-events";
                page0_textarea.hidden = true;
                page1_add_files.hidden = false;
                break;
        }

        resizeMenuWidth();

    }

    function setStatusByUser() {

        location.replace(location.href.replace("#form_completed_task", "#"));

        const errorString = getResultStringOfComparingBigFilesArray(fileList, ${Constant.MAX_FILE_SIZE});

        if (errorString !== "") {
            document.getElementById("info_result").innerHTML = errorString;
        } else {

            const formData = new FormData(document.forms["executor_task"]);
            for (let i = 0; i < fileList.length; i++) formData.append("fileList[]", fileList[i]);

            const xhr = new XMLHttpRequest();
            xhr.open("POST", "${pageContext.request.contextPath}", true);
            xhr.send(formData);

            xhr.onerror = function () {
                afterErrorPageCheckResult(xhr);
            };
            xhr.onload = function () {
                afterLoadingPageCheckResult(xhr, true);
            };

            document.getElementById("info_result").innerHTML = "Отправка..." + getHtmlBlackoutAndLoading();
        }

    }

    function changeCommandBarParameter(parameterString, infoResultString) {

        const formData = new FormData(document.forms["form_command_bar"]);
        formData.append("param", parameterString);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}", true);
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, true);
        };

        document.getElementById("info_result").innerHTML = infoResultString+'... ';

    }

</script>

<%--- Command bar BEGIN ---%>

<form method="post" enctype="multipart/form-data">
    <div class="command-bar">
        <div class="horizontal">

            <c:choose><c:when test="${markedAvailable}">
                <div>
                    <a href="#" class="link-like-button" onclick="changeCommandBarParameter('mark', 'Флаг')">
                        <div class="command-bar-mark ${isMarkedElement==true ? 'marked_button' : ''}"></div></a>
                </div>
            </c:when></c:choose>

            <div><a href="#" class="link-like-button" onclick="window.close();">
                <div class="command-bar-close-black"></div></a></div>

            <div id="command-bar-buttons-accept-decline">
                <div style="${isTrash==true ? "display:none" : ""}">
                    <c:forEach var="cell" items="${processType.availableStatus}" varStatus="status">
                        <a href="#form_completed_task" class="link-like-button"
                           onclick="return setProcessType('${cell}', ${status.index});">
                            <c:choose>
                                <c:when test="${(status.index == 0) && (currentUser == executorTask.executor) && (executorTask.result == null)}">
                                    <img src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/${processType.enName.toLowerCase()}/accept.png"
                                         class="command-bar-dimensions">
                                    <span id="command-bar-accept">${cell}</span></c:when>
                                <c:when test="${(status.index == 1) && (currentUser == executorTask.executor) && (executorTask.result == null)}">
                                    <img src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/${processType.enName.toLowerCase()}/decline.png"
                                         class="command-bar-dimensions">
                                    <span id="command-bar-decline">${cell}</span></c:when>
                            </c:choose></a>
                    </c:forEach></div>
            </div>

            <div id="command-bar-buttons-send">
                <div id="send" class="div-like-button" style="${isTrash==true ? "display:none" : ""}">
                    <ul>
                        <li id="command-bar-send">
                            <div class="command-bar-send"></div>
                            Отправить ▼
                            <ul>
                                <c:forEach var="cell" items="${documentEdi.documentProperty.processTypeList}"
                                           varStatus="status">
                                    <li><a href="#form_send_to_users" class="button"
                                           onclick="return checkParameterAndSetTypeProcess(${status.index});">
                                        <img src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/${cell.enName.toLowerCase()}/accept.png">
                                        <span class="submit">На ${cell.ruName.toLowerCase()}</span></a>
                                    </li>
                                </c:forEach>

                                <li><a href="#form_send_to_users" class="button"
                                       onclick="return checkParameterAndSetTypeProcess(<%=Constant.SCENARIO_NUMBER%>);">
                                    <img src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/scenario/accept.png">
                                    <span class="submit">По сценарию</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul> 
                </div>
            </div>

            <c:choose><c:when test="${(withdrawAvailable!=null) && (withdrawAvailable)}">
                <div>
                    <a href="#" class="link-like-button" onclick="changeCommandBarParameter('withdraw','Отозвать')">
                        <div class="command-bar-withdraw"></div>
                        <span>Отозвать</span></a>
                </div>
            </c:when></c:choose>

            <c:choose><c:when test="${((mapHistory!= null) && (mapHistory.size() > 0))}">
                <div><a href="#form_history" class="link-like-button"><div class="command-bar-history"></div>
                    <span id="command-bar-history">История</span></a></div>
            </c:when></c:choose>

            <c:choose><c:when test="${(executorTask != null)}">
                <div id="command_bar_trash"></div>
            </c:when></c:choose>

            <c:choose><c:when test="${((mapStop!= null) && (mapStop.size() > 0))}">
                <div>
                    <a href="#form_stop" class="link-like-button">
                        <div class="command-bar-stop"></div>
                        <span id="command-bar-stop">Отменить</span></a>
                </div>
            </c:when></c:choose>
        </div>
        <div id="info_result">${infoResult}</div>
    </div>
</form>
<%--- Command bar END ---%>

<%--- Modal forms BEGIN ---%>
<a href="#" class="overlay_completed_task" id="form_completed_task"></a>
<form class="modal_completed_task"
      name="executor_task" id="executor_task"
      onload="resizeMenuWidth()" accept-charset="UTF-8">
    <div style='font-weight: bold; font-size:14.0pt;font-family:"Times New Roman",serif'>
        ${processType.ruName}</div>

    <div class="tabs" id="panelCompletedTask">
        <div class="open" id="page0">
            <a id="page0_a" class="link-like-text no-events" href="javascript:void(0)"
               onclick="changePageOnPanelCompletedTask(0)">Комментарий :</a>
        </div>
        <div class="last-element" id="page1">
            <a id="page1_a" class="link-like-text" href="javascript:void(0)"
               onclick="changePageOnPanelCompletedTask(1)">Вложения</a>
        </div>
    </div>

    <div class="div05">&nbsp</div>

    <textarea id="page0_textarea" style="width:95%; height:70%" name="comment" placeholder="Комментарий :"
              class="message"> </textarea>

    <div id="page1_add_files" hidden>
        <div class="div-like-button" id="div_add_files">
            <input class="agent-hide-file" type="file" id="input_add_files" name="input_add_files[]" multiple
                   onchange="addFilesAndResizeTable()">
            <img src="${pageContext.request.contextPath}/resources/images/add_attachment.png"
                 class="command-bar-add-attachment">Добавить вложение
        </div>
        <button onclick="deleteRowInTableFiles()" formaction="javascript:void(0)">
            <img class="command-bar-cancel" src="${pageContext.request.contextPath}/resources/images/command-bar/close.png">
            Удалить вложение
        </button>
        <div class="table-wrapper" style="width:95%; height:57%" id="div-table-wrapper">
            <div class="table-scroll-add-files-small">
                <table class="table-files" id="table_files"></table>
            </div>
        </div>
    </div>

    <div><input name="closeDocument" placeholder="Закрыть документ" type="checkbox" checked/>Закрыть документ</div>
    <input name="chosenAction" id="chosenAction" class="btn" type="submit" value="x" onclick="setStatusByUser()"
           formaction="javascript:void(0)"/>
    <input name="chosenActionId" id="chosenActionId" type="hidden"/>
    <a href="#" class="recipients-buttons"><span class="btn">Закрыть</span></a>
</form>

<a href="#" class="overlay_history" id="form_history"></a>
<form method="post" class="modal_history">
    <div id="history"></div>
</form>

<a href="#" class="overlay_stop" id="form_stop"></a>
<form method="post" class="modal_stop" name="modal_stop" id="modal_stop">
    <div id="stop"></div>
    <input type="hidden" name="param" value="stop"/>
    <input type="hidden" name="businessProcessSequenceId[]" value="x" id="businessProcessSequenceId"/>
</form>
<%--- Modal forms END ---%>

<div id="comment-for-recipients" style="${commentForRecipients == "" ? "display:none" : ""}">
    <div class="WordSection2">${(commentForRecipients.contains(PageContainer.EXECUTOR_TASK_PAGE) ? CommonModule.getDocumentLinkView(commentForRecipients) : commentForRecipients)}</div>
</div>
<div id="review_document"></div>

<a href="#" class="overlay_send_to_users" id="form_send_to_users"></a>
<form method="post" action="${pageContext.request.contextPath}/executor_task" class="modal_send_to_users"
      name="menu_send_to_users" id="menu_send_to_users" enctype="multipart/form-data">
</form>

<a href="#" class="overlay_choose_users" id="form_choose_users"></a>
<form class="modal_choose_users" action="" id="menu_choose_users">
</form>

<form method="post" action="" id="downloadFileForm" hidden>
    <input type="hidden" name="uploadedFileName" id="uploadedFileName"/>
    <input type="hidden" name="uploadedName" id="uploadedName"/>
    <input type="hidden" name="tempId" id="tempId"/>
    <input type="hidden" name="executorTaskId" id="executorTaskId"/>
</form>

</body>
</html>
