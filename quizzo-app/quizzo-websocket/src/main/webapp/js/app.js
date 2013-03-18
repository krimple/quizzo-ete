(function() {

	var answers = [];
	var players = [];
	var data = new Array(20);

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
			if (data[answer.questionNumber] == undefined) {
				console.log("initializing data for questionNumber " + answer.questionNumber);
				data[answer.questionNumber] = {};
				data[answer.questionNumber]['a'] = 0;
				data[answer.questionNumber]['b'] = 0;
				data[answer.questionNumber]['c'] = 0;
				data[answer.questionNumber]['d'] = 0;
			}
			data[answer.questionNumber][answer.choice] = data[answer.questionNumber][answer.choice] + 1;
			
			
			answers.push(answer);
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

		var refreshFrequency = 750,
			timespan = 5 * 60 * 1000;

		var chartElement, questionScale, xAxis, playerScale,yAxis;

		var keepDrawing = true;

		function init() {
			var margin = {top: 6, right: 0, bottom: 20, left: 40},
					width = 960 - margin.right,
					height = 200 - margin.top - margin.bottom;

			questionScale = d3.scale.linear().range([0, width]).domain([1,200]);
			xAxis = d3.svg.axis().scale(questionScale).orient("bottom");
		
			playerScale = d3.scale.linear().domain([0, 100]).range([height, 0]);

			chartElement = d3.select("div#chart")
						.append("svg")
						.attr("width", 1020)
						.attr("height", 200)
						.append("g")
						.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			timeline = chartElement.append("g")
						.attr("class", "x axis")
						.attr("transform", "translate(0," + height + ")")
						.call(xAxis);

			chartElement.append("g")
					.attr("class", "y axis")
					.call(d3.svg.axis().scale(playerScale).orient("left"));
		}
		
		function xcoord(d) {
			var val = d.questionNumber*10;
			if (d.choice == 'a') {
				val = val+1;
			}
			if (d.choice == 'b') {
				val = val+2;
			}
			if (d.choice == 'c') {
				val = val+3;
			}
			if (d.choice == 'd') {
				val = val+4;
			}
			return questionScale(val);
		}

		function redraw() {
			//ping();

			if (!keepDrawing) {
				return;
			}
			// join quiz data to points on the graph
			// see http://bost.ocks.org/mike/join
			var circle = chartElement.selectAll("circle")
					.data(answers, function(d, i) { return d._id });
			
			circle.enter().append("circle")
					.style("stroke", "gray")
					.style("fill", function(d,i) {
						if (d.choice == 'a') {
							return "red";
						}
						if (d.choice == 'b') {
							return "blue";
						}
						if (d.choice == 'c') {
							return "green";
						}
						if (d.choice == 'd') {
							return "black";
						}
					})
					.attr("cx", function(d, i) {return xcoord(d);})
					.attr("cy", function(d, i) {return playerScale(data[d.questionNumber][d.choice]) })
					.attr("r", 0)
					.transition().duration(refreshFrequency)
					.attr("r", 5);

			circle.transition()
					.duration(refreshFrequency)
					.ease("linear")
					.attr("cx", function(d){return xcoord(d);});

			circle.exit().transition()
					.duration(refreshFrequency)
					.attr("r", 0)
					.ease("linear")
					.attr("cx", function(d){return xcoord(d);})
					.remove();

			// slide the time-axis left
			timeline.transition()
						.duration(refreshFrequency)
						.ease("linear")
						.call(xAxis)
						.each("end", redraw);
		}

		function ping() {
			console.log("PING33");
			wsSocket.send("ping");
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


