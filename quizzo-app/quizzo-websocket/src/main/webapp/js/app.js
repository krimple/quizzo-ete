(function() {

	var answers = [];
	var players = [];
	var data = new Array(120);
	
	var keepDrawing = true;
	var chartElement;
	var rect;
	var margin = {top: 6, right: 0, bottom: 160, left: 40};
	var width = 960 - margin.right;
	var height = 400 - margin.top - margin.bottom;
	
	function redraw() {
		// Workaround for Tomcat not having a separate timeout value for WebSockets
		// See http://comments.gmane.org/gmane.comp.jakarta.tomcat.user/221280
		ping();
		if (!keepDrawing) {
			return;
		}
		
		rect = chartElement.selectAll("rect")
	   .data(data)
	   .transition()
	   .duration(1000)
	   .attr("y", function(d,i){ return height - 2.5*d - 24; } )
	   .attr("height", function(d,i) { return  2.5*d; });
	}

	function QuizzoWebSocket() {

		var ready = false;

		var socket = new WebSocket('ws://localhost:8081');

		socket.onopen = function(event) {
			ready = true;
			socket.send("Client Ready");
		};

		socket.onmessage = function(event) {
			//console.log(event.data);
			var answer = JSON.parse(event.data);
			if (players.indexOf(answer.playerId) < 0) {
				players.push(answer.playerId);
			}
			
			var questionBase = (answer.questionNumber-1)*6;
			
			if (answer.choice == 'a') {
				data[questionBase + 1]++;
			}
			if (answer.choice == 'b') {
				data[questionBase + 2]++;
			}
			if (answer.choice == 'c') {
				data[questionBase + 3]++;
			}
			if (answer.choice == 'd') {
				data[questionBase + 4]++;
			}
			redraw();
		};

		socket.onerror = function(event) {
			console.log("A WebSocket error occured");
			console.log(event);
			ready = false;
		};

		socket.onclose = function(event) {
			console.log("Remote host closed or refused WebSocket connection");
			ready = false;
		};

		return {
			send : function(data) {
				if (ready == true) {
					socket.send(data);
				} else {
					console.log("Websocket not ready.");
				}
			}
		}
	}

	var wsSocket = QuizzoWebSocket();

	function QuizzoChart() {
	
		function fill(d,i) {
			if (i % 6 == 1) {
				return "green";
			}
			if (i % 6 == 2) {
				return "red";
			}
			if (i % 6 == 3) {
				return "blue";
			}
			if (i % 6 == 4) {
				return "orange";
			}
			return "white";
		}

		function init() {
			var rectWidth = 8;
			var maxQuestions = 20;
			var maxChoices = 4;
			var chartWidth = maxQuestions*(rectWidth)*(maxChoices + 2); // + 2 spaces for each question
			
			var questionWidth = chartWidth/(maxQuestions -1);
			
			var xScale = d3.scale.linear().range([0, chartWidth]).domain([1,maxQuestions]);
			var xAxis = d3.svg.axis().scale(xScale).ticks(20).orient("bottom");
			
			for (i=0; i< data.length; i++) {
				data[i] = 0;
			}
		
			
			chartElement = d3.select("div#chart")
						.append("svg")
						.attr("width", chartWidth + 40) // some margin included
						.attr("height", height);
			
			var rect = chartElement.selectAll("rect")
			   .data(data)
			   .enter()
			   .append("rect")
			   .attr("x", function(d, i) { return (questionWidth/(maxChoices+2))*i + 9; //Bar width + offset
			    })
			   .attr("y", function(d,i){ return height - d - 20 ; } )
			   .attr("width", rectWidth)
			   .attr("height", function(d,i) { return  d; })
			   .attr("fill",function(d,i) {return fill(d,i);});
		
	
			chartElement.append("g")
			.attr("class", "x axis")
			.attr("transform", "translate(18,"+ (height-25) +")")
			.call(xAxis);
		}
		
		init();

		return {
			start : function() {
				keepDrawing = true;
				redraw();
			},
			stop : function() {
				keepDrawing = false;
			},
			ping : function() {

				ping();
			},
		}

	}
	function ping() {
		console.log("PING33");
		wsSocket.send("ping");
	}

	var quizzoChart = QuizzoChart();

	var button = d3.select("#control");
	button.on("click", function() {
		if (button.text() === "Resume") {
			quizzoChart.start();
			button.text("Pause");
		}
		else {
			quizzoChart.stop();
			button.text("Resume");
		}
	});

	quizzoChart.start();
	quizzoChart.ping();

})();


