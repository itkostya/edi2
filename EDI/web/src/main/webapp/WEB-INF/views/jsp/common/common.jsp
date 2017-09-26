<%--@elvariable id="Constant" type="enumerations"--%>
<%@ page import="app_info.Constant" %>

<%--@elvariable id="PageContainer" type="tools"--%>
<%@ page import="tools.PageContainer"%>

<%--@elvariable id="documentPropertyString" type="java.lang.String"--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>

    //noinspection JSUnusedLocalSymbols
    function insertCellInRow(index, row, name) {
        row.insertCell(index).innerHTML = name;
    }

    function getCreateDocumentPageName(documentPropertyString) {
        switch (documentPropertyString.toUpperCase()) {
            case "MEMORANDUM":
                return "${PageContainer.getCreatePageStringByDocumentProperty('MEMORANDUM')}";
            case "MESSAGE":
                return "${PageContainer.getCreatePageStringByDocumentProperty('MESSAGE')}";
        }
        return "";
    }

    //noinspection JSUnusedLocalSymbols
    function createNewDocument(operationType, documentPropertyString, documentCopyId) {
        const formCreateDocument = document.forms["createDocument"];
        formCreateDocument.action = getCreateDocumentPageName(documentPropertyString);
        formCreateDocument.elements["operationType"].value = operationType;
        formCreateDocument.elements["documentCopyId"].value = documentCopyId;
        formCreateDocument.elements["tempId"].value = getRandomInt();
        formCreateDocument.submit();
    }

    //noinspection JSUnusedLocalSymbols
    function onClickOpenTask(formName, documentId, executorTaskId, isExecutorTaskDraft, documentPropertyString) {
        const myForm = document.forms[formName];
        myForm.action = "${pageContext.request.contextPath}" + (isExecutorTaskDraft ? getCreateDocumentPageName(documentPropertyString): "${PageContainer.EXECUTOR_TASK_PAGE}");
        myForm.elements["documentId"].value = documentId;
        myForm.elements["executorTaskId"].value = executorTaskId;
        myForm.elements["tempId"].value = getRandomInt();
        myForm.submit();
    }

    function getRandomInt() {
        const min = 0;
        const max = Math.pow(2, 30);
        return Math.floor(Math.random() * (max - min)) + min;
    }

    // FOR SORTING AND VIEW TABLES BEGIN

    // Example: <button type="submit" name="sortColumn" class="btn-link2" value="0.'+getColOrder("0", colNum, colOrd, defSort,'-')+'">'+getColSymbol("0", colNum, colOrd, defSort,'&darr;')+
    // 'Process type</button>');
    // Examples of value: 0.+, 1.-, 2.n equal: 0 column descending sorting, 1 column ascending sorting, 2 column no sorting
    // Down (asc), Up (desc)

    //noinspection JSUnusedLocalSymbols
    function getColOrder(constColumn, colNum, colOrd, defSort, defaultSymbol) {
        return (defSort ? defaultSymbol : (constColumn === colNum ? colOrd : 'n'));
    }

    //noinspection JSUnusedLocalSymbols
    function getColSymbol(constColumn, colNum, colOrd, defSort, defaultSymbol) {
        return (defSort ? defaultSymbol : (constColumn === colNum ? ((colOrd === '+' || colOrd === 'n') ? '&darr;' : '&uarr;') : ''));  //  &uarr - Up (desc), &darr - Down (asc)
    }

    // FOR SORTING AND VIEW TABLES END

    //noinspection JSUnusedLocalSymbols
    function getExtensionImageByFilename(fileName) {

        const extension = fileName.substr(fileName.lastIndexOf('.') + 1);

        switch (extension) {
            case "doc":
            case "dot":
            case "rtf":
            case "docx":
                return "word.png";
            case "rar":
            case "zip":
            case "7z":
                return "archive.png";
            case "xls":
            case "xlw":
            case "xlsx":
                return "excel.png";
            case "jpg":
            case "jpeg":
            case "jp2":
            case "jpe":
            case "bmp":
            case "dib":
            case "tif":
            case "gif":
            case "png":
                return "photo.png";
            case "txt":
            case "log":
            case "ini":
                return "text.png";
            case "pdf":
                return "pdf.png";
            case "java":
            case "jar":
                return "java.png";
            case "dt":
            case "1cd":
            case "cf":
            case "cfu":
            case "epf":
            case "erf":
                return "1c.png";
            default:
                return "unknown.png"
        }

    }

    //  -----  Filter - fill field and set mark BEGIN -----

    //noinspection JSUnusedLocalSymbols
    function onKeyupSearchString(event, formName) {

        if (document.getElementsByName(event.srcElement.name)!== null) {

            let currentString = document.getElementsByName(event.srcElement.name)[0].value;
            setTimeout(function () {
                searchingWithDelay(currentString, event, formName)
            }, 1500);
        }
    }

    function searchingWithDelay(currentString, event, formName) {
        if (document.getElementsByName(event.srcElement.name)!== null) {
            if (currentString === document.getElementsByName(event.srcElement.name)[0].value) {
                document.forms[formName].submit();
            }
        }
    }

    //noinspection JSUnusedLocalSymbols
    function getHighlightedText(basicString, filterString) {

        if ((basicString === "") || (basicString === null) || (filterString === "") || (filterString === null)) return basicString;

        return basicString.replace(new RegExp(filterString.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\\\$&'), 'gi'),
            function(match)
                {return "<mark>"+match+"</mark>"});

    }

    //  -----  Filter - fill field and set mark END -----

    //  -----  Control array files sizes BEGIN -----

    // if return "" -> it's ok else error
    //noinspection JSUnusedLocalSymbols
    function getResultStringOfComparingBigFilesArray(arr, maxFileSize) {

        let bigFilesArray = arr.filter(function (value) {
            return (value.size > maxFileSize);
        });

        return (bigFilesArray.length > 0 ? "Размер файла(-ов) превышает максимальный ("+maxFileSize+"): "+createStringForFileArray(bigFilesArray) : "");

    }

    function createStringForFileArray(arr){
        return arr.map(f => "Файл: " + f.name + " размер: " + f.size).join(" ");
    }

    //  -----  Control array files sizes END -----

    // Zhurov   Konstantin  Aleksandrovich  -> Zhurov K. A. (with more than 1 whitespaces)
    // Zhurov  Konstantin -> Zhurov K.
    //noinspection JSUnusedLocalSymbols
    function getFioAbbreviated(userFioFull) {

        userFioFull = userFioFull.replace("&nbsp", " ").replace("&nbsp", " ");
        userFioFull = userFioFull.replace(String.fromCharCode(160), " ").replace(String.fromCharCode(160), " ");
        userFioFull = userFioFull.replace(/\s\s+/g, ' ').split(" ");
        if (userFioFull.length > 0 && userFioFull[userFioFull.length - 1] === "") userFioFull.splice(userFioFull.length - 1, 1);
        return (userFioFull.length >= 1 ? userFioFull[0] + (userFioFull.length >= 2 ? " " + userFioFull[1].charAt(0) + "." +
            (userFioFull.length >= 3 ? " " + userFioFull[2].charAt(0) + "." : "") : "") : "");

    }

    //  -----  Check result after sending document BEGIN -----

    //noinspection JSUnusedLocalSymbols
    function afterErrorPageCheckResult(xhr) {

        if (xhr.status !== 200) {
            document.getElementById("info_result").innerHTML = "Ошибка (возможно превышен максимальный размер файла ${Constant.MAX_FILE_SIZE} или всех файлов ${Constant.MAX_REQUEST_SIZE})";
        }
    }

    //noinspection JSUnusedLocalSymbols
    function afterLoadingPageCheckResult(xhr, reloadParentsPage) {

        if (xhr.readyState === 4) {
            let el = document.createElement('html');
            el.innerHTML = xhr.responseText;
            if (el.getElementsByTagName('div') !== null && el.getElementsByTagName('div').length !== 0) {
                let newInfoResult = el.getElementsByTagName('div').namedItem('info_result').innerHTML;
                if ((newInfoResult === null) || (newInfoResult === '')) {
                    window.location.reload(true);
                    if (reloadParentsPage) window.opener.location.reload(true); // Reload parent's page
                } else {
                    document.getElementById("info_result").innerHTML = newInfoResult;
                }
            } else {
                window.location.reload(true);
                if (reloadParentsPage) window.opener.location.reload(true); // Reload parent's page
            }
        }
    }

    //  -----  Check result after sending document END -----

    //noinspection JSUnusedLocalSymbols
    function replaceTextInHtmlElement(htmlElement, basicText, newText){
        if (htmlElement !== null) htmlElement.innerHTML = htmlElement.innerHTML.replace(basicText, newText);
    }

    //noinspection JSUnusedLocalSymbols
    function getHtmlBlackoutAndLoading() {

        return "<div style='background: #000;top: 0; left: 0; position:fixed; height: 100%;width: 100%;opacity: 0.5; z-index: 9990;'></div>" +
            "<img  style='position:absolute; left:40%; top:40%;' src='${pageContext.request.contextPath}/resources/images/gif/loading.gif' >";

    }

</script>