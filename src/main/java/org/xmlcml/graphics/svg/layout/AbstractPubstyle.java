package org.xmlcml.graphics.svg.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.util.NamePattern;

/** a pubstyle and/or its components
 * 
 * @author pm286
 *
 */
public abstract class AbstractPubstyle extends SVGG {
	private static final Logger LOG = Logger.getLogger(AbstractPubstyle.class);
	private List<NamePattern> namePatternList;
	static {
		LOG.setLevel(Level.DEBUG);
	}

	protected AbstractPubstyle() {
		super();
		this.setClassName(getPubstyleClassName());
	}

	/** create from parsed element.
	 * 
	 * @param element
	 */
	public AbstractPubstyle(SVGElement element) {
		this();
		this.copyAttributesChildrenElements(element);
	}

	/** the class for each sub-PubStyle (e.g. "abstract")
	 * 
	 * @return
	 */
	protected abstract String getPubstyleClassName();
	
	public List<SVGElement> extractElements(SVGElement inputSVGElement) {
		List<SVGElement> elements = SVGElement.extractSelfAndDescendantElements(inputSVGElement);
		List<SVGElement> elementsInBox = SVGElement.extractElementsContainedInBox(elements, this.getBoundingBox());
		return elementsInBox;
	}

	public List<String> matchTexts(List<SVGText> texts) {
		getOrCreateNamePatternList();
		List<String> matchedList = new ArrayList<String>();
		LOG.debug("=============PAGE============");
		for (NamePattern namePattern : namePatternList) {
			List<String> nameList = namePattern.getNameList();
			for (SVGText text : texts) {
				String value = text.getText();
				Matcher matcher = namePattern.getPattern().matcher(value);
				if (matcher.matches()) {
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i <= matcher.groupCount(); i++) {
						sb.append(nameList.get(i - 1)+"="+matcher.group(i)+";");
					}
					matchedList.add(sb.toString());
				}
			}
		}
		return matchedList;
	}

	private List<NamePattern> getOrCreateNamePatternList() {
		if (namePatternList == null) {
			List<SVGText> templateTexts = SVGText.extractSelfAndDescendantTexts(this);
			namePatternList = new ArrayList<NamePattern>();
			for (SVGText templateText : templateTexts) {
				String textValue = templateText.getText().trim();
				// replace all whitespace
				textValue = textValue.replaceAll("[\\s\\n]", "");
				if (textValue.startsWith("(") && textValue.endsWith(")")) {
					String regex = textValue.substring(1,  textValue.length() - 1);
					Pattern pattern = Pattern.compile(regex);
					List<String> captureNameList = NamePattern.makeCaptureNameList(regex);
					NamePattern namePattern1 = new NamePattern(pattern, captureNameList);
					NamePattern namePattern = namePattern1;
					namePatternList.add(namePattern);
				}
			}
		}
		return namePatternList;
	}
	
//	public List<String> makeCaptureNameList(String regex) {
//		Pattern regexPattern = Pattern.compile("\\?<([A-Za-z]+)>");
//
//		LOG.debug("RRR "+regex);
//		Matcher matcher = regexPattern.matcher(regex);
//		List<String> captureNameList = new ArrayList<String>();
//		while (matcher.find()) {
//			String s = regex.substring(matcher.start(), matcher.end());
//			captureNameList.add(s);
//		}
//		return captureNameList;
//	}


	private List<String> extractNamedGroups(String textValue) {
		// TODO Auto-generated method stub
		return null;
	}


}
