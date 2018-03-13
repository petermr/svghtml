package org.xmlcml.graphics.svg.layout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;

/** right column of 2 or 3
 * 
 * @author pm286
 *
 */
public class SVGPubstyleRightColumn extends AbstractPubstyle {
	private static final Logger LOG = Logger.getLogger(SVGPubstyleRightColumn.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	public final static String SVG_CLASSNAME = "rightColumn";

	public SVGPubstyleRightColumn() {
		super();
	}
	
	public SVGPubstyleRightColumn(SVGElement element) {
		super(element);
	}

	@Override
	protected String getPubstyleClassName() {
		return SVG_CLASSNAME;
	}

}
