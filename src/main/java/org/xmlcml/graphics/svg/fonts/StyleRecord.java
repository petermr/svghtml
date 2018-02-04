package org.xmlcml.graphics.svg.fonts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.euclid.util.MultisetUtil;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.StyleAttributeFactory;
import org.xmlcml.graphics.svg.StyleBundle;
import org.xmlcml.graphics.svg.cache.PageCacheTest;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/** represents data from CSSStyle attributes.
 * font Family, size, styles, etc.  
 * 
 * also captures YCoordinates
 * 
 * may overlap with earlier work.
 * See also pdf2svg.AMIFont which is primarily in PDF context
 * Experimental
 * 
 * @author pm286
 *
 */
public class StyleRecord {
	private static final Logger LOG = Logger.getLogger(StyleRecord.class);

	static {
		LOG.setLevel(Level.DEBUG);
	}

	private Multiset<String> characterSet;
	private StyledFont styledFont;
	private StyleBundle styleBundle;
	private Multiset<Double> yCoordinateSet;

	public StyleRecord(String cssStyle) {
		styledFont = new StyledFont(cssStyle);
		styleBundle = new StyleBundle(cssStyle);
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
		String cssStyle = null;
		if (text != null) {
			StyleAttributeFactory attributeFactory = new StyleAttributeFactory();
			attributeFactory.addFontName(text);
			attributeFactory.addFontSize(text);
			attributeFactory.addFontStyle(text);
			attributeFactory.addFontWeight(text);
			attributeFactory.addFill(text);
			attributeFactory.addStroke(text);
			attributeFactory.addFontWeight(text);
			cssStyle = attributeFactory.getAttributeValue();
		}
		return cssStyle;
	}

	public String toString() {
		List<Multiset.Entry<String>> characterEntries = MultisetUtil.createStringListSortedByValue(characterSet);
		List<Multiset.Entry<Double>> yCoordinateEntries = MultisetUtil.createDoubleListSortedByValue(yCoordinateSet);
		return "chars: total: "+characterSet.size()+"; unique: "+characterEntries.size()+"; coords: "+yCoordinateEntries.size()+" "+yCoordinateEntries.toString();
	}

	public void addNonWhitespaceCharacters(String value) {
		if (value != null) {
			for (int i = 0; i < value.length(); i++) {
				char ch = value.charAt(i);
				String s = String.valueOf(ch);
				if (!Character.isWhitespace(ch)) {
					this.characterSet.add(String.valueOf(ch));
				}
			}
		}
	}

	public String getFontName() {
		return styledFont.getFontName();
	}
	
	public String getFontWeight() {
		return styleBundle == null ? StyleBundle.NORMAL : styleBundle.getFontWeight();
	}
	
	public String getFontStyle() {
		return styleBundle == null ? StyleBundle.NORMAL : styleBundle.getFontStyle();
	}
	
	public Double getFontSize() {
		return styleBundle == null ? null : styleBundle.getFontSize();
	}
	
	public String getFill() {
		return styleBundle == null ? StyleBundle.BLACK : styleBundle.getFill();
	}
	
	public String getStroke() {
		return styleBundle == null ? StyleBundle.NONE : styleBundle.getStroke();
	}
	
	public String getCSSStyle() {
		return styleBundle == null ? null : styleBundle.getCSSStyle();
	}

	public Multiset<Double> getOrCreateYCoordinateSet() {
		if (yCoordinateSet == null) {
			yCoordinateSet = HashMultiset.create();
		}
		return yCoordinateSet;
	}

	public void addYCoordinate(Double yCoordinate) {
		getOrCreateYCoordinateSet();
		yCoordinateSet.add(yCoordinate);
	}

	/** sorted list of child arithmetic progressions.
	 * split a realArray into a list of sub arrays each of which is an arithmetic progression
	 * within the tolerance of epsilon.
	 * 
	 * the inijtial Ycoordinate array is sorted and compressed to remove "near equivalents"
	 * (i.e. within epsilon) and then chunked into RealArrays which are APs within epsilon.
	 * 
	 * @param epsilon
	 * @return list of RealArrays ; some could have one element
	 */
	public List<RealArray> createSortedCompressedYCoordAPList(double epsilon) {
		// 
		List<Double> yCoords = new ArrayList<Double>(getOrCreateYCoordinateSet().elementSet());
		Collections.sort(yCoords);
		RealArray realArray = new RealArray(yCoords);
		realArray = realArray.compressNearNeighbours(epsilon);
		List<RealArray> aps = realArray.createArithmeticProgressionList(epsilon);
		return aps;
	}
	
}
