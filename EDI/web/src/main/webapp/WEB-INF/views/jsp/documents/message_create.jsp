<%--@elvariable id="Constant" type="enumerations"--%>
<%@ page import="app_info.Constant" %>

<%@ page import="documents.DocumentProperty" %>

<%--@elvariable id="ProcessOrderType" type="enumerations"--%>
<%@ page import="enumerations.ProcessOrderType" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%--@elvariable id="ElementStatus" type="enumerations"--%>
<%@ page import="model.ElementStatus" %>

<%--??? Is it correct type - or better enumerations ? --%>
<%--@elvariable id="CommonModule" type="tools.CommonModule"--%>
<%@ page import="tools.CommonModule" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="infoResult" type="java.lang.String"--%>
<%--@elvariable id="selectedUser" type="java.lang.String"--%>
<%--@elvariable id="sessionDataElement" type="model.SessionDataElement"--%>
<%--@elvariable id="theme" type="java.lang.String"--%>
<%--@elvariable id="textInfo" type="java.lang.String"--%>
<%--@elvariable id="uploadedFiles" type="java.util.List<categories.UploadedFile>"--%>
<%--@elvariable id="whomId" type="java.lang.Long"--%>

<html>

<head>
    <title>Создание сообщения</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/resources/css/common/modal_forms.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/add_files.jsp"/>
    <jsp:include page="../common/common.jsp"/>
    <%--<jsp:include page="../common/send_document.jsp"/>--%>
    <jsp:include page="../common/whom_menu.jsp"/>
</head>

<body>

<script>

    let fileList = [];
    let uploadedFileList = [];
    let rowMarkedTableIndex;
    let rowMarkedIndex;

    window.onload = function () {

        //window.alert("onLoad message");
        rowMarkedTableIndex = -1;
        rowMarkedIndex = -1;

        <c:choose>
        <c:when test="${sessionDataElement.elementStatus == ElementStatus.CLOSE}">window.close();
        </c:when>
        <%--<c:otherwise>window.alert("not close");</c:otherwise>--%>
        </c:choose>

        <c:set value="${(sessionDataElement.elementStatus == ElementStatus.CREATE || sessionDataElement.elementStatus == ElementStatus.ERROR)}" var="isNewElement"/>

        refreshChooseOneUser(document.getElementById("table_choose_one_user"),  <%=DocumentProperty.MESSAGE.getId()%>);
        setUsersWhomList();
        uploadedFileList = fillUploadedFiles();

        resizeElements();
        window.addEventListener('resize', resizeElements);

    };

    function resizeElements() {

        const clientWidth = document.firstChild.clientWidth;
        const clientHeight = document.firstChild.clientHeight;

        const div_add_files = document.getElementById("div_add_files");  // Set width of<input class="agent-hide-file" id="input_add_files"> depends on parent <div id="div_add_files">
        if (div_add_files !== null) document.getElementById("input_add_files").style.cssText = "width: " + div_add_files.clientWidth + "; height: " + div_add_files.clientHeight;

        if (clientWidth < 680) {
            document.getElementById("delete-addition").innerHTML = document.getElementById("delete-addition").innerHTML.replace("Удалить вложение", "Удалить");
            document.getElementById("chose-whom").innerHTML = document.getElementById("chose-whom").innerHTML.replace("Выбрать кому", "Кому");
            document.getElementById("command-bar-close").innerHTML = document.getElementById("command-bar-close").innerHTML.replace("Закрыть", "");
            document.getElementById("command-bar-draft").innerHTML = document.getElementById("command-bar-draft").innerHTML.replace("Сохранить как черновик", "");
            document.getElementById("command-bar-send").innerHTML = document.getElementById("command-bar-send").innerHTML.replace("Отправить", "");
            if (document.getElementById("command-bar-view") !== null) {
                document.getElementById("command-bar-view").innerHTML = document.getElementById("command-bar-view").innerHTML.replace("Просмотр", "");
            }
        }

        const heightTableWhomSelected = document.getElementById("table-whom-selected").tBodies.item(0).rows[0].cells.length === 0 ? 0 :
            (clientHeight > 800 ? 5 : clientHeight <= 510 ? 12 : clientHeight <= 560 ? 10 : clientHeight <= 650 ? 7 : 6);

        document.getElementById("whom-selected").style.cssText = "height: "+heightTableWhomSelected+"%";

        document.getElementById("div-text-and-files").style.cssText =
            "height: " + ((clientHeight > 940 ? 96 : clientHeight <= 510 ? 75 : clientHeight <= 531 ? 84 :
                clientHeight <= 550 ? 85 : clientHeight <= 580 ? 86 : clientHeight <= 601 ? 87 :
                    clientHeight <= 630 ? 88 : clientHeight <= 660 ? 89 : clientHeight <= 700 ? 90 :
                        clientHeight <= 780 ? 91 : clientHeight <= 830 ? 93 : clientHeight <= 880 ? 94 : 95) - heightTableWhomSelected )+ "%";

        document.getElementById("popup-menu").style.cssText =
            "width: " + (clientWidth > 1420 ? "91.5" :
            clientWidth <= 550 ? "79" :  clientWidth <= 750 ? "82":
            clientWidth <= 1000 ? "83.5" :  clientWidth <= 1200 ? "88": "90")+ "%";

        document.getElementById("selectedUser").style.cssText =
            "width: " + (clientWidth > 1420 ? "96.5" :
            clientWidth <= 550 ? "85" :  clientWidth <= 750 ? "90":
                clientWidth <= 1000 ? "92.5" :  clientWidth <= 1200 ? "94.5": "95.5")+ "%";

        document.getElementById("themeBasic").style.cssText =
            "width: " + (clientWidth > 1420 ? "96.5" :
            clientWidth <= 550 ? "86" :  clientWidth <= 750 ? "91":
                clientWidth <= 1000 ? "94" :  clientWidth <= 1200 ? "95": "96")+ "%";

        addFilesAndResizeTable(uploadedFileList);

    }

    function messageSaveSend(stringParamSaveSend, stringInfoResultProcess, errorString) {

        errorString += getResultStringOfComparingBigFilesArray(fileList, ${Constant.MAX_FILE_SIZE});

        if (errorString !== "") {
            document.getElementById("info_result").innerHTML = errorString;
        } else {

            const post_users = Array.from(document.getElementsByClassName("hidden-id")).map(f => f.innerText);
            let i;

            const formData = new FormData(document.forms["doc_message_create"]);
            formData.append("post_users[]", post_users);

            if (uploadedFileList !== null) {
                let uploadedFileString = "";
                for (i = 0; i < uploadedFileList.length; i++) uploadedFileString += uploadedFileList[i][1] + ";"; // File's md5Hex sum
                formData.append("uploadedFileString", uploadedFileString);
            }

            for (i = 0; i < fileList.length; i++) formData.append("fileList[]", fileList[i]);
            formData.append("param", stringParamSaveSend);

            const xhr = new XMLHttpRequest();
            xhr.open("POST", "${pageContext.request.contextPath}", true);
            xhr.timeout = 30000;
            xhr.send(formData);

            xhr.onerror = function () {
                afterErrorPageCheckResult(xhr);
            };
            xhr.onload = function () {
                afterLoadingPageCheckResult(xhr, true);
            };

            document.getElementById("info_result").innerHTML = stringInfoResultProcess + getHtmlBlackoutAndLoading();

        }

    }

    function saveAsDraft() {

        messageSaveSend("save", "Сохранение...", "");

    }

    function sendMessageAfterChecking() {

        messageSaveSend("send", "Отправка...", ((document.getElementById("table-whom-selected").tBodies.item(0).rows[0].cells.length === 0) ? "Не выбран получатель(-ли); " :""));

    }

</script>

<form method="post" action="${pageContext.request.contextPath}/doc_message_create" autocomplete="off"
      style="height: 91%"
      name="doc_message_create" id="doc_message_create">
    <div class="left_up_panel">Создание сообщения</div>
    <div class="div05">&nbsp</div>
    <div class="horizontal">
        <div>
            <button name="param" value="close" id="command-bar-close" onClick="window.close();">
                <img class="command-bar-close" src="${pageContext.request.contextPath}/resources/images/command-bar/close.png"/>Закрыть
            </button>
        </div>
        <div>
            <div class="div-like-button"
                 onclick="saveAsDraft();" id="command-bar-draft">
                <div class="command-bar-save"></div>
                Сохранить как черновик
            </div>
        </div>
        <div id="send" class="div-like-button" onclick="return sendMessageAfterChecking()">
            <div class="command-bar-send"></div>
            <span class="submit">Отправить</span>
        </div>

        <div class="div-like-button ${isNewElement==true ? "" : "button-disabled"}" id="div_add_files">
            <input class="agent-hide-file" type="file" id="input_add_files" name="input_add_files[]" multiple
                   onchange="addFilesAndResizeTable(uploadedFileList)"/>
            <img src="${pageContext.request.contextPath}/resources/images/add_attachment.png"
                 class="command-bar-add-attachment"/>Добавить вложение
        </div>
    </div>
    <div class="div05">&nbsp</div>
    <div class="horizontal">
        <div id="popup-menu" style="width: 91.5%; vertical-align:top;">
            <ul>
                <li><label for="selectedUser">Кому: </label><input tabindex="1" name="whom" style="width:96.5%"
                                                                   onkeyUp="selectUsers()"
                                                                   onkeypress="selectUsers()"
                                                                   onkeydown="selectUsersOnKeyDown()" id="selectedUser"
                                                                   value="${selectedUser}" ${isNewElement==true ? "": "disabled"}>
                    <input type="hidden" name="whomId" id="whomIdBasic" value="${whomId}"/>
                    <input type="hidden" id="positionToBasic"/>
                    <ul id="selectMenuUser"></ul>
                </li>
            </ul>
        </div>

        <div>
            <a href="#form_choose_one_user" class="link-like-button ${isNewElement==true ? "": "button-disabled"}">
                <div class="select-whom"></div>
                <span class="submit" id="chose-whom">Выбрать кому</span></a>
        </div>
    </div>

    <div id="whom-selected" class="horizontal-scroll-menu"><table id="table-whom-selected"><thead></thead><tbody><tr></tr></tbody></table></div>

    <div class="div05">&nbsp</div>
    <div><label for="themeBasic">Тема: </label><input name="theme" style="width:96%" id="themeBasic"
                                                      value="${theme}" ${isNewElement==true ? "": "disabled"}></div>
    <div class="div05">&nbsp</div>
    <div class="horizontal" id="div-text-and-files">
        <div style="height: 90%; width: 65%; vertical-align:top;">
            <textarea style="height:100%; width:100%" name="textInfo" class="text-block"
                      id="textInfoBasic" ${isNewElement==true ? "": "disabled"}
                      title="Текст документа">${textInfo}</textarea>
        </div>
        <div style="height:90%; width:33%">
            <div style="height:3%; width:80%">
                <button onclick="deleteRowInTableFiles()"
                        formaction="javascript:void(0)" ${isNewElement==true ? "": "disabled"} id="delete-addition">
                    <div class="command-bar-cancel"></div>
                    Удалить вложение
                </button>
            </div>
            <div class="div05"></div>
            <div class="table-wrapper ${isNewElement==true ? "": "button-disabled"}">
                <div class="table-scroll-add-files">
                    <table class="table-files" id="table_files"></table>
                </div>
            </div>
        </div>
        <div id="info_result">${infoResult}</div>
    </div>
    <input type="hidden" name="tempId" value="${param.tempId}" id="tempId"/>
</form>

<a href="#" class="overlay_send_to_users" id="form_send_to_users"></a>
<form method="post" action="${pageContext.request.contextPath}/doc_message_create" class="modal_send_to_users"
      name="menu_send_to_users" id="menu_send_to_users"
      accept-charset="UTF-8">
</form>

<a href="#" class="overlay_choose_users" id="form_choose_users"></a>
<form class="modal_choose_users" action="" id="menu_choose_users">
</form>

<a href="#" class="overlay_choose_one_user" id="form_choose_one_user"></a>
<form method="post" action="${pageContext.request.contextPath}/doc_message_create" class="modal_choose_one_user"
      id="menu_choose_one_user">
    <div>Выберите сотрудника:</div>
    <div class="table-wrapper">
        <div class="table-scroll">
            <table class="table-whom" id="table_choose_one_user"></table>
        </div>
    </div>
</form>

<a href="#" class="overlay_review_document" id="form_review_document"></a>
<form class="modal_review_document" id="menu_review_document">
</form>

</body>

</html>
