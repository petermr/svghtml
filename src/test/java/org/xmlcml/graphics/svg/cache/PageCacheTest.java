package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.fonts.StyleRecord;
import org.xmlcml.graphics.svg.fonts.StyleRecordSet;
import org.xmlcml.graphics.svg.util.SuperPixelArray;

/** analyses pages for components.
 * may extend to compete documents.
 * 
 * @author pm286
 *
 */
// @Ignore // too long
public class PageCacheTest {
	public static final Logger LOG = Logger.getLogger(PageCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private ComponentCache componentCache;

	/** a page with a page header, two tables and some text
	 * get spanning rects
	 */
	@Test
	public void testPage6Rects() {
		List<? extends SVGElement> componentList = extractAndDisplayComponents(
				new File(SVGHTMLFixtures.TABLE_PAGE_DIR, "page6.svg"), new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, "page6.svg"));
		Assert.assertEquals("components", /*2995*/ 2982, componentList.size());
		RectCache rectCache = componentCache.getOrCreateRectCache();
		Assert.assertEquals("rects", 3, rectCache.getOrCreateRectList().size());
		List<SVGRect> spanningRectList = rectCache.getOrCreateHorizontalPanelList();
		Assert.assertEquals("panels", 3, spanningRectList.size());
	}
	/** a page with a page header, two tables and some text
	 * get spanning rects
	 * 
	 */
	@Test
	public void testPage6Texts() {
		List<? extends SVGElement> componentList = extractAndDisplayComponents(
				new File(SVGHTMLFixtures.TABLE_PAGE_DIR, "page6.svg"), new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, "page6.svg"));
		Assert.assertEquals("components", 2982/*2995*/, componentList.size());
		TextCache textCache = componentCache.getOrCreateTextCache();
		List<SVGText> textList = textCache.getTextList();
		Assert.assertEquals("components", 2964, textList.size());
		SVGG g = textCache.createCompactedTextsAndReplace();
		List<SVGText> convertedTextList = SVGText.extractSelfAndDescendantTexts(g);
		Assert.assertEquals("compacted", 100, convertedTextList.size());
		textList = textCache.getTextList();
		Assert.assertEquals("compacted", 100, textList.size());
		SVGSVG.wrapAndWriteAsSVG(g, new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, "texts6.svg"));
	}

	/** a page with a page header, two tables and some text
	 * get spanning rects
	 * 
	 */
	@Test
	public void testFindWhitespace() {
		extractAndDisplayComponents(new File(SVGHTMLFixtures.TABLE_PAGE_DIR, "page6.svg"), new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, "page6.svg"));
		TextCache textCache = componentCache.getOrCreateTextCache();
		SVGElement g = textCache.createCompactedTextsAndReplace();
		Assert.assertEquals("bounding boxes", 118/*131*/, componentCache.getBoundingBoxList().size());
		double dx = 5;
		double dy = 5;
		SVGG gg = componentCache.createWhitespaceG(dx, dy);
		SVGSVG.wrapAndWriteAsSVG(gg, new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, "whitespace6.svg"));
	}
	
	@Test
	@Ignore // too long
	public void testArticleWhitespace() {
		String root = "10.1136_bmjopen-2016-011048";
		File outDir = new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, root);
		File journalDir = new File(SVGHTMLFixtures.TABLE_DIR, root);
		File svgDir = new File(journalDir, "svg");
		for (File svgFile : svgDir.listFiles()) {
			System.out.print(".");
			String basename = FilenameUtils.getBaseName(svgFile.toString());
			extractAndDisplayComponents(svgFile, new File(outDir, basename+".convert.svg"));
			TextCache textCache = componentCache.getOrCreateTextCache();
			SVGElement g = textCache.createCompactedTextsAndReplace();
			SVGG gg = componentCache.createWhitespaceG(5, 5);
			SVGSVG.wrapAndWriteAsSVG(gg, new File(outDir, basename+".textline.svg"));
		}
		
	}
	
	@Test
	@Ignore // DEVELOP// too long
	public void testSuperPixelArray() {
		String root = "10.1136_bmjopen-2016-011048";
		File outDir = new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, root);
		File journalDir = new File(SVGHTMLFixtures.TABLE_DIR, root);
		File svgDir = new File(journalDir, "svg");
		SuperPixelArray versoPixelArray = null;
		SuperPixelArray rectoPixelArray = null;
		boolean verso = true;
		boolean recto = false;
		for (File svgFile : svgDir.listFiles()) {
			recto = !recto;
			verso = !verso;
			System.out.print(".");
			String basename = FilenameUtils.getBaseName(svgFile.toString());
			SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
			componentCache = new ComponentCache();
			componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
			TextCache textCache = componentCache.getOrCreateTextCache();
			textCache.createCompactedTextsAndReplace();
			Real2Range bbox = Real2Range.createTotalBox(componentCache.getBoundingBoxList());
			LOG.debug(">> "+bbox+" "+componentCache.getBoundingBoxList().size());
			SuperPixelArray superPixelArray = new SuperPixelArray(new Int2Range(bbox));
			superPixelArray.setPixels(1, componentCache.getBoundingBoxList());
			SVGG g = new SVGG();
			superPixelArray.draw(g, new File(outDir, basename+".superPixels.svg"));
			if (verso) {
				versoPixelArray = superPixelArray.plus(versoPixelArray);
			}
			if (recto) {
				rectoPixelArray = superPixelArray.plus(rectoPixelArray);
			}
			
		}
		versoPixelArray.draw(new SVGG(), new File(outDir, "versoPixels.svg"), true);
		rectoPixelArray.draw(new SVGG(), new File(outDir, "rectoPixels.svg"), true);
	}
	
	/** analyses a group of papers and outputs diagrams of the whitespace.
	 * 
	 */
	@Test
	@Ignore // DEVELOP// too long
	public void testArticlesWhitespace() {
		File[] journalDirs = SVGHTMLFixtures.TABLE_DIR.listFiles();
		for (File journalDir : journalDirs) {
			System.out.print("*");
			String root = journalDir.getName();
			File outDir = new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, root);
			File svgDir = new File(journalDir, "svg");
			if (svgDir.listFiles() == null) continue;
			for (File svgFile : svgDir.listFiles()) {
				System.out.print(".");
				String basename = FilenameUtils.getBaseName(svgFile.toString());
				extractAndDisplayComponents(svgFile, new File(outDir, basename+".convert.svg"));
				TextCache textCache = componentCache.getOrCreateTextCache();
				SVGElement g = textCache.createCompactedTextsAndReplace();
				SVGG gg = componentCache.createWhitespaceG(5, 5);
				SVGSVG.wrapAndWriteAsSVG(gg, new File(outDir, basename+".textline.svg"));
			}
		}		
	}

	/** analyze group of articles for superpixels.
	 * 
	 */
	@Test
	@Ignore // DEVELOP// too long
	public void testSuperPixelArrayForArticles() {
		File[] journalDirs = SVGHTMLFixtures.TABLE_DIR.listFiles();
		for (File journalDir : journalDirs) {
			if (!journalDir.isDirectory()) continue;
			System.out.println(">>"+journalDir);
			String root = journalDir.getName();
			File outDir = new File(SVGHTMLFixtures.TARGET_TABLE_CACHE_DIR, root);
			File svgDir = new File(journalDir, "svg");
			SuperPixelArray versoPixelArray = null;
			SuperPixelArray rectoPixelArray = null;
			boolean verso = true;
			boolean recto = false;
			if (svgDir.listFiles() == null) continue;
			for (File svgFile : svgDir.listFiles()) {
				recto = !recto;
				verso = !verso;
				System.out.print(".");
				PageCache pageCache = new PageCache();
				pageCache.setSvgFile(svgFile);
				SuperPixelArray superPixelArray = pageCache.createSuperpixelArray(outDir, svgFile);
				SVGG g = new SVGG();
				superPixelArray.draw(g, new File(outDir, pageCache.getBasename()+".superPixels.svg"));
				if (verso) {
					versoPixelArray = superPixelArray.plus(versoPixelArray);
				}
				if (recto) {
					rectoPixelArray = superPixelArray.plus(rectoPixelArray);
				}
				
			}
			versoPixelArray.draw(new SVGG(), new File(outDir, "versoPixels.svg"), true);
			rectoPixelArray.draw(new SVGG(), new File(outDir, "rectoPixels.svg"), true);
		}
	}
	
	/** extraction of equations by text style
	 * 
	 */
	@Test
	public void testDisplayStyles() {
		File svgFile = new File(SVGHTMLFixtures.FONTS_DIR, "styledequations.svg");
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		List<SVGText> svgTexts = SVGText.extractSelfAndDescendantTexts(SVGElement.readAndCreateSVG(svgElement));
		StyleRecordSet styleRecordSet = StyleRecordSet.createStyleRecordSet(svgTexts);
		SVGElement g = styleRecordSet.createStyledTextBBoxes(svgTexts);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/demos/", "equations.svg"));
	}
	
	/** extraction of equations by text style
	 * 
	 */
	@Test
	public void testDisplayPage() {
		File svgFile = new File(SVGHTMLFixtures.FONTS_DIR, "styledequations.svg");
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		List<SVGText> svgTexts = SVGText.extractSelfAndDescendantTexts(SVGElement.readAndCreateSVG(svgElement));
		StyleRecordSet styleRecordSet = StyleRecordSet.createStyleRecordSet(svgTexts);
		SVGElement g = styleRecordSet.createStyledTextBBoxes(svgTexts);
		// won't work as we don't have lines till they have gone through the Caches
		List<SVGLine> lines = SVGLine.extractSelfAndDescendantLines(svgElement);
		List<SVGLine> lines1 = SVGLine.findHorizontaLines(lines, 0.001);
		g.appendChildren(lines1);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/demos/", "equations.svg"));
	}
	
	/** extraction of equations by text style
	 * 
	 */
	@Test
	public void testDissectMainPage() {
		File svgFile = new File(SVGHTMLFixtures.FONTS_DIR, "styledequations.svg");
		File targetDir = new File("target/demos/varga/");
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		List<SVGText> svgTexts = SVGText.extractSelfAndDescendantTexts(SVGElement.readAndCreateSVG(svgElement));
		Real2Range cropBox = new Real2Range(new RealRange(13, 513), new RealRange(63, 683));
		Assert.assertEquals("raw", 351, svgTexts.size());
		List<SVGElement> workingTexts = SVGElement.extractElementsContainedInBox(svgTexts, cropBox);
		SVGSVG.wrapAndWriteAsSVG(workingTexts, new File(targetDir, "page7cropped.svg"));
		Assert.assertEquals("cropped", 339, workingTexts.size());
		// get chunks
		//this will come from clipping
		Real2Range cropBoxLeft = new Real2Range(new RealRange(13, 256), new RealRange(63, 683));
		List<SVGText> leftTexts = SVGText.extractTexts(SVGElement.extractElementsContainedInBox(svgTexts, cropBoxLeft));
		SVGSVG.wrapAndWriteAsSVG(leftTexts, new File(targetDir, "page7left.svg"));
		Assert.assertEquals("leftTexts", 98, leftTexts.size());
		StyleRecordSet leftStyleRecordSet = StyleRecordSet.createStyleRecordSet(leftTexts);
		List<StyleRecord> sortedStyleRecords = leftStyleRecordSet.createSortedStyleRecords();
		Assert.assertEquals("styleRecords", 3, sortedStyleRecords.size());
		// italics
		Assert.assertEquals("record 0", "chars: total: 25; unique: 6; coords: 4 [523.7, 534.7, 545.7, 655.6]", sortedStyleRecords.get(0).toString());
		Assert.assertEquals("record 1", "chars: total: 50; unique: 13; coords: 7 ["
				+ "278.7 x 4, 355.7 x 4, 360.9, 377.7 x 6, 382.9, 388.7 x 2, 421.7 x 4"
				+ "]", sortedStyleRecords.get(1).toString());
		Assert.assertEquals("record 2", "chars: total: 2600; unique: 50; coords: 57 [72.7, 83.7, 94.7, 105.7,"
				+ " 116.7, 127.7, 138.7, 149.7, 160.7, 171.7, 182.7, 193.7, 204.7, 215.7, 226.6, 237.6,"
				+ " 248.6, 259.6, 270.6, 281.6, 281.7 x 2, 292.7, 303.7, 314.7, 325.7, 336.7, 347.7,"
				+ " 358.7 x 4, 369.7 x 3, 380.7 x 4, 391.7 x 3, 402.7, 413.7, 424.7 x 3, 435.7, 446.7,"
				+ " 457.7, 468.7, 479.7, 490.7, 501.7, 512.7, 523.7, 534.7, 545.7 x 2, 556.7, 567.7,"
				+ " 578.7, 589.6, 600.6, 611.6, 622.6, 633.6, 644.6, 655.6 x 2, 666.6, 677.6]", sortedStyleRecords.get(2).toString());
		double eps = 0.2;
		List<RealArray> aps = sortedStyleRecords.get(0).createSortedCompressedYCoordAPList(eps);
		Assert.assertEquals("[(523.7,534.7,545.7)]",  aps.toString());
		aps = sortedStyleRecords.get(1).createSortedCompressedYCoordAPList(eps);
		Assert.assertEquals("[(355.7,360.9), (377.7,382.9)]",  aps.toString());
		aps = sortedStyleRecords.get(2).createSortedCompressedYCoordAPList(eps);
		Assert.assertEquals("["
				+ "(72.7,83.7,94.7,105.7,116.7,127.7,138.7,149.7,160.7,171.7,182.7,193.7,204.7,215.7,"
				+ "226.6,237.6,248.6,259.6,270.6,281.6,292.7,303.7,314.7,325.7,336.7,347.7,358.7,"
				+ "369.7,380.7,391.7,402.7,413.7,424.7,435.7,446.7,457.7,468.7,479.7,490.7,501.7,"
				+ "512.7,523.7,534.7,545.7,556.7,567.7,578.7,589.6,600.6,611.6,622.6,633.6,644.6,655.6,666.6,677.6)]",
				aps.toString());
		
		SVGElement g = leftStyleRecordSet.createStyledTextBBoxes(leftTexts);
		// won't work as we don't have lines till they have gone through the Caches
		List<SVGLine> lines = SVGLine.extractSelfAndDescendantLines(svgElement);
		List<SVGLine> lines1 = SVGLine.findHorizontaLines(lines, 0.001);
		g.appendChildren(lines1);
		SVGSVG.wrapAndWriteAsSVG(g, new File(targetDir, "page7leftBoxes.svg"));
	}
	
	// ============================
	
	private List<? extends SVGElement> extractAndDisplayComponents(File infile, File outfile) {
		SVGElement svgElement = SVGElement.readAndCreateSVG(infile);
		componentCache = new ComponentCache();
		componentCache.readGraphicsComponentsAndMakeCaches(svgElement);
		SVGSVG.wrapAndWriteAsSVG(componentCache.getOrCreateConvertedSVGElement(), outfile);
		List<? extends SVGElement> componentList = componentCache.getOrCreateElementList();
		return componentList;
	}
}
