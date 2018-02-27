package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.fonts.StyleRecord;
import org.xmlcml.graphics.svg.fonts.StyleRecordSet;
import org.xmlcml.graphics.svg.text.SVGTextLine;
import org.xmlcml.graphics.svg.text.SVGTextLineList;

import com.google.common.collect.Multimap;

import junit.framework.Assert;

/** analyses pages for maths.
 * 
 * @author pm286
 *
 */
public class MathCacheTest {
	public static final Logger LOG = Logger.getLogger(MathCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	/** these are text processing initially but will turn into math later.
	 * 
	 */
	
	@Test
	public void testStyles() {
		File svgFile = new File(SVGHTMLFixtures.MATH_DIR, "equations7.svg");
		ComponentCache cache = new ComponentCache();
		cache.readGraphicsComponentsAndMakeCaches(svgFile);
		TextCache textCache = cache.getOrCreateTextCache();
		StyleRecordSet styleRecordSet = textCache.getOrCreateHorizontalStyleRecordSet();
		Assert.assertEquals(2, styleRecordSet.size());
		Multimap<Double, StyleRecord> styleRecordByFontSize = styleRecordSet.getStyleRecordByFontSize();
		Assert.assertEquals("sizes", 2, styleRecordByFontSize.size());
	}
		
	@Test
	@Ignore
	public void testLines() {
		File svgFile = new File(SVGHTMLFixtures.MATH_DIR, "equations7.svg");
		ComponentCache cache = new ComponentCache();
		cache.readGraphicsComponentsAndMakeCaches(svgFile);
		MathCache mathCache = cache.getOrCreateMathCache();
		TextCache textCache = cache.getOrCreateTextCache();
		// assume that y-coords will be the most important structure
		StyleRecordSet horizontalStyleRecordSet = mathCache.getOwnerComponentCache()
				.getOrCreateTextCache().getOrCreateHorizontalStyleRecordSet();
		Double largestFont = horizontalStyleRecordSet.getLargestFontSize();
		Assert.assertEquals(6.0, largestFont, 0.1);
		List<SVGTextLine> textLineList = textCache.getTextLinesForFontSize(largestFont);
		Assert.assertEquals(19, textLineList.size());
	}
		
	@Test
	@Ignore
	public void testIndents() {
		File svgFile = new File(SVGHTMLFixtures.MATH_DIR, "equations7.svg");
		File targetDir = new File("target/demos/varga/");
		ComponentCache componentCache = new ComponentCache();
		componentCache.readGraphicsComponentsAndMakeCaches(svgFile);
		MathCache mathCache = componentCache.getOrCreateMathCache();
		SVGTextLineList textLineList = mathCache.createTextLineList();
		Assert.assertEquals(19, textLineList.size());
		RealArray indents = textLineList.calculateIndents(1);
		Assert.assertEquals(""
				+ "(269.7,269.7,278.2,269.7,278.2,269.7,269.7,278.2,269.7,269.7,"
				+ "269.7,269.7,269.7,279.7,269.7,269.7,269.7,269.7,269.7)", 
				indents.toString());
	}

	@Test
	@Ignore
	public void testJoinLinesAtIndents() {
		File svgFile = new File(SVGHTMLFixtures.MATH_DIR, "equations7.svg");
		File targetDir = new File("target/demos/varga/");
		ComponentCache componentCache = new ComponentCache();
		componentCache.readGraphicsComponentsAndMakeCaches(svgFile);
		TextCache textCache = componentCache.getOrCreateTextCache();
//		SVGTextLineList textLineList = mathCache.createAndJoinTextLineList();
//		Assert.assertEquals(19, textLineList.size());
	}



}
