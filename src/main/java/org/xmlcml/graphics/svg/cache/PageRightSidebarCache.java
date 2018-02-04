package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGSVG;

public class PageRightSidebarCache extends PageComponentCache {
	private static final Logger LOG = Logger.getLogger(PageRightSidebarCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private static Double XPAGE_MAX = 600.;
	private static Double XMIN = XPAGE_MAX - 25.; // I think this is a good start
	private Double xmin = null;
	
	public PageRightSidebarCache(PageCache pageCache) {
		xmin = XMIN;
		setPageCache(pageCache);
		processCache();
	}

	private void processCache() {
		findBottomWhitespace();
		getSVGElement();
		SVGSVG.wrapAndWriteAsSVG(svgElement, new File("target/debug/pageRightSidebar"+pageCache.getSerialNumber()+".svg"));
	}

	private void findBottomWhitespace() {
		List<SVGElement> elements = SVGElement.extractSelfAndDescendantElements(pageCache.getOriginalSVGElement());
		getOrCreateAllElementList();

		for (SVGElement element : elements) {
			Real2Range bbox = element.getBoundingBox();
			if (bbox != null && bbox.getXMin() > xmin) {
				this.allElementList.add(element);
			}
		}
		LOG.debug("RIGHT "+allElementList.size());
	}
	
	public Double getXmin() {
		return xmin;
	}
	
	public void setXmin(Double xmin) {
		this.xmin = xmin;
	}

}
