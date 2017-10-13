<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="uploadedFiles" type="java.util.List<categories.UploadedFile>"--%>

<html>
<head>
    <%--Should be included in documents (memorandum_create, executor_task, etc.) before including this file.--%>
    <%--I don't include common.jsp here because result file (memorandum_create, executor_task, etc.) --%>
    <%--with different common files (add_files, send_document, whom_menu) would include common.jsp many times --%>
    <%--<jsp:include page="../common/common.jsp"/>--%>
</head>

<script>

    //noinspection JSUnusedLocalSymbols
    function fillUploadedFiles() {

        uploadedFileList.length = 0;
        <c:forEach var="cell" items="${uploadedFiles}" varStatus="status">
        uploadedFileList[${status.index}] = [];
        uploadedFileList[${status.index}][0] = "${cell.name}";
        uploadedFileList[${status.index}][1] = "${cell.fileName}";  // File's md5Hex sum
        </c:forEach>

        return uploadedFileList;
    }

    //noinspection JSUnusedLocalSymbols
    function addFilesAndResizeTable(uploadedFileList = null) {

        let bodyUploadedList, bodyFileList, row;

        const current_table = document.getElementById("table_files");
        const input_add_files = document.getElementById("input_add_files");
        const coefficientColumnFileNames = 28;
        let i, file;

        if (current_table !== null) {

            if (current_table.tHead === null || current_table.tHead.rows.length === 0) {
                row = current_table.createTHead().insertRow(0);
                insertCellInRow(0, row, '');
                insertCellInRow(1, row, 'Наименование');
            }

            if (current_table.tBodies === null || current_table.tBodies.length === 0) {
                bodyUploadedList = current_table.appendChild(document.createElement('tbody'));
                bodyFileList = current_table.appendChild(document.createElement('tbody'));
            }
            else {
                bodyUploadedList = current_table.tBodies.item(0);
                bodyFileList = current_table.tBodies.item(1);
            }

            bodyUploadedList.innerHTML = "";
            bodyFileList.innerHTML = "";

            if (uploadedFileList !== null && uploadedFileList.length > 0) {
                for (i = 0; i < uploadedFileList.length; i++) {
                    row = bodyUploadedList.insertRow(i);
                    let i1 = i;
                    insertCellInRow(0, row, "<img src='${pageContext.request.contextPath}/resources/images/files/" + getExtensionImageByFilename(uploadedFileList[i1][0]) + "'>");
                    insertCellInRow(1, row,
                        (document.body.clientWidth < coefficientColumnFileNames * uploadedFileList[i1][0].length) ? uploadedFileList[i1][0].substr(0, (document.body.clientWidth / coefficientColumnFileNames) - 3) + "..." : uploadedFileList[i1][0]);

                    row.onclick = function () {setMarkOfRowTableFiles(0, i1)};
                }
            }

            if ('files' in input_add_files) {
                for (i = 0; i < input_add_files.files.length; i++) {
                    file = input_add_files.files[i];
                    if ('name' in file) {
                        fileList.push(file)
                    }
                }

                document.getElementById("input_add_files").value = "";

                for (i = 0; i < fileList.length; i++) {
                    row = bodyFileList.insertRow(i);
                    let i1 = i;
                    file = fileList[i];
                    if ('name' in file) {
                        insertCellInRow(0, row, "<img src='${pageContext.request.contextPath}/resources/images/files/" + getExtensionImageByFilename(file.name) + "'>");
                        insertCellInRow(1, row,
                            (document.body.clientWidth < coefficientColumnFileNames * file.name.length) ? file.name.substr(0, (document.body.clientWidth / coefficientColumnFileNames) - 3) + "..." : file.name);
                    }
                    row.onclick = function () {setMarkOfRowTableFiles(1, i1)};
                }
            }
            setMarkLineTableFiles(-1,-1);
        }

    }

    function setMarkOfRowTableFiles(currentTableIndex, currentPosition) {

        const prevRowMarkedTableIndex = rowMarkedTableIndex;
        const prevRowMarkedIndex = rowMarkedIndex;

        rowMarkedTableIndex = currentTableIndex;
        rowMarkedIndex = currentPosition;
        setMarkLineTableFiles(prevRowMarkedTableIndex, prevRowMarkedIndex);
        
    }

    function setMarkLineTableFiles(prevRowMarkedTableIndex, prevRowMarkedIndex) {

        if (rowMarkedIndex !== -1 ) {
            document.getElementById("table_files").tBodies.item(rowMarkedTableIndex).rows[rowMarkedIndex].className = "files-tr-marked";
        }

        if (prevRowMarkedIndex !== -1 ) {
            document.getElementById("table_files").tBodies.item(prevRowMarkedTableIndex).rows[prevRowMarkedIndex].className = "";
        }

    }

    //noinspection JSUnusedLocalSymbols
    function deleteRowInTableFiles() {

        if (rowMarkedIndex !== -1 ) {

            const current_table = document.getElementById("table_files");
            const body = current_table.tBodies.item(rowMarkedTableIndex);
            let row;

            body.deleteRow(rowMarkedIndex);

            const currentList = ( rowMarkedTableIndex === 0 ? uploadedFileList : fileList);
            if (currentList!== null) {
                currentList.splice(rowMarkedIndex, 1);
                for (let i = rowMarkedIndex; i < currentList.length; i++) {
                    row = body.rows[i];
                    let i1 = i;
                    row.onclick = function () {
                        setMarkOfRowTableFiles(rowMarkedTableIndex, i1)
                    }
                }
            }

            const uploadedFileListLength = (uploadedFileList === null ? 0 : uploadedFileList.length);
            const commonTabIndex = Math.min(rowMarkedTableIndex * uploadedFileListLength + rowMarkedIndex, uploadedFileListLength + fileList.length);
            rowMarkedTableIndex = ( uploadedFileListLength === 0 ? 1 : (fileList.length === 0 ? 0 : (commonTabIndex >= uploadedFileListLength ? 1 : 0 )));
            rowMarkedIndex = (commonTabIndex >= (uploadedFileListLength + fileList.length) ? commonTabIndex - rowMarkedTableIndex * uploadedFileListLength - 1 : commonTabIndex - rowMarkedTableIndex * uploadedFileListLength);

            setMarkLineTableFiles(-1,-1);

        }
    }

</script>

</html>
