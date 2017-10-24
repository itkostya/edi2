<%--@elvariable id="CommonModule" type="enumerations"--%>
<%@ page import="tools.CommonModule" %>

<%--@elvariable id="PageContainer" type="enumerations"--%>
<%@ page import="tools.PageContainer" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="docDate" type="java.text.SimpleDateFormat"--%>
<%--@elvariable id="docNumber" type="java.lang.String"--%>
<%--@elvariable id="docText" type="java.lang.String"--%>
<%--@elvariable id="docTheme" type="java.lang.String"--%>
<%--@elvariable id="docType" type="java.lang.String"--%>
<%--@elvariable id="positionFrom" type="java.lang.String"--%>
<%--@elvariable id="positionTo" type="java.lang.String"--%>
<%--@elvariable id="userFrom" type="java.lang.String"--%>
<%--@elvariable id="userTo" type="java.lang.String"--%>

<%--suppress HtmlUnknownAttribute --%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="urn:schemas-microsoft-com:office:office" >

<link href="<c:url value="/resources/css/documents/memorandum_view.css"/>" rel="stylesheet" type="text/css">

<div style='text-align: center;'>
    <div class=WordSection1>

        <p class=MsoNoSpacing align=right style='text-align:right'><span class=SpellE>
            <span class='TextSpan' id='positionTo'>${positionTo}</span></span>
            <span class='TextSpan'><o:p/></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'><span class=SpellE>
            <span class='TextSpan' id='userTo'>${userTo}</span></span>
            <span class='TextSpan'><o:p/></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'><span class=SpellE>
            <span class='TextSpan'>${positionFrom}</span></span>
            <span class='TextSpan'><o:p/></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'>
            <span class=SpellE><span class='TextSpan'>${userFrom}</span></span>
            <span class='TextSpan'><o:p/></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'>
            <span class='TextSpan'><o:p>&nbsp;</o:p></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'>
            <span class='TextSpan'><o:p>&nbsp;</o:p></span></p>

        <p class=MsoNoSpacing align=right style='text-align:right'>
            <span class='TextSpan'><o:p>&nbsp;</o:p></span></p>

        <p class=MsoNoSpacing align=center style='text-align:center'>
            <b style='mso-bidi-font-weight:normal'><span class='TextSpan'>${docType} № <u>${docNumber}</u>
                от ${docDate}</span></b></p>

        <p class=MsoNoSpacing align=center style='text-align:center'>
            <b style='mso-bidi-font-weight:normal'><span class='TextSpan'><o:p>&nbsp;</o:p></span></b></p>

        <p class=MsoNoSpacing align=center style='text-align:center'>
            <span class=SpellE><b style='mso-bidi-font-weight:normal'>
            <span class='TextSpan' id='docTheme'>${docTheme}</span></b></span>
            <b style='mso-bidi-font-weight:normal'><span class=Text><o:p/></span></b></p>

        <div style='text-align: left;padding-left:2em'>
            <p class=MsoNoSpacing><span class='TextSpan' id='docText'>
                ${(docText.contains(PageContainer.EXECUTOR_TASK_PAGE) ? CommonModule.getDocumentLinkView(docText): docText)}
            </span></p>
        </div>

        <p class=MsoNoSpacing>
            <span class='TextSpan'><o:p>&nbsp;</o:p></span></p>

        <p class=MsoNoSpacing>
            <span class='TextSpan'><o:p>&nbsp;</o:p></span></p>

        <div id='uploadedFiles' style='text-align:left'></div>

        <p class=MsoNoSpacing align=right style='text-align:right'>
            <span class='TextSpan'>________________
                <span class=SpellE>${userFrom}</span><o:p/></span></p>

        <br><br>
        <div id="signatures"></div>
    </div>
</div>

</html>
