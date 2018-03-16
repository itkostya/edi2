<%@ page import="tools.PageContainer" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Edi - version 0.125 - beta</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/jsp/common/common.jsp"/>
    <jsp:useBean id="now" class="java.util.Date"/>
</head>

<body>

<script>
    function onClickOpenElement() {
        const myForm = document.forms["formOpenElement"];
        myForm.action = "${pageContext.request.contextPath}${PageContainer.CATEGORY_USER_ELEMENT_PAGE}";
        myForm.elements["tempId"].value = getRandomInt();
        myForm.elements["createNew"].value = true;
        myForm.submit();
    }
</script>

<h2>Hello - today is <fmt:formatDate value="${now}" pattern="MM/dd/yyyy HH:mm:ss" /></h2>

<%--<a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_USER_ELEMENT_PAGE}' onclick="onClickOpenElement()">Sign up</a>--%>

<div class="horizontal">
    <div> <a class="link-like-button" href='${pageContext.request.contextPath}/admin'>
            <img src="${pageContext.request.contextPath}/resources/images/authorization/login.png"></a>
    </div>
    <div> <a  href="javascript:void(0)" class="link-like-button" onclick="onClickOpenElement()">
        <img src="${pageContext.request.contextPath}/resources/images/authorization/sign-up.png"></a>
    </div>
 </div>

<form hidden id="formOpenElement" target="_blank">
    <input type="hidden" name="tempId"/>
    <input type="hidden" name="createNew"/>
</form>

</body>
</html>
