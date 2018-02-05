package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;

public class PageLeftSidebarCache extends PageComponentCache {
	private static final Logger LOG = Logger.getLogger(PageLeftSidebarCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private static Double XMAX = 25.; // I think this is a good start
	private Double xmax = null;
	
	public PageLeftSidebarCache(PageCache pageCache) {
		xmax = XMAX;
		setPageCache(pageCache);
		processCache();
	}

	private void processCache() {
		findLeftWhitespace();
		getSVGElement();
		SVGSVG.wrapAndWriteAsSVG(svgElement, new File("target/debug/pageLeftSidebar"+pageCache.getSerialNumber()+".svg"));
	}

	private void findLeftWhitespace() {
		List<SVGElement> elements = SVGElement.extractSelfAndDescendantElements(pageCache.getOriginalSVGElement());
		getOrCreateAllElementList();

		for (SVGElement element : elements) {
			Real2Range bbox = element.getBoundingBox();
			if (bbox != null && bbox.getXMax() < xmax) {
//				String s = "";
//				if (element instanceof SVGText) {
//					s = ((SVGText) element).getText();
//				}
				this.allElementList.add(element);
			}
		}
		LOG.debug("LEFT "+allElementList.size());
	}
	
	public Double getXmax() {
		return xmax;
	}
	
	public void setXmin(Double xmax) {
		this.xmax = xmax;
	}


}
