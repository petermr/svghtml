package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.graphics.html.HtmlDiv;
import org.xmlcml.graphics.html.HtmlElement;
import org.xmlcml.graphics.html.HtmlP;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.xml.XMLUtil;

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
		documentCache.processSVGInCTreeDirectory(new File(SVGHTMLFixtures.PAGE_DIR, "varga/compact"));
		// superimposed pages
		SVGElement g = documentCache.getOrCreateConvertedSVGElement();
		Assert.assertNotNull("non-null g", g);
		Assert.assertTrue("empty g", g.getChildCount() > 0);
		File file = new File("target/document/varga/boxes.svg");
		LOG.debug("wrote: "+file.getAbsolutePath());
		SVGSVG.wrapAndWriteAsSVG(g, file);
		Assert.assertTrue("file exists: "+file, file.exists());
	}
	
	@Test 
	public void testPageComponents() {
		
		DocumentCache documentCache = new DocumentCache();
		LOG.warn("incomplete, pageLayout not fixed");
		documentCache.analyzePages(SVGHTMLFixtures.PAGE_DIR, PageLayout.AMSOCGENE, 9, "varga/compact/", new File("target/cache"));
//		documentCache.analyzePages(PageLayout.BMC, 8, "bmc/1471-2148-11-329/", new File("target/cache"));
//		documentCache.analyzePages(PageLayout.PLOSONE2016, 15, "TimmermansPLOS/", new File("target/cache"));
	}
	

	@Test
	public void testCreateHTMLPageAllCrop() throws IOException {
		File targetDir = new File("target/document/varga1");
		SVGHTMLFixtures.cleanAndCopyDir(new File(SVGHTMLFixtures.PAGE_DIR, "varga1/"), targetDir);
		DocumentCache documentCache = new DocumentCache(targetDir);
		documentCache.processSVGInCTreeDirectory(targetDir);
		XMLUtil.debug(documentCache.getHtmlDiv(), new File("target/html/pages.html"), 1);

	}
	


}
