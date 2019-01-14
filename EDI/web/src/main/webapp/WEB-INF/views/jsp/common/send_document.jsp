<%--@elvariable id="Constant" type="enumerations"--%>
<%@ page import="app_info.Constant" %>

<%@ page import="documents.DocumentProperty" %>
<%@ page import="enumerations.ProcessOrderType" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="userList" type="java.util.List<categories.User>"--%>
<%--@elvariable id="finalDate" type="java.sql.Date"--%>

<%-- Should be code on page:

window.onload = function () {
        document.getElementById("menu_send_to_users").innerHTML = createMenuSendToUsers();
        document.getElementById("menu_choose_users").innerHTML = createMenuChooseUsers();
        setUsersOnPage();
}

<a href="#send_to_users" class="overlay_send_to_users" id="form_send_to_users"></a>
<form method="post" action="${pageContext.request.contextPath}/doc_memorandum_create" class="modal_send_to_users"
      id="menu_send_to_users">
</form>

<a href="#choose_users" class="overlay_choose_users" id="form_choose_users"></a>
<form class="modal_choose_users" action="" id="menu_choose_users">
</form>

--%>

<html>

<head>
    <link href="<c:url value="/resources/css/common/send_document.css"/>" rel="stylesheet" type="text/css">
    <%--Should be included in documents (memorandum_create, executor_task, etc.) before including this file.--%>
    <%--I don't include common.jsp here because result file (memorandum_create, executor_task, etc.) --%>
    <%--with different common files (add_files, send_document, whom_menu) would include common.jsp many times --%>
    <%--<jsp:include page="../common/common.jsp"/>--%>
</head>

<%--suppress JSUnusedLocalSymbols --%>
<script>

    //noinspection JSUnusedLocalSymbols
    function resizeElementsSendDocument() {

        const clientWidth = document.firstChild.clientWidth;
        const clientHeight = window.innerHeight;

        document.getElementById("separator").style.cssText = (clientHeight < 630 ? "display: none;" : "");
        document.getElementById("recipients-number").style.cssText = (clientHeight < 690 ? "display: none;" : "");

        if (clientWidth < 680) {
            let changeable = document.getElementsByClassName("changeable");
            for (let i = 0; i < changeable.length; i++) {
                if (changeable[i].innerHTML === "Вверх") {
                    changeable[i].innerHTML = "&nbsp;&#8657;&nbsp;"
                }
                else if (changeable[i].innerHTML === "Вниз") {
                    changeable[i].innerHTML = "&nbsp;&#8659;&nbsp;"
                }
            }
        }

    }

    //noinspection JSUnusedLocalSymbols
    function setUsersOnPage() {

        let head, body, row;

        table_users = document.getElementById("table-users");
        table_recipients = document.getElementById("table-recipients");

        row = table_users.createTHead().insertRow(0);
        insertCellInRow(0, row, '');
        insertCellInRow(1, row, 'Наименование');
        insertCellInRow(2, row, 'Должность');

        body = table_users.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${userList}" varStatus="status">
        body.insertRow(${status.index});
        addRowInTable(body, ${status.index},
            ['<input name=${cell.id} value="${cell.lastName} ${cell.firstName} ${cell.middleName}" placeholder="" type="checkbox" id="id${status.index}"/>',
                '${cell.lastName} ${cell.firstName} ${cell.middleName}',
                '${cell.position.name}'], "recipients-number");
        </c:forEach>

        head = table_recipients.createTHead();
        addRowInTable(head, 0, ['Пользователь', 'Порядок', 'Тип процесса'], "recipients-consequence-first-line");
        body = table_recipients.appendChild(document.createElement('tbody'));
        addRowInTable(body, 0, ['&nbsp', '&nbsp', '&nbsp'], "");

        setVisibilityOfColumns();
    }

    //noinspection JSUnusedLocalSymbols
    function addUsers() {

        const orderMenu = getOrderMenu(0);
        const processTypeMenu = getProcessTypeMenu(0);

        if (table_recipients !== null) {
            let index1 = table_recipients.tBodies[0].rows.length - 1;
            for (let i1 = 0; i1 < table_users.rows.length; i1++) {
                const checkbox = document.getElementById("id" + i1);
                if ((checkbox !== null) && (checkbox.checked)) {
                    addRowInTable(table_recipients.tBodies[0], index1,
                        ['<input name=' + checkbox.name + ' value="' + checkbox.value + '" placeholder="" onfocus="checkRow(' + index1 + ')" class="recipients-number-td" readonly/>',
                            orderMenu.replace('row_number', index1),
                            processTypeMenu.replace('row_number', index1)],
                        "recipients-number");
                    index1++;
                    checkbox.checked = false;
                }
            }
        }

        setVisibilityOfColumns();

    }

    //noinspection JSUnusedLocalSymbols
    function checkParameterAndSetTypeProcess(type_process) {

        if (document.getElementById("themeBasic") !== null && document.getElementById("themeBasic").value === "") {
            const infoResult = document.getElementById("info_result");
            infoResult.innerText = "Не заполнена тема";
            return false;
        } else {
            if (process_type !== type_process) {
                process_type = type_process;
                deleteAllRows();
                setVisibilityOfColumns();
            }
            if (document.getElementById("theme") !== null && document.getElementById("themeBasic") !== null)
                document.getElementById("theme").value = document.getElementById("themeBasic").value;

            if (document.getElementById("textInfo") !== null && document.getElementById("textInfoBasic") !== null)
                document.getElementById("textInfo").value = document.getElementById("textInfoBasic").value;

            if (document.getElementById("whomId") !== null && document.getElementById("whomIdBasic") !== null)
                document.getElementById("whomId").value = document.getElementById("whomIdBasic").value;
        }
        return true;
    }

    function setVisibilityOfColumns() {

        if (table_recipients !== null) {
            for (let i = 0; i < table_recipients.rows.length; i++) {
                table_recipients.rows[i].cells[1].hidden = (table_recipients.rows.length <= 3);// Order
                table_recipients.rows[i].cells[2].hidden = (process_type !== <%=Constant.SCENARIO_NUMBER%>); // Type of process
            }
            setNumberOfRecipients();
        }

    }

    function getOrderMenu(selected_value) {

        let select_menu = '<select onchange = "changeOrder(row_number, this.selectedIndex);"';
        select_menu += '>';

        <c:forEach var="cell" items="<%=ProcessOrderType.values()%>" varStatus="status">
        select_menu +=
            '<option ' + ((selected_value === ${status.index} ? 'selected' : '') + ' value = "${cell.id}">${cell.ruDescription}</option>');
        </c:forEach>
        select_menu += '</select>';

        return select_menu;
    }

    function getProcessTypeMenu(selected_value) {

        let process_type_menu = '<select onchange = "changeProcessType(row_number, this.selectedIndex);"';
        process_type_menu += '>';

        if (process_type === <%=Constant.SCENARIO_NUMBER%>) {
            <c:forEach var="cell" items="<%=DocumentProperty.MEMORANDUM.getProcessTypeList()%>" varStatus="status">
            process_type_menu +=
                '<option ' + ((selected_value === ${status.index} ? 'selected' : '') + ' value = "${status.index}">${cell.ruName}</option>');
            </c:forEach>
        }
        process_type_menu += '</select>';

        return process_type_menu;

    }

    //noinspection JSUnusedLocalSymbols
    function changeOrder(row_index, selectedIndex) {

        recipients_row_index = row_index;
        if ((table_recipients !== null) && (recipients_row_index >= 0)) table_recipients.tBodies[0].rows[recipients_row_index].cells[1].id = selectedIndex;
        const menu = getOrderMenu(selectedIndex);
        table_recipients.tBodies[0].rows[recipients_row_index].cells[1].innerHTML = menu.replace('row_number', row_index);
    }

    //noinspection JSUnusedLocalSymbols
    function changeProcessType(row_index, selectedIndex) {

        recipients_row_index = row_index;

        if ((table_recipients !== null) && (recipients_row_index >= 0)) table_recipients.tBodies[0].rows[recipients_row_index].cells[2].id = selectedIndex;
        const menu = getProcessTypeMenu(selectedIndex);
        table_recipients.tBodies[0].rows[recipients_row_index].cells[2].innerHTML = menu.replace('row_number', row_index);
    }

    //noinspection JSUnusedLocalSymbols
    function checkRow(row_index) {
        recipients_row_index = row_index;
        setMarkOfLine();
    }

    function deleteAllRows() {

        // Should be in the table only head and tail
        // first row (0) - for normal view
        // last row (table.rows.length- 1)- for normal view

        if (table_recipients !== null
            && table_recipients.tBodies !== null
            && table_recipients.tBodies.length > 0
            && table_recipients.tBodies[0].rows.length > 1) {
            for (let i = table_recipients.tBodies[0].rows.length - 2; i >= 0; i--) {
                table_recipients.tBodies[0].deleteRow(i);
            }
            setVisibilityOfColumns();
        }

    }

    //noinspection JSUnusedLocalSymbols
    function deleteElement() {

        let current_row;

        if (recipients_row_index !== -1
            && table_recipients !== null
            && table_recipients.tBodies !== null
            && table_recipients.tBodies.length > 0
            && table_recipients.tBodies[0].rows.length > 1
        ) {

            for (let i = table_recipients.tBodies[0].rows.length - 1; i > recipients_row_index; i--) {
                current_row = table_recipients.tBodies[0].rows[i];
                current_row.cells[0].innerHTML = current_row.cells[0].innerHTML.replace("checkRow(" + i, "checkRow(" + (i - 1));
                current_row.cells[1].innerHTML = current_row.cells[1].innerHTML.replace("changeOrder(" + i, "changeOrder(" + (i - 1));
                current_row.cells[2].innerHTML = current_row.cells[2].innerHTML.replace("changeProcessType(" + i, "changeProcessType(" + (i - 1));
            }

            table_recipients.tBodies[0].deleteRow(recipients_row_index);
            recipients_row_index = -1;
            if (table_recipients.tBodies[0].rows.length <= 2) setVisibilityOfColumns();
            setNumberOfRecipients();
        }

    }

    //noinspection JSUnusedLocalSymbols
    function moveUpElement() {

        if ((table_recipients !== null) && (  table_recipients.tBodies[0].rows.length >= 3) && (recipients_row_index > 0)) {
            changeElementsInRow(recipients_row_index, recipients_row_index - 1);
            recipients_row_index--;
            setMarkOfLine();
        }

    }

    //noinspection JSUnusedLocalSymbols
    function moveDownElement() {

        if ((table_recipients !== null) && ( table_recipients.tBodies[0].rows.length >= 3) && (table_recipients.tBodies[0].rows.length - 2 > recipients_row_index)) {
            changeElementsInRow(recipients_row_index, recipients_row_index + 1);
            recipients_row_index++;
            setMarkOfLine();
        }

    }

    function changeElementsInRow(first, last) {

        let first_row_cell, last_row_cell, temp;

        for (let i = 0; i < table_recipients.tBodies[0].rows[0].cells.length; i++) {

            first_row_cell = table_recipients.tBodies[0].rows[first].cells[i];
            last_row_cell = table_recipients.tBodies[0].rows[last].cells[i];

            temp = first_row_cell.innerHTML;
            first_row_cell.innerHTML = last_row_cell.innerHTML;
            last_row_cell.innerHTML = temp;

            if (i === 0) {
                first_row_cell.innerHTML = first_row_cell.innerHTML.replace("checkRow(" + last, "checkRow(" + first);
                last_row_cell.innerHTML = last_row_cell.innerHTML.replace("checkRow(" + first, "checkRow(" + last)
            }
            else if (i === 1) {
                first_row_cell.innerHTML = first_row_cell.innerHTML.replace("changeOrder(" + last, "changeOrder(" + first);
                last_row_cell.innerHTML = last_row_cell.innerHTML.replace("changeOrder(" + first, "changeOrder(" + last)
            }
            else if (i === 2) {
                first_row_cell.innerHTML = first_row_cell.innerHTML.replace("changeProcessType(" + last, "changeProcessType(" + first);
                last_row_cell.innerHTML = last_row_cell.innerHTML.replace("changeProcessType(" + first, "changeProcessType(" + last)
            }
        }
    }

    //noinspection JSUnusedLocalSymbols
    function createMenuSendToUsers() {

        return '<input type="hidden" name="post_users[]" value="x" class="post_users" id="post_users"/>' +
            '<input type="hidden" name="post_order_type[]" value="0" class="post_order_type" id="post_order_type"/>' +
            '<input type="hidden" name="process_type" value="0" class="process_type" id="process_type"/>' +
            '<input type="hidden" name="post_process_type[]" value="0" class="post_process_type" id="post_process_type"/>' +
            '<input type="hidden" name="theme" value="x" id="theme"/>' +
            '<input type="hidden" name="textInfo" value="x" id="textInfo"/>' +
            '<input type="hidden" name="param" value="send" id="param"/>' +
            '<input type="hidden" name="whomId" value="x" id="whomId"/>' +
            '<input type="hidden" name="tempId" value="${param.tempId}" id="tempId"/>' +
            '<div class="horizontal">' +
            '<div>Конечный срок:</div>' +
            '<div><input name="finalDate"  placeholder="Конечный срок:" class="name" type="date" id = "finalDate" value="${finalDate}"required/></div>' +
            '</div>' +
            '<fieldset class="recipients">' +
            '<legend>Список рассылки:</legend>' +
            '<div class="recipients-buttons">' +
            '<a href="#form_choose_users" class="recipients-buttons"> <span class="btn">Выбрать</span> </a>' +
            '<a class="recipients-buttons"> <span class="btn" onclick="deleteElement();">Удалить</span> </a>' +
            '<a class="recipients-buttons"> <span class="btn" onclick="deleteAllRows();">Удалить все</span></a>' +
            '<a class="recipients-buttons"> <span class="btn changeable" onclick="moveUpElement();">Вверх</span></a>' +
            '<a class="recipients-buttons"> <span class="btn changeable" onclick="moveDownElement();">Вниз</span> </a>' +
            '</div>' +
            '<div class="table-wrapper">' +
            '<div class="table-scroll">' +
            '<table class="recipients-consequence" id="table-recipients"></table>' +
            '</div>' +
            '</div>' +
            '<div id="recipients-number">Адресатов в списке:<span id="recipients-number-element"></span></div>' +
            '</fieldset>' +
            '<div class="comment-for-recipients">Комментарий адресатам:</div>' +
            '<div class="div05">&nbsp</div>' +
            '<textarea style="width:100%; height:10%" name="comment"></textarea>' +
            '<div><input name="closeDocument" placeholder="Закрыть документ после отправки" type="checkbox" checked/>Закрыть документ после отправки</div>' +
            '<div id="separator" class="div05">&nbsp</div>' +
            '<div>' +
            '<div class="horizontal">' +
            '<div><input name="choose" class="btn" type="submit" value="Отправить" onclick="createUsersSequence();" formaction="javascript:void(0)"/></div>' +
            '<div>&nbsp;</div>' +
            '<div><a href="#current_document" class="recipients-buttons"><span class="btn">Отмена</span></a></div>' +
            '</div>' +
            '</div>';
    }

    //noinspection JSUnusedLocalSymbols
    function createMenuChooseUsers() {

        return '    Пользователи' +
            '<div class="div05">&nbsp</div>' +
            '    <a href="#form_send_to_users" class="recipients-buttons" onClick="addUsers();">' +
            '<span class="btn">Выбрать</span>' +
            '</a>' +
            '<table class="recipients-consequence" cellspacing="0" border="thin" cellpadding="0" id="table-users">' +
            '<tbody style="overflow: scroll">' +
            '</tbody>' +
            '</table>';
    }

    //noinspection JSUnusedLocalSymbols
    function createUsersSequence() {

        const post_users = [];
        const post_order_type = [];
        const post_process_type = [];
        const should_be_table_order_type = (table_recipients.tBodies[0].rows.length >= 4);  // 2 or more
        let should_be_table_post_process_type = (process_type === <%=Constant.SCENARIO_NUMBER%>);
        let i;

        location.replace(location.href.replace("#form_send_to_users", "#"));

        const errorString = getResultStringOfComparingBigFilesArray(fileList, ${Constant.MAX_FILE_SIZE});
        if (errorString !== "") {
            document.getElementById("info_result").innerHTML = errorString;
        } else {

            for (i = 0; i <= table_recipients.tBodies[0].rows.length - 2; i++) {

                // string = "name = "9" value="Журов Константин Александрович" placeholder="" type="text" onfocus="checkRow(1)" class="recipients-number-td">"
                const name_criteria_begin = "name=\"";
                const name_criteria_end = "\"";

                const first_row_cell = table_recipients.tBodies[0].rows[i].cells[0];

                const pos_begin = first_row_cell.innerHTML.indexOf(name_criteria_begin);
                const pos_end = first_row_cell.innerHTML.substring(pos_begin + name_criteria_begin.length).indexOf(name_criteria_end);
                post_users.push(first_row_cell.innerHTML.substring(pos_begin + name_criteria_begin.length, pos_begin + name_criteria_begin.length + pos_end));
                if (should_be_table_order_type === true) post_order_type.push((table_recipients.tBodies[0].rows[i].cells[1].id === "") ? "0" : table_recipients.tBodies[0].rows[i].cells[1].id);
                if (should_be_table_post_process_type === true) post_process_type.push((table_recipients.tBodies[0].rows[i].cells[2].id === "") ? "0" : table_recipients.tBodies[0].rows[i].cells[2].id);

            }

            document.getElementById("post_users").value = post_users;

            if (should_be_table_order_type === true) document.getElementById("post_order_type").value = post_order_type;

            document.getElementById("process_type").value = process_type;
            if (should_be_table_post_process_type === true) document.getElementById("post_process_type").value = post_process_type;

            const formData = new FormData(document.forms["menu_send_to_users"]);

            if (uploadedFileList !== null) {
                let uploadedFileString = "";
                for (i = 0; i < uploadedFileList.length; i++) uploadedFileString += uploadedFileList[i][1] + ";"; // File's md5Hex sum
                formData.append("uploadedFileString", uploadedFileString);
            }

            for (i = 0; i < fileList.length; i++) formData.append("fileList[]", fileList[i]);

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

            document.getElementById("info_result").innerHTML = "Отправка..." + getHtmlBlackoutAndLoading();

        }

    }

    function setMarkOfLine() {
        table_recipients.tBodies[0].rows[recipients_row_index].className = "recipients-tr-marked";
        for (let i = 0; i < table_recipients.tBodies[0].rows.length; i++)
            if ((i !== recipients_row_index) && (table_recipients.tBodies[0].rows[i].className === "recipients-tr-marked"))
                table_recipients.tBodies[0].rows[i].className = "recipients-number";

    }

    function setNumberOfRecipients() {
        document.getElementById("recipients-number-element").innerHTML = "" + (table_recipients.rows.length - 2);
    }

</script>

</html>
