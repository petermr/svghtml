package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.util.MultisetUtil;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.fonts.StyleRecordSet;
import org.xmlcml.graphics.svg.util.SuperPixelArray;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/** cache of components relevant to a single page.
 * 
 * NYI fully
 * 
 * 
 * @author pm286
 *
 */
public class PageCache extends ComponentCache {
	private static final Logger LOG = Logger.getLogger(PageCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static Double DEFAULT_XMAX = 600.0;
	public final static Double DEFAULT_YMAX = 800.0;

	private File svgFile;
	private int serialNumber;
	private DocumentCache documentCache;
	private SVGElement extractedSVGElement;
	private PageHeaderCache headerCache;
	private PageFooterCache footerCache;
	private PageLeftSidebarCache leftSidebarCache;
	private PageRightSidebarCache rightSidebarCache;
	private PageComponentCache bodyCache;
	private String basename;
	private List<SVGRect> rectsList;
	private PageLayout pageLayout;

	public PageCache() {
	}
	
	public PageCache(DocumentCache documentCache) {
		this.setDocumentCache(documentCache);
	}
	
	@Override
	public void readGraphicsComponentsAndMakeCaches(SVGElement svgElement) {
		super.readGraphicsComponentsAndMakeCaches(svgElement);
		ensurePageComponentCaches();
	}
	

	private void ensurePageComponentCaches() {
		// done in this order so the margins can inform the body size
		ensureTopBottomLeftRightMarginCaches();
		// this must be the order at present as body is defined 
		// by the others
		getOrCreateBodyCache();
	}

	void ensureTopBottomLeftRightMarginCaches() {
		getOrCreateHeaderCache();
		getOrCreateFooterCache();
		getOrCreateLeftSidebarCache();
		getOrCreateRightSidebarCache();
	}

	public void getOrCreateBodyCache() {
		if (bodyCache == null) {
			bodyCache = new PageBodyCache(this);
		}
	}

	public PageHeaderCache getOrCreateHeaderCache() {
		if (headerCache == null) {
			headerCache = new PageHeaderCache(this);
		}
		return headerCache;
	}

	public PageFooterCache getOrCreateFooterCache() {
		if (footerCache == null) {
			footerCache = new PageFooterCache(this);
		}
		return footerCache;
	}

	public PageLeftSidebarCache getOrCreateLeftSidebarCache() {
		if (leftSidebarCache == null) {
			leftSidebarCache = new PageLeftSidebarCache(this);
		}
		return leftSidebarCache;
	}

	public PageRightSidebarCache getOrCreateRightSidebarCache() {
		if (rightSidebarCache == null) {
			rightSidebarCache = new PageRightSidebarCache(this);
		}
		return rightSidebarCache;
	}

	void createSummaryBoxes(File svgFile) {
		this.svgFile = svgFile;
		Multiset<Int2Range> intBoxes1 = HashMultiset.create();
//		this.processPages(svgFiles, intBoxes1);
		SVGElement boxg = this.getStyledBoxes(intBoxes1);
		// DEBUG this
		LOG.debug("DEBUG HERE");
		getOrCreateExtractedSVGElement().appendChild(boxg);
		Multiset<Int2Range> intBoxes = intBoxes1;
		List<Multiset.Entry<Int2Range>> sortedIntBoxes1 = MultisetUtil.createInt2RangeListSortedByCount(intBoxes);
		for (Multiset.Entry<Int2Range> box : sortedIntBoxes1) {
			int count = box.getCount();
			if (count > 1) {
				SVGRect rect = SVGRect.createFromReal2Range(Real2Range.createReal2Range(box.getElement()));
				rect.setStrokeWidth((double) count / 3.);
				rect.setFill("none");
				rect.setStroke("red");
				getOrCreateExtractedSVGElement().appendChild(rect);
			}
		}
	}

	private SVGElement getStyledBoxes(Multiset<Int2Range> intBoxes) {
		List<SVGText> svgTexts = SVGText.extractSelfAndDescendantTexts(SVGElement.readAndCreateSVG(svgFile));
		StyleRecordSet styleRecordSet = StyleRecordSet.createStyleRecordSet(svgTexts);
		SVGElement g = styleRecordSet.createStyledTextBBoxes(svgTexts);
		List<SVGRect> boxes = SVGRect.extractSelfAndDescendantRects(g);
		for (SVGRect box : boxes) {
			Int2Range intBox = new Int2Range(box.getBoundingBox());
			intBoxes.add(intBox);
		}
		return g;
	}

	public void setSerialNumber(int serial) {
		this.serialNumber = serial;
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}
	
	public File getSVGFile() {
		return svgFile;
	}

	public void setSVGFile(File svgFile) {
		this.svgFile = svgFile;
	}

	public DocumentCache getDocumentCache() {
		return documentCache;
	}

	public void setDocumentCache(DocumentCache documentCache) {
		this.documentCache = documentCache;
	}

	public SVGElement getOrCreateExtractedSVGElement() {
		if (extractedSVGElement == null) {
			extractedSVGElement = new SVGG();
		}
		return extractedSVGElement;
	}

	public SuperPixelArray createSuperpixelArray(File outDir, File svgFile) {
		basename = getBaseName(svgFile);
		SVGElement svgElement = SVGElement.readAndCreateSVG(svgFile);
		readGraphicsComponentsAndMakeCaches(svgElement);
		TextCache textCache = getOrCreateTextCache();
		textCache.createCompactedTextsAndReplace();
		Real2Range bbox = Real2Range.createTotalBox(getBoundingBoxList());
		LOG.debug(">> "+bbox+" "+getBoundingBoxList().size());
		SuperPixelArray superPixelArray = new SuperPixelArray(new Int2Range(bbox));
		superPixelArray.setPixels(1, getBoundingBoxList());
		return superPixelArray;
	}

	private String getBaseName(File svgFile) {
		return svgFile == null ? null : FilenameUtils.getBaseName(svgFile.toString());
	}

	public void setSvgFile(File svgFile) {
		this.svgFile = svgFile;
	}

	public File getSvgFile() {
		return svgFile;
	}

	public SVGElement getExtractedSVGElement() {
		return extractedSVGElement;
	}

	void readPageLayoutAndMakeBBoxesAndMargins(PageLayout pageLayout) {
		ensurePageComponentCaches();
		bodyCache.boundingBox = pageLayout.getBodyLimits();
		LOG.debug("body "+bodyCache.boundingBox);
		this.headerCache.setYMax(bodyCache.boundingBox.getYMin());
		this.footerCache.setYMin(bodyCache.boundingBox.getYMax());
		this.leftSidebarCache.setXMax(bodyCache.boundingBox.getXMin());
		this.rightSidebarCache.setXMin(bodyCache.boundingBox.getXMax());
		rectsList = pageLayout.getRectList(PageLayout.BODY);
		LOG.debug("page ***** "+this);
	}

	public String getBasename() {
		return basename;
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("body: "+bodyCache.toString()+"\n");
		sb.append("header: "+headerCache.toString()+"\n");
		sb.append("footer: "+footerCache.toString()+"\n");
		sb.append("left: "+leftSidebarCache.toString()+"\n");
		sb.append("right: "+rightSidebarCache.toString()+"\n");
		return sb.toString();
	}

	public SVGElement createSVGElementFromComponents() {
		SVGElement g = new SVGG();
		addElementAndChildren(g, bodyCache);
		addElementAndChildren(g,headerCache);
		addElementAndChildren(g,footerCache);
		addElementAndChildren(g,leftSidebarCache);
		addElementAndChildren(g,rightSidebarCache);
		g.appendChild(originalSvgElement.copy());
		for (SVGRect rect : rectsList) {
			g.appendChild(rect.copy());
		}
		return g;
	}

	private void addElementAndChildren(SVGElement g, PageComponentCache cache) {
		g.appendChild(cache.getSVGElement().copy());
		for (SVGElement element : cache.getOrCreateAllElementList()) {
			g.appendChild(element.copy());
		}
	}

	public void setPageLayout(PageLayout pageLayout) {
		this.pageLayout = pageLayout;
	}

}
