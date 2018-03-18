package org.xmlcml.graphics.layout;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.layout.DocumentChunk;
import org.xmlcml.graphics.svg.layout.PubstyleManager;
import org.xmlcml.graphics.svg.layout.SVGPubstyle;
import org.xmlcml.graphics.svg.layout.SVGPubstyle.PageType;
import org.xmlcml.graphics.svg.layout.SVGPubstyleAbstract;
import org.xmlcml.graphics.svg.layout.SVGPubstyleHeader;
import org.xmlcml.graphics.svg.layout.SVGPubstylePage;

public class PubstyleTest {
	public static final Logger LOG = Logger.getLogger(PubstyleTest.class);
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
		Assert.assertNotNull("null page", pubstylePage1);
		SVGElement pubstyleHeader = bmcPubstyle.getHeader(PageType.P1);
		Assert.assertNotNull("null header", pubstyleHeader);
		SVGElement pubstyleAbstract = bmcPubstyle.getAbstract();
		Assert.assertNotNull("null abstract", pubstyleAbstract);
		SVGElement abstractSection = bmcPubstyle.getAbstractSection();
		Assert.assertNotNull("abstractSection", abstractSection);
	}
	
	@Test
	public void testPubstylePage2() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGPubstylePage pubstylePage2 = pubstyle.getPubstylePage(PageType.P2);
		Assert.assertNotNull("page2", pubstylePage2);
		LOG.debug("page2 "+pubstylePage2.toXML());
		SVGPubstyleAbstract pubstyleAbstract = pubstyle.getAbstract();
		Assert.assertNotNull("abstract", pubstyleAbstract);
		SVGElement pubstyleHeader = pubstyle.getHeader(PageType.P2);
		Assert.assertNotNull("header", pubstyleHeader);
		SVGElement pubstyleFooter = pubstyle.getFooter(PageType.P2);
		Assert.assertNotNull("footer", pubstyleFooter);
		SVGElement pubstyleLeft = pubstyle.getLeft(PageType.P2);
		Assert.assertNotNull("left", pubstyleLeft);
		SVGElement pubstyleRight = pubstyle.getRight(PageType.P2);
		Assert.assertNotNull("right", pubstyleRight);
		SVGElement pubstyleWideImage = pubstyle.getWideImage(PageType.P2);
		Assert.assertNotNull("wideImage", pubstyleWideImage);
		SVGElement pubstyleWideTable = pubstyle.getWideTable(PageType.P2);
		Assert.assertNotNull("wideTable", pubstyleWideTable);
	}
	
	@Test
	public void testPubstylePage2Header() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		SVGPubstyleHeader pubstyleHeader = pubstyle.getHeader(PageType.P2);
		Assert.assertNotNull(pubstyleHeader);
		Assert.assertEquals("bbox rect", 1, SVGRect.extractSelfAndDescendantRects(pubstyleHeader).size());
		Assert.assertEquals("header texts", 3, SVGText.extractSelfAndDescendantTexts(pubstyleHeader).size());
		Real2Range bbox = pubstyleHeader.getBoundingBox();
		Assert.assertEquals("bbox", "((0.0,540.0),(0.0,47.0))", pubstyleHeader.getBoundingBox().toString());
	}

	@Test
	public void testPubstylePage2HeaderAgainstText() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		for (int ipage = 1; ipage <= 13; ipage++) {
			SVGPubstyleHeader pubstyleHeader = null;
			if (ipage == 1) {
				pubstyleHeader = pubstyle.getHeader(PageType.P1);
			} else if (ipage == 13) {
				pubstyleHeader = pubstyle.getHeader(PageType.PN);
			} else {
				pubstyleHeader = pubstyle.getHeader(PageType.P2);
			}
			File inputSvgFile = new File(SVGHTMLFixtures.CORPUS_DIR, 
					"mosquitos/12936_2017_Article_1948/svg/fulltext-page"+ipage+".svg.compact.svg");
			SVGElement inputSVGElement = SVGElement.readAndCreateSVG(inputSvgFile);
			Map<String, String> keyValues = pubstyleHeader.extractKeyValues(inputSVGElement);
			Assert.assertEquals((ipage == 1) ? 5 : 7, keyValues.size());
			Assert.assertEquals("16", keyValues.get("vol"));
			Assert.assertEquals(ipage == 1 ? null : ""+ipage, keyValues.get("page"));
		}

	}

	@Test
	public void testPubstyleSections() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		int end = 99;//13;
		int start = 1;
		String dirRoot = "mosquitos/12936_2017_Article_1948";
		String pageRoot = dirRoot + "/svg/fulltext-page";
		pubstyle.setEndPage(end);
		for (int page = start; page <= end; page++) {
			pubstyle.setCurrentPage(page);
			File inputSvgFile = new File(SVGHTMLFixtures.CORPUS_DIR, pageRoot+page+".svg.compact.svg");
			if (!inputSvgFile.exists()) {
				LOG.debug("====================FINISHED=================");
				break;
			}
			SVGElement inputSVGElement = SVGElement.readAndCreateSVG(inputSvgFile);
			LOG.debug("inputSVG: "+inputSVGElement.toXML().length());
			List<DocumentChunk> documentChunks = pubstyle.createDocumentChunks(inputSVGElement);
			LOG.debug("DocumentChunks: "+documentChunks.size());
			SVGSVG.wrapAndWriteAsSVG(documentChunks, new File("target/pubstyle/" + dirRoot + "/page"+page+".svg"));
		}
	}

	@Test
	public void testPubstyleSectionsInCorpus() {
		PubstyleManager pubstyleManager = new PubstyleManager();
		SVGPubstyle pubstyle = pubstyleManager.getSVGPubstyleFromPubstyleString("bmc");
		int end = 99;//13;
		int start = 1;
		String[] dirRoots = {
				"mosquitos1/12936_2017_Article_1948",
				"mosquitos1/12936_2017_Article_2115",
				"mosquitos1/12936_2017_Article_2156",
				"mosquitos1/13071_2017_Article_2342",
				"mosquitos1/13071_2017_Article_2417",
				"mosquitos1/13071_2017_Article_2489",
				"mosquitos1/13071_2017_Article_2546",
				"mosquitos1/13071_2017_Article_2581",
				"mosquitos1/13071_2018_Article_2625",
				"mosquitos1/wellcomeopenres-2-13662",
		};
		for (String dirRoot : dirRoots) {
			String pageRoot = dirRoot + "/svg/fulltext-page";
			pubstyle.setEndPage(end);
			pubstyle.setDirRoot(dirRoot);
			for (int page = start; page <= end; page++) {
				pubstyle.setCurrentPage(page);
				File inputSvgFile = new File(SVGHTMLFixtures.CORPUS_DIR, pageRoot+page+".svg.compact.svg");
				if (!inputSvgFile.exists()) {
					LOG.debug("====================FINISHED=================");
					break;
				}
				SVGElement inputSVGElement = SVGElement.readAndCreateSVG(inputSvgFile);
				List<DocumentChunk> documentChunks = pubstyle.createDocumentChunks(inputSVGElement);
				SVGSVG.wrapAndWriteAsSVG(documentChunks, new File("target/pubstyle/" + dirRoot + "/page"+page+".svg"));
			}
		}
	}



}
