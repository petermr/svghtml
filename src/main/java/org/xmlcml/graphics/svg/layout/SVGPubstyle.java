package org.xmlcml.graphics.svg.layout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.xml.XMLUtil;

/** per publisher pubstyle.
 * 
<g id="pubstyle" pubstyle="bmc" publisher="BioMedCentral" doi="10.1186">
 * pages are numbered from 1
	<g id="page1" number="P1">
		<g id="header">
			<rect x="0" width="540" y="0" height="47" />
			<text id="biblio" fontName="MyriadPro-It" y="38.469" x="450"
				fontStyle="italic|normal" fontWeight="boldnormal">
				<![CDATA[(?<auth>)(\{\d+\)\s+(\d+:\d+)})]]> </text>
			<!-- DOI 10.1186/s12936-017-1948-z -->
			<text id="doi" fontName="MyriadPro-It" y="48.469" fontStyle="italic|normal"
				fontWeight="boldnormal"><![CDATA[(DOI (10\.1186/s\d+\-\d+\-\d+\-z))]]> </text>
			<text id="journalName" fontName="" fontSize="16.2" fontWeight="normal"
				y="47.307" />
			<title justify="left" wrap="true" fontSize="99" suscript="true"
				font="" />
		</g>

		<g id="abstract">
			<text level="2" x="63.122" fontSize="10.3" fontWeight="bold">Abstract
			</text>
			<g id="abs.section">
				<text level="3" x="63.122" fontSize="10.0" fontWeight="bold">
					(Background|Methods|Results|Discussion|Keywords):
				</text>
				<!-- running text -->
				<text level="3" fontSize="10.0" fontWeight="bold">ANY</text>
			</g>
		</g>
		<g id="page2" number="P2">
			<g id="header">
				<rect x="0" width="540" y="0" height="47" />
				<text id="biblio" fontName="MyriadPro-*" y="38.469" x="450" fontStyle="(italic|normal)" fontWeight="(bold|normal)">
					<![CDATA[(?<authors>)(\{\d+\)\s+(\d+:\d+)})]]></text>
				<text id="pages" justify="right" fontName="MyriadPro-*" fontSize="8.0" fontWeight="normal"
					y="47.307" ><![CDATA[(Page?<page>\s+\d+\s+of\s+\d+)]]></text>
<!--
<text fontName="MyriadPro-Regular" y="38.72" x="56.694,...96.198" 
style="fill:#000000;font-family:Helvetica;font-size:8.0px;font-weight:normal;">Sumarnrote </text>
<text fontName="MyriadPro-It" y="38.472" x="97.894,...,183.869" 
style="fill:#000000;font-family:Helvetica;font-size:8.0px;font-style:italic;font-weight:normal;">et al. Malar J  (2017) 16:299 </text>
<text fontName="MyriadPro-Regular" y="38.984" x="498.15,...513.0" 
style="fill:#000000;font-family:Helvetica;font-size:8.0px;font-weight:normal;">Page 2 of 13</text>
-->					
			</g>

			<g id="wideimage">
				<!-- diagram -->
				<!-- background? -->
				<path style="fill:none;stroke-width:1.0;stroke:#000000;" signature="MLLLZ"
					d="M57.068 486.972 L538.958 486.972 L538.958 94.092 L57.068 94.092 Z" />

				<image xmlns:xlink="http://www.w3.org/1999/xlink"
					transform="matrix(0.240,-0.0,-0.0,0.240,106.67,94.09)" xlink:href="fulltext.p4.i1.png"
					x="0.0" y="0.0" width="1595.0" height="1637.0" />
				<!-- rounded corners rect -->
				<path style="stroke:blue;stroke-width:2.25;fill:yellow;"
					d="M61.068 88.092 
	    C61.068 88.092 57.068 88.092 57.068 92.092 L57.068 519.852 
	    C57.068 519.852 57.068 523.852 61.068 523.852 L534.958 523.852 
	    C534.958 523.852 538.958 523.852 538.958 519.852 L538.958 92.092 
	    C538.958 92.092 538.958 88.092 534.958 88.092 L61.068 88.092 Z"
					signature="MCLCLCLCLZ" />


			</g>

			<g id="widetable">
				<!-- diagram -->
				<!-- table rules -->
				<path style="stroke:#000000;stroke-width:0.15;" d="M56.693 572.729 L538.583 572.729 " />
				<path style="stroke:#000000;stroke-width:0.15;" d="M56.693 703.503 L538.583 703.503 " />

				<!-- background? -->
				<path style="fill:none;stroke-width:1.0;stroke:red;" signature="MLLLZ"
					d="M57.068 486.972 L538.958 486.972 L538.958 94.092 L57.068 94.092 Z" />


			</g>

			<g id="left">
				<rect x="55" width="238" y="82" height="639" />
				<text fontName="MyriadPro-Bold"
					style="fill:#000000;font-family:Helvetica;font-size:10.3px;font-weight:bold;">Background</text>
				<text fontName="WarnockPro-Regular" x="56.693 ... 290.57"
					style="fill:#000000;font-size:9.8px;font-weight:normal;">In 2015, ... countries </text>
			</g>
			<g id="right">
				<rect x="302" width="238" y="82" height="639" />
				<text fontName="WarnockPro-Regular" y="94.917" x="304.721...531.08"
					style="fill:#000000;font-size:9.8px;font-weight:normal;">only found in Ho Chi Minh City [</text>
			</g>
			<g id="footer">
			</g>
		</g>
	</g>
</g>

 * @author pm286
 *
 */
public class SVGPubstyle extends AbstractPubstyle {




	public enum PageType {
		P1,
		P2,
		PN
	}
	private static final Logger LOG = Logger.getLogger(SVGPubstyle.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static String CLASSNAME = "pubstyle";
	
	private static final String NUMBER = "number";
	private static final String ABSTRACT = SVGPubstyleAbstract.SVG_CLASSNAME;
	private static final String ABSTRACT_XPATH = ".//*[@" + NUMBER + "='"+PageType.P1+"']/*[@id='" + ABSTRACT + "']";
	private static final String ABS_SECTION = "abs.section";
	private static final String ABS_SECTION_XPATH = "./*[@id='" + ABS_SECTION + "']";
	private static final String DOI = "doi";
	private static final String DOI_XPATH = "./*/@" + DOI;
	private static final String FOOTER =  SVGPubstyleFooter.SVG_CLASSNAME;
	private static final String FOOTER_XPATH = "./*[@id='" + FOOTER + "']";
	private static final String HEADER =  SVGPubstyleHeader.SVG_CLASSNAME;
	private static final String HEADER_XPATH = "./*[@id='" + HEADER + "']";
	private static final String LEFT =  SVGPubstyleLeftColumn.SVG_CLASSNAME;
	private static final String LEFT_XPATH = "./*[@id='" + LEFT + "']";
	private static final String MIDDLE = SVGPubstyleMiddleColumn.SVG_CLASSNAME;
	private static final String MIDDLE_XPATH = "./*[@id='" + MIDDLE + "']";
	private static final String PUBSTYLE = "pubstyle";
	private static final String PUBSTYLE_XPATH = "./*/@" + PUBSTYLE;
	private static final String PUBLISHER = "publisher";
	private static final String PUBLISHER_XPATH = "./*/@" + PUBLISHER;
	private static final String RIGHT = SVGPubstyleRightColumn.SVG_CLASSNAME;
	private static final String RIGHT_XPATH = "./*[@id='" + RIGHT + "']";
	private static final String WIDE_IMAGE =  SVGPubstyleWideImage.SVG_CLASSNAME;
	private static final String WIDE_IMAGE_XPATH = "./*[@id='" + WIDE_IMAGE + "']";
	private static final String WIDE_TABLE =  SVGPubstyleWideTable.SVG_CLASSNAME;
	private static final String WIDE_TABLE_XPATH = "./*[@id='" + WIDE_TABLE + "']";
	
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
	public SVGElement getRawPage(PageType type) {
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

	public SVGPubstyleAbstract getAbstract() {
		SVGElement element = (SVGElement)XMLUtil.getSingleElement(this, ABSTRACT_XPATH);
		return element == null ? null : new SVGPubstyleAbstract(element);
	}
	
	public SVGPubstyleFooter getFooter(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, FOOTER_XPATH);
		return element == null ? null : new SVGPubstyleFooter(element);
	}

	public SVGPubstyleHeader getHeader(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, HEADER_XPATH);
		return element == null ? null : new SVGPubstyleHeader(element);
	}

	public SVGPubstyleLeftColumn getLeft(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, LEFT_XPATH);
		return element == null ? null : new SVGPubstyleLeftColumn(element);
	}

	public SVGPubstylePage getPubstylePage(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, FOOTER_XPATH);
		return element == null ? null : new SVGPubstylePage(element);
	}

	public SVGPubstyleRightColumn getRight(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, RIGHT_XPATH);
		return element == null ? null : new SVGPubstyleRightColumn(element);
	}

	public SVGElement getAbstractSection() {
		SVGElement abstractElement = this.getAbstract();
		return abstractElement == null ? null : (SVGElement)XMLUtil.getSingleElement(abstractElement, ABS_SECTION_XPATH);
	}
	
	public SVGPubstyleWideImage getWideImage(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, WIDE_IMAGE_XPATH);
		return element == null ? null : new SVGPubstyleWideImage(element);
	}
	
	public SVGPubstyleWideTable getWideTable(PageType type) {
		SVGElement page = getRawPage(type);
		SVGElement element = page == null ? null : (SVGElement)XMLUtil.getSingleElement(page, WIDE_TABLE_XPATH);
		return element == null ? null : new SVGPubstyleWideTable(element);
	}

	@Override
	protected String getPubstyleClassName() {
		return CLASSNAME;
	}
	
	
}
