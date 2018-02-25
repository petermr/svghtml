package org.xmlcml.graphics.svg.plot;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.svg.SVGG;

public class SVGLineAnalyzerTest {

	private static final Logger LOG = Logger.getLogger(SVGLineAnalyzerTest.class);

	@Test
	@Ignore // until we write the axis stuff
	public void testFindAxes() {
		AbstractCMElement g = SVGG.createSVGGChunk(SVGHTMLFixtures.LINEPLOTS_10_2_SVG,  "./svg:g", 0);
		SVGLineAnalyzer lineAnalyzer = new SVGLineAnalyzer();
		List<GraphPlotBox> plotBoxList = lineAnalyzer.findGraphPlotBoxList(g);
		LOG.trace("boxes: "+plotBoxList.size());
	}
	
	@Test
	@Ignore // until we write the axis stuff
	public void testFindAxesBadEps() {
		
		AbstractCMElement g = SVGG.createSVGGChunk(SVGHTMLFixtures.LINEPLOTS_10_2_SVG,  "./svg:g", 0);
		SVGLineAnalyzer lineAnalyzer = new SVGLineAnalyzer();
		lineAnalyzer.setEpsilon(0.5);
		List<GraphPlotBox> plotBoxList = lineAnalyzer.findGraphPlotBoxList(g);
		LOG.trace("boxes: "+plotBoxList.size());
	}
	
	public void testInternal() {
		AbstractCMElement g = SVGG.createSVGGChunk(SVGHTMLFixtures.LINEPLOTS_10_2_SVG,  "./svg:g", 0);
		SVGLineAnalyzer lineAnalyzer = new SVGLineAnalyzer();
		LOG.debug("=====================debug====================");
		LOG.debug(lineAnalyzer.debug());
	}
	
	public void testLineAngles() {
		AbstractCMElement g = SVGG.createSVGGChunk(SVGHTMLFixtures.LINEPLOTS_10_2_SVG,  "./svg:g", 0);
		SVGLineAnalyzer lineAnalyzer = new SVGLineAnalyzer();
		LOG.debug("====================line angles======================");
		LOG.debug("LineAngles: \n"+lineAnalyzer.getLineAngleMap());
		GraphPlotBox graphPlotBox = lineAnalyzer.getPlotBox();
	}
}
