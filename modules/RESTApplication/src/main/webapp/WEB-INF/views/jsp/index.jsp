<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html lang="en">
    <head>
        <script src="jquery.min.js"></script>
        <script>
            var refreshTable = function (jobs) {
                if ( typeof jobs != 'undefined' && jobs ) {
                    console.log("Incomming update size: " + jobs.allJobs.length);
                    $(document).ready(function() {
                        $('#allJobs').replaceWith('<div id="allJobs"></div>');

                        var table = $('<table/>').appendTo($('#allJobs'));
                        $(jobs.allJobs).each(function(i, jobStatus) {
                            $('<tr/>').appendTo(table)
                                .append($('<td/>').text(jobStatus.id))
                                .append($('<td/>').text(jobStatus.inputParams.query));
                        });
                    });
                };
            };

            var buildTable = function() {
                refreshTable(${jobs});
            };

            var connect = function () {
                var source = new EventSource('/register');

                // Handle correct opening of connection
                source.addEventListener('open', function (e) {
                    console.log('Connected.');
                });

                // Update the state when ever a message is sent
                source.addEventListener('message', function (e) {
                    var jobs = JSON.parse(e.data);
                    refreshTable(jobs);
                }, false);

                source.addEventListener('error', function(e) {
                    console.log('Disconnected.');
                    if (e.readyState == EventSource.CLOSED) {
                        connected = false;
                        connect();
                    }
                }, false);
            };
        </script>
    </head>
<header><title>GMS Vass test</title>

<body onload="connect();buildTable();">
    <h2>This is the start page for GMS Vaas HehE</h2>

    <form action="<c:url value='addJob' />" method="post" name="GMSAction">
        Query: <input id = "query" type = "text" name = "query" value="and dmOwnerTable in ('SWAP', 'MM', 'MUST_TR')"><br>
        <input id = "WorkflowNew" type="submit" value="StartWorkflow(post)">
    </form>

    <div id="allJobs"></div>

</body>

</html>