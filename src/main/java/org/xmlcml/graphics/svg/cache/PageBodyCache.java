package org.xmlcml.graphics.svg.cache;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGUtil;

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

	private Real2Range boundingBox;
	
	public PageBodyCache(PageCache pageCache) {
		setPageCache(pageCache);
		processCache();
	}

	private void processCache() {
		pageCache.ensureTopBottomLeftRightMarginCaches();
		findBox();
	}

	/**
	 * this uses the other components to define the box. Later we may
	 * need to reverse the direction.
	 */
	private void findBox() {
		// note the mins and max's are reversed 
		Double xmin = pageCache.getOrCreateLeftSidebarCache().getXmax();
		Double xmax = pageCache.getOrCreateRightSidebarCache().getXmin();
		Double ymin = pageCache.getOrCreateHeaderCache().getYmax();
		Double ymax = pageCache.getOrCreateFooterCache().getYmin();
		LOG.debug(""+xmin+"/"+xmax+"//"+ymin+"/"+ymax);
		boundingBox = new Real2Range(
				new RealRange(xmin, xmax), new RealRange(ymin, ymax));
		LOG.debug("BB "+boundingBox);
		
	}

	public void setBoxLimits() {
		List<SVGElement> boxList = SVGUtil.getQuerySVGElements(
				pageCache.getPageLayoutElement(), ".//*[local-name()='g' and @class='body']/*[local-name()='rect']");
		SVGRect rect = (boxList.size() == 1) ? (SVGRect) boxList.get(0): null;
		LOG.debug("Rect: "+rect);
	}

	public String toString() {
		return "body: "+(boundingBox == null ? null : boundingBox.toString());
	}
	
}
