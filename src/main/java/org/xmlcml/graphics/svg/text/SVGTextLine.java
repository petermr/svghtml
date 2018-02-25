package org.xmlcml.graphics.svg.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.SVGTextComparator;

/** holds a line for text, including subscripts, etc.
 * 
 * assembles SVGText with equal Y into a single line.
 * 
 * CHECK WHETHER PHRASE DOES THIS.
 * or see if Phrase should be moved into TextCache
 * @author pm286
 *
 */
public class SVGTextLine {
	private static final Logger LOG = Logger.getLogger(SVGTextLine.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private List<SVGText> lineTexts;
	private Double fontSize;
	private String fontName;

	public SVGTextLine() {
		
	}

	public SVGTextLine(List<SVGText> lineTexts) {
		this.lineTexts = new ArrayList<SVGText>(lineTexts);
		Collections.sort(lineTexts, new SVGTextComparator(SVGTextComparator.TextComparatorType.X_COORD));
		getOrCreateCommonFontSize();
		getOrCreateCommonFontName();
	}
	
	public Double getOrCreateCommonFontSize() {
		fontSize = null;
		for (SVGText lineText : lineTexts) {
			Double fs = lineText.getFontSize();
			if (fontSize == null) {
				fontSize = fs;
			} else if (fs == null) {
				fontSize = null;
			} else if (!fontSize.equals(fs)) {
				fontSize = null;
			}
			if (fontSize == null) {
				break;
			}
		}
		return fontSize;
	}

	public String getOrCreateCommonFontName() {
		fontName = null;
		for (SVGText lineText : lineTexts) {
			String fn = lineText.getSVGXFontName();
			if (fontName == null) {
				fontName = fn;
			} else if (fn == null) {
				fontName = null;
			} else if (!fontName.equals(fn)) {
				fontName = null;
			}
			if (fontName == null) {
				break;
			}
		}
		return fontName;
	}

	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(""+fontName+"; "+fontSize+"\n");
		sb.append(lineTexts.toString());
		return sb.toString();
	}
}
