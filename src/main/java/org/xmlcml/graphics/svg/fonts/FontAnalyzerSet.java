package org.xmlcml.graphics.svg.fonts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.StyleAttributeFactory;

/** collection of fonts, normally created from page/s in document.
 * 
 * @author pm286
 *
 */
public class FontAnalyzerSet {
	private static final Logger LOG = Logger.getLogger(FontAnalyzerSet.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private Map<String, FontAnalyzer> fontAnalyzerByStyle;

	public FontAnalyzerSet() {
		fontAnalyzerByStyle = new HashMap<String, FontAnalyzer>();
	}
	
	public static FontAnalyzerSet createFontAnalyzerSet(List<SVGText> texts) {
		FontAnalyzerSet fontAnalyzerSet = null;
		if (texts != null) {
			fontAnalyzerSet = new FontAnalyzerSet();
			for (SVGText text : texts) {
				fontAnalyzerSet.getAddFontAndCharacterCount(text);
			}
		}
		return fontAnalyzerSet;
		
	}

	/** creates a style from the text.
	 * searches for FontAnalyzer with that style. If noe, creates one
	 * increments the count for character/s in text
	 * 
	 * @param text
	 * @return
	 */
	public FontAnalyzer getAddFontAndCharacterCount(SVGText text) {
		FontAnalyzer fontAnalyzer = null;
		if (text != null) {
			String style = FontAnalyzer.getCSSStyle(text);
			String value = text.getValue();
			if (style != null) {
				fontAnalyzer = fontAnalyzerByStyle.get(style);
				if (fontAnalyzer == null) {
					fontAnalyzer = new FontAnalyzer(style);
					fontAnalyzerByStyle.put(style, fontAnalyzer);
				}
				fontAnalyzer.addValue(value);
			}
		}
		return fontAnalyzer;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<String> styles = new ArrayList<String>(fontAnalyzerByStyle.keySet());
		Collections.sort(styles);
		for (String style : styles) {
			FontAnalyzer fontAnalyzer = fontAnalyzerByStyle.get(style);
			sb.append(fontAnalyzer.toString()+"\n");
		}
		return fontAnalyzerByStyle.toString();
	}

	public static FontAnalyzerSet createFontAnalyzerSet(File svgFile) {
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		return createFontAnalyzerSet(svgElement);
	}

	public static FontAnalyzerSet createFontAnalyzerSet(SVGElement svgElement) {
		List<SVGText> texts = SVGText.extractSelfAndDescendantTexts(svgElement);
		FontAnalyzerSet fontAnalyzerSet = FontAnalyzerSet.createFontAnalyzerSet(texts);
		return fontAnalyzerSet;
	}

	/** gets all FontAnalyzers with given attribute.
	 * 
	 * @param attName
	 * @param attValue
	 * @return
	 */
	public FontAnalyzerSet getFontAnalyzerSet(String attName, String attValue) {
		FontAnalyzerSet fontAnalyzerSet = new FontAnalyzerSet();
		if (attName != null && attValue != null) {
			for (String style : fontAnalyzerByStyle.keySet()) {
				StyleAttributeFactory attributeFactory = new StyleAttributeFactory(style);
				String value = attributeFactory.getAttributeValue(attName);
				if (attValue.equals(value)) {
					FontAnalyzer fontAnalyzer = fontAnalyzerByStyle.get(style);
					fontAnalyzerSet.add(style, fontAnalyzer);
				}
			}
		}
		return fontAnalyzerSet;
	}

	private void add(String style, FontAnalyzer fontAnalyzer) {
		fontAnalyzerByStyle.put(style, fontAnalyzer);
	}

	public int size() {
		return fontAnalyzerByStyle == null ? 0 : fontAnalyzerByStyle.size();
	}
}
