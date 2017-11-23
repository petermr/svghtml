package org.xmlcml.graphics.svg.builder;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.Angle;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Angle.Units;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGSVG;

public class MoleculeBuilderTest {

	private static final Logger LOG = Logger.getLogger(MoleculeBuilderTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	
	@Test
	public void testCreateSimpleMolecule() {
		String fileroot = "atomSymbolsFromPaths";
		String dirRoot = "glyphs/figure1.M1";
		File outputDir = new File("target/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createWeightedLabelledGraph(svgElement);
		SVGElement svgx = moleculeBuilder.getOrCreateSVG();
		
//		g.appendChild(svgElement.copy());
		SVGSVG.wrapAndWriteAsSVG(svgx, new File(outputDir, fileroot+".svg"));

	}
	
	/*
Nodes: 
[
n0; (191.877,400.278) S; edges:
n1; (199.41,421.644) N; edges:
n2; (186.21,386.278) C; edges: e0;
n3; (186.21,394.799) C; edges: e0;
n4; (193.483,407.533) C; edges: e1;
n5; (186.211,411.769) C; edges: e1;
n6; (193.483,409.298) C; edges: e2;
n7; (187.692,412.641) C; edges: e2;
n8; (193.483,402.331) C; edges: e3;
n9; (193.483,407.556) C; edges: e3;
n10; (186.211,394.775) C; edges: e4;
n11; (190.918,397.53) C; edges: e4;
n12; (186.21,411.746) C; edges: e5;
n13; (186.21,420.078) C; edges: e5;
n14; (193.483,424.291) C; edges: e6;
n15; (186.211,420.102) C; edges: e6;
n16; (193.483,422.551) C; edges: e7;
n17; (187.692,419.208) C; edges: e7;
n18; (197.768,421.843) C; edges: e8;
n19; (193.483,424.291) C; edges: e8;
n20; (200.733,411.746) C; edges: e9;
n21; (200.733,417.16) C; edges: e9;
n22; (199.25,412.64) C; edges: e10;
n23; (199.25,417.16) C; edges: e10;
n24; (193.483,407.533) C; edges: e11;
n25; (200.732,411.769) C; edges: e11;
n26; (193.483,424.315) C; edges: e12;
n27; (193.483,432.93) C; edges: e12; ]
Edges: [
 line: from((186.21001,386.27802)) to((186.21001,394.79901)) label: ; wt: 1.0
 line: from((193.483,407.53299)) to((186.211,411.76901)) label: ; wt: 1.0
 line: from((193.483,409.298)) to((187.692,412.64099)) label: ; wt: 1.0
 line: from((193.483,402.33099)) to((193.483,407.556)) label: ; wt: 1.0
 line: from((186.211,394.77499)) to((190.918,397.53)) label: ; wt: 1.0
 line: from((186.21001,411.746)) to((186.21001,420.078)) label: ; wt: 1.0
 line: from((193.483,424.29099)) to((186.211,420.10199)) label: ; wt: 1.0
 line: from((193.483,422.55099)) to((187.692,419.20801)) label: ; wt: 1.0
 line: from((197.76801,421.84299)) to((193.483,424.29099)) label: ; wt: 1.0
 line: from((200.733,411.746)) to((200.733,417.16)) label: ; wt: 1.0
 line: from((199.25,412.64001)) to((199.25,417.16)) label: ; wt: 1.0
 line: from((193.483,407.53299)) to((200.73199,411.76901)) label: ; wt: 1.0
 line: from((193.483,424.315)) to((193.483,432.92999)) label: ; wt: 1.0
]
	 */
	@Test
	public void testCreateSimpleMolecule0() {
		String fileroot = "simpleMolecule0";
		String dirRoot = "glyphs/figure1.M1";
		File outputDir = new File("target/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createWeightedLabelledGraph(svgElement);
		SVGElement svgx = moleculeBuilder.getOrCreateSVG();
		LOG.debug("++++++++++++++++++++++++++++++++++++++");
		LOG.debug(moleculeBuilder);
		
//		g.appendChild(svgElement.copy());
		SVGSVG.wrapAndWriteAsSVG(svgx, new File(outputDir, fileroot+".svg"));

	}

	@Test
	public void testCreateSimpleMolecule00() {
		String fileroot = "simpleMolecule00";
		String dirRoot = "glyphs/figure1.M1";
		File outputDir = new File("target/", dirRoot);
		File inputDir = new File(SVGHTMLFixtures.SVG_DIR, dirRoot);
		File inputFile = new File(inputDir, fileroot + ".svg");
		Assert.assertTrue("exists: "+inputFile, inputFile.exists());
		SVGElement svgElement = SVGElement.readAndCreateSVG(inputFile);
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createWeightedLabelledGraph(svgElement);
		SVGElement svgx = moleculeBuilder.getOrCreateSVG();
		LOG.debug("++++++++++++++++++++++++++++++++++++++");
		LOG.debug(moleculeBuilder);
		
//		g.appendChild(svgElement.copy());
		SVGSVG.wrapAndWriteAsSVG(svgx, new File(outputDir, fileroot+".svg"));

	}



}
