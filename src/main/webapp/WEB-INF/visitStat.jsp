<%@ page
    language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib
    uri="http://java.sun.com/jsp/jstl/core"
    prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Visits</title>
<link
    href="css/banner.css"
    rel="stylesheet"
    type="text/css" />
<link
    href="css/event.css"
    rel="stylesheet"
    type="text/css">
<link
    href="css/text.css"
    rel="stylesheet"
    type="text/css"></link>
<link
    href="css/table.css"
    rel="stylesheet"
    type="text/css"></link>
<script
    type="text/javascript"
    src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">

      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawChart1);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart1() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows([${chartRow}]);

        // Set chart options
        var options = {is3D: true,
        				'width':590,
                             'height':280,
                             'chartArea': {'width': '100%', 'height': '80%'},
                             backgroundColor: { fill:'transparent' },
                       legend: { position: 'right', alignment: 'center' ,textStyle: {color: 'white', fontSize: 14}}};

        // Instantiate and draw our chart, passing in some options.
          var chart = new google.visualization.PieChart(document.getElementById('drawChart1'));

        chart.draw(data, options);
      }
</script>
</head>
<body>
    <ul
        class="banner"
        style="margin: 2%;">
        <li style="float: right; margin-left: 4%;">
            <a
                class="active"
                href="/event/faces/logout.xhtml">Déconnection</a>
        </li>
        <li style="float: right">
            <a
                class="active"
                href="/event/faces/eventsList.xhtml">Liste d'évènements</a>
        </li>
    </ul>
    <div class="wrapper">
        <br>
        <form
            class="form"
            method="post">
            <div class="texte_white">Nombre de visites par page</div>
            <div class="emailList">
                <table>
                    <tr>
                        <td>URL</td>
                        <td>Nombre de visites</td>
                    </tr>
                    <c:forEach
                        var="stats"
                        items="${statsList}">
                        <tr>
                            <td>
                                <c:out value="${stats.webpageName}" />
                            </td>
                            <td>
                                <c:out value="${stats.count}" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="emailList">
                <div id="drawChart1"></div>
            </div>
            <input
                type="submit"
                class="submit"
                value="Effacer l'historique des visites"
                onclick="if(!confirm('Supprimer l`historique des visites ?')) {return false;}"
                name="deleteAllVisits">
        </form>
    </div>
</body>
</html>