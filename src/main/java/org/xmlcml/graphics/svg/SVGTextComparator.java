package org.xmlcml.graphics.svg;

import java.util.Comparator;

public class SVGTextComparator implements Comparator<SVGText> {

	public enum TextComparatorType {
		COORDINATES,
		ALPHA,
		
	}
	
	private TextComparatorType type;
	
	public SVGTextComparator(TextComparatorType type) {
		this.type = type;
	}
	
	public int compare(SVGText t1, SVGText t2) {
		if (TextComparatorType.ALPHA.equals(type)) {
			
		}
		return t1.getText().compareTo(t2.getText());
	}

}
