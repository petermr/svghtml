package org.xmlcml.graphics.svg.fonts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.StyleBundle;

import junit.framework.Assert;

public class FontAnalyzerTest {
	private static final Logger LOG = Logger.getLogger(FontAnalyzerTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testCreateFontAnalyzersFromPage() {
		File infile = new File(SVGHTMLFixtures.TEXT_DIR, "CM_pdf2svg_BMCCancer_9_page4.svg");
		SVGElement svgElement = SVGElement.readAndCreateSVG(infile);
		FontAnalyzerSet fontAnalyzerSet = FontAnalyzerSet.createFontAnalyzerSet(svgElement);
		Assert.assertEquals("fontAnalyzers ", 
		"{font-name:YtvlrqAdvTTe45e47d2;font-size:8.0px;font-weight:normal;=chars: [,, -, . x 2, / x 5, 0 x 4, 1 x 4, 2 x 4, 4 x 3, 5 x 2, 6 x 2, 7 x 2, 9, : x 2, H, P, a x 3, b, c x 2, d, e x 4, f, g, h, i, k, l, m x 2, n, o x 3, p, r, s, t x 3, w x 4],"
		+ " font-name:CdrtkwAdvTTaf7f9f4f.B;font-size:8.0px;font-weight:bold;=chars: [1, 2],"
		+ " font-name:KchfjlAdvTT83913201.I;font-size:9.2px;font-style:italic;font-weight:normal;=chars: [C x 4, H, I, L, Q, R x 3, W, a x 4, c x 6, d x 2, e x 10, f x 3, g, i x 8, k, m, n x 8, o x 4, p, r x 6, s x 5, t x 5, u, v x 2],"
		+ " font-name:WnxtlfAdvTT99c4c969;font-size:9.2px;font-weight:normal;=chars: [I, e x 2, i, n x 3, o, r, t x 2, v],"
		+ " font-name:MsjdcxAdvTT7329fd89.I;font-size:8.0px;font-style:italic;font-weight:normal;=chars: [., B, C x 2, M, a x 2, c, e x 2, l, n, r, t],"
		+ " font-name:JwkdtrAdvTT86d47313;font-size:9.8px;font-weight:normal;=chars: [%, ( x 16, ) x 16, , x 58, - x 50, . x 26, /, 0 x 14, 1 x 8, 2 x 4, 3 x 7, 4 x 9, 5 x 5, 6 x 13, 7 x 2, 8 x 6, 9 x 5, : x 7, ; x 8, > x 4, ? x 2, A x 9, B, C x 19, D, F x 3, H x 5, I x 6, L x 3, M x 3, P x 12, Q, R x 5, S x 6, T x 15, U, W x 4, Y, [ x 12, ] x 12, a x 326, b x 39, c x 173, d x 160, e x 510, f x 66, g x 88, h x 178, i x 344, k x 30, l x 161, m x 84, n x 331, o x 283, p x 99, q x 2, r x 230, s x 264, t x 349, u x 95, v x 72, w x 53, x x 11, y x 53],"
		+ " font-name:BwrrfnAdvTT86d47313+22;font-size:9.8px;font-weight:normal;=chars: [≤ x 2],"
		+ " font-name:TrfkklAdvTT8861b38f.I;font-size:9.8px;font-style:italic;font-weight:normal;=chars: [C x 4, R x 2, W x 2, a x 13, b, c x 2, d x 10, e x 23, f x 8, g x 5, h x 11, i x 23, k, l x 9, m x 2, n x 11, o x 18, p x 7, r x 9, s x 11, t x 14, u x 8, v x 3, w, y x 8],"
		+ " font-name:WshmcrAdvTT86d47313+20;font-size:9.8px;font-weight:normal;=chars: [‘ x 7, ’ x 8, “, ”]}",		
	    fontAnalyzerSet.toString());
	}
	
	/**  gets single font analyzer by attribute.
	 *  (There is only one bold)
	 */
	@Test
	public void testGetSingleFontAnalyzerByStyle() {
		File infile = new File(SVGHTMLFixtures.TEXT_DIR, "CM_pdf2svg_BMCCancer_9_page4.svg");
		SVGElement svgElement = SVGElement.readAndCreateSVG(infile);
		FontAnalyzerSet fontAnalyzerSet = FontAnalyzerSet.createFontAnalyzerSet(svgElement);
		FontAnalyzerSet boldSet = fontAnalyzerSet.getFontAnalyzerSet(StyleBundle.FONT_WEIGHT, StyleBundle.BOLD);
		Assert.assertEquals("bold", "{font-name:CdrtkwAdvTTaf7f9f4f.B;font-size:8.0px;font-weight:bold;=chars: [1, 2]}", boldSet.toString());
	}

	@Test
	/** get all italic styles (there are 3)
	 * 
	 */
	public void testGetMultipleFontAnalyzersByStyle() {
		File infile = new File(SVGHTMLFixtures.TEXT_DIR, "CM_pdf2svg_BMCCancer_9_page4.svg");
		SVGElement svgElement = SVGElement.readAndCreateSVG(infile);
		FontAnalyzerSet fontAnalyzerSet = FontAnalyzerSet.createFontAnalyzerSet(svgElement);
		FontAnalyzerSet italicSet = fontAnalyzerSet.getFontAnalyzerSet(StyleBundle.FONT_STYLE, StyleBundle.ITALIC);
		Assert.assertEquals("italic", 
				"{font-name:KchfjlAdvTT83913201.I;font-size:9.2px;font-style:italic;font-weight:normal;=chars: [C x 4, H, I, L, Q, R x 3, W, a x 4, c x 6, d x 2, e x 10, f x 3, g, i x 8, k, m, n x 8, o x 4, p, r x 6, s x 5, t x 5, u, v x 2],"
				+ " font-name:MsjdcxAdvTT7329fd89.I;font-size:8.0px;font-style:italic;font-weight:normal;=chars: [., B, C x 2, M, a x 2, c, e x 2, l, n, r, t],"
				+ " font-name:TrfkklAdvTT8861b38f.I;font-size:9.8px;font-style:italic;font-weight:normal;=chars: [C x 4, R x 2, W x 2, a x 13, b, c x 2, d x 10, e x 23, f x 8, g x 5, h x 11, i x 23, k, l x 9, m x 2, n x 11, o x 18, p x 7, r x 9, s x 11, t x 14, u x 8, v x 3, w, y x 8]}",
			italicSet.toString());
	}
	
	/** from many pages
	 * 
	 */
	@Test
	public void testCreateFontAnalyzersFromDocument() {
		// just testing unicode
		char c = 0x0020;
		char cc = '\u0020';
		Assert.assertEquals(c, cc);
		String s = "U+0020";
		Assert.assertEquals(6,  s.length());
		String ss = "\u0020";
		Assert.assertEquals(1,  ss.length());
		
		File indir = new File(
		"src/test/resources/org/xmlcml/graphics/svg/bar/BakkerVanDijkWicherts2012/svg/");
		Assert.assertTrue("indir exists "+indir, indir.exists());
		File[] files = indir.listFiles();
		List<File> svgFiles = new ArrayList<File>();
		for (File file : files) {
			if (file.toString().endsWith(".svg")) {
				svgFiles.add(file);
			}
		}
		List<SVGText> svgTexts = SVGText.readAndCreateTexts(svgFiles);
		FontAnalyzerSet fontAnalyzerSet = FontAnalyzerSet.createFontAnalyzerSet(svgTexts);
		Assert.assertEquals("font analyzer set", 57, fontAnalyzerSet.size());
		// omit because of whitespace characters
/**
 		Assert.assertEquals("fontAnalyzers ", 

				"{font-name:Helvetica;font-size:6.831px;font-weight:normal;=chars: [  x 39, ( x 7, ) x 7, , x 15, . x 38, 0 x 45, 1 x 28, 2 x 9, 3 x 11, 4 x 9, 5 x 9, 6 x 5, 7 x 11, 8 x 10, 9 x 15, < x 8, = x 30, C x 7, E x 16, I x 7, S x 8, Z x 8, h x 7, i x 7, p x 15, s x 8, t x 8, − x 7],"
				+ " font-name:TimesNewRomanPSMT;font-size:6.5px;font-weight:normal;=chars: [0 x 4, 2 x 3, A x 4],"
				+ " font-name:GillSans-Bold;font-size:5.85px;font-weight:bold;=chars: [2],"
				+ " font-name:HelveticaNeue-Condensed;font-size:7.204px;font-weight:normal;=chars: [  x 5, ( x 2, ) x 2, , x 3, . x 7, 0 x 9, 1 x 4, 2 x 4, 3 x 3, 4, 5, 7 x 2, 8, 9 x 2, <, = x 7, B, C, E x 2, I x 2, Q, S, Z, ^, a, h, i x 2, p x 3, s x 2, t, −],"
				+ " font-name:HelveticaNeue-Condensed;font-size:6.834px;font-weight:normal;=chars: [  x 41, ( x 14, ) x 14, , x 21, . x 52, 0 x 53, 1 x 36, 2 x 26, 3 x 11, 4 x 24, 5 x 11, 6 x 19, 7 x 11, 8 x 12, 9 x 10, < x 2, = x 54, B x 7, C x 7, E x 14, I x 14, Q x 7, S x 7, Z x 7, ^ x 7, a x 7, h x 7, i x 14, p x 21, s x 14, t x 7, − x 9],"
				+ " font-name:HelveticaNeue-Condensed;font-size:6.401px;font-weight:normal;=chars: [  x 5, ( x 2, ) x 2, , x 3, . x 8, 0 x 7, 1 x 5, 2 x 4, 3 x 4, 4 x 3, 5 x 3, 6 x 2, 8, 9 x 2, = x 8, B, C, E x 2, I x 2, Q, S, Z, ^, a, h, i x 2, p x 3, s x 2, t, −],"
				+ " font-name:GillSans;font-size:10.0px;font-weight:normal;=chars: [  x 196, %, ( x 2, ) x 2, , x 7, - x 2, . x 8, /, 0 x 2, 1 x 3, 2 x 2, 3 x 2, 4 x 8, 5 x 18, 6 x 2, 7, 8 x 2, 9 x 2, <, I, P, S, T, W x 3, a x 85, b x 12, c x 42, d x 28, e x 130, f x 40, g x 23, h x 40, i x 94, j, k, l x 53, m x 17, n x 66, o x 64, p x 31, q, r x 66, s x 96, t x 86, u x 31, v x 13, w x 11, x x 3, y x 17, z],"
				+ " font-name:GillSans;font-size:5.85px;font-weight:normal;=chars: [2],"
				+ " font-name:Helvetica-BoldOblique;font-size:9.0px;font-weight:bold;=chars: [  x 3, P x 2, S, a, c x 5, e x 5, g, h, i x 3, l x 2, n x 2, o x 3, p, r, s x 3, t, v, y],"
				+ " font-name:HelveticaNeue-BoldCondObl;font-size:10.0px;font-style:italic;font-weight:bold;=chars: [N x 6],"
				+ " font-name:GillSans-Italic;font-size:8.0px;font-style:italic;font-weight:normal;=chars: [I x 2, N x 5, Q, Z, d x 3, k],"
				+ " font-name:GillSans-Italic;font-size:9.0px;font-style:italic;font-weight:normal;=chars: [(, ), E, N x 2, Q, S, c, d x 2, e, i, k, n x 2, o, p x 3, t, u],"
				+ " font-name:TimesNewRomanPS-ItalicMT;font-size:9.0px;font-style:italic;font-weight:normal;=chars: [  x 166, & x 5, ,, - x 18, 0 x 4, 1 x 18, 2 x 9, 3 x 11, 4 x 14, 5 x 11, 6 x 12, 7 x 12, 8 x 7, 9 x 9, : x 5, A x 11, B x 11, C x 9, D x 4, E x 7, F x 3, G x 3, I x 2, J x 13, L x 3, M x 9, N x 3, O x 3, P x 61, R x 12, S x 21, T x 5, a x 112, b x 4, c x 137, d x 26, e x 152, f x 22, g x 54, h x 69, i x 153, j x 2, k, l x 130, m x 33, n x 113, o x 176, p x 21, r x 68, s x 117, t x 72, u x 37, v x 23, w x 9, x x 2, y x 73, z],"
				+ " font-name:GillSans;font-size:8.0px;font-weight:normal;=chars: [  x 384, %, ( x 19, ) x 19, *, , x 20, - x 8, . x 26, 0 x 21, 1 x 15, 2 x 9, 3, 4 x 3, 5 x 8, 7 x 2, 8 x 3, 9 x 4, :, ; x 10, = x 16, @, A x 7, B x 5, C x 6, D x 3, E x 11, F x 4, G, H x 6, I x 7, M x 5, N x 4, O, P x 4, Q, R x 3, S x 10, T x 7, U x 4, W, X, Z x 2, a x 119, b x 20, c x 37, d x 61, e x 235, f x 55, g x 36, h x 58, i x 117, j x 4, k x 7, l x 69, m x 44, n x 133, o x 106, p x 34, q, r x 119, s x 147, t x 161, u x 46, v x 9, w x 16, x x 8, y x 21, z x 7, ’ x 8],"
				+ " font-name:HelveticaNeue-Condensed;font-size:18.0px;font-weight:normal;=chars: [1, 2, 3, 4, A, B, C, D],"
				+ " font-name:HelveticaNeue-BoldCond;font-size:10.0px;font-weight:bold;=chars: [  x 51, 0 x 6, 1 x 5, 2 x 2, 4 x 2, ; x 3, = x 6, B x 3, C x 3, E x 3, G x 3, L x 3, R x 3, S x 12, a x 21, c x 6, e x 12, f x 6, g x 6, h x 3, i x 15, l x 15, m x 6, n x 12, o x 3, s x 9, t x 18, u x 3],"
				+ " font-name:HelveticaNeue-Condensed;font-size:6.531px;font-weight:normal;=chars: [  x 5, ( x 2, ) x 2, , x 3, . x 8, 0 x 10, 1 x 4, 2 x 5, 3 x 2, 5 x 2, 6 x 5, 7 x 3, <, = x 7, B, C, E x 2, I x 2, Q, S, Z, ^, a, h, i x 2, p x 3, s x 2, t, − x 2],"
				+ " font-name:HelveticaNeue-Condensed;font-size:5.83px;font-weight:normal;=chars: [0, A],"
				+ " font-name:HelveticaNeue-Condensed;font-size:7.0px;font-weight:normal;=chars: [  x 72, 1 x 12, 5 x 12, L x 12, P x 12, Q x 12, R x 12, S x 36, a x 24, d x 24, e x 24, g x 12, h x 12, i x 24, l x 24, m x 12, r x 12, s x 24, t x 36, u x 24, w x 12, y x 12],"
				+ " font-name:Symbol;font-size:9.0px;font-weight:normal;=chars: ["
				+ " x 2,   x 12, +, 1, 4, 8, 9, : x 5, ; x 4, B, D, F, N, P, S x 2, T, U, a x 3, b x 2, c x 2, e x 2, f x 2, h, i x 2, l x 4, m x 3, n x 4, o x 3, r x 2, t, u, y x 3, χ, − x 4, ❾],"
+ " font-name:GillSans-Bold;font-size:18.0px;font-weight:bold;=chars: [  x 7, C, G, P, R, S, T, a x 3, c x 4, d, e x 7, f, g, h x 3, i x 2, l x 5, m, n, o x 3, s x 2, t, u, y],"
+ " font-name:TimesNewRomanPSMT;font-size:9.0px;font-weight:normal;=chars: [  x 1546, & x 40, ( x 77, ) x 77, * x 11, , x 386, - x 65, . x 546, / x 41, 0 x 238, 1 x 290, 2 x 139, 3 x 110, 4 x 69, 5 x 62, 6 x 73, 7 x 99, 8 x 67, 9 x 94, : x 71, <, ? x 6, A x 54, B x 37, C x 35, D x 27, E x 27, F x 15, G x 20, H x 19, I x 17, J x 47, K x 20, L x 26, M x 50, N x 18, O x 5, P x 30, Q x 2, R x 39, S x 33, T x 32, U x 5, V x 5, W x 29, X, Y x 3, Z, a x 515, b x 70, c x 246, d x 233, e x 684, f x 141, g x 115, h x 199, i x 557, j x 11, k x 33, l x 280, m x 135, n x 483, o x 473, p x 122, q x 4, r x 377, s x 450, t x 422, u x 122, v x 67, w x 50, x x 15, y x 124, z x 19, – x 59, — x 2, “ x 2, ” x 2],"
+ " font-name:Helvetica;font-size:5.0px;font-weight:bold;=chars: [  x 143, , x 13, . x 26, 0 x 13, 1 x 13, 2 x 26, 8 x 13, A x 26, D x 13, E x 13, G x 13, N x 13, S x 13, U x 13, a x 65, b x 26, c x 13, d x 39, e x 91, f x 13, g x 13, i x 39, l x 13, m x 65, n x 52, o x 78, p x 39, r x 52, s x 52, t x 52, u x 13, v x 39, w x 13],"
+ " font-name:HelveticaNeue-Condensed;font-size:6.831px;font-weight:normal;=chars: [  x 47, ( x 7, ) x 7, , x 14, . x 35, 0 x 41, 1 x 26, 2 x 15, 3 x 5, 4 x 12, 5 x 9, 6 x 5, 7 x 9, 8 x 10, 9 x 9, < x 4, = x 31, C x 7, E x 14, I x 7, S x 7, Z x 7, h x 7, i x 7, p x 14, s x 7, t x 7, − x 9],"
+ " font-name:GillSans-Bold;font-size:10.0px;font-weight:bold;=chars: [  x 3, A, C, D, F, I, K, R, a x 3, b, c x 4, d x 2, e x 8, f x 3, g x 2, i x 4, l x 2, n x 7, o x 4, r x 5, s x 5, t x 6, u, w, y],"
+ " font-name:GillSans-Italic;font-size:10.0px;font-style:italic;font-weight:normal;=chars: [  x 28, . x 4, B x 4, G x 4, R x 4, T x 4, a x 12, e x 24, f x 4, h x 8, k x 8, l x 8, m x 4, o x 4, p, r x 4, s x 4, t x 8, u x 4],"
+ " font-name:HelveticaNeue-Condensed;font-size:0.0px;font-weight:normal;=chars: [  x 48, 0 x 8, 1 x 24, 2 x 24, 3 x 8, 4 x 16, 5 x 8, 6 x 16, 7 x 8, 8 x 8, E x 16, I x 16, S x 16, a x 32, d x 32, e x 32, f x 16, n x 32, o x 32, r x 80, s x 16, t x 16, v x 16],"
+ " font-name:HelveticaNeue-Condensed;font-size:6.316px;font-weight:normal;=chars: [  x 5, ( x 2, ) x 2, , x 3, . x 7, 0 x 9, 1 x 4, 2 x 2, 3, 4 x 3, 5 x 3, 6 x 2, 7 x 3, 9 x 3, <, = x 7, B, C, E x 2, I x 2, Q, S, Z, ^, a, h, i x 2, p x 3, s x 2, t, − x 2],"
+ " font-name:HelveticaNeue-Condensed;font-size:9.0px;font-weight:normal;=chars: [. x 186, 0 x 219, 1 x 115, 2 x 58, 3 x 10, 4 x 27, 5 x 65, 6 x 21, 7 x 2, 8 x 20, 9, < x 2, >, p x 3, – x 25, − x 41],"
+ " font-name:GillSans;font-size:9.0px;font-weight:normal;=chars: [  x 114, & x 6, ( x 70, ) x 71, , x 8, - x 5, . x 128, 0 x 108, 1 x 102, 2 x 71, 3 x 42, 4 x 48, 5 x 38, 6 x 44, 7 x 34, 8 x 32, 9 x 33, :, < x 5, A x 9, B x 4, C x 4, D, E x 6, F x 3, G x 3, H x 4, I x 2, L, M x 6, O, P x 3, R x 7, S x 9, T x 3, U, W x 2, a x 35, b x 5, c x 13, d x 13, e x 57, f x 9, g x 8, h x 7, i x 30, j, l x 35, m x 8, n x 33, o x 25, p x 3, r x 23, s x 34, t x 27, u x 9, v x 3, w, x x 3, y x 7, z x 2],"
+ " font-name:Helvetica;font-size:10.0px;font-weight:bold;=chars: [  x 3, . x 2, / x 2, :, A, F, P, S, a x 4, b, c x 7, e x 3, g x 2, h x 2, i x 6, l x 3, m, n x 3, o x 7, p x 2, r, s x 5, t x 4, u, w x 3, y],"
+ " font-name:Helvetica-Bold;font-size:12.0px;font-weight:bold;=chars: [  x 7, C, G, P, R, S, T, a x 3, c x 4, d, e x 7, f, g, h x 3, i x 2, l x 5, m, n, o x 3, s x 2, t, u, y],"
+ " font-name:GillSans-Bold;font-size:12.0px;font-weight:bold;=chars: [  x 20, ! x 2, +, , x 2, ., 0, 2, 5, A x 5, B, D x 2, G x 3, I, J, L, M x 2, P, R x 2, S x 2, W, a x 12, c x 5, d, e x 18, g x 3, h x 5, i x 10, j x 2, k x 4, l x 6, m x 3, n x 11, o x 7, p x 3, r x 9, s x 5, t x 13, u x 3, v x 2, y x 3, z],"
+ " font-name:Helvetica;font-size:36.0px;font-weight:bold;=chars: [  x 2, P x 2, S, a, c x 5, e x 5, g, h, i x 3, l x 2, n x 2, o x 3, p, r, s x 3, t, v, y],"
+ " font-name:GillSans-Bold;font-size:9.0px;font-weight:bold;=chars: [  x 9, ( x 8, ) x 7, . x 17, 0 x 21, 1 x 4, 2 x 9, 3 x 2, 4 x 7, 5 x 3, 6, 7, 8, I, T, a, b, e, l],"
+ " font-name:Helvetica-Oblique;font-size:11.0px;font-weight:bold;=chars: [  x 3, P x 2, S, a, c x 5, e x 5, g, h, i x 3, l x 2, n x 2, o x 3, p, r, s x 3, t, v, y],"
+ " font-name:Helvetica-Bold;font-size:9.0px;font-weight:bold;=chars: [  x 17, : x 5, A x 2, E, P, R, S, a x 6, b x 2, c x 3, d x 4, e x 6, f x 3, i x 11, l x 3, m x 3, n x 9, o x 7, p x 2, r x 7, s x 9, t x 6, u x 2, v],"
+ " font-name:HelveticaNeue-Condensed;font-size:6.366px;font-weight:normal;=chars: [  x 5, ( x 2, ) x 2, , x 3, . x 7, 0 x 8, 1 x 4, 2 x 4, 3, 4 x 3, 6 x 2, 7, 8 x 4, 9 x 2, = x 8, B, C, E x 2, I x 2, Q, S, Z, ^, a, h, i x 2, p x 3, s x 2, t, −],"
+ " font-name:Helvetica;font-size:11.0px;font-weight:bold;=chars: [  x 42, ,, . x 4, / x 7, 0 x 4, 1 x 7, 2 x 3, 3 x 2, 4 x 4, 5 x 4, 6 x 4, 7 x 5, 9 x 2, : x 6, A, B, D x 2, I, J, M x 2, O x 2, P, T, W, a x 10, b x 5, c x 5, d x 3, e x 15, f x 4, g, h x 6, i x 7, j x 2, k x 3, l x 5, m, n x 13, o x 7, p x 4, r x 5, s x 6, t x 11, u x 3, v x 2, y],"
+ " font-name:GillSans-Bold;font-size:8.0px;font-weight:bold;=chars: [  x 7, (, ), . x 9, 1, 2, 3, 4 x 2, :, A, C, F x 5, c, d x 2, e x 2, g x 6, h, i x 7, n x 4, o x 4, p, r x 3, s, t x 2, u x 2],"
+ " font-name:TimesNewRomanPSMT;font-size:10.0px;font-weight:normal;=chars: [  x 3550, % x 17, & x 27, ( x 137, ) x 137, *, , x 239, - x 110, . x 221, / x 18, 0 x 150, 1 x 131, 2 x 99, 3 x 25, 4 x 32, 5 x 47, 6 x 18, 7 x 25, 8 x 27, 9 x 56, : x 8, ; x 37, < x 3, = x 27, >, ?, A x 25, B x 17, C x 15, D x 4, E x 34, F x 28, G x 3, H x 23, I x 40, J x 6, K x 6, L x 8, M x 13, N x 14, O x 6, P x 32, Q x 16, R x 24, S x 82, T x 59, U x 3, V, W x 25, Y, Z x 2, ^, a x 1380, b x 219, c x 525, d x 661, e x 2200, f x 407, g x 315, h x 660, i x 1263, j x 16, k x 45, l x 831, m x 373, n x 1128, o x 1058, p x 397, q x 13, r x 973, s x 1389, t x 1453, u x 473, v x 136, w x 206, x x 37, y x 325, z x 40, | x 2, – x 3, —, ’ x 12, “ x 9, ” x 9],"
+ " font-name:Helvetica;font-size:12.0px;font-weight:bold;=chars: [. x 2, / x 3, :, a, b, c, e, g, h, m, o, p x 4, s x 2, t x 2, u],"
+ " font-name:Helvetica;font-size:9.0px;font-weight:bold;=chars: [. x 10, / x 13, : x 4, P, R, a x 9, b x 5, c x 6, e x 7, g x 5, h x 4, i x 6, j x 2, l x 3, m x 5, n x 7, o x 8, p x 14, r x 6, s x 16, t x 11, u x 7, v x 2, w x 6],"
+ " font-name:GillSans;font-size:7.0px;font-weight:normal;=chars: [  x 13, ( x 2, ) x 2, . x 5, / x 4, 0 x 4, 1 x 7, 2 x 3, 3, 4 x 4, 5 x 5, 6 x 4, 7 x 4, 9 x 2, : x 3, A, D, I, O, P x 3, R, S, T, a x 6, b x 2, c x 7, d, e x 11, g x 3, h x 4, i x 8, j, l x 3, m x 4, n x 8, o x 9, p x 8, r x 6, s x 14, t x 5, u x 4, v x 2, y, ©, –],"
+ " font-name:Symbol;font-size:10.0px;font-weight:normal;=chars: ["
+" x 4,   x 24, + x 2, 1 x 2, 4 x 2, 8 x 2, 9 x 2, : x 10, ; x 8, B x 2, D x 2, F x 2, N x 2, P x 2, S x 4, T x 2, U x 2, a x 6, b x 4, c x 4, e x 4, f x 4, h x 2, i x 4, l x 8, m x 6, n x 8, o x 6, r x 4, t x 2, u x 2, y x 6, α x 4, χ x 2, − x 2, ❾ x 2],"
+ " font-name:HelveticaNeue-Condensed;font-size:10.0px;font-weight:normal;=chars: [  x 109, (, ), 0 x 2, 3, 5 x 2, = x 2, B x 3, C x 8, D x 23, E x 19, F x 8, H x 2, I x 14, M x 21, N x 3, P x 4, R, S x 41, T x 8, U x 2, a x 109, b x 2, c x 21, d x 100, e x 160, f x 66, h x 8, i x 71, l x 9, m x 9, n x 112, o x 67, p x 4, q, r x 161, s x 50, t x 50, u x 3, v x 13, w, z x 13, –],"
+ " font-name:GillSans-Italic;font-size:5.2px;font-style:italic;font-weight:normal;=chars: [r],"
+ " font-name:Symbol;font-size:8.0px;font-weight:normal;=chars: ["
+" x 10,   x 60, + x 5, 1 x 5, 4 x 5, 8 x 5, 9 x 5, : x 25, ; x 20, B x 5, D x 5, F x 5, N x 5, P x 5, S x 10, T x 5, U x 5, a x 15, b x 10, c x 10, e x 10, f x 10, h x 5, i x 10, l x 20, m x 15, n x 20, o x 15, r x 10, t x 5, u x 5, y x 15, α, χ, ❾ x 5],"
+ " font-name:TimesNewRomanPS-ItalicMT;font-size:10.0px;font-style:italic;font-weight:normal;=chars: [  x 26, &, , x 3, - x 3, : x 2, A x 2, B, C, D x 3, E x 8, F x 2, H, I, J x 4, L, M, N x 7, P x 8, Q, R, S x 6, Z x 2, a x 14, b, c x 10, d x 10, e x 17, f x 6, g x 8, h x 6, i x 13, l x 17, m x 8, n x 21, o x 27, p x 18, r x 13, s x 7, t x 8, u x 6, v x 2, w, x x 2, y x 12],"
+ " font-name:GillSans;font-size:5.2px;font-weight:normal;=chars: [0 x 2, 1, 2 x 4, A],"
+ " font-name:MTMI;font-size:10.0px;font-style:italic;font-weight:normal;=chars: [/],"
+ " font-name:Helvetica;font-size:6.7px;font-weight:normal;=chars: [ , (, ), ,, . x 2, 0, 1 x 4, 2, 3, 4, 6, = x 2, C, I, h, i, p, −],"
+ " font-name:HelveticaNeue-Condensed;font-size:6.906px;font-weight:normal;=chars: [  x 5, (, ), , x 2, . x 5, 0 x 8, 1 x 3, 2, 3, 4 x 2, 5 x 3, 9 x 2, <, = x 4, C, E x 2, I, S, Z, h, i, p x 2, s, t, −],"
+ " font-name:HelveticaNeue-BoldCond;font-size:12.0px;font-weight:bold;=chars: [  x 128, & x 4, . x 18, 0 x 29, 1 x 30, 2 x 16, 5 x 12, 8 x 4, 9, ; x 16, = x 16, A, B x 3, C, D, E x 16, F, G x 2, H, L x 9, M, P x 8, Q x 8, R x 10, S x 40, T x 16, W x 10, a x 27, b, c x 2, d x 21, e x 50, f x 2, g x 8, h x 9, i x 23, l x 30, m x 8, n x 8, o x 10, r x 32, s x 22, t x 30, u x 35, w, y x 10],"
+ " font-name:GillSans-Bold;font-size:7.8px;font-weight:bold;=chars: [1 x 2, 2],"
+ " font-name:MTSY;font-size:10.0px;font-weight:normal;=chars: ["
+" x 2,   x 12, +, 4 x 2, 5, 6, 7, : x 5, ; x 4, B, D, F, M x 2, N, P, S x 2, T x 3, U, Y x 2, a x 3, c x 2, e x 2, f x 2, h, i x 2, l x 2, m, n x 4, o, r x 2, t, u, y, ❾],"
+ " font-name:TimesNewRomanPS-ItalicMT;font-size:6.5px;font-style:italic;font-weight:normal;=chars: [r]}",
	    fontAnalyzerSet.toString());
	    */
	}
	

}
