<%--@elvariable id="PageContainer" type="tools"--%>
<%@ page import="tools.PageContainer"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<script>

    //noinspection JSUnusedLocalSymbols
    function insertCellInRow(index, row, name) {
        row.insertCell(index).innerHTML = name;
    }

    //noinspection JSUnusedLocalSymbols
    function onClickOpenTask(document, formName, documentId, executorTaskId, isExecutorTaskDraft) {
        const myForm = document.forms[formName];
        myForm.action = "${pageContext.request.contextPath}" + (isExecutorTaskDraft ? "${PageContainer.DOCUMENT_MEMORANDUM_CREATE_PAGE}" : "${PageContainer.EXECUTOR_TASK_PAGE}");
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

</script>