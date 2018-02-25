package org.xmlcml.graphics.svg.objects;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGLine.LineDirection;
import org.xmlcml.graphics.svg.SVGShape;

public class SVGPlot extends SVGDiagram {
	
	
	static final Logger LOG = Logger.getLogger(SVGPlot.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private SVGShape mainRect;
	private List<SVGLine> horizontalList;
	private List<SVGLine> verticalList;

	public SVGPlot(AbstractCMElement diagram) {
		this.rawDiagram = diagram;
	}

	public void createPlot() {
		createPathsTextAndShapes();
		this.createAxisBox(eps);
		
	}

	// FIXME
	private void createAxisBox(double delta) {
		SVGLine.normalizeAndMergeAxialLines(lineList, delta);
		horizontalList = SVGLine.extractAndRemoveHorizontalVerticalLines(
				lineList, eps, LineDirection.HORIZONTAL);
		verticalList = SVGLine.extractAndRemoveHorizontalVerticalLines(
				lineList, eps, LineDirection.VERTICAL);
		if (rectList.size() == 1) {
			mainRect = rectList.get(0);
		}
	}

}
