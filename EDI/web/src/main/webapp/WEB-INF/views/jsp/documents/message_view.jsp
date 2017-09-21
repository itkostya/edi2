<%--@elvariable id="CommonModule" type="enumerations"--%>
<%@ page import="tools.CommonModule" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="docText" type="java.lang.String"--%>
<%--@elvariable id="docTheme" type="java.lang.String"--%>
<%--@elvariable id="documentEdi" type="abstract_entity.AbstractDocumentEdi"--%>
<%--@elvariable id="userFrom" type="java.lang.String"--%>
<%--@elvariable id="userWhomString" type="java.lang.String"--%>

<html>

<link href="<c:url value="/resources/css/documents/message_view.css"/>" rel="stylesheet" type="text/css">

<body>

<div id='command-bar-buttons-accept-decline-message' style="display: none"></div>

<div id='command-bar-buttons-send-message' style="display: none">
    <div><a href="#" class="link-like-button" onclick=createNewDocument('forward','${documentEdi.documentProperty}','${documentEdi.id}')>
        <div class="command-bar-send-forward"></div>
        <span id="text-send-forward">Переслать</span></a></div>
    <div>
        <%-- createNewDocument without inverted commas or double ones --%>
        <a href='javascript:void(0)' class='link-like-button' onclick=createNewDocument('reply','${documentEdi.documentProperty}','${documentEdi.id}')>
        <div class="command-bar-send-reply"></div>
        <span id="text-send-reply">Ответ</span></a></div>
</div>

<div style="text-align: center;">
    <div class=a5>
        <div style="text-align:left;">
            <span class='TextSpanMessage'>
                <strong><em>От кого</em></strong> <em><u>${userFrom}</u></em><br>
                <strong><em>Кому</em></strong> <em><u>${userWhomString}<br></u></em>
            </span>
        </div>
        <div style="text-align:right;z-index:1">
            <img src="${pageContext.request.contextPath}/resources/images/documents/message/postcode.png"
                 alt="" style="width:200px;height:59px;"></div>
        <div style="text-align:left;">
            <p>
                <span class='TextSpanMessage'><strong><em>${docTheme}</em></strong></span>
            </p>
        </div>

        <div style="text-align:left;">
            <span class='TextSpanMessage'><em>
                ${(docText.contains(PageContainer.EXECUTOR_TASK_PAGE) ? CommonModule.getDocumentLinkView(docText): docText)}
            </em></span>
        </div>
        <div id='uploadedFiles' style='text-align:left'></div>
    </div>
</div>

<form hidden action="" id="createDocument" target="_blank">
    <input type="hidden" name="tempId"/>
    <input type="hidden" name="documentCopyId">
    <input type="hidden" name="operationType">
</form>

</body>

</html>
