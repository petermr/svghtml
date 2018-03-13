package org.xmlcml.graphics.svg.layout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;

/** middle column of 3 or single column
 * 
 * @author pm286
 *
 */
public class SVGPubstyleMiddleColumn extends AbstractPubstyle {
	private static final Logger LOG = Logger.getLogger(SVGPubstyleMiddleColumn.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	public final static String SVG_CLASSNAME = "middleColumn";

	public SVGPubstyleMiddleColumn() {
		super();
	}
	
	public SVGPubstyleMiddleColumn(SVGElement element) {
		super(element);
	}

	@Override
	protected String getPubstyleClassName() {
		return SVG_CLASSNAME;
	}

}
