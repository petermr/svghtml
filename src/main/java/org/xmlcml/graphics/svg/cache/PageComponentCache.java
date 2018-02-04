package org.xmlcml.graphics.svg.cache;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;

public abstract class PageComponentCache extends ComponentCache {
	private static final Logger LOG = Logger.getLogger(PageComponentCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	protected PageCache pageCache;
	protected SVGElement svgElement;
	
	protected PageComponentCache() {
		super();
	}

	protected void setPageCache(PageCache pageCache) {
		this.pageCache = pageCache;
	}

	protected void getOrCreateAllElementList() {
		if (allElementList == null) {
			allElementList = new ArrayList<SVGElement>();
		}
	}
	
	public SVGElement getSVGElement() {
		svgElement = new SVGG();
		for (SVGElement element : allElementList) {
			svgElement.appendChild(element.copy());
		}
		return svgElement;
	}

}
