var selectionRect = {
	currentRect		: null,
	previousRect : null,
	currentY		: 0,
	currentX		: 0,
	originX			: 0,
	originY			: 0,
	
	setRectAndRememberPrevious: function(ele) {
		this.previousRect = this.currentRect;
		this.currentRect = ele;
		// this.previousRect.on("mouseover", handleMouseOver);
		// this.previousRect.on("mouseout", handleMouseOut);
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
            .style("stroke", annotator.color)
            .style("stroke-width", "4.5")
		this.currentRect.type = annotator.type;
		
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
		selectionRect.updateStrokeAndWidth();
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

var doiPicker = document.querySelector('input[name="doiButton"]');
var titlePicker = document.querySelector('input[name="titleButton"]');
var authPicker = document.querySelector('input[name="authButton"]');
var annotator = {
		color : "red",
		type : "doi"
}

function selectDOI() {
	messageText.text("selectDOI");
	doiPicker.fill="red";
	console.log("selectDOI");
	annotator.color = "red";
	annotator.type = "doi";
	
}
function selectTitle() {
	messageText.text("selectTitle");
	titlePicker.fill="red";
	console.log("selectTitle");
	annotator.color = "green";
	annotator.type = "title";
}

function textClick() {
	messageText.text("testClick");
	console.log("testClick");
	
}

function handleMouseOver(d, i) {
	messageText.text("mouseOverX");
	console.log("mouseOverX");
}

function handleMouseOut(d, i) {
	messageText.text("mouseOutX");
	console.log("mouseOouX");
}

// // Create Event Handlers for mouse
// function handleMouseOver(d, i) {  // Add interactivity
//
//       // Use D3 to select element, change color and size
//       d3.select(this).attr({
//         fill: "orange",
//         r: radius * 2
//       });
//
//       // Specify where to put label of text
//       svg.append("text").attr({
//          id: "t" + d.x + "-" + d.y + "-" + i,  // Create an id for text so we can select it later for removing on mouseout
//           x: function() { return xScale(d.x) - 30; },
//           y: function() { return yScale(d.y) - 15; }
//       })
//       .text(function() {
//         return [d.x, d.y];  // Value of the text
//       });
//     }

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
		.on("entry", textClick);
}

	    // <input name="doiButton"       class="doi"    value="DOI"   onclick="selectDOI()" type="button">
	    // <input name="titleButton"     class="title"  value="Title" onclick="selectTitle()" type="button">
	    // <input name="authorsButton"   class="author" value="Auth"  onclick="selectAuthors()" type="button">
	    // <input name="affilButton"     class="affil"  value="Affil" onclick="selectAffil()" type="button">
	    // <input name="emailButton"     class="email"  value="Email" onclick="selectEmail()" type="button">
	    // <input name="abstractButton"  class="abst"   value="Abst"  onclick="selectAbstract()" type="button">
	    // <input name="leftColButton"   class="lCol"   value="LCol"  onclick="selectLeftCol()" type="button">
	    // <input name="rightColButton"  class="rCol"   value="RCol"  onclick="selectRightCol()" type="button">
	    // <input name="sectionButton"     class="section"  value="Sect" onclick="selectSection()" type="button">
	    // <input name="sectionHeadButton" class="sectionHead" value="SHead" onclick="selectSectionHead()" type="button">
	    // <input name="sectionSubHeadButton" class="subSectionHead" value="SubHd" onclick="selectSubSectionHead()" type="button">
	    // <input name="biblioButton"    class="biblio" value="Bib"   onclick="selectBiblio()" type="button">
	    // <input name="pageButton"      class="page"   value="Page"  onclick="selectPage()" type="button">
	    // <input name="figureButton"    class="figure" value="Fig" onclick="selectFigure()" type="button">
	    // <input name="figureCaptionButton"   class="figureCaption" value="fCap" onclick="selectFigureCaption()" type="button">
	    // <input name="tableButton"   class="table" value="Table" onclick="selectTable()" type="button">
	    // <input name="tableTitleButton"   class="tableTitle" value="TTitl" onclick="selectTableTitle()" type="button">
	    // <input name="tableHeaderButton"   class="tableHead" value="THead" onclick="selectTableHead()" type="button">
	    // <input name="tableBodyButton"   class="table" value="TBody" onclick="selectTableBody()" type="button">
	    // <input name="tableFooterButton"   class="table" value="TFoot" onclick="selectTableFoot()" type="button">
	    // <input name="mathsButton"   class="math" value="Math" onclick="selectMath()" type="button">
	    // <input name="referenceButton"   class="references" value="Refs" onclick="selectRefs()" type="button">



