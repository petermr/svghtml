package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;

/** tests svgElements containing lines
 * 
 * @author pm286
 *
 */
public class LineCacheTest {


	/** a 2-level header bar which involves rowspans
	 * the actual body of the table is lines, 
	 * 
	 */
	@Test
	public void testLine11() {
		List<SVGLine> lineList = extractAndDisplayLines(SVGHTMLFixtures.TABLE_RECT_DIR, "table11.svg", "line11.svg");
		// all boxes are formed from 4 lines 7*14 + 2 + 4 + 1+7 + 7*2 + 14 * 8 === 98+14+14+112 => 238
		// two lines in top row are butted
		Assert.assertEquals("lines", 238, lineList.size());
	}

	// ============================
	
	private List<SVGLine> extractAndDisplayLines(File inDir, String svgName, String outName) {
		AbstractCMElement svgElement = SVGElement.readAndCreateSVG(new File(inDir, svgName));
		ComponentCache componentCache = new ComponentCache();
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		LineCache lineCache = componentCache.getOrCreateLineCache();
		SVGSVG.wrapAndWriteAsSVG(lineCache.getOrCreateConvertedSVGElement(), new File("target/table/cache/" + outName));
		List<SVGLine> lineList = lineCache.getOrCreateLineList();
		return lineList;
	}
}
