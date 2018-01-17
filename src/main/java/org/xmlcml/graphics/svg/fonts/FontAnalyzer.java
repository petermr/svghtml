package org.xmlcml.graphics.svg.fonts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Util;
import org.xmlcml.euclid.util.MultisetUtil;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.StyleAttributeFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/** represents data from font.
 * Family size, styles, etc.  
 * 
 * may overlap with earlier work.
 * See also pdf2svg.AMIFont which is primarily in PDF context
 * Experimental
 * 
 * @author pm286
 *
 */
public class FontAnalyzer {
	private static final Logger LOG = Logger.getLogger(FontAnalyzer.class);

	static {
		LOG.setLevel(Level.DEBUG);
	}

	private Multiset<String> characterSet;
	private List<SVGText> textList;
	private String style;

	public FontAnalyzer(String style) {
		this.style= style;
		this.characterSet = HashMultiset.create();
	}

	/** gets style attributes related to font.
	 * currently name, size, style, weight
	 * maybe should add fill and stroke later
	 * 
	 * @param text
	 * @return
	 */
	public static String getCSSStyle(SVGText text) {
		String style = null;
		if (text != null) {
			StyleAttributeFactory attributeFactory = new StyleAttributeFactory();
			attributeFactory.addFontName(text);
			attributeFactory.addFontSize(text);
			attributeFactory.addFontStyle(text);
			attributeFactory.addFontWeight(text);
			style = attributeFactory.getAttributeValue();
		}
		return style;
	}

	public String toString() {
		List<Multiset.Entry<String>> entries = MultisetUtil.createStringListSortedByValue(characterSet);
		return "chars: "+entries.toString();
	}

	public void addText(SVGText text) {
		if (text != null) {
			getOrCreateTextList();
			addValue(text.getValue());
			textList.add(text);
		}
	}
	
	private List<SVGText> getOrCreateTextList() {
		if (textList == null) {
			textList = new ArrayList<SVGText>();
		}
		return textList;
	}

	public void addValue(String value) {
		if (value != null) {
			for (int i = 0; i < value.length(); i++) {
				char ch = value.charAt(i);
				String s = String.valueOf(ch);
				if (Character.isWhitespace(ch)) {
					s = Util.createUnicodeString(ch);
//					throw new RuntimeException("C:"+(int)ch);
				}
				this.characterSet.add(String.valueOf(ch));
			}
		}
	}
}
