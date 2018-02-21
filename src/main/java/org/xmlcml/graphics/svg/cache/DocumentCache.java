package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.util.CMFileUtil;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;

/** manages a complete document of several pages.
 * @author pm286
 *
 */
public class DocumentCache extends ComponentCache {
	
	private static final Logger LOG = Logger.getLogger(DocumentCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public static final String DOT_SVG = ".svg";
	public static final String FULLTEXT_PAGE = "fulltext-page";
	private static final String BOX = ".box";

	private File svgDir;
//	private SVGElement totalSvgElement;
	private boolean createSummaryBoxes;
	private List<File> svgFiles;
	private List<PageCache> pageCacheList;

	private PageLayout frontPageLayout;
	private PageLayout middlePageLayout;
	private PageLayout backPageLayout;
	private PageLayout currentPageLayout;
	private int npages;

	public DocumentCache() {
		
	}
	
	public SVGElement processSVGDirectory(File svgDir) {
		this.setSvgDir(svgDir);
		svgFiles = SVGElement.extractSVGFiles(svgDir);
		processSVGFiles(svgFiles);
		return convertedSVGElement;
	}

	/**
	 * this can be called publicly as well as internally
	 * 
	 * @param svgFiles
	 */
	public void processSVGFiles(List<File> svgFiles) {
		this.svgFiles = CMFileUtil.sortUniqueFilesByEmbeddedIntegers(svgFiles);
		convertedSVGElement = new SVGG();
		getOrCreatePageCacheList();
		for (int ifile = 0; ifile < svgFiles.size(); ifile++) {
			File svgFile = svgFiles.get(ifile);	
			LOG.debug("processingPage: "+svgFile);
			PageCache pageCache = new PageCache(this);
			pageCache.setSerialNumber(ifile + 1);
			pageCache.readGraphicsComponentsAndMakeCaches(svgFile);
			pageCacheList.add(pageCache);
			SVGElement extractedSvgCacheElement = pageCache.getExtractedSVGElement();
			if (extractedSvgCacheElement == null) {
				throw new RuntimeException("null element in cache");
			}
			convertedSVGElement.appendChild(extractedSvgCacheElement.copy());
		}
		summarizePages();
	}

	public void analyzePages(File pageDir, String pubstyle, int npages, String fileDir, File targetDir) {
		makePageLayouts(pubstyle);
		this.setPageCount(npages);
		for (int ipage = 1; ipage <= npages; ipage++) {
			LOG.debug("PAGE "+ipage);
			PageCache pageCache = new PageCache(this);
			SVGElement boxes = debugPage(pageDir, fileDir, ipage, pageCache);
			File outFileSVG = new File(targetDir, fileDir+"/fulltext-page" + ipage + DocumentCache.DOT_SVG);
			LOG.debug("out "+outFileSVG);
			SVGSVG.wrapAndWriteAsSVG(boxes, outFileSVG);
		}
	}

	private void setPageCount(int npages) {
		this.npages = npages;
	}

	private SVGElement debugPage(File pageDir, String fileDir, int ipage, PageCache pageCache) {
		currentPageLayout = getCurrentPageLayout(ipage);
		pageCache.setPageLayout(currentPageLayout);
		File svgFile = new File(pageDir, fileDir + "/" + DocumentCache.FULLTEXT_PAGE + ipage + PageLayout.DOT_SVG);
		pageCache.readGraphicsComponentsAndMakeCaches(svgFile);
		pageCache.readPageLayoutAndMakeBBoxesAndMargins(currentPageLayout);
		SVGElement boxg = pageCache.createSummaryBoxes(svgFile);
		SVGElement boxes = pageCache.createSVGElementFromComponents();
		boxes.appendChild(boxg.copy());
		return boxes;
	}

	private void summarizePages() {
		LOG.debug("SUMMARIZE PAGES NYI");
	}

	private List<PageCache> getOrCreatePageCacheList() {
		if (pageCacheList == null) {
			pageCacheList = new ArrayList<PageCache>();
		}
		return pageCacheList;
	}

	public File getSvgDir() {
		return svgDir;
	}

	public void setSvgDir(File svgDir) {
		this.svgDir = svgDir;
	}

	public boolean isCreateSummaryBoxes() {
		return createSummaryBoxes;
	}

	/** create SVGRect bounding boxes for the components discovered.
	 * draws rects on totalSvgElement
	 * @param createSummaryBoxes
	 */
	public void setCreateSummaryBoxes(boolean createSummaryBoxes) {
		this.createSummaryBoxes = createSummaryBoxes;
	}

	public List<File> getOrCreateSvgFiles() {
		if (svgFiles == null) {
			svgFiles = new ArrayList<File>();
		}
		return svgFiles;
	}

	public void setSvgFiles(List<File> svgFiles) {
		this.svgFiles = svgFiles;
	}

	private void makePageLayouts(String pubstyle) {
		InputStream frontInputStream = getClass().getResourceAsStream(pubstyle+PageLayout.FRONT+PageLayout.DOT_SVG);
		this.frontPageLayout = PageLayout.readPageLayoutFromStream(frontInputStream);
		InputStream middleInputStream = getClass().getResourceAsStream(pubstyle+PageLayout.MIDDLE+PageLayout.DOT_SVG);
		this.middlePageLayout = PageLayout.readPageLayoutFromStream(middleInputStream);
		InputStream backInputStream = getClass().getResourceAsStream(pubstyle+PageLayout.BACK+PageLayout.DOT_SVG);
		this.backPageLayout = PageLayout.readPageLayoutFromStream(backInputStream);
	}
	
	private PageLayout getCurrentPageLayout(int ipage) {
		PageLayout pageLayout = null;
		if (ipage == 1 && frontPageLayout != null) {
			pageLayout = frontPageLayout;
		} else if (ipage == npages && backPageLayout != null) {
			pageLayout = backPageLayout;
		} else {
			pageLayout = middlePageLayout;
		}
		if (pageLayout == null) {
			pageLayout = PageLayout.getDefaultPageLayout();
			LOG.warn("Couldn't find bespoke layout, using default");
		}
		return pageLayout;
	}

}
