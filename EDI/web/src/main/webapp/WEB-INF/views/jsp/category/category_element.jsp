<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="categoryElement" type="abstract_entity.AbstractCategory"--%>
<%--@elvariable id="columnSet" type="Set<? extends SingularAttribute<? extends AbstractCategory, ?>>"--%>
<%--@elvariable id="ruPluralShortName" type="java.lang.String"--%>

<html>
<head>
    <title>${categoryElement.name}(${ruPluralShortName})</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../common/common.jsp"/>
</head>

<body>
<script>

    window.onload = function () {
        refreshAttributes(document.getElementById("table-attributes"));
    };

    function refreshAttributes(current_table) {

        let body, row;
        let row_style;

        row = current_table.createTHead().insertRow(0);
        insertCellInRow(0, row, 'Field', "");
        insertCellInRow(1, row, 'Value', "");

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">

            row = body.insertRow(${statusCol.index});
            insertCellInRow(0, row, '${cellCol.name}', "");
            insertCellInRow(1, row, '<div ${((cellCol.name=="id") ? "": "contenteditable")}>${((cellCol.getType().getPersistenceType() == "BASIC")? categoryElement[cellCol.name]: categoryElement[cellCol.name].name)}</div>', "");

            row_style = "";
            <c:choose><c:when test="${(statusCol.index % 2) == 0}">
            row_style += " background: rgb(255, 248, 234); ";
            </c:when></c:choose>
            row.style = row_style;

            row.onkeyup = function () {
                onKeyUpElement(${cellCol.id});
            };

        </c:forEach>

    }

    function onKeyUpElement(elementId) {
        event.currentTarget.cells[1].style = " color: red ";
    }

    function saveData() {
        const formData = new FormData(document.forms["formSaveElement"]);
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}${PageContainer.CATEGORY_USER_ELEMENT_PAGE}", true);

        //var json = JSON.stringify([{ domain: "kkk", id:"10L"}, { domain: "kkk2", id: "100L"}]);
        var json = JSON.stringify([{ field: "kkk", value:"10L"}, { field: "kkk2", value: "100L"}]);

        formData.append("param", json);
        xhr.send(formData);
    }

</script>

<div class="horizontal">
    <div class="div-like-button"
         onclick="saveData();" id="command-save">
        <div class="command-bar-save"></div>
        Save
    </div>

    <div>
        <button name="param" value="close" id="command-bar-close" onClick="window.close();">
            <img class="command-bar-close" src="${pageContext.request.contextPath}/resources/images/command-bar/close.png"/>Close
        </button>
    </div>
</div>

<div style="height:90%;" id="div-for-table-attributes">
    <div class="table-wrapper">
        <div class="table-scroll">
            <table class="table-attributes" id="table-attributes"></table>
        </div>
    </div>
</div>

<form hidden id="formSaveElement">
    <input type="hidden" name="elementId"/>
</form>

</body>

</html>
