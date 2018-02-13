var selectionRect = {
	currentRect			: null,
	previousRect : null,
	currentY		: 0,
	currentX		: 0,
	originX			: 0,
	originY			: 0,
	
	setRectAndRememberPrevious: function(ele) {
		this.previousRect = this.currentRect;
		this.currentRect = ele;
	},

	getNewXYRecalculateWH: function() {
		var x = this.currentX<this.originX?this.currentX:this.originX;
		var y = this.currentY<this.originY?this.currentY:this.originY;
		var width = Math.abs(this.currentX - this.originX);
		var height = Math.abs(this.currentY - this.originY);
		return {
	        x       : x,
	        y       : y,
	        width  	: width,
	        height  : height
		};
	},
	getCurrentRectCoordsAsObject: function() {
		// use plus sign to convert string into number
		var x = +this.currentRect.attr("x");
		var y = +this.currentRect.attr("y");
		var width = +this.currentRect.attr("width");
		var height = +this.currentRect.attr("height");
		return {
			x1  : x,
	        y1	: y,
	        x2  : x + width,
	        y2  : y + height
		};
	},
	// thes will be listed at the top
	getCurrentRectCoordsAsText: function() {
		var rectCoords = this.getCurrentRectCoordsAsObject();
		return "x1: " + rectCoords.x1 + " x2: " + rectCoords.x2 + " y1: " + rectCoords.y1 + " y2: " + rectCoords.y2;
	},
	createNewRect: function(newX, newY) {
		// create a new rectangle
		var rectElement = svg.append("rect")
		    .attr({
		        rx      : 4,
		        ry      : 4,
		        x       : 0,
		        y       : 0,
		        width   : 0,
		        height  : 0
		    })
		    .classed("selection", true);
	    this.setRectAndRememberPrevious(rectElement);
		this.originX = newX;
		this.originY = newY;
		this.updateXYandWH(newX, newY);
	},
	updateXYandWH: function(newX, newY) {
		this.currentX = newX;
		this.currentY = newY;
		this.currentRect.attr(this.getNewXYRecalculateWH());
	},
	updateStrokeAndWidth: function() {
		messageText.text("updateStrokeAndWidth");
        this.currentRect
            .style("stroke", "red")
            .style("stroke-width", "2.5");
    },
    removeCurrentRect: function() {
    	this.currentRect.remove();
    	this.currentRect = null;
    },
    removePreviousRect: function() {
		// this allows previous rect to be removed
		if (true) return
    	 if(this.previousRect) {
    	 	this.previousRect.remove();
    	 }
    }
	// ,
	// .on("click", function(){
	// 	messageText.text("CLICK");
	// 	// // Determine if current line is visible
	// 	// var active   = blueLine.active ? false : true,
	// 	//   newOpacity = active ? 0 : 1;
	// 	// // Hide or show the elements
	// 	// d3.select("#blueLine").style("opacity", newOpacity).style("stroke-width", 3);
	// 	// d3.select("#blueAxis").style("opacity", newOpacity);
	// 	// // Update whether or not the elements are active
	// 	// blueLine.active = active;
	// })
	
};

// created by D3 from SVG
var svg = d3.select("svg");
// created from (p) elements with attributes
var clickTime = d3.select("#clicktime");
var attributesText = d3.select("#attributestext");
var messageText = d3.select("#messagetext");


function dragStart() {
	messageText.text("dragStart");
	console.log("dragStart");
    var xy = d3.mouse(this);
	messageText.text(""+xy);
    selectionRect.createNewRect(xy[0], xy[1]);
	selectionRect.removePreviousRect();
}

function dragMove() {
	messageText.text("dragMove");
	console.log("dragMove");
	var xy = d3.mouse(this);
    selectionRect.updateXYandWH(xy[0], xy[1]);
    attributesText
    	.text(selectionRect.getCurrentRectCoordsAsText());
}

function dragEnd() {
	messageText.text("dragEnd");
	console.log("dragEnd");
	var finalCoords = selectionRect.getCurrentRectCoordsAsObject();
	console.dir(finalCoords);
	if(finalCoords.x2 - finalCoords.x1 > 1 && finalCoords.y2 - finalCoords.y1 > 1){
		messageText.text("dragEnd");
		console.log("range selected");
		// range selected
		d3.event.sourceEvent.preventDefault();
//		selectionRect.updateStrokeAndWidth();
	} else {
		messageText.text("single point no drag");		
		console.log("single point");
        // single point selected
        selectionRect.removeCurrentRect();
        // trigger click event manually
        clicked();
    }
}

var dragBehavior = d3.behavior.drag()
    .on("drag", dragMove)
    .on("dragstart", dragStart)
    .on("dragend", dragEnd);



svg.call(dragBehavior);

function selectTitle() {
	messageText.text("selectTitle");
}
function textClick() {
	messageText.text("testClick");
	console.log("testClick");
	
}

function clicked() {
	var d = new Date();
    clickTime
    	.text("Clicked at " + d.toTimeString().substr(0,8) + ":" + d.getMilliseconds());
		// PMR added this
	var rectElement = svg.append("rect")
	    .attr({
	        rx      : 4,
	        ry      : 4,
	        x       : 300,
	        y       : 200,
	        width   : 50,
	        height  : 100,
			fill    : "none",
			stroke  : "blue",
	    }).style("stroke-width", "3.5")
		.on("click", textClick);
}

