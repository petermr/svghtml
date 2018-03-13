package org.xmlcml.graphics.layout;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.layout.PubstyleManager;
import org.xmlcml.graphics.svg.layout.SVGPubstyle;
import org.xmlcml.graphics.svg.layout.SVGPubstyle.PageType;
import org.xmlcml.graphics.svg.layout.SVGPubstyleAbstract;
import org.xmlcml.graphics.svg.layout.SVGPubstyleHeader;
import org.xmlcml.graphics.svg.layout.SVGPubstylePage;

public class PubstyleTest {
	private static final Logger LOG = Logger.getLogger(PubstyleTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testGetPubstyleByPubstyleString() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle bmcStyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		LOG.debug("style: "+bmcStyle);
		Assert.assertEquals("bmc", bmcStyle.getPubstyle());
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
		SVGElement pubstylePage1 = bmcPubstyle.getRawPage(PageType.P1);
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
		SVGPubstylePage pubstylePage2 = pubstyle.getPubstylePage(PageType.P2);
		Assert.assertNotNull(pubstylePage2);
		LOG.debug("abstract "+pubstylePage2.toXML());
		SVGPubstyleAbstract pubstyleAbstract = pubstyle.getAbstract();
		Assert.assertNotNull(pubstyleAbstract);
		LOG.debug("abstract "+pubstyleAbstract.toXML());
		SVGElement pubstyleHeader = pubstyle.getHeader(PageType.P2);
		Assert.assertNotNull(pubstyleHeader);
		LOG.debug("header "+pubstyleHeader.toXML());
		SVGElement pubstyleFooter = pubstyle.getFooter(PageType.P2);
		Assert.assertNotNull(pubstyleFooter);
		LOG.debug("footer "+pubstyleHeader.toXML());
		SVGElement pubstyleLeft = pubstyle.getLeft(PageType.P2);
		Assert.assertNotNull(pubstyleLeft);
		LOG.debug("left "+pubstyleLeft.toXML());
		SVGElement pubstyleRight = pubstyle.getRight(PageType.P2);
		Assert.assertNotNull(pubstyleRight);
		LOG.debug("right "+pubstyleRight.toXML());
		SVGElement pubstyleWideImage = pubstyle.getWideImage(PageType.P2);
		Assert.assertNotNull(pubstyleWideImage);
		LOG.debug("wideImage "+pubstyleWideImage.toXML());
		SVGElement pubstyleWideTable = pubstyle.getWideTable(PageType.P2);
		Assert.assertNotNull(pubstyleWideTable);
		LOG.debug("wideTable "+pubstyleWideTable.toXML());
	}
	
	@Test
	public void testPubstylePage2Header() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGPubstyleHeader pubstyleHeader = pubstyle.getHeader(PageType.P2);
		Assert.assertNotNull(pubstyleHeader);
		Assert.assertEquals("bbox rect", 1, SVGRect.extractSelfAndDescendantRects(pubstyleHeader).size());
		Assert.assertEquals("header texts", 2, SVGText.extractSelfAndDescendantTexts(pubstyleHeader).size());
		Real2Range bbox = pubstyleHeader.getBoundingBox();
		Assert.assertEquals("bbox", "((0.0,540.0),(0.0,47.0))", pubstyleHeader.getBoundingBox().toString());
	}

	@Test
	public void testPubstylePage2HeaderAgainstText() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGPubstyleHeader pubstyleHeader = pubstyle.getHeader(PageType.P2);
		for (int i = 1; i <= 13; i++) {
			File inputSvgFile = new File(SVGHTMLFixtures.CORPUS_DIR, 
					"mosquitos/12936_2017_Article_1948/svg/fulltext-page"+i+".svg.compact.svg");
			SVGElement inputSVGElement = SVGElement.readAndCreateSVG(inputSvgFile);
			List<SVGElement> headerElements = pubstyleHeader.extractElements(inputSVGElement);
			List<SVGText> texts = SVGText.extractTexts(headerElements);
			List<String> matchedStrings = pubstyleHeader.matchTexts(texts);
			LOG.debug("matches "+matchedStrings);
		}

	}

}
