package org.xmlcml.graphics.svg.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Angle;
import org.xmlcml.euclid.Angle.Units;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.svg.SVGCircle;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.cache.ComponentCache;
import org.xmlcml.graphics.svg.linestuff.SVGNode;
import org.xmlcml.graphics.svg.plot.XPlotBox;

/** a simple class to make molecules from SVGs.
 * 
 * @author pm286
 *
 */
public class MoleculeBuilder {
	private static final String BOND_ID = "e";
	private static final String NODE_ID = "n";
	private static final Logger LOG = Logger.getLogger(MoleculeBuilder.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	private static final String C = "C";
	private static final String H = "H";

	private double endDelta = 0.1;
	private double midPointDelta = 2.0;
	private Angle parallelEps = new Angle(0.05, Units.RADIANS);
	private List<SVGAtom> atomList;
	private List<SVGBond> bondList;
	private Map<String, SVGBond> bondById;
	private Set<SVGAtom> nonCarbonAtoms;

	public void createWeightedLabelledGraph(SVGElement svgElement) {
		XPlotBox xPlotBox = new XPlotBox();
		ComponentCache componentCache = new ComponentCache(xPlotBox); 
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		List<SVGLine> lineList = componentCache.getOrCreateLineCache().getOrCreateLineList();
		bondList = createBondList(lineList);
		List<SVGText> textList = componentCache.getOrCreateTextCache().getTextList();
		// make nodes from all texts
		addNonCarbonAtoms(textList);
		addLinesAndJoin();
		getOrCreateBondMap();
		gatherMultipleBonds();
		joinDisconnectedAtoms();
		createSmiles();
	}

    private Map<String, SVGBond> getOrCreateBondMap() {
    	if (bondById == null) {
    		bondById = new HashMap<String, SVGBond>();
    	}
    	return bondById;
	}

	private void joinDisconnectedAtoms() {
    	for (SVGAtom atom : nonCarbonAtoms) {
    		Real2 xy = atom.getCenterOfIntersectionOfNeighbouringStubBonds();
    		if (xy != null) {
    			atom.setXY(xy);
    			atom.joinNeighbouringStubBonds();
    		}
    	}
	}

	/** inefficient quadratic
 * only does double bonds ATM
 */
	private void gatherMultipleBonds() {
		SVGG g = new SVGG();
		for (int i = 0; i < getBondList().size() - 1; i++) {
			SVGBond bondi = getBondList().get(i);
			for (int j = i + 1; j < getBondList().size(); j++) {
				SVGBond bondj = getBondList().get(j);
				if (bondi.isAntiParallelTo(bondj, parallelEps) || bondi.isParallelTo(bondj, parallelEps)) {
					double delta = bondi.getMidPoint().getDistance(bondj.getMidPoint());
					if (delta < midPointDelta) {
						SVGBond primaryBond = getPrimaryBond(bondi, bondj);
						if (primaryBond == null) {
							primaryBond = createAverageBond(bondi, bondj);
						}
						primaryBond.setWeight(2);
						SVGBond minorBond = primaryBond == null ? null : (primaryBond == bondi ? bondj : bondi);
						removeAtoms(minorBond.getAtomList());
						removeBond(minorBond);
					}
				}
			}
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/debug/double.svg"));
	}

	private void removeAtoms(List<SVGAtom> atomList) {
		for (SVGAtom atom : atomList) {
			this.getAtomList().remove(atom);
		}
	}

	/** find average bond
	 * 
	 * @param bondi // takes this as the result; modified
	 * @param bondj
	 * @return
	 */
	private SVGBond createAverageBond(SVGBond bondi, SVGBond bondj) {
		int indexi = 0;
		int indexj = 1;
		if (bondi.isAntiParallelTo(bondj, parallelEps)) {
			indexi = 1;
			indexj = 0;
		}
		Real2 xy0i = bondi.getXY(0);
		Real2 xy0j = bondj.getXY(indexi);
		Real2 xymean = xy0i.getMidPoint(xy0j);
		bondi.setXY(xymean, 0);
		Real2 xy1i = bondi.getXY(1);
		Real2 xy1j = bondj.getXY(indexj);
		bondi.setXY(xy1i.getMidPoint(xy1j), 1);
		return bondi;
	}

	private void removeBond(SVGBond bond) {
		if (bond != null) {
			// don't remove atoms!
			getBondList().remove(bond);
		}
	}

	/** finds most prominent edge.
	 * for finading major partner in double bond
	 * if edge has no branches choose other edge
	 * if neither has branches (ethene) return null
	 * if both have branches return null
	 * 
	 * @param bondi
	 * @param bondj
	 * @return null if equal
	 */
	private SVGBond getPrimaryBond(SVGBond bondi, SVGBond bondj) {
		int branchEdgeCounti = bondi.getBranchEdgeCount();
		int branchEdgeCountj = bondj.getBranchEdgeCount();
		if (branchEdgeCounti == 0) {
			return branchEdgeCountj == 0 ? null : bondj;
		} else if (branchEdgeCountj == 0) {
			return bondi;
		}
		// separated double bond, each with branch/es
		return null;
	}

	private void debugGraph() {
		SVGG g = new SVGG();
		g.appendChild(debugAtoms());
		g.appendChild(debugBonds());
		SVGSVG.wrapAndWriteAsSVG(g, new File(new File("target/debug"), "graph.svg"));
	}

	private SVGG debugBonds() {
		SVGG g = new SVGG();
		for (int j = 0; j < getBondList().size(); j++) {
			SVGLine line = getBondList().get(j);
			g.appendChild(line.copy());
		}
		return g;
	}


	private SVGG debugAtoms() {
		SVGG g = new SVGG();
		for (int i = 0; i < getAtomList().size(); i++) {
			SVGAtom atom = getAtomList().get(i);
			Real2 xy = atom.getXY();
			SVGCircle circle = new SVGCircle(xy, 2.0);
			circle.setCSSStyle("fill:none;stroke:red;stroke-width:0.3;");
			SVGText t = new SVGText(xy, ""+i);
			t.setCSSStyle("fill:blue;font-size:2;");
			g.appendChild(t);
			g.appendChild(circle);
		}
		return g;
	}

	private List<SVGBond> createBondList(List<SVGLine> lineList) {
		bondList = new ArrayList<SVGBond>();
		for (int i = 0; i < lineList.size(); i++) {
			SVGLine line = lineList.get(i);
			SVGBond bond = new SVGBond(line);
			addBondAndCreateId(bond);
		}
		return bondList;
	}

	private void addBondAndCreateId(SVGBond bond) {
		bond.setId(BOND_ID + bondList.size());
		bondList.add(bond);
		getOrCreateBondMap().put(bond.getId(), bond);
	}

	void addNodeAndCreateId(SVGAtom atom) {
		getOrCreateAtomList();
		atom.setId(NODE_ID + getAtomList().size());
		getAtomList().add(atom);
	}

	public List<SVGAtom> getOrCreateAtomList() {
		if (atomList == null) {
			atomList = new ArrayList<SVGAtom>();
		}
		return atomList;
	}

	public List<SVGBond> getOrCreateBondList() {
		if (bondList == null) {
			bondList = new ArrayList<SVGBond>();
		}
		return bondList;
	}

	private void addNonCarbonAtoms(List<SVGText> textList) {
		getOrCreateAtomList();
		nonCarbonAtoms = new HashSet<SVGAtom>();
		for (int idx = 0; idx < textList.size(); idx++) {
			SVGAtom atom = this.createAtom(textList.get(idx));
			atom.setId(NODE_ID+idx);
			addNodeAndCreateId(atom);
			nonCarbonAtoms.add(atom);
		}
	}

	private SVGAtom createAtom(SVGText svgText) {
		SVGAtom atom = new SVGAtom(this, svgText);
		return atom;
	}

	private void addLinesAndJoin() {
		for (int idx = 0; idx < getBondList().size(); idx++) {
			SVGBond bond = getBondList().get(idx);
			joinOrCreateAtomsAtEnd(bond, 0);
			joinOrCreateAtomsAtEnd(bond, 1);
		}
	}
	
	private void joinOrCreateAtomsAtEnd(SVGBond bond, int iend) {
		Real2 xyEnd = bond.getXY(iend);
		SVGAtom foundAtom = joinToExistingAtom(xyEnd);
		if (foundAtom == null) {
			// make new carbon
			SVGText text = SVGText.createDefaultText(xyEnd, "C");
			foundAtom = this.createAtom(text);
			getAtomList().add(foundAtom);
			foundAtom.setId("n"+getAtomList().size());
		}
		bond.addNode(foundAtom, iend);
	}

	private SVGAtom joinToExistingAtom(Real2 xyEnd) {
		SVGAtom foundAtom = null;
		for (int i = 0; i < getAtomList().size(); i++) {
			SVGAtom atom = getAtomList().get(i);
			if (atom.getXY().getDistance(xyEnd) < endDelta) {
				foundAtom = atom;
				break;
			}
		}
		return foundAtom;
	}

	public SVGElement getOrCreateSVG() {
		SVGG g = new SVGG();
		for (SVGAtom atom : getAtomList()) {
			g.appendChild(atom.getOrCreateSVG());
		}
		for (SVGBond bond : getBondList()) {
			g.appendChild(bond.getOrCreateSVG());
		}
		return g;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("================>>>\n");
		getOrCreateAtomList();
		for (SVGAtom atom : getAtomList()) {
			sb.append(atom.toString());
			sb.append("\n");
		}
		getOrCreateBondList();
		for (SVGBond bond : getBondList()) {
			sb.append(bond.toString());
			sb.append("\n");
		}
		sb.append("<<<================\n");
		return sb.toString();
	}
	
	public String createSmiles() {
		
		StringBuilder sb = new StringBuilder();
		LOG.debug("NYI ? CML");
//		Set<SVGAtom> unused = new HashSet<SVGAtom>(atomList);
//		Set<SVGAtom> used = new HashSet<SVGAtom>();
//		while (!unused.isEmpty()) {
//			SVGAtom atom = unused.iterator().next();
//			
//		}
		return sb.toString();
	}

	public List<SVGAtom> getAtomList() {
		return atomList;
	}

	public List<SVGBond> getBondList() {
		return bondList;
	}

}
