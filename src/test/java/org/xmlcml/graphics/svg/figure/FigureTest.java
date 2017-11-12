package org.xmlcml.graphics.svg.figure;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.SVGUtil;
import org.xmlcml.graphics.svg.cache.ComponentCache;
import org.xmlcml.graphics.svg.plot.SVGBarredPoint;
import org.xmlcml.graphics.svg.plot.YPlotBox;
import org.xmlcml.svg.SVGTextComparator;
import org.xmlcml.svg.SVGTextComparator.TextComparatorType;

public class FigureTest {
	private static final Logger LOG = Logger.getLogger(FigureTest.class);
	private static final double DISTANCE_DELTA = 1.0;
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testFigureLayout() {
		String fileroot = "page";
		String dirRoot = "nature/p2";
		File outputDir = new File("target/figures/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.FIGURE_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue(""+inputFile, inputFile.exists());
		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
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

	@Test
	public void testFigureErrorBars() {
		String fileroot = "figure";
		String dirRoot = "nature/p3.a";
		File outputDir = new File("target/figures/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.FIGURE_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
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

//	@Test
//	public void testFigure() {
//		String fileroot = "page";
//		String dirRoot = "nature/p2";
//		File outputDir = new File("target/figures/", dirRoot);
//		File inputDir = new File(SVG2XMLFixtures.FIGURE_DIR, dirRoot);
//		File inputFile = new File(inputDir, fileroot + ".svg");
//		Assert.assertTrue(""+inputFile, inputFile.exists());
//		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
//	//	List<SVGElement> largeCharsElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-size[.='12.0']]");
//		List<SVGText> largeCharsText = SVGText.getQuerySVGTexts(svgElement, ".//*[local-name()='text' and @font-size[.='12.0']]");
//		SVGSVG.wrapAndWriteAsSVG(largeCharsText, new File(outputDir, "largeChars.svg"));
//		List<SVGElement> helveticaElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-family[.='Helvetica']]");
//		SVGSVG.wrapAndWriteAsSVG(helveticaElements, new File(outputDir, "helvetica.svg"));
//		List<SVGElement> timesElements = SVGUtil.getQuerySVGElements(svgElement, ".//*[local-name()='text' and @font-family[.='TimesNewRoman']]");
//		SVGSVG.wrapAndWriteAsSVG(timesElements, new File(outputDir, "times.svg"));
//		
//		SVGG g = new SVGG();
//		Collections.sort(largeCharsText, new SVGTextComparator(TextComparatorType.ALPHA));
//		for (int i = 0; i < largeCharsText.size(); i++) {
//			SVGText largeChar = (SVGText) largeCharsText.get(i);
//			g.appendChild(largeChar.copy());
//			Real2 xy = largeChar.getXY();
//			SVGText label = new SVGText(xy, String.valueOf(i));
//			label.setCSSStyle("font-size:5;fill:blue;");
//			if (xy.getY() < 40) continue; // omit header
//			xy.plusEquals(new Real2(0.0, -largeChar.getFontSize()));
//			Real2 xy1 = xy.plus(new Real2(100, 60));
//			SVGRect rect = new SVGRect(xy, xy1);
//			label.translate(new Real2(10.,0));
//			rect.setCSSStyle("fill:none;stroke-width:1.0;stroke:red;");
//			g.appendChild(rect);
//			g.appendChild(label);
//			LOG.debug(label.toXML());
//		}
//		SVGSVG.wrapAndWriteAsSVG(g, new File(outputDir, "rects.svg"));
//		
//	}


	// ==============================================
	
	
}

