<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>Welcome</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/admin.css?1"/>" rel="stylesheet" type="text/css">
</head>

<body>
<div class="container form-welcome">

    <h2 class="form-welcome-heading">Administration Panel</h2>

    <form method="post" action="${pageContext.request.contextPath}/admin">
        <div>
            <button name="param" value= "1"> Create departments</button>
            <button name="param" value= "2"> Create positions</button>
            <button name="param" value= "3"> Create users</button>
            <button name="param" value= "4" style="color:red"> Create categories (departments, positions, users)</button>
        </div>

        <div>
            <button name="param" value= "5"> Create Memorandums</button>
            <button name="param" value= "6"> Create Business Processes</button>
            <button name="param" value= "7"> Create BP Sequence</button>
            <button name="param" value= "8"> Create Executor Tasks</button>
        </div>

        <div>
            <button name="param" value= "20"> Create (check) folder for files</button>
            <button name="param" value= "101"> Create ALL</button>
        </div>

    </form>

</div>
</body>

</html>
