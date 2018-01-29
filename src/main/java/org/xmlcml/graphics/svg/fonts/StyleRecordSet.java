package org.xmlcml.graphics.svg.fonts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Util;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.StyleAttributeFactory;
import org.xmlcml.graphics.svg.StyleBundle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/** collection of fonts, normally created from page/s in document.
 * 
 * @author pm286
 *
 */
public class StyleRecordSet {
	private static final Logger LOG = Logger.getLogger(StyleRecordSet.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private Map<String, StyleRecord> styleRecordByCSSStyle;
	private Multimap<String, StyleRecord> styleRecordByFontName;
	private Multimap<Double, StyleRecord> styleRecordByFontSize;
	private Multimap<String, StyleRecord> styleRecordByFontWeight;
	private Multimap<String, StyleRecord> styleRecordByFontStyle;
	private Multimap<String, StyleRecord> styleRecordByFill;
	private Multimap<String, StyleRecord> styleRecordByStroke;
	private Multimap<Double, StyleRecord> styleRecordByYCoord;
	private TypefaceMaps typefaceMaps;
	private Multiset<String> fontNameSet;

	public StyleRecordSet() {
		styleRecordByCSSStyle =   new HashMap<String, StyleRecord>();
		styleRecordByFontName =   ArrayListMultimap.create();
		styleRecordByFontSize =   ArrayListMultimap.create();
		styleRecordByFontWeight = ArrayListMultimap.create();
		styleRecordByFontStyle =  ArrayListMultimap.create();
		styleRecordByFill =       ArrayListMultimap.create();
		styleRecordByStroke =     ArrayListMultimap.create();
		styleRecordByYCoord =     ArrayListMultimap.create();
		typefaceMaps = new TypefaceMaps();

	}
	
	public static StyleRecordSet createStyleRecordSet(List<SVGText> texts) {
		StyleRecordSet styleRecordSet = null;
		if (texts != null) {
			styleRecordSet = new StyleRecordSet();
			for (SVGText text : texts) {
				styleRecordSet.getOrCreateStyleRecord(text);
			}
		}
		return styleRecordSet;
		
	}

	/** creates a style from the text.
	 * searches for StyleRecord with that style. If none, creates one
	 * increments the count for character/s in text
	 * 
	 * @param text
	 * @return
	 */
	public StyleRecord getOrCreateStyleRecord(SVGText text) {
		StyleRecord styleRecord = null;
		if (text != null) {
//			LOG.debug(text.toXML());
			String cssStyle = StyleRecord.getCSSStyle(text);
			String value = text.getValue();
			value = value.replaceAll("\\s+", "");
			if (cssStyle != null) {
				styleRecord = styleRecordByCSSStyle.get(cssStyle);
				if (styleRecord == null) {
					styleRecord = new StyleRecord(cssStyle);
					styleRecordByCSSStyle.put(cssStyle, styleRecord);
					AddToOtherAnalyzers(cssStyle, styleRecord);
				}
				Double yCoordinate = createYCoordinate(text.getY());
				styleRecord.addYCoordinate(yCoordinate);
				styleRecordByYCoord.put(yCoordinate, styleRecord);
				styleRecord.addNonWhitespaceCharacters(value);
			}
		}
		return styleRecord;
	}
	
	private Double createYCoordinate(Double y) {
		return y == null ? null : Util.format(y, 1);
	}

	private void AddToOtherAnalyzers(String cssStyle, StyleRecord styleRecord) {
		StyleBundle styleBundle = new StyleBundle(cssStyle);
		addStyleRecordByAttribute(styleRecordByFill,       styleBundle.getFill(),       styleRecord);
		addStyleRecordByAttribute(styleRecordByStroke,     styleBundle.getStroke(),     styleRecord);
		addStyleRecordByAttribute(styleRecordByFontSize,   (Double) styleBundle.getFontSize(),   styleRecord);
		addStyleRecordByAttribute(styleRecordByFontWeight, styleBundle.getFontWeight(), styleRecord);
		addStyleRecordByAttribute(styleRecordByFontStyle,  styleBundle.getFontStyle(),  styleRecord);
		addStyleRecordByAttribute(styleRecordByFontName,   styleBundle.getFontName(),   styleRecord);
		addStyleRecordByAttribute(styleRecordByFill,       styleBundle.getFill(),       styleRecord);
	}

	private void addStyleRecordByAttribute(Multimap<String, StyleRecord> styleRecordByAttribute, 
			String attributeValue, StyleRecord styleRecord) {
		if (attributeValue != null) {
			styleRecordByAttribute.put(attributeValue, styleRecord);
		}
	}

	private void addStyleRecordByAttribute(Multimap<Double, StyleRecord> styleRecordByAttribute, 
			Double attributeValue, StyleRecord styleRecord) {
		if (attributeValue != null) {
			styleRecordByAttribute.put(attributeValue, styleRecord);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<String> styles = new ArrayList<String>(styleRecordByCSSStyle.keySet());
		Collections.sort(styles);
		for (String style : styles) {
			StyleRecord styleRecord = styleRecordByCSSStyle.get(style);
			sb.append(styleRecord.toString()+"\n");
		}
		return styleRecordByCSSStyle.toString();
	}

	public static StyleRecordSet createStyleRecordSet(File svgFile) {
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		return createStyleRecordSet(svgElement);
	}

	public static StyleRecordSet createStyleRecordSet(SVGElement svgElement) {
		List<SVGText> texts = SVGText.extractSelfAndDescendantTexts(svgElement);
		StyleRecordSet styleRecordSet = StyleRecordSet.createStyleRecordSet(texts);
		return styleRecordSet;
	}

	/** gets all StyleRecords with given attribute.
	 * 
	 * @param attName
	 * @param attValue
	 * @return
	 */
	public StyleRecordSet getStyleRecordSet(String attName, String attValue) {
		StyleRecordSet styleRecordSet = new StyleRecordSet();
		if (attName != null && attValue != null) {
			for (String cssStyle : styleRecordByCSSStyle.keySet()) {
				StyleAttributeFactory attributeFactory = new StyleAttributeFactory(cssStyle);
				String value = attributeFactory.getAttributeValue(attName);
				if (attValue.equals(value)) {
					StyleRecord styleRecord = styleRecordByCSSStyle.get(cssStyle);
					styleRecordSet.add(cssStyle, styleRecord);
				}
			}
		}
		return styleRecordSet;
	}

	private void add(String cssStyle, StyleRecord styleRecord) {
		styleRecordByCSSStyle.put(cssStyle, styleRecord);
	}

	public int size() {
		return styleRecordByCSSStyle == null ? 0 : styleRecordByCSSStyle.size();
	}

	public TypefaceMaps extractTypefaceMaps(String mapsName) {
		extractFontNameSet();
		typefaceMaps.setMapsName(mapsName);
		List<String> fontNames = new ArrayList<String>(fontNameSet.elementSet());
		Collections.sort(fontNames);
		for (String fontName : fontNames) {
			List<StyleRecord> styleRecordList = new ArrayList<StyleRecord>(styleRecordByFontName.get(fontName));
			List<String> fontWeights = getFontAttributes(StyleBundle.FONT_WEIGHT, styleRecordList);
			List<String> fontStyles = getFontAttributes(StyleBundle.FONT_STYLE, styleRecordList);
			Typeface typeface = new Typeface(fontName);
			typeface.setFontWeights(fontWeights);
			typeface.setFontStyles(fontStyles);
			List<String> fills = getFontAttributes(StyleBundle.FILL, styleRecordList);
			typeface.setFills(fills);
			List<String> strokes = getFontAttributes(StyleBundle.STROKE, styleRecordList);
			typeface.setStrokes(strokes);
			List<Double> sizes = getFontAttributeDoubles(StyleBundle.FONT_SIZE, styleRecordList);
			typeface.setFontSizes(sizes);
			typefaceMaps.add(typeface);
		}
		return typefaceMaps;
	}

	private List<String> getFontAttributes(String attributeName, List<StyleRecord> styleRecordList) {
		List<String> attributeList = new ArrayList<String>();
		for (StyleRecord styleRecord : styleRecordList) {
			if (attributeName != null) {
				String attributeValue = null;
				if (attributeName.equals(StyleBundle.FONT_STYLE)) {
					attributeValue = styleRecord.getFontStyle();
				} else if (attributeName.equals(StyleBundle.FONT_WEIGHT)) {
					attributeValue = styleRecord.getFontWeight();
				} else if (attributeName.equals(StyleBundle.FILL)) {
					attributeValue = styleRecord.getFill();
				} else if (attributeName.equals(StyleBundle.STROKE)) {
					attributeValue = styleRecord.getStroke();
				}
				if (attributeValue != null && !attributeList.contains(attributeValue)) {
					attributeList.add(attributeValue);
				}
			}
		}
		return attributeList;
	}

	private List<Double> getFontAttributeDoubles(String attributeName, List<StyleRecord> styleRecordList) {
		List<Double> attributeList = new ArrayList<Double>();
		for (StyleRecord styleRecordAnalyzer : styleRecordList) {
			if (attributeName != null) {
				Double attributeValue = null;
				if (attributeName.equals(StyleBundle.FONT_SIZE)) {
					attributeValue = styleRecordAnalyzer.getFontSize();
				}
				if (!attributeList.contains(attributeValue)) {
					attributeList.add(attributeValue);
				}
			}
		}
		return attributeList;
	}

	public Multiset<String> extractFontNameSet() {
		fontNameSet = HashMultiset.create();
		for (StyleRecord styleRecord : styleRecordByCSSStyle.values()) {
			String fontName = styleRecord.getFontName();
			fontNameSet.add(fontName);
		}
		return fontNameSet;
	}
	
}
