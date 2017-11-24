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
	private List<SVGAtom> atomList;
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public SVGBond(SVGLine line) {
		super(line);
	}
	
	public SVGAtom getAtom(int index) {
		getOrCreateNodeList();
		return index >= 2 ? null :  (SVGAtom) nodeList.get(index);
	}

	public List<SVGAtom> getAtomList() {
		// these are actually atoms
		List<SVGNode> nodes = super.getOrCreateNodeList();
		atomList = new ArrayList<SVGAtom>();
		for (SVGNode node : nodes) {
			atomList.add((SVGAtom)node);
		}
		return atomList;
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
		}
		return g;
	}

	public String toString() {
		getAtomList();
		StringBuilder sb = new StringBuilder();
		sb.append(this.getId()+": ");
		SVGAtom atom0 = atomList.get(0);
		sb.append(atom0 == null ? "null " : atom0.getId()+"; ");
		SVGAtom atom1 = atomList.get(1);
		sb.append(atom1 == null ? "null ": atom1.getId()+"; ");
		sb.append(this.getXY(0)+" "+this.getXY(1)+"; label: "+label+"; wt: "+getWeight()+" ");
		return sb.toString();
	}


	
}
