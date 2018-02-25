package org.xmlcml.graphics.svg.linestuff;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGPolyline;
import org.xmlcml.graphics.svg.cache.ComponentCache;
import org.xmlcml.graphics.svg.plot.XPlotBox;

public class PolylineTest {
	private static final Logger LOG = Logger.getLogger(PolylineTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testFigure2b() {
		String fileroot = "figure2b";
		String dirRoot = "glyphs";
		File outputDir = new File("target/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		createPolylinesAndAnalyze(fileroot, outputDir, inputFile);
		
	}

	// ===========================
	
	private void createPolylinesAndAnalyze(String fileroot, File outputDir, File inputFile) {
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		XPlotBox xPlotBox = new XPlotBox();
		ComponentCache componentCache = new ComponentCache(xPlotBox); 
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
//		componentCache.debug();
		List<SVGPolyline> polylines = componentCache.getOrCreatePolylineCache().getOrCreatePolylineList();
		LOG.debug("P "+polylines.size());
		AbstractCMElement g = new SVGG();
		for (SVGPolyline polyline : polylines) {
			LOG.debug(polyline.toXML());
		}
//		List<Multiset.Entry<String>> sigsByCount = glyphSet.getSignaturesSortedByCount();
//		LOG.debug(sigsByCount);
//		for (int i = 0; i < sigsByCount.size(); i++) {
//			Entry<String> sigEntry = sigsByCount.get(i);
//			String sig = sigEntry.getElement();
//			List<SVGGlyph> glyphList = new ArrayList<SVGGlyph>(glyphSet.getOrCreateGlyphMap().get(sig));
//			SVGG gg = createSVG(glyphList, i);
//			SVGSVG.wrapAndWriteAsSVG(gg, new File(outputDir, fileroot+"/"+"glyph."+i+".svg"), 300, 100);
//		}
	}


}
