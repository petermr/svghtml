package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.util.MultisetUtil;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGRect;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.svg.fonts.StyleRecordSet;

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

	private File svgFile;
	private int serialNumber;
	private DocumentCache documentCache;
	private SVGElement extractedSVGElement;
	private PageHeaderCache headerCache;
	private PageFooterCache footerCache;
	private PageLeftSidebarCache leftSidebarCache;
	private PageRightSidebarCache rightSidebarCache;

	public PageCache() {
		
	}
	
	public PageCache(DocumentCache documentCache) {
		this.setDocumentCache(documentCache);
	}
	
	@Override
	public void readGraphicsComponentsAndMakeCaches(SVGElement svgElement) {
		super.readGraphicsComponentsAndMakeCaches(svgElement);
		makePageComponentCaches();
	}
	

	private void makePageComponentCaches() {
		makeHeaderCache();
		makeFooterCache();
		makeLeftSidebarCache();
		makeRightSidebarCache();
	}

	private void makeHeaderCache() {
		headerCache = new PageHeaderCache(this);
	}

	private void makeFooterCache() {
		footerCache = new PageFooterCache(this);
	}

	private void makeLeftSidebarCache() {
		leftSidebarCache = new PageLeftSidebarCache(this);
	}

	private void makeRightSidebarCache() {
		rightSidebarCache = new PageRightSidebarCache(this);
	}

	void createSummaryBoxes() {
		Multiset<Int2Range> intBoxes1 = HashMultiset.create();
//		this.processPages(svgFiles, intBoxes1);
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

	private void processPage(Multiset<Int2Range> intBoxes, File svgFile) {
		String name = svgFile.getName();
		LOG.debug(name);
		List<SVGText> svgTexts = SVGText.extractSelfAndDescendantTexts(SVGElement.readAndCreateSVG(svgFile));
		StyleRecordSet styleRecordSet = StyleRecordSet.createStyleRecordSet(svgTexts);
		SVGElement g = styleRecordSet.createStyledTextBBoxes(svgTexts);
		List<SVGRect> boxes = SVGRect.extractSelfAndDescendantRects(g);
		for (SVGRect box : boxes) {
			Int2Range intBox = new Int2Range(box.getBoundingBox());
			intBoxes.add(intBox);
		}
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

	

}
