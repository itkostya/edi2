<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>Welcome</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/work_area/work_area_main.css"/>" rel="stylesheet" type="text/css">
</head>

<body>

<div>${pageContext.request.pathInfo} </div>
<div>${pageContext.request.requestURI} </div>
<div>${pageContext.request.requestURL} </div>
<div>${pageContext.request.servletPath} </div>

<div class="container form-welcome">

    <h2 class="form-welcome-heading">Administration Panel</h2>

    <form method="post" action="${pageContext.request.contextPath}/admin">
        <div>
            <button name="param" value= "1" hidden> Create departments</button>
            <button name="param" value= "2" hidden> Create positions</button>
            <button name="param" value= "3" hidden> Create users</button>
            <button name="param" value= "4" style="color:red"> Create categories (departments, positions, users)</button>
        </div>

        <div hidden>
            <button name="param" value= "5"> Create Memorandums</button>
            <button name="param" value= "6"> Create Business Processes</button>
            <button name="param" value= "7"> Create BP Sequence</button>
            <button name="param" value= "8"> Create Executor Tasks</button>
        </div>

        <div>
            <button name="param" value= "20" hidden> Create (check) folder for files</button>
            <button name="param" value= "21" hidden> Create ALL</button>
        </div>

        <div>
            <button name="param" value= "31"> Admin access (temp) </button>
        </div>

        <div>
            <table>
                <tr>
                    <td>
                        <div class="relative-with-border" id="div-category-user">
                            <div>
                                <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_USER_JOURNAL_PAGE}' target="_blank">
                                    <img src="${pageContext.request.contextPath}/resources/images/work_area/User.png"
                                         alt="Пользователи" class="button_dark_green"></a>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="relative-with-border" id="div-category-position">
                            <div>
                                <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_POSITION_JOURNAL_PAGE}' target="_blank">
                                    <img src="${pageContext.request.contextPath}/resources/images/work_area/Position.png"
                                         alt="Должности" class="button_dark_green"></a>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="relative-with-border" id="div-category-department">
                            <div>
                                <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_DEPARTMENT_JOURNAL_PAGE}' target="_blank">
                                    <img src="${pageContext.request.contextPath}/resources/images/work_area/Department.png"
                                         alt="Департаменты" class="button_dark_green"></a>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="relative-with-border" id="div-category-proposal-template">
                            <div>
                                <a href='${pageContext.request.contextPath}${PageContainer.CATEGORY_PROPOSAL_TEMPLATE_JOURNAL_PAGE}' target="_blank">
                                    <img src="${pageContext.request.contextPath}/resources/images/work_area/ProposalTemplate.png"
                                         alt="Шаблоны заявок" class="button_golden_rod"></a>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>

        </div>

    </form>

</div>
</body>

</html>
