package org.xmlcml.graphics.svg.cache;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;

/** tests all page companents (Top, Bottom, etc.).
 * May change later
 * @author pm286
 *
 */
public class PageComponentCacheTest {
	private static final Logger LOG = Logger.getLogger(PageComponentCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test 
	public void testPageComponents() {
		File svgFile = new File(SVGHTMLFixtures.PAGE_DIR, "varga/compact/fulltext-page2.svg");
		PageCache pageCache = new PageCache();
		pageCache.readPageLayoutFromResource(PageLayout.BMC);
		
		pageCache.readGraphicsComponentsAndMakeCaches(svgFile);
	}
	
}
