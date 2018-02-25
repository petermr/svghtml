package org.xmlcml.graphics.svg.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.SVGTextComparator;
import org.xmlcml.graphics.svg.fonts.StyleRecord;
import org.xmlcml.graphics.svg.fonts.StyleRecordFactory;
import org.xmlcml.graphics.svg.fonts.StyleRecordSet;
import org.xmlcml.graphics.svg.math.SVGMath;
import org.xmlcml.graphics.svg.text.SVGTextLine;

import com.google.common.collect.Multiset;

/** extracts polylines within graphic area.
 * 
 * @author pm286
 *
 */
public class MathCache extends AbstractCache {
	static final Logger LOG = Logger.getLogger(MathCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private List<SVGText> horizontalTextList;
	private List<SVGMath> mathList;
	private StyleRecordSet horizontalStyleRecordSet;
	
	private MathCache() {
		
	}
	
	public MathCache(ComponentCache containingComponentCache) {
		super(containingComponentCache);
		siblingTextCache = containingComponentCache.getOrCreateTextCache();
		if (siblingTextCache == null) {
			throw new RuntimeException("null siblingTextCache");
		}
		horizontalTextList = getOrCreateHorizontalTextListSortedY();
		// 
	}

	public List<SVGText> getOrCreateHorizontalTextListSortedY() {
		if (horizontalTextList == null) {
			horizontalTextList = siblingTextCache == null ? null : siblingTextCache.getOrCreateHorizontalTexts();
			if (horizontalTextList == null) {
				horizontalTextList = new ArrayList<SVGText>();
			}
			Collections.sort(horizontalTextList, new SVGTextComparator(SVGTextComparator.TextComparatorType.Y_COORD));
			horizontalStyleRecordSet = new StyleRecordFactory().createStyleRecordSet(horizontalTextList);
		}
		return horizontalTextList;
	}

	public List<SVGMath> getOrCreateMathList() {
		if (mathList == null) {
			mathList = new ArrayList<SVGMath>();
		}
		return mathList;
	}


	public List<? extends SVGElement> getOrCreateElementList() {
		return getOrCreateMathList();
	}

	@Override
	public String toString() {
		getOrCreateMathList();
		String s = ""
		+ "text: "+horizontalTextList.size()+"\n"
		+ "math: "+mathList.size();
		return s;

	}

	@Override
	public void clearAll() {
		superClearAll();
		mathList = null;
	}

	public StyleRecordSet getOrCreateHorizontalTextStyleMultiset() {
		if (horizontalStyleRecordSet == null) {
			horizontalStyleRecordSet = new StyleRecordFactory().createStyleRecordSet(getOrCreateHorizontalTextListSortedY());
		}
		return horizontalStyleRecordSet;
	}

	public void createTextLineList() {
		TextCache textCache = ownerComponentCache.getOrCreateTextCache();
		// assume that y-coords will be the most important structure
		StyleRecordSet styleRecordSet = getOrCreateHorizontalTextStyleMultiset();
		Double largestFont = styleRecordSet.getLargestFontSize();
		List<SVGTextLine> textLineList = textCache.getTextLinesForFontSize(largestFont);
	}

}
