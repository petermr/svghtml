package org.xmlcml.graphics.svg.layout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;

/** a footer (can be on any/every page
 * 
 * @author pm286
 *
 */
public class SVGPubstyleLeftColumn extends AbstractPubstyle {
	private static final Logger LOG = Logger.getLogger(SVGPubstyleLeftColumn.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	public final static String SVG_CLASSNAME = "leftColumn";

	public SVGPubstyleLeftColumn() {
		super();
	}
	
	public SVGPubstyleLeftColumn(SVGElement element) {
		super(element);
	}

	@Override
	protected String getPubstyleClassName() {
		return SVG_CLASSNAME;
	}

}
