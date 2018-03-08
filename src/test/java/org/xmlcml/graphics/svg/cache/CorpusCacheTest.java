package org.xmlcml.graphics.svg.cache;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.graphics.html.HtmlElement;

public class CorpusCacheTest {
	private static final Logger LOG = Logger.getLogger(CorpusCacheTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testCorpusCache() {
		File corpusDir = new File("../norma/demo/mosquitos");
		if (!corpusDir.exists()) {
			LOG.info("directory not found: "+corpusDir);
			return;
		}
		CorpusCache corpusCache = new CorpusCache(corpusDir);
		LOG.debug("MADE CORPUS");
		List<HtmlElement> htmlElementList = corpusCache.getHtmlElementList();
	}
}
