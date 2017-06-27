<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>

<html lang="en">

<header><title>GMS Vass test</title>

<body>
    <h2>This is the start page for GMS Vaas HehE</h2>
    <c:forEach var="entry" items="${requests}">
        Name: ${entry.key} <br/>
        Value: ${entry.value} <br/>
    </c:forEach>

    <form action="<c:url value='tradeId' />" method="post" name="GMSAction">
<!--    <form action="#" th:action="@{/tradeId}" th:object="${QueryImp}" method="post" name="GMSAction">-->
        Query: <input id = "query" type = "text" name = "query" value="and dmOwnerTable in ('SWAP', 'MM', 'MUST_TR')"><br>
        <input id = "WorkflowNew" type="submit" value="StartWorkflow(post)">
    </form>
</body>

</html>