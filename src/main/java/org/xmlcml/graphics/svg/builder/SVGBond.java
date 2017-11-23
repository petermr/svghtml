package org.xmlcml.graphics.svg.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.linestuff.SVGEdge;
import org.xmlcml.graphics.svg.linestuff.SVGNode;

public class SVGBond extends SVGEdge {
	private static final Logger LOG = Logger.getLogger(SVGBond.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public SVGBond(SVGLine line) {
		super(line);
	}
	
//	private 
	public SVGAtom getAtom(int index) {
		getOrCreateNodeList();
		return index >= 2 ? null :  (SVGAtom) nodeList.get(index);
	}

	public List<SVGAtom> getAtomList() {
		// these are actually atoms
		List<SVGNode> nodes = super.getOrCreateNodeList();
		List<SVGAtom> atoms = new ArrayList<SVGAtom>();
		for (SVGNode node : nodes) {
			atoms.add((SVGAtom)node);
		}
		return atoms;
	}

	public SVGElement getOrCreateSVG() {
		SVGG g = new SVGG();
		SVGLine edgeCopy = (SVGLine) this.copy();
		edgeCopy.setStrokeWidth(this.getWeight());
		g.appendChild(edgeCopy);
		g.appendChild(SVGText.createText(this.getMidPoint(), getId(), "fill:green;font-size:2;"));
		if (getWeight() > 1.5) {
			SVGLine line = new SVGLine(edgeCopy);
			line.setWidth(1.0 / 3.0);
			line.setStroke("white");
			g.appendChild(line);
			LOG.debug("LINE: "+line);
		}
		return g;
	}

	
}
