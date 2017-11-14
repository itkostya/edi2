<%--@elvariable id="ElementStatus" type="enumerations"--%>
<%@ page import="model.ElementStatus" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="categoryElement" type="abstract_entity.AbstractCategory"--%>
<%--@elvariable id="columnSet" type="Set<? extends SingularAttribute<? extends AbstractCategory, ?>>"--%>
<%--@elvariable id="infoResult" type="java.lang.String"--%>
<%--@elvariable id="ruPluralShortName" type="java.lang.String"--%>
<%--@elvariable id="sessionDataElement" type="model.SessionDataElement"--%>

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
        <c:choose>
        <c:when test="${sessionDataElement.elementStatus == ElementStatus.CLOSE}">window.close();
        </c:when>
        </c:choose>
        refreshAttributes(document.getElementById("table-attributes"));
    };

    function refreshAttributes(current_table) {

        let body, row;
        let row_style;

        row = current_table.createTHead().insertRow(0);
        insertCellInRow(0, row, 'Field', "");
        insertCellInRow(1, row, 'Value', "");
        row.insertCell(2).outerHTML = "<td hidden>Code</td>";

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cellCol" items="${columnSet}" varStatus="statusCol">

            row = body.insertRow(${statusCol.index});
            insertCellInRow(0, row, '${cellCol.name}', "");
            insertCellInRow(1, row,
                '${cellCol.getBindableJavaType().getName()== 'boolean' ? "<input type=checkbox ".concat(( categoryElement[cellCol.name] == true ? "checked": "")).concat(">") :
                    cellCol.getBindableJavaType().getName() == 'java.sql.Date' ?  "<input type=date value=".concat(categoryElement[cellCol.name]).concat(">"):
                        cellCol.name=='id' ? "<div>".concat(categoryElement["id"]).concat("</div>"):
                             cellCol.getBindableJavaType().getName()=='java.lang.Long' ? "<input type=number value=".concat(categoryElement[cellCol.name]).concat(">"):
                                "<div contenteditable=true>".concat((cellCol.getType().getPersistenceType() == "BASIC") ? categoryElement[cellCol.name]:
                                    categoryElement[cellCol.name].name).concat("</div>")}', "");
            row.insertCell(2).outerHTML = '<td hidden>${
                (((cellCol.getBindableJavaType().getName() == 'boolean')||(cellCol.getBindableJavaType().getName() == 'java.sql.Date')||(cellCol.getBindableJavaType().getName() == 'java.lang.Long' && cellCol.name !='id')) ? cellCol.getBindableJavaType().getName() :
                    ((cellCol.getType().getPersistenceType() == "BASIC") ? "BASIC": categoryElement[cellCol.name].id))}</td>';

            row_style = "";
            <c:choose><c:when test="${(statusCol.index % 2) == 0}">
            row_style += " background: rgb(255, 248, 234); ";
            </c:when></c:choose>
            row.style = row_style;

            row.onchange = function () {
                onChangeElement();
            };

            row.onkeyup = function () {
                onChangeElement();
            };

            <c:choose><c:when test="${(cellCol.getType().getPersistenceType()!='BASIC')}">
                   row.onclick = function() {
                       onClickComplexElement("${PageContainer.getChoicePage(cellCol.getBindableJavaType())}", "${cellCol.name}", "${cellCol.id}"); // Get page name from element type
                };
            </c:when></c:choose>

        </c:forEach>

    }

    function onChangeElement() {
        event.currentTarget.cells[1].firstElementChild.style.cssText = ' color: red ';
    }

    function onClickComplexElement(pageName, attributeName, elementId) {
        const myForm = document.forms["formOpenElement"];
        myForm.action = "${pageContext.request.contextPath}"+pageName;
        myForm.elements["attributeName"].value = attributeName;
        myForm.elements["elementId"].value = elementId;
        myForm.elements["tempId"].value = getRandomInt();
        myForm.submit();
    }

    function callBackChoice(elementJsonString) {
        let elementJson = JSON.parse(elementJsonString);
        for (let i = 0; i < document.getElementById("table-attributes").tBodies[0].rows.length; i++){
            if (elementJson.attributeName === document.getElementById("table-attributes").tBodies[0].rows[i].cells[0].innerHTML){
                document.getElementById("table-attributes").tBodies[0].rows[i].cells[1].innerHTML = elementJson.name;
                document.getElementById("table-attributes").tBodies[0].rows[i].cells[1].style = ' color: red ';
                document.getElementById("table-attributes").tBodies[0].rows[i].cells[2].outerHTML = '<td hidden>'+elementJson.id+'</td>';
                i = document.getElementById("table-attributes").tBodies[0].rows.length;
            }
        }
    }

    function saveData() {
        const formData = new FormData(document.forms["formSaveElement"]);
        const xhr = new XMLHttpRequest();

        xhr.open("POST", "${pageContext.request.contextPath}", true);

        let current_table = document.getElementById("table-attributes");
        let body;
        let jsonString = "";

        body = current_table.tBodies[0];

        for (let i = 0; i < ${columnSet.size()}; i++)
        jsonString += " " + body.rows[i].cells[0].innerHTML + ": " +
        (body.rows[i].cells[2].firstChild === null ?
            "null" :
            (body.rows[i].cells[2].firstChild.data === 'boolean' ?
                "'" + body.rows[i].cells[1].firstChild.checked + "'" :
                (body.rows[i].cells[2].firstChild.data === 'java.lang.Long' ?
                    "'" + body.rows[i].cells[1].firstChild.value + "'" :
                    (body.rows[i].cells[2].firstChild.data === 'java.sql.Date' ?
                    "'" + body.rows[i].cells[1].firstChild.value + "'"  :
                        (body.rows[i].cells[2].firstChild.data === 'BASIC' ?
                            "'" + body.rows[i].cells[1].firstChild.innerHTML + "'" :
                            "{ 'id': '" + body.rows[i].cells[2].firstChild.data + "'}"))))) + ",";

        //formData.append("param", "{ id: '30', endOfPeriod: '2017-11-16', beginOfPeriod: '2017-11-23', deletionMark: 'false', name: 'Декабрь 2017', code: null }");
        //formData.append("param", "{'lastName':'Чегалкин','firstName':'Сергей','middleName':'Викторович','login':'it_director','password':'123','position':{'id':'3'},'department':{'id':'1'},'fio':'Чегалкин Сергей Викторович','id':'10','name':'it_director','deletionMark':'false','isFolder':'false'}");
        formData.append("param", "{"+jsonString.slice(0, jsonString.length - 1).replace(/('')/g, "null")+" }");
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, true);
        };

        let button = document.getElementById("info_result");
        button.innerHTML = "Сохранение..." + getHtmlBlackoutAndLoading();
        button.disabled = true;
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

    <div id="info_result">${infoResult}</div>

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

<form hidden id="formOpenElement" target="_blank">
    <input type="hidden" name="attributeName"/>
    <input type="hidden" name="elementId"/>
    <input type="hidden" name="tempId"/>
</form>

</body>

</html>
