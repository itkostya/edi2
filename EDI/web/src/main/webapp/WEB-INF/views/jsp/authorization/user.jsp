<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="userList" type="java.util.List<categories.User>"--%>

<html>

<head>
    <title>Welcome</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/authorization/user.css?2"/>" rel="stylesheet" type="text/css">
</head>

<body>

<div class="container">
    <form class="form-login" action="${pageContext.request.contextPath}/user" method="post">

        <table>
            <tr>
                <td><img src="${pageContext.request.contextPath}/resources/images/edi.png"
                         width="115" height="75" alt="lorem"></td>
                <td class="head_name"> Access to EDI</td>
            </tr>

            <tr>
                <td>
                </td>
                <td>
                    <table class="selection">
                        <tr>
                            <td><label for="userLogin">User:</label></td>
                            <td>
                                <select name = "login" id="userLogin">
                                    <c:forEach var="cell" items="${userList}">
                                        <option value= "${cell.login}">${cell.login}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>Password:</td>
                            <td><input class="form-control" id="inputPassword" type="password"
                                       placeholder="Enter password"
                                       name="pass"
                                       required>
                            </td>
                        </tr>

                        <tr>
                            <td>
                            </td>

                            <td>
                                <button id = "ok">Ok</button>
                                <button id = "cancel">Cancel</button>
                            </td>

                        </tr>
                    </table>
                </td>
            </tr>

        </table>
    </form>
</div>

</body>

</html>
