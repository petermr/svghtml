package org.xmlcml.graphics.layout;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.layout.PubstyleManager;
import org.xmlcml.graphics.svg.layout.SVGPubstyle;
import org.xmlcml.graphics.svg.layout.SVGPubstyle.PageType;

public class PubstyleTest {
	private static final Logger LOG = Logger.getLogger(PubstyleTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testGuessPubstyle() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		File inputSvgFile = new File(SVGHTMLFixtures.CORPUS_DIR, 
				"mosquitos/12936_2017_Article_1948/svg/fulltext-page1.svg.compact.svg");
		SVGPubstyle pubstyle = pubstyleManager.guessPubstyleFromFirstPage(inputSvgFile);
		Assert.assertNotNull("bmc", pubstyle);
		Assert.assertEquals("bmc", pubstyle.getPubstyle());
	}
	
	@Test
	public void testPubstylePage1() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle bmcPubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGElement pubstylePage1 = bmcPubstyle.getPage(PageType.P1);
		Assert.assertNotNull(pubstylePage1);
		SVGElement pubstyleHeader = bmcPubstyle.getHeader(PageType.P1);
		Assert.assertNotNull(pubstyleHeader);
//		LOG.debug("header "+pubstyleHeader.toXML());
		SVGElement pubstyleAbstract = bmcPubstyle.getAbstract();
		Assert.assertNotNull(pubstyleAbstract);
		SVGElement abstractSection = bmcPubstyle.getAbstractSection();
		Assert.assertNotNull("abstractSection", abstractSection);
//		LOG.debug("abstract "+abstractSection.toXML());
	}
	
	@Test
	public void testPubstylePage2() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGElement pubstylePage2 = pubstyle.getPage(PageType.P2);
		Assert.assertNotNull(pubstylePage2);
		SVGElement pubstyleAbstract = pubstyle.getAbstract();
		Assert.assertNotNull(pubstyleAbstract);
		LOG.debug("abstract "+pubstyleAbstract.toXML());
		SVGElement pubstyleHeader = pubstyle.getHeader(PageType.P2);
		Assert.assertNotNull(pubstyleHeader);
		LOG.debug("header "+pubstyleHeader.toXML());
	}
	
	@Test
	public void testGetPubstyleByPubstyleString() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle bmcStyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		LOG.debug("style: "+bmcStyle);
		Assert.assertEquals("bmc", bmcStyle.getPubstyle());
	}
}
