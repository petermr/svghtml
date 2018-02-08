package org.xmlcml.graphics.svg.cache;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.RealRange;

/** the main body of the page.
 * 
 * @author pm286
 *
 */
public class PageBodyCache extends PageComponentCache {
	private static final Logger LOG = Logger.getLogger(PageBodyCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	private static String CYAN = "cyan";

	public PageBodyCache(PageCache pageCache) {
		setPageCache(pageCache);
		processCache();
	}

	private void processCache() {
		pageCache.ensureTopBottomLeftRightMarginCaches();
		findBoxFromMargins();
	}

	/**
	 * this uses the other components to define the box. Later we may
	 * need to reverse the direction.
	 */
	private void findBoxFromMargins() {
		// note the mins and max's are reversed !! 
		Double xmin = pageCache.getOrCreateLeftSidebarCache().getXMax();
		Double xmax = pageCache.getOrCreateRightSidebarCache().getXMin();
		Double ymin = pageCache.getOrCreateHeaderCache().getYMax();
		Double ymax = pageCache.getOrCreateFooterCache().getYMin();
		boundingBox = new Real2Range(
				new RealRange(xmin, xmax), new RealRange(ymin, ymax));
		LOG.debug("Margins give: "+boundingBox);
		
	}

	public String toString() {
		return "body: "+(boundingBox == null ? null : boundingBox.toString());
	}
	
	public String getFill() {
		return CYAN;
	}
	
	

	
}
