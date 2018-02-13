package org.xmlcml.graphics.svg.cache;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
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
		LOG.debug("g "+g.toXML().length());
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/document/varga/boxes00.svg"));
//		Assert.assertEquals("g child Elements", 1, g.getChildElements().size());
		File file = new File("target/document/varga/boxes.svg");
		LOG.debug("wrote: "+file.getAbsolutePath());
		SVGSVG.wrapAndWriteAsSVG(g, file);
		Assert.assertTrue("file exists: "+file, file.exists());
	}
	
	@Test 
	public void testPageComponents() {
		
		DocumentCache documentCache = new DocumentCache();
		documentCache.analyzePages(PageLayout.AMSOCGENE, 9, "varga/compact/", new File("target/cache"));
//		documentCache.analyzePages(PageLayout.BMC, 8, "bmc/1471-2148-11-329/", new File("target/cache"));
//		documentCache.analyzePages(PageLayout.PLOSONE2016, 15, "TimmermansPLOS/", new File("target/cache"));
	}

}
