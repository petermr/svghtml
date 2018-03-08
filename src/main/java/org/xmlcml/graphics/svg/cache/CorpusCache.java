package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.AbstractCMElement;
import org.xmlcml.graphics.html.HtmlBody;
import org.xmlcml.graphics.html.HtmlElement;
import org.xmlcml.graphics.html.HtmlHtml;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.graphics.util.CMineGlobberNew;

/** manages a complete corpus of several documents.
 * not suitable for large numbers
 * @author pm286
 *
 */
public class CorpusCache extends ComponentCache {
	
	private static final Logger LOG = Logger.getLogger(CorpusCache.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public static String DIR_REGEX = "(.*)/fulltext\\.(pdf|xml)";

	private File cProjectDir;
	private List<DocumentCache> documentCacheList;
	private List<File> cTreeFiles;

	private List<HtmlElement> htmlElementList;


	public CorpusCache() {
		
	}
	
	public CorpusCache(File cproject) {
		SVGG g = null;
		try {
			g = (SVGG) this.processCProject(cproject);
		} catch (IOException ioe) {
			throw new RuntimeException("Glob failed: "+ioe);
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/demos/corpus.svg"), 200., 200.);
	}

	public AbstractCMElement processCProject(File cProjectDir) throws IOException {
		List<File> dirFiles = getChildCTrees(cProjectDir);
		LOG.debug(dirFiles.size()+"; "+dirFiles);
		this.setCProject(cProjectDir);
		getOrCreateDocumentCacheList();
		convertedSVGElement = new SVGG();
		convertedSVGElement.setFontSize(10.);
		double x = 10.0;
		double y = 20.0;
		double deltaY = 10.;
		int count = 0;
		for (File cTreeDir : dirFiles) {
			count++;
			if (count != 4) continue; // this has problems
			LOG.debug("*****"+count+"*****making DocumentCache: "+cTreeDir+" ****************");
			DocumentCache documentCache = new DocumentCache(cTreeDir);
//			documentCacheList.add(documentCache);
			HtmlElement htmlDiv = documentCache.getHtmlDiv();
			HtmlHtml.wrapAndWriteAsHtml(htmlDiv, new File(cTreeDir, "html/html.html"));
			convertedSVGElement.appendChild(new SVGText(new Real2(x, y), cTreeDir.getName()));
			y += deltaY;
		}
		return convertedSVGElement;
	}

	private List<File> getChildCTrees(File cProjectDir) throws IOException {
		CMineGlobberNew globber = new CMineGlobberNew();
		globber.setRegex(CorpusCache.DIR_REGEX)
		    .setUseDirectories(true)
		    .setLocation(cProjectDir.toString());
		cTreeFiles = globber.listFiles();
		return cTreeFiles;
	}

	private void getOrCreateDocumentCacheList() {
		if (documentCacheList == null) {
			documentCacheList = new ArrayList<DocumentCache>();
		}
	}

	private void setCProject(File cProjectDir) {
		this.cProjectDir = cProjectDir;
	}

	/** concatenates the documents as one huge HTML
	 * 
	 * @return
	 */
	public HtmlElement getOrCreateConvertedHtmlElement() {
		if (this.convertedHtmlElement == null) {
			this.convertedHtmlElement = new HtmlHtml();
			HtmlBody bodyAll = ((HtmlHtml)convertedHtmlElement).getOrCreateBody();
			for (DocumentCache documentCache : documentCacheList) {
				LOG.debug("hack this later");
//				HtmlElement element = documentCache.getOrCreateConvertedHtmlElement();
				
			}
		}
		return convertedHtmlElement;
	}

	public List<HtmlElement> getHtmlElementList() {
		if (this.htmlElementList == null) {
			this.htmlElementList = new ArrayList<HtmlElement>();
			for (DocumentCache documentCache : documentCacheList) {
				LOG.debug("********************Document: "+documentCache.getTitle()+"******************");
				HtmlElement element = documentCache.getOrCreateConvertedHtmlElement();
				htmlElementList.add(element);
			}
		}
		return htmlElementList;
	}



}
