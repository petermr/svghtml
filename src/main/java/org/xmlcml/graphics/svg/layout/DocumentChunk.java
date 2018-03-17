package org.xmlcml.graphics.svg.layout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGPath;
import org.xmlcml.graphics.svg.SVGText;

public class DocumentChunk extends SVGText {
	private static final Logger LOG = Logger.getLogger(DocumentChunk.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	public final static String LEVEL = "level";

	private Integer level;
	
	public DocumentChunk(SVGPath path) {
		this.copyAttributesChildrenElements(path);
	}
	
	public DocumentChunk(SVGText text) {
		this.copyAttributesChildrenElements(text);
		getLevel();
	}
	
	public Integer getLevel() {
		String ll = this.getAttributeValue(LEVEL);
		Integer level = null;
		if (ll != null) {
			try {
				level = new Integer(Integer.parseInt(ll));
			} catch (Exception nfe) {
				throw new RuntimeException("failed to parse level as integer: "+ll);
			}
		}
		return level;
	}
	
	
}
