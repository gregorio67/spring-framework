<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/resources/css/jquery-ui-1.10.1.css" type="text/css">
<link rel="stylesheet" href="/resources/css/table.css" type="text/css">
<link rel="stylesheet" href="/resources/css/c3.min.css" type="text/css">
<link rel="stylesheet" href="/resources/css/melon.datepicker.css" type="text/css">



<script type="text/javascript" src="/resources/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui-1.10.1.min.js"></script>  
<script type="text/javascript" src="/resources/js/c3.min.js"></script>
<script type="text/javascript" src="/resources/js/d3.v3.min.js" charset="utf-8"></script>

<script>

	$(document).ready(function() {
		$("#btnSearch").click(function(e) {
		    e.preventDefault();
		    
			debugger;
			
			var objectData =
	         {
	             startDate: $('#startDate').val(),
	             chartType: $('#chartType').val(),
	             interfaceId: $('#interfaceId').val()
	         };

			var sendData = JSON.stringify(objectData);
			
		    $.ajax({
		        type: "POST",
		        url: '/chart/showStatistics.do',
		        data : sendData,
		        dataType : "json",  //Cross domain
		        contentType: "application/json",
		        success : function(response, status, xhr) {
		        	console.log(response); 
		        	showChart(response);
		        }, 
		        error: function(jqXHR, textStatus, errorThrown) {
		        	console.log(jqXHR.responseText); 
		        }
		    });
		});	

	});

	//Date Picker
	$(function() {
	    $( "#startDate" ).datepicker({
	    	defaultDate: new Date(),
	    	dateFormat: "yy-mm-dd",
	    	changeMonth: true, 
	        changeYear: true,
	        dayNames:['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat']
	    });
	});
	
	
	function showChart(response) {
		debugger;
		var graphType =  $('#chartType').val().toUpperCase() + " Statistic Graph";
		$( "#graphType" ).text(graphType);
		
		//Generate Categories
		var categories=[];
		
		$.each(response.categories, function(idx, data) {
			categories.push(data);			
		});
		
		//Generate Column Data
		var columns = [];
		$.each(response.columns, function (idx, data) {
			var col = [];
			var idx = 0;
			$.each(data, function(key, value) {
				debugger;
				col[idx] = value;
				idx++;
			});
			columns.push(col);
			
		});
		
		var chart = c3.generate({
			bindto: "#interfacechart",
		    size: {
		        height: 600,
		        width: 1200
		    },
		    data: {
		        type: 'bar',
		        columns: columns,
		    },
		    bar: {
		        width: {
		            ratio: 0.3 // this makes bar width 50% of length between ticks
		        }
		        // or
		        //width: 100 // this makes bar width 100px
		    },
		    axis: {
		    	x: {
		    		type: 'category',
		    		categories:categories
		    	}
		    },
		    grid: {
		        x: {
		            show: true
		        },
		        y: {
		            show: true
		        }
		    }
		});

//		setTimeout(function () {
//			chart.load({
//		        columns: [
//		            ['data3', 130, -150, 200, 300, -200, 100]
//		        ]
//		    });
//		}, 1000);
	}

	
</script>
<title>Interface Monitoring</title>
</head>

<body>
	<!-- Search Area -->
	<div class="container">
        <form action="">
        	<div class="fieldName">
	       		<label>START DATE</label>
        	</div>
        	<div class="select">
	            <input type="text" id="startDate" size=10 style="align=center;"/>
        	</div>
        	
        	<div class="fieldName">
	           	<label>Statistic Period</label>
 			</div>
        	<div class="select">
	            <select name="chartType" id="chartType">
			        <option value="daily">Daily</option>
			        <option value="monthly">Monthly</option>
			    </select>
			</div>
        	<div class="fieldName">
	           	<label>Interface Name</label>
 			</div>
			<div class="data">
				<input type="text" id="interfaceId" title="Enter Interface Name" size="30"></input>
			</div>
        	<div class="logbutton">
	           	<button  id="btnSearch">Search</button>
	        </div>
 		</form>
	</div>

	<div>
		<label style="height:100%;padding:5px; margin-top:10px; font-family:Sans-serif; font-size:1.2em;" id="graphType">Statistic Graph</label>
	</div>
	
	<div id="interfacechart" style="width: 1200px; height: 500px;"></div>
</body>
  
</body>
</html>
