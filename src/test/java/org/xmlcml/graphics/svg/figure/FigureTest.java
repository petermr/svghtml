package org.xmlcml.graphics.svg.figure;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.SVGTextComparator;
import org.xmlcml.graphics.svg.SVGUtil;
import org.xmlcml.graphics.svg.SVGTextComparator.TextComparatorType;
import org.xmlcml.graphics.svg.cache.ComponentCache;
import org.xmlcml.graphics.svg.plot.SVGBarredPoint;
import org.xmlcml.graphics.svg.plot.XPlotBox;
import org.xmlcml.graphics.svg.plot.YPlotBox;

public class FigureTest {
	private static final Logger LOG = Logger.getLogger(FigureTest.class);
	private static final double DISTANCE_DELTA = 1.0;
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	/** complete page with several typefaces and 2 separate diagrams.
	 * 
	 */
	public void testFigureLayout() {
		String fileroot = "page";
		String dirRoot = "nature/p2";
		File outputDir = new File("target/figures/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.FIGURE_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue(""+inputFile, inputFile.exists());
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(inputFile);
	//	List<SVGElement> largeCharsElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-size[.='12.0']]");
		List<SVGText> largeCharsText = SVGText.getQuerySVGTexts(svgElement, ".//*[local-name()='text' and @font-size[.='12.0']]");
		SVGSVG.wrapAndWriteAsSVG(largeCharsText, new File(outputDir, "largeChars.svg"));
		List<SVGElement> helveticaElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-family[.='Helvetica']]");
		SVGSVG.wrapAndWriteAsSVG(helveticaElements, new File(outputDir, "helvetica.svg"));
		List<SVGElement> timesElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-family[.='TimesNewRoman']]");
		SVGSVG.wrapAndWriteAsSVG(timesElements, new File(outputDir, "times.svg"));
		
		SVGG g = new SVGG();
		Collections.sort(largeCharsText, new SVGTextComparator(TextComparatorType.ALPHA));
		for (int i = 0; i < largeCharsText.size(); i++) {
			SVGText largeChar = (SVGText) largeCharsText.get(i);
			g.appendChild(largeChar.copy());
			Real2 xy = largeChar.getXY();
			SVGText label = new SVGText(xy, String.valueOf(i));
			label.setCSSStyle("font-size:5;fill:blue;");
			if (xy.getY() < 40) continue; // omit header
			xy.plusEquals(new Real2(0.0, -largeChar.getFontSize()));
			Real2 xy1 = xy.plus(new Real2(100, 60));
			SVGRect rect = new SVGRect(xy, xy1);
			label.translate(new Real2(10.,0));
			rect.setCSSStyle("fill:none;stroke-width:1.0;stroke:red;");
			g.appendChild(rect);
			g.appendChild(label);
			LOG.debug(label.toXML());
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File(outputDir, "rects.svg"));
		
	}

	/** single subpanel with simple bar chart.
	 * 
	 */
	@Test
	public void testFigureErrorBars() {
		String fileroot = "figure";
		String dirRoot = "nature/p3.a";
		File outputDir = new File("target/figures/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.FIGURE_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		YPlotBox yPlotBox = new YPlotBox();
		ComponentCache componentCache = new ComponentCache(yPlotBox);
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		List<SVGLine> horizontalLines = componentCache.getOrCreateLineCache().getOrCreateHorizontalLineList();
		List<SVGLine> verticalLines = componentCache.getOrCreateLineCache().getOrCreateVerticalLineList();
		List<SVGBarredPoint> barredPoints = SVGBarredPoint.extractErrorBarsFromIBeams(horizontalLines, verticalLines);
		
		File file = new File(outputDir, "errorBars.svg");
		LOG.debug("wrote "+file.getAbsolutePath());
		SVGG g = new SVGG();
		// barred point isn't a true SVGElement yet so have to create the G
		for (SVGBarredPoint barredPoint : barredPoints) {
			g.appendChild(barredPoint.createSVGElement()); 
		}
		SVGSVG.wrapAndWriteAsSVG(g, file);
		LOG.debug("barred:"+file+";"+g.toXML());
		Assert.assertTrue("exists "+file, file.exists());
	}

	@Test
	/** figure has 14 subpanels (molecules) in 5 * 3 grid.
	 * NO separating lines - all done by whitespace
	 * at this stage just simple horizontal and vertical lines
	 * 
	 */
	public void textSplitSubPanels() {
		String fileroot = "figure1";
		String dirRoot = "glyphs";
		File outputDir = new File("target/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		XPlotBox xPlotBox = new XPlotBox();
		ComponentCache componentCache = new ComponentCache(xPlotBox); 
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		List<Real2Range> bboxList = componentCache.getBoundingBoxList();
		SVGG g = new SVGG();
		addColouredBBoxes(bboxList, g);
		g.appendChild(svgElement.copy());
		SVGSVG.wrapAndWriteAsSVG(g, new File(outputDir, "bboxList.svg"));

		double interBoxMargin = 1.0; // fairly critical
		bboxList = mergeBoxesTillNoChange(bboxList, interBoxMargin);
		
		SVGG gg = new SVGG();
		List<SVGRect> rects = SVGRect.createFromReal2Ranges(bboxList, 1.0);
		SVGElement.setCSSStyle(rects, "fill:none;stroke:blue;stroke-width:0.5;");
		gg.appendChild(svgElement);
		gg.appendChildren(rects);
		SVGSVG.wrapAndWriteAsSVG(gg, new File(outputDir, "bboxMerged.svg"));
			
		
	}

	@Test
	/** figure has 2 subpanels (molecules).
	 * NO separating lines - all done by whitespace
	 * at this stage just simple horizontal and vertical lines
	 * 
	 */
	public void textSplitSubPanels2() {
		String fileroot = "figure2graphic";
		String dirRoot = "glyphs";
		File outputDir = new File("target/", dirRoot+"/"+fileroot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		XPlotBox xPlotBox = new XPlotBox();
		ComponentCache componentCache = new ComponentCache(xPlotBox); 
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		LOG.debug("created caches");
		List<Real2Range> bboxList = componentCache.getBoundingBoxList();
		SVGG g = new SVGG();
		addColouredBBoxes(bboxList, g);
		g.appendChild(svgElement.copy());
		SVGSVG.wrapAndWriteAsSVG(g, new File(outputDir, "bboxList.svg"));

		double interBoxMargin = 2.0; // fairly critical
		bboxList = mergeBoxesTillNoChange(bboxList, interBoxMargin);
		
		SVGG gg = new SVGG();
		List<SVGRect> rects = SVGRect.createFromReal2Ranges(bboxList, 1.0);
		SVGElement.setCSSStyle(rects, "fill:none;stroke:blue;stroke-width:0.5;");
		gg.appendChild(svgElement);
		gg.appendChildren(rects);
		SVGSVG.wrapAndWriteAsSVG(gg, new File(outputDir, "bboxMerged.svg"));		
	}

	private void addColouredBBoxes(List<Real2Range> bboxList, SVGG g) {
		for (Real2Range bbox : bboxList) {
			SVGRect rect = SVGRect.createFromReal2Range(bbox);
			rect.setCSSStyle("fill:none;stroke-width:0.5;stroke:red;");
			g.appendChild(rect);
		}
	}
	
	// ==========================================

	private List<Real2Range> mergeBoxesTillNoChange(List<Real2Range> bboxList, double interBoxMargin) {
		while (true) {
			int start = bboxList.size();
			List<Real2Range> mergedBoxes = mergeBoxes(bboxList, interBoxMargin);
			int end = mergedBoxes.size();
			LOG.trace(start+", "+end);
			if (mergedBoxes.size() >= bboxList.size()) {
				break;
			}
			bboxList = mergedBoxes;
		}
		return bboxList;
	}

	private List<Real2Range> mergeBoxes(List<Real2Range> bboxList, double delta) {
		List<Real2Range> mergedBoxes = new ArrayList<Real2Range>();
		for (int i = 0; i < bboxList.size(); i++) {
			Real2Range bbox = bboxList.get(i);
			boolean merged = false;
			for (int j = 0; j < mergedBoxes.size(); j++) {
				Real2Range mergedBox = mergedBoxes.get(j);
				if (bbox.intersects(mergedBox, delta)) {
					merged = true;
					mergedBox.plusEquals(bbox);
				}
			}
			if (!merged) {
				mergedBoxes.add(bbox);
			}
		}
		return mergedBoxes;
	}

	// ==============================================
	
	
}

