package org.xmlcml.graphics.svg.layout;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.xml.XMLUtil;

import nu.xom.Element;

/** per publisher pubstyle.
 * 
<g id="pubstyle" pubstyle="bmc" publisher="BioMedCentral" doi="10.1186">
 * pages are numbered from 1
	<g id="page1" number="1">
		<g id="header1">
			<rect x="0" width="540" y="0" height="47" />
			<text id="biblio" svgx:fontName="MyriadPro-It" y="38.469" x="450"
				font-style="italic|normal" font-weight="boldnormal"
				regex="(?>auth)(\{\d+\)\s+(\d+:\d+)})" />
			<!-- DOI 10.1186/s12936-017-1948-z -->
			<text id="page1.doi" svgx:fontName="MyriadPro-It" y="48.469"
				font-style="italic|normal" font-weight="boldnormal"
				regex="DOI (10\.1186/s\d+\-\d+\-\d+\-z)" />
			<text id="journalName" font-name="" font-size="16.2"
				font-weight="normal" y="47.307" />
			<title justify="left" wrap="true" font-size="99" suscript="true"
				font="" />
		</g>

		<g id="abstract">
			<text level="2" x="63.122" font-size="10.3" font-weight="bold">Abstract
			</text>
			<g id="abs.background">
				<text level="3" x="63.122" font-size="10.0" font-weight="bold">Background:
				</text>
				<text level="3" font-size="10.0" font-weight="bold" />
			</g>
			<g id="abs.methods">
				<text level="3" x="63.122" font-size="10.0" font-weight="bold">Methods:
				</text>
				<text level="3" font-size="10.0" font-weight="bold"></text>
			</g>
			<g id="abs.results">
				<text level="3" x="63.122" font-size="10.0" font-weight="bold">Results:
				</text>
				<text level="3" font-size="10.0" font-weight="bold"></text>
			</g>
			<g id="abs.discussion">
				<text level="3" x="63.122" font-size="10.0" font-weight="bold">Discussion:
				</text>
				<text level="3" font-size="10.0" font-weight="bold"></text>
			</g>
			<g id="abs.keywords">
				<text level="3" x="63.122" font-size="10.0" font-weight="bold">Keywords:
				</text>
				<text level="3" font-size="10.0" font-weight="bold"></text>
			</g>
		</g>
		
	</g>
</g>

 * @author pm286
 *
 */
public class SVGPubstyle extends SVGSVG {




	public enum PageType {
		P1,
		P2,
		PN
	}
	private static final Logger LOG = Logger.getLogger(SVGPubstyle.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private static final String ABSTRACT = "abstract";
	private static final String ABS_SECTION = "abs.section";
	private static final String ABS_SECTION_XPATH = "./*[@id='" + ABS_SECTION + "']";
	private static final String DOI = "doi";
	private static final String DOI_XPATH = "./*/@" + DOI;
	private static final String HEADER = "header";
	private static final String HEADER_XPATH = "./*[@id='" + HEADER + "']";
	private static final String NUMBER = "number";
	private static final String ABSTRACT_XPATH = ".//*[@" + NUMBER + "='"+PageType.P1+"']/*[@id='" + ABSTRACT + "']";
	private static final String PUBSTYLE = "pubstyle";
	private static final String PUBSTYLE_XPATH = "./*/@" + PUBSTYLE;
	private static final String PUBLISHER = "publisher";
	private static final String PUBLISHER_XPATH = "./*/@" + PUBLISHER;
	
	private PubstyleManager pubstyleManager ;

	private SVGPubstyle() {
		
	}

	public SVGPubstyle(SVGElement svgElement, PubstyleManager pubstyleManager) {
		this();
		this.pubstyleManager = pubstyleManager;
		if (svgElement instanceof SVGSVG) {
			this.copyAttributesChildrenElements(svgElement);
		} else {
			this.appendChild(svgElement.copy());
		}
	}
	
	/** number can be 1, 2, last
	 * 
	 * @param pageNumber
	 * @return
	 */
	public SVGElement getPage(PageType type) {
		String xpath = ".//*[@" + NUMBER + "='"+type+"']";
		SVGElement page = (SVGElement)XMLUtil.getSingleElement(this, xpath);
		return page;
	}

	public String getPublisher() {
		return XMLUtil.getSingleValue(this, PUBLISHER_XPATH);
	}
	
	public String getPubstyle() {
		return XMLUtil.getSingleValue(this, PUBSTYLE_XPATH);
	}
	
	public String getDoi() {
		return XMLUtil.getSingleValue(this, DOI_XPATH);
	}

	public SVGElement getAbstract() {
		SVGElement section = (SVGElement)XMLUtil.getSingleElement(this, ABSTRACT_XPATH);
		return section;
	}
	
	public SVGElement getHeader(PageType type) {
		SVGElement page = getPage(type);
		
		SVGElement section = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, HEADER_XPATH);
		return section;
	}

	public SVGElement getAbstractSection() {
		SVGElement abstractElement = this.getAbstract();
		return abstractElement == null ? null : (SVGElement)XMLUtil.getSingleElement(abstractElement, ABS_SECTION_XPATH);
	}
	
}
