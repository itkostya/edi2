<%@ page contentType="text/html;charset=UTF-8" %>

<%--@elvariable id="error_message" type="java.lang.String"--%>

<html>
<head>
    <title>Error</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/user">

    <table>
        <tr>
            <td>
                <img src="${pageContext.request.contextPath}/resources/images/cancel.png"
                     width="37" height="36" alt="lorem">
            </td>
            <td><div>${error_message}</div>
                <button>Go to registration page</button>
            </td>
        </tr>

    </table>
</form>

</body>
</html>
