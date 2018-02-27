package org.xmlcml.graphics.svg.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
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
public class SVGTextLine implements List<SVGText> {
	private static final Logger LOG = Logger.getLogger(SVGTextLine.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private List<SVGText> lineTexts;
	private Double fontSize;
	private String fontName;

	public SVGTextLine() {
		this.lineTexts = new ArrayList<SVGText>();
	}

	public SVGTextLine(List<SVGText> lineTexts) {
		this();
		this.lineTexts = new ArrayList<SVGText>(lineTexts);
		sortAndGetCommonValues();
	}

	private void sortAndGetCommonValues() {
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

	public String getTextValue() {
		StringBuilder sb = new StringBuilder();
		for (SVGText lineText : lineTexts) {
			sb.append(lineText.getText()+"|");
		}
		return sb.toString();
	}

	public Double getLeftX() {
		return lineTexts == null || lineTexts.size() == 0 ? null : lineTexts.get(0).getX();
	}

	public boolean isLeftIndented(double minimumIndent, double minimumLeftX) {
		Double leftX = getLeftX();
		return leftX - minimumLeftX > minimumIndent;
	}

	public int size() {
		return lineTexts.size();
	}

	public boolean isEmpty() {
		return lineTexts.isEmpty();
	}

	public boolean contains(Object o) {
		return lineTexts.contains(o);
	}

	public Iterator<SVGText> iterator() {
		return lineTexts.iterator();
	}

	public Object[] toArray() {
		return lineTexts.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return lineTexts.toArray(a);
	}

	public boolean add(SVGText e) {
		return lineTexts.add(e);
	}

	public boolean remove(Object o) {
		return lineTexts.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return lineTexts.containsAll(c);
	}

	public boolean addAll(Collection<? extends SVGText> c) {
		return lineTexts.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends SVGText> c) {
		return lineTexts.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return lineTexts.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return retainAll(c);
	}

	public void clear() {
		lineTexts.clear();
	}

	public SVGText get(int index) {
		return lineTexts.get(index);
	}

	public SVGText set(int index, SVGText element) {
		return lineTexts.set(index, element);
	}

	public void add(int index, SVGText element) {
		lineTexts.add(index, element);
	}

	public SVGText remove(int index) {
		return lineTexts.remove(index);
	}

	public int indexOf(Object o) {
		return lineTexts.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return lineTexts.lastIndexOf(o);
	}

	public ListIterator<SVGText> listIterator() {
		return lineTexts.listIterator();
	}

	public ListIterator<SVGText> listIterator(int index) {
		return lineTexts.listIterator(index);
	}

	public List<SVGText> subList(int fromIndex, int toIndex) {
		return subList(fromIndex, toIndex);
	}

	/** appends one text line, and increments its X coordinates.
	 * this should  result in a single new line
	 * @param appendTextLine
	 */
	public void append(SVGTextLine appendTextLine, double deltaX) {
		List<SVGText> appendLineTexts = new ArrayList<SVGText>(appendTextLine.lineTexts);
		double offset0 = this.getRightX() + deltaX;
		double appendLeftX = appendTextLine.getLeftX();
		double offset = offset0 /*+ textCopy.getX()*/ - appendLeftX;
		double y = this.getY();
		LOG.debug(offset0+"; "+appendLeftX);
		for (SVGText appendText : appendLineTexts) {
			SVGText textCopy = (SVGText) appendText.copy();
			LOG.debug("append copy: "+textCopy+"; "+textCopy.getText().length());
			// end of "this" + space and relative position in appendLine
			RealArray xArray = textCopy.getXArray();
			LOG.debug("off "+offset+"; xArray"+xArray); 
			xArray = xArray.plus(offset);
			LOG.debug("; xArray1"+xArray); 
			textCopy.setX(xArray);
			textCopy.setY(y);
			this.lineTexts.add(textCopy);
		}
		LOG.debug(">"+this.lineTexts);
		clearVariables();
	}

	public Double getY() {
		return lineTexts.size() == 0 ? null : lineTexts.get(0).getY();
	}

	private double getRightX() {
		SVGText text = lineTexts.get(lineTexts.size() - 1);
		double xMax = text.getBoundingBox().getXMax();
		return xMax;
	}

	private void clearVariables() {
		fontSize = null;
		fontName = null;
	}

	public SVGElement createSVGElement() {
		SVGG g = new SVGG();
		for (SVGText text : lineTexts) {
			g.appendChild(text.copy());
		}
		return g;
	}
	
	public Real2Range getBoundingBox() {
		Real2Range bbox = lineTexts.size() == 0 ? null : lineTexts.get(0).getBoundingBox();
		if (bbox != null) {
			for (int i = 1; i < lineTexts.size(); i++) {
				bbox = bbox.plus(lineTexts.get(i).getBoundingBox());
			}
		}
		return bbox;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineTexts == null) ? 0 : lineTexts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SVGTextLine other = (SVGTextLine) obj;
		if (lineTexts == null) {
			if (other.lineTexts != null)
				return false;
		} else if (!lineTexts.equals(other.lineTexts))
			return false;
		return true;
	}

	public void mergeLine(SVGTextLine textLine) {
		lineTexts.addAll(textLine.lineTexts);
		this.sortAndGetCommonValues();
	}

	
}
