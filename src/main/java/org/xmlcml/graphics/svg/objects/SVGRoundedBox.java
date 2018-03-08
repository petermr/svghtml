package org.xmlcml.graphics.svg.objects;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGPath;

public class SVGRoundedBox extends SVGG {
	
	public static final String ROUNDED_BOX = "roundedBox";
	private static final Logger LOG = Logger.getLogger(SVGRoundedBox.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private SVGPath path;
	
	public SVGRoundedBox() {
		super();
		this.setClassName(ROUNDED_BOX);
	}
	
	public SVGRoundedBox(SVGPath path) {
		this();
		this.path = path;
	}

	public static SVGRoundedBox createRoundedBox(SVGPath path) {
		SVGRoundedBox roundedBox = null;
		if ("MCLCLCLCZ".equals(path.getOrCreateSignatureAttributeValue())) {
			roundedBox = new SVGRoundedBox(path);
		}
		return roundedBox;
	}

	public SVGPath getPath() {
		return path;
	}
}
