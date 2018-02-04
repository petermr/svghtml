package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGSVG;

public class PageHeaderCache extends PageComponentCache {
	private static final Logger LOG = Logger.getLogger(PageHeaderCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private static Double YMAX = 100.; // I think this is a good start
	private Double ymax = null;
	
	public PageHeaderCache(PageCache pageCache) {
		ymax = YMAX;
		setPageCache(pageCache);
		processCache();
	}

	private void processCache() {
		findTopWhitespace();
		getSVGElement();
		SVGSVG.wrapAndWriteAsSVG(svgElement, new File("target/debug/pageHeader"+pageCache.getSerialNumber()+".svg"));
	}

	private void findTopWhitespace() {
		List<SVGElement> elements = SVGElement.extractSelfAndDescendantElements(pageCache.getOriginalSVGElement());
		getOrCreateAllElementList();

		for (SVGElement element : elements) {
			Real2Range bbox = element.getBoundingBox();
			if (bbox != null && bbox.getYMax() < ymax) {
				this.allElementList.add(element);
			}
		}
		LOG.debug("TOP "+allElementList.size());
	}
	
	public Double getYmax() {
		return ymax;
	}
	
	public void setYmax(Double ymax) {
		this.ymax = ymax;
	}

}
