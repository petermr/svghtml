package org.xmlcml.graphics.svg.builder;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGSVG;

public class MoleculeBuilderTest {

	private static final Logger LOG = Logger.getLogger(MoleculeBuilderTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	
	@Test
	public void testCreateFullMolecule() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("atomSymbolsFromPaths", "glyphs/figure1.M1");
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
	public void testCreateSimpleMolecule0() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("simpleMolecule0", "glyphs/figure1.M1");

	}

	/** minimal molecule to test double bond gathering.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testDoubleBond() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("doubleBond", "glyphs/figure1.M1");

	}

	/** minimal molecule to test bonds meeting hetero atom.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testHetero() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("hetero", "glyphs/figure1.M1");

	}


	/** test stub join.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testStubJoin() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("stubJoin", "glyphs/figure1.M1");

	}

	/** test undeleted atom.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testUndeletedAtom() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("propionic", "glyphs/figure1.M1");

	}

	/** test undeleted atom.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testUndeletedAtom0() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		moleculeBuilder.createTestMoleculeAndDefaultOutput("formic", "glyphs/figure1.M1");

	}

	/** test undeleted atom.
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testUndeletedAtom1() throws IOException {
		MoleculeBuilder moleculeBuilder = new MoleculeBuilder();
		// this is very sensitive
		// 0.1 misses a join while 0.3 fails to join an N-N bond.
		// needs more work
		moleculeBuilder.setEndDelta(0.2);
		moleculeBuilder.createTestMoleculeAndDefaultOutput("doubleBond1", "glyphs/figure1.M1");

	}



}
