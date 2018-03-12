package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.graphics.html.HtmlElement;
import org.xmlcml.graphics.html.HtmlHtml;
import org.xmlcml.graphics.svg.SVGHTMLFixtures;

import junit.framework.Assert;

public class CorpusCacheTest {
	private static final Logger LOG = Logger.getLogger(CorpusCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	@Ignore // toolong // FIXME
	public void testCorpusCache() {
		File corpusDir = new File(SVGHTMLFixtures.CORPUS_DIR, "mosquitos/");
		if (!corpusDir.exists()) {
			LOG.info("directory not found: "+corpusDir);
			return;
		}
		CorpusCache corpusCache = new CorpusCache(corpusDir);
		List<DocumentCache> documentCacheList = corpusCache.getOrCreateDocumentCacheList();
		// gets this wrong (returns 985??)
		Assert.assertEquals("doc cache",  4, documentCacheList.size());
		DocumentCache docCache0 = documentCacheList.get(0);
		LOG.debug(docCache0);
		LOG.debug("MADE CORPUS");
		List<HtmlElement> htmlElementList = corpusCache.getHtmlElementList();
		Assert.assertEquals("html files ", 4, htmlElementList.size());
		HtmlHtml.wrapAndWriteAsHtml(htmlElementList, corpusDir);
	}
}
