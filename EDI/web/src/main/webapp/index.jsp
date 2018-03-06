<%@ page import="tools.PageContainer" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<body>
<jsp:useBean id="now" class="java.util.Date"/>
<h2>Hello - today is <fmt:formatDate value="${now}" pattern="MM/dd/yyyy HH:mm:ss" /></h2>

<%--<a href='${pageContext.request.contextPath}/admin'>Admin page</a>--%>
<%--<a href='${pageContext.request.contextPath}/user'>User page</a>--%>

<a href='${pageContext.request.contextPath}${PageContainer.USER_PAGE}'>Log in</a>
<a href='${pageContext.request.contextPath}${PageContainer.NEW_USER_PAGE}'>Sign up</a>

</body>
</html>
