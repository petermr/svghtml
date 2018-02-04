package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.util.CMFileUtil;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;

/** manages a complete document of several pages.
 * NYI
 * @author pm286
 *
 */
public class DocumentCache extends ComponentCache {
	private static final Logger LOG = Logger.getLogger(DocumentCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private File svgDir;
	private SVGElement totalSvgElement;
	private boolean createSummaryBoxes;
	private List<File> svgFiles;
	private List<PageCache> pageCacheList;
	
	public DocumentCache() {
		
	}
	
	public SVGElement processSVGDirectory(File svgDir) {
		this.setSvgDir(svgDir);
		svgFiles = SVGElement.extractSVGFiles(svgDir);
		processSVGFiles(svgFiles);
		return totalSvgElement;
	}

	/**
	 * this can be called publicly as well as internally
	 * 
	 * @param svgFiles
	 */
	public void processSVGFiles(List<File> svgFiles) {
		this.svgFiles = CMFileUtil.sortUniqueFilesByEmbeddedIntegers(svgFiles);
		totalSvgElement = new SVGG();
		getOrCreatePageCacheList();
		for (int ifile = 0; ifile < svgFiles.size(); ifile++) {
			File svgFile = svgFiles.get(ifile);	
			LOG.debug("processingPage: "+svgFile);
			PageCache pageCache = new PageCache(this);
			pageCache.setSerialNumber(ifile + 1);
			try {
				pageCache.readGraphicsComponents(svgFile);
			} catch (FileNotFoundException e) {
				LOG.warn("File not found: "+e.getMessage());
				continue;
			}
			pageCacheList.add(pageCache);
			pageCache.setSVGFile(svgFile);
		}
		summarizePages();
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

	public SVGElement getTotalSvgElement() {
		return totalSvgElement;
	}

}
