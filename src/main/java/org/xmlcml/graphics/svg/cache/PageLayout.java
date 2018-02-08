package org.xmlcml.graphics.svg.cache;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;

/** defines components of how the page should be laid out
 * 
 * @author pm286
 *
 */
public class PageLayout {
	private static final Logger LOG = Logger.getLogger(PageLayout.class);
	public static final String RESOURCE_PREFIX = "/org/xmlcml";
	public static final String LAYOUT = RESOURCE_PREFIX+"/layout";
	public static final String BMC = LAYOUT+"/bmc"+".svg";
	
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private SVGElement layout;

	public PageLayout() {
	}
	
	public PageLayout(SVGElement layout) {
		this.setLayout(layout);
	}

	public void setLayout(SVGElement layout) {
		this.layout = layout;
	}
	
 }
