package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;

public class TextCacheTest {


private static final Logger LOG = Logger.getLogger(TextCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testAsciiCompact() {
		ComponentCache componentCache = new ComponentCache();
		SVGElement svgElement = SVGElement.readAndCreateSVG(new File(SVGHTMLFixtures.FONTS_DIR, "bugascii.svg"));
		TextCache textCache = new TextCache(componentCache);
		textCache.setUseCompactOutput(true);
		textCache.extractTexts(svgElement);
		List<SVGText> texts = textCache.getTextList();
		Assert.assertEquals("compacted from ascii", 
				"[[3-9]((304.054,534.992))]]", texts.toString());

	}
	@Test
	public void testCompactBug() {
		ComponentCache componentCache = new ComponentCache();
		SVGElement svgElement = SVGElement.readAndCreateSVG(new File(SVGHTMLFixtures.FONTS_DIR, "bug.svg"));
		TextCache textCache = new TextCache(componentCache);
		textCache.setUseCompactOutput(true);
		textCache.extractTexts(svgElement);
		List<SVGText> texts = textCache.getTextList();
		// note confusing to read since there are square brackets in stream!
		Assert.assertEquals("compacted from ascii", 
				"[[3((304.054,534.992))], [–((309.219,534.992))], [9]((313.718,534.992))]]", texts.toString());

	}
	@Test
	public void testExtendBug() {
		ComponentCache componentCache = new ComponentCache();
		SVGElement svgElement = SVGElement.readAndCreateSVG(new File(SVGHTMLFixtures.FONTS_DIR, "bug.svg"));
		TextCache textCache = new TextCache(componentCache);
		textCache.setUseCompactOutput(false);
		textCache.extractTexts(svgElement);
		List<SVGText> texts = textCache.getTextList();
		// note confusing to read since there are square brackets in stream!
		Assert.assertEquals("uncompact", 
				"[[3((304.054,534.992))], [–((309.219,534.992))], [9((313.718,534.992))], []((318.883,534.992))]]",
				texts.toString());

	}
}
