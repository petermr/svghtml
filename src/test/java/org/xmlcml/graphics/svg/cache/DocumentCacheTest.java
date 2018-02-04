package org.xmlcml.graphics.svg.cache;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGSVG;

public class DocumentCacheTest {


public static final Logger LOG = Logger.getLogger(DocumentCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	/** 9-page article.
	 * 
	 */
	public void testDocument() {
		DocumentCache documentCache = new DocumentCache();
		documentCache.setCreateSummaryBoxes(true);
		documentCache.processSVGDirectory(new File(SVGHTMLFixtures.PAGE_DIR, "varga/compact"));
		SVGElement g = documentCache.getOrCreateConvertedSVGElement();
		File file = new File("target/document/varga/boxes.svg");
		LOG.debug("wrote: "+file.getAbsolutePath());
		SVGSVG.wrapAndWriteAsSVG(g, file);
	}
}
