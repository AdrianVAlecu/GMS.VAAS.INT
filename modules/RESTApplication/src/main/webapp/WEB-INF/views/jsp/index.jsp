<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="en">

<header><title>GMS Vass test</title>

<body>
    <h2>This is the start page for GMS Vaas HehE</h2>
    <form action="<c:url value='api/tradeId' />" method="post" name="GMSAction">
        Query: <input id = "query" type = "text" name = "query"><br>
        <input id = "WorkflowNew" type="submit" value="StartWorkflow(post)">
    </form>
</body>

</html>