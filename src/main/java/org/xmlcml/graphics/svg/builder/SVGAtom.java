package org.xmlcml.graphics.svg.builder;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGCircle;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.linestuff.SVGNode;

public class SVGAtom extends SVGNode {
	private static final Logger LOG = Logger.getLogger(SVGAtom.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private static Map<String, String> atomColorMap = new HashMap<String, String>();
	static {
		atomColorMap.put("C", "black");
		atomColorMap.put("N", "blue");
		atomColorMap.put("O", "red");
		atomColorMap.put("OH", "red");
		atomColorMap.put("S", "orange");
	};
	private double radius = 2.0;
	
	public SVGAtom(SVGText text) {
		super(text);
	}

	@Override
	public SVGElement getOrCreateSVG() {
		SVGG g = new SVGG();
		if (label != null && !"C".equals(label)) {
			SVGCircle circle = new SVGCircle(this.getMidXY(), radius);
			circle.setFill("white");
			g.appendChild(circle);
			String color = atomColorMap.get(label);
			SVGText text = SVGText.createText(getXY(), label, "fill:"+color+";");
			text.setFontSize(5.0);
			g.appendChild(text);
		} else {
//			g.appendChild(new SVGCircle(this.getMidXY(), 0.5));
		}
		return g;
	}

	private Real2 getMidXY() {
		Real2Range bbox = this.getBoundingBox();
		Real2[] corners = bbox.getLLURCorners();
		Real2 xyMid = corners[0].getMidPoint(corners[1]);
		return xyMid;
	}

	
}
