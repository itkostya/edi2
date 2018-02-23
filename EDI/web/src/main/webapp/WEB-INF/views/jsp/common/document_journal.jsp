<%--@elvariable id="TimeModule" type="enumerations"--%>
<%@ page import="app_info.TimeModule" %>

<%--@elvariable id="FolderStructure" type="enumerations"--%>
<%@ page import="enumerations.FolderStructure" %>

<%--@elvariable id="ProcessResult" type="enumerations"--%>
<%@ page import="enumerations.ProcessResult" %>

<%--@elvariable id="CommonModule" type="enumerations"--%>
<%@ page import="tools.CommonModule" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--@elvariable id="bookMark2" type="java.lang.String"--%>
<%--@elvariable id="filterString" type="java.lang.String"--%>
<%--@elvariable id="groupBy" type="java.lang.String"--%>
<%--@elvariable id="mapSortValue" type="java.lang.String"--%>
<%--@elvariable id="propertyMap" type="java.util.Map<enumerations.FolderStructure,java.lang.Integer>"--%>
<%--@elvariable id="tasksList" type="java.util.List<business_processes.ExecutorTaskFolderStructure>"--%>

<script>

    //noinspection JSUnusedLocalSymbols
    function refreshFolderStructure() {

        let row;
        const current_table = document.getElementById("folder_structure");
        let classNameText;

        <c:forEach var="cell" items="<%=FolderStructure.values()%>" varStatus="status">
        row = current_table.insertRow(${status.index});
        <c:choose>
        <c:when test="${bookMark2==cell.name()}">
        let isBold = (true === ${propertyMap.containsKey(cell)&&propertyMap[cell]>0} ? 'bold' : '');
        classNameText = '<div class="open2 ' + isBold + '">';
        insertCellInRow(0, row,
            classNameText + '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/enumerators/FolderStructure/${cell.enName.toLowerCase()}.png"><span class="folder_name">&nbsp;${(CommonModule.getFormattedText("%-13s",cell.ruName)).replace(" ","&nbsp;")}</span><span style=" word-break: normal;">${propertyMap.containsKey(cell) ? (CommonModule.getFormattedText("%3s",propertyMap[cell])).replace(" ","&nbsp;"):""}</span></div>');
        </c:when>
        <c:otherwise>
        classNameText = (true === ${propertyMap.containsKey(cell)&&propertyMap[cell]>0} ? '<div class="bold">' : '');
        insertCellInRow(0, row,
            classNameText + '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/enumerators/FolderStructure/${cell.enName.toLowerCase()}.png" onclick="document.getElementById(${cell.id}).click();"><button type="submit" name="bookMark2" value=${cell.name()} class="btn-link2 ${propertyMap.containsKey(cell)&&propertyMap[cell]>0 ? "bold":""}" id=${cell.id}><span class="folder_name">&nbsp;${(CommonModule.getFormattedText("%-13s",cell.ruName)).replace(" ","&nbsp;")}</span><span style=" word-break: normal;">${propertyMap.containsKey(cell) ? (CommonModule.getFormattedText("%3s",propertyMap[cell])).replace(" ","&nbsp;"):""}</span></button></div>');
        </c:otherwise>
        </c:choose>
        </c:forEach>

    }

    //noinspection JSUnusedLocalSymbols
    function refreshTasksListByGroup(current_table, documentType) {

        let body, row;
        let img_style, img_marked, row_style;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_tasks_list";
        insertCellInRow(0, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("0", colNum, colOrd, defSort, '') + '${(FolderStructure.valueOf(bookMark2) == FolderStructure.INBOX || FolderStructure.valueOf(bookMark2) ==  FolderStructure.TRASH) ? (groupBy=='sender' ? 'Отправитель' : 'Автор') : (groupBy=='sender' ? 'Получатель' : 'Кому')}</button>');
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="1.' + getColOrder("1", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("1", colNum, colOrd, defSort, '') + 'Тема</button>');
        insertCellInRow(2, row, '<button name="sortColumn" class="btn-link2" value="2.' + getColOrder("2", colNum, colOrd, defSort, '-') + '">' + getColSymbol("2", colNum, colOrd, defSort, '&uarr;') + 'Дата</button>');  // Up (desc)
        <c:choose><c:when test="${FolderStructure.valueOf(bookMark2) == FolderStructure.MARKED}">
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="3.' + getColOrder("3", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("3", colNum, colOrd, defSort, '') + '${(groupBy=='sender' ? 'Отправитель' : 'Автор')}</button>');
        </c:when></c:choose>

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${tasksList}" varStatus="status">  //tasksList: List<ExecutorTaskFolderStructure> getExecutorList(User user, FolderStructure folderStructure)

        row = body.insertRow(${status.index});

        img_marked = '<img class="image-for-table" ';
        <c:choose>
        <c:when test="${cell.folder == FolderStructure.TRASH}">
        img_marked += 'src="${pageContext.request.contextPath}/resources/images/documents/trash.png">';
        </c:when>
        <c:when test="${cell.marked}">
        img_marked += 'src="${pageContext.request.contextPath}/resources/images/command-bar/mark.png">';
        </c:when>
        <c:otherwise>
        img_marked += 'src="${pageContext.request.contextPath}/resources/images/documents/'+documentType+'/document_type.png">';
        </c:otherwise>
        </c:choose>
        insertCellInRow(0, row, img_marked + getHighlightedText('${(cell.folder == FolderStructure.INBOX || cell.folder ==  FolderStructure.TRASH) ? ((groupBy=='sender') ? cell.executorTask.author.fio : cell.executorTask.document.author.fio) : ((groupBy=='sender')? cell.executorTask.executor.fio : cell.executorTask.document.whomString) }', filterString));

        img_style = '<img class="image-for-table" ';
        <c:choose>
        <c:when test="${cell.executorTask.result == null && cell.executorTask.processType == null}">
        img_style += 'src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/accommodation/accept.png">';
        </c:when>
        <c:when test="${cell.executorTask.result == null}">
        img_style += 'src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessType/${cell.executorTask.processType.enName.toLowerCase()}/accept.png">';
        </c:when>
        <c:when test="${cell.executorTask.result == ProcessResult.DECLINED}">img_style += 'src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessResult/declined.png">';
        </c:when>
        <c:when test="${cell.executorTask.result == ProcessResult.CANCELED}">img_style += 'src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessResult/canceled.png">';
        </c:when>
        <c:otherwise>img_style += 'src="${pageContext.request.contextPath}/resources/images/enumerators/ProcessResult/completed.png">';
        </c:otherwise>
        </c:choose>
        insertCellInRow(1, row, img_style + getHighlightedText('  ${cell.executorTask.processType.ruName}, ${cell.executorTask.document.number}, ${CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)}', filterString));

        insertCellInRow(2, row, getHighlightedText('${TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm')}', filterString));

        <c:choose><c:when test="${FolderStructure.valueOf(bookMark2) == FolderStructure.MARKED}">
        insertCellInRow(1, row, img_marked + getHighlightedText('${groupBy=='sender' ? cell.executorTask.author.fio : cell.executorTask.document.author.fio}', filterString));
        </c:when></c:choose>

        row_style = "";
        <c:choose><c:when test="${!cell.executorTask.completed}">row_style += "color:red; font: bold 100% serif;";
        </c:when></c:choose>
        <c:choose><c:when test="${cell.executorTask.completed && cell.executorTask.result != ProcessResult.DECLINED &&  cell.executorTask.result != ProcessResult.CANCELED}">row_style += "color:rgb(14, 101, 5);";
        </c:when></c:choose>
        <c:choose><c:when test="${cell.executorTask.completed && cell.folder == FolderStructure.TRASH}">row_style += "color:rgb(0, 0, 0);";
        </c:when></c:choose>
        <c:choose><c:when test="${cell.executorTask.completed && cell.folder == FolderStructure.DRAFT}">row_style += "color:rgb(111, 111, 111);";
        </c:when></c:choose>
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += " background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style = row_style;

        <c:choose>
        <c:when test="${cell.folder == FolderStructure.INBOX}">
        row.ondblclick = function () {
            onClickOpenTask("myForm", ${cell.executorTask.document.id}, ${cell.executorTask.id}, ${cell.executorTask.draft}, "${cell.executorTask.document.documentProperty}")
        };
        </c:when>
        <c:otherwise>
        row.ondblclick = function () {
            onClickOpenTask("myForm", ${cell.executorTask.document.id}, null, ${cell.executorTask.draft}, "${cell.executorTask.document.documentProperty}")
        };
        </c:otherwise>
        </c:choose>

        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><c:choose><c:when test='${FolderStructure.valueOf(bookMark2) == FolderStructure.MARKED}'><td class='td_sr'>4</td></c:when></c:choose></tr>";

    }

    //noinspection JSUnusedLocalSymbols
    function refreshFullTasksList(current_table, documentType) {

        let body, row;
        let row_style, img_marked, img_executed;
        let defSort = (true === ${mapSortValue == 'default'});
        let colNum = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(0)}");
        let colOrd = (true === ${mapSortValue == 'default'} ? null : "${mapSortValue.charAt(2)}");
        let filterString = '${filterString}';

        row = current_table.createTHead().insertRow(0);
        row.className = "first_row_tasks_list";
        insertCellInRow(0, row, '<button name="sortColumn" class="btn-link2" value="0.' + getColOrder("0", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("0", colNum, colOrd, defSort, '') + 'Номер');
        insertCellInRow(1, row, '<button name="sortColumn" class="btn-link2" value="1.' + getColOrder("1", colNum, colOrd, defSort, '-') + '">' + getColSymbol("1", colNum, colOrd, defSort, '&uarr;') + 'Дата');  // Up (desc)
        insertCellInRow(2, row, '<button name="sortColumn" class="btn-link2" value="2.' + getColOrder("2", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("2", colNum, colOrd, defSort, '') + 'Документ');
        insertCellInRow(3, row, '<button name="sortColumn" class="btn-link2" value="3.' + getColOrder("3", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("3", colNum, colOrd, defSort, '') + 'Автор');
        insertCellInRow(4, row, '<button name="sortColumn" class="btn-link2" value="4.' + getColOrder("4", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("4", colNum, colOrd, defSort, '') + 'Отправитель');
        insertCellInRow(5, row, '<button name="sortColumn" class="btn-link2" value="5.' + getColOrder("5", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("5", colNum, colOrd, defSort, '') + 'Получатель');
        insertCellInRow(6, row, '<button name="sortColumn" class="btn-link2" value="6.' + getColOrder("6", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("6", colNum, colOrd, defSort, '') + 'Тема');
        insertCellInRow(7, row, '<button name="sortColumn" class="btn-link2" value="7.' + getColOrder("7", colNum, colOrd, defSort, 'n') + '">' + getColSymbol("7", colNum, colOrd, defSort, '') + 'ok'); // Выполнена

        body = current_table.appendChild(document.createElement('tbody'));
        <c:forEach var="cell" items="${tasksList}" varStatus="status">
        row = body.insertRow(${status.index});

        img_marked = '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/documents/'+documentType+'/document_type.png"> ';
        insertCellInRow(0, row, img_marked + getHighlightedText('${cell.executorTask.document.number}', filterString));
        insertCellInRow(1, row, getHighlightedText('${TimeModule.getDate(cell.executorTask.date, 'dd.MM.yyyy HH:mm:ss')}', filterString));
        insertCellInRow(2, row, getHighlightedText('${cell.executorTask.document.getDocumentView("dd.MM.yyyy")}', filterString));
        insertCellInRow(3, row, getHighlightedText('${cell.executorTask.document.author.fio}', filterString));
        insertCellInRow(4, row, getHighlightedText('${cell.executorTask.author.fio}', filterString));
        insertCellInRow(5, row, getHighlightedText('${cell.executorTask.executor.fio}', filterString));
        insertCellInRow(6, row, getHighlightedText('${CommonModule.getCorrectStringForWeb(cell.executorTask.document.theme)}', filterString));

        img_executed = (true === ${true == cell.executorTask.completed} ? '<img class="image-for-table" src="${pageContext.request.contextPath}/resources/images/GreenMark.png">' : '');
        insertCellInRow(7, row, img_executed);

        row_style = "";
        <c:choose><c:when test="${!cell.executorTask.completed}">row_style += "color:red; font: bold 100% serif;";
        </c:when></c:choose>
        <c:choose><c:when test="${(status.index % 2) == 0}">
        row_style += "background: rgb(255, 248, 234); ";
        </c:when></c:choose>
        row.style = row_style;

        row.ondblclick = function () {
            onClickOpenTask("myForm", ${cell.executorTask.document.id}, ${cell.executorTask.id}, ${cell.executorTask.draft}, "${cell.executorTask.document.documentProperty}")
        };

        </c:forEach>

        current_table.createTHead().insertRow(1).outerHTML = "<tr class='second_row'><td class='td_sr'>1</td><td class='td_sr'>2</td><td class='td_sr'>3</td><td class='td_sr'>4</td><td class='td_sr'>5</td><td class='td_sr'>6</td><td class='td_sr'>7</td><td class='td_sr'>8</td></tr>";

    }

</script>