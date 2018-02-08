package org.xmlcml.graphics.svg.cache;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGUtil;

/** defines components of how the page should be laid out
 * 
 * @author pm286
 *
 */
public class PageLayout {
	private static final Logger LOG = Logger.getLogger(PageLayout.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public static final String RESOURCE_PREFIX = "/org/xmlcml";
	public static final String LAYOUT = RESOURCE_PREFIX+"/layout";
	
	public static final String BODY = "body";
	public static final String HEADER = "header";
	public static final String FOOTER = "footer";
	public static final String LEFTBAR = "leftSidebar";
	public static final String RIGHTBAR = "rightSidebar";
	public static final String ALL = "all";
	private static final String G = "g";

	public static final String FRONT = "front";
	public static final String MIDDLE = "middle";
	public static final String BACK = "back";
	public static final String DOT_SVG = ".svg";
	public static final String AMSOCGENE = LAYOUT+"/asgt"+DOT_SVG;
	public static final String BMC = LAYOUT+"/bmc"+DOT_SVG;
	public static final String PLOSONE2016 = LAYOUT+"/plosone2016/";
	

	private SVGElement layoutElement;

	public PageLayout() {
	}
	
	public PageLayout(SVGElement layout) {
		this.setLayout(layout);
	}

	public void setLayout(SVGElement layout) {
		this.layoutElement = layout;
	}

	public Real2Range getBodyLimits() {
		return getLimits(BODY);
	}

	private Real2Range getLimits(String boxClass) {
		Real2Range limits = (layoutElement != null) ? getBoundingBoxFromId(/*PageLayout.ALL*/ "main.body") : null;
		return limits;
	}
	
	List<SVGRect> getRectList(String boxClass) {
		String tagName = SVGRect.TAG;
		List<SVGElement> elements = SVGUtil.getQuerySVGElements(
				layoutElement, ".//*[local-name()='" + G + "' and @class='" + boxClass + "']/"
						+ "*[local-name()='" + tagName + "']");
		return SVGRect.extractRects(elements);
	}

	private Real2Range getBoundingBoxFromId(String role) {
//		String xpath = ".//*[local-name()='" + G + "' and @class='" + boxClass + "']/"
//				+ "*[local-name()='" + tagName + "' and @class='" + role + "']";
		String xpath = ".//*[@id='" + role + "']";
		List<SVGElement> boxList = SVGUtil.getQuerySVGElements(layoutElement, xpath);
		LOG.debug(xpath);
///		LOG.debug("LAY "+layoutElement.toXML());
		SVGRect rect = (boxList.size() == 1) ? (SVGRect) boxList.get(0): null;
		Real2Range limits = rect == null ? null : rect.getBoundingBox();
		return limits;
	}

	public static PageLayout readPageLayoutFromResource(String layoutResource) {
		PageLayout pageLayout = null;
		try {
			InputStream is = PageLayout.class.getResourceAsStream(layoutResource);
			pageLayout = PageLayout.readPageLayoutFromStream(is);
		} catch (RuntimeException e) {
			throw new RuntimeException("cannot read pageLayout "+layoutResource, e);
		}
		return pageLayout;
	}

	public static PageLayout readPageLayoutFromStream(InputStream is) {
		PageLayout pageLayout = null;
		if (is != null) {
			SVGElement layoutElement = (SVGElement) SVGElement.readAndCreateSVG(is);
			pageLayout = new PageLayout(layoutElement);
		}
		return pageLayout;
	}



 }
