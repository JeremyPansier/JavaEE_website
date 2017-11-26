function drawChart(id, rows) {
	// Create the data table.
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Topping');
	data.addColumn('number', 'Slices');
	data.addRows(rows);

	// Set chart options
	if (rows.length <= 3) {
		var options = {
				is3D: true,
				width:600,
				height:280,
				chartArea: {width: '90%', height: '80%'},
				backgroundColor: {fill:'transparent'},
				colors : ['#37E54C','red', '#0085E7'],
				legend: {
					position: 'right',
					alignment: 'center',
					textStyle: {color: 'white',
					fontSize: 14}}
				};
	} else {
		var options = {
				is3D: true,
				width:600,
				height:280,
				chartArea: {width: '90%', height: '80%'},
				backgroundColor: {fill:'transparent'},
				legend: {
					position: 'right',
					alignment: 'center',
					textStyle: {color: 'white',
					fontSize: 14}}
					}
				};
	
	// Instantiate and draw our chart, passing in some options.
	var chart = new google.visualization.PieChart(document.getElementById(id));
	
	chart.draw(data, options);
}
