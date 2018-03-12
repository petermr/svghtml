package org.xmlcml.graphics.svg.layout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.html.HtmlElement;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.cache.PageCache;
import org.xmlcml.graphics.svg.util.SVGHtmlProperties;
import org.xmlcml.graphics.util.FilePathGlobber;
import org.xmlcml.xml.XMLUtil;

/** manages the pubstyles on the system.
 *  and tries to identify the pubstyle of a document.
 * 
 * <publisherList>
  <publisher name="BioMedCentral" doiPrefix="10.1186" pubstyle="bmc"/>
</publisherList>

 * @author pm286
 *
 */
public class PubstyleManager {
	public static final Logger LOG = Logger.getLogger(PubstyleManager.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	private static final String RESOURCE_ROOT = "resourceRoot";
	private static final String PUBSTYLE_RESOURCE_ROOT = "pubstyle.resource.root";
	private static final String PUBSTYLE_PROPERTIES = "pubstyle.properties";
	private static final String PUBSTYLE_FILE_NAME = "pubstyle.file";
	private static final String MVN_ROOT = "src/main/resource";
	
	public Map<String, SVGPubstyle> pubstyleByDoi;
	public Map<String, SVGPubstyle> pubstyleByPubstyleString;
	
	private List<Path> pubstylePathList;
	private Properties svgHtmlProperties;
	private String pubstylePropertiesFile;
	private String pubstyleRoot;
	private String jarRoot;
	private String pubstyleFileName;
	
	public PubstyleManager() {
		init();
	}
	
	private void init() {
		getOrCreateProperties();
		getPubstylePathFromDirectories();
	}

	private void getPubstylePathFromDirectories() {
		Path pubstylePath = null;
		try {
		    FilePathGlobber globber = new FilePathGlobber();
			Path path = globber.getFolderPath(pubstyleRoot, jarRoot);
			globber.setRecurse(false);
			// direct childdirectories of root
			pubstylePathList = globber.createDirOrFileList(path, FilePathGlobber.FSType.DIR); 
			LOG.debug("pubstylePathList: "+pubstylePathList);
			pubstyleByDoi = new HashMap<String, SVGPubstyle>();
			pubstyleByPubstyleString = new HashMap<String, SVGPubstyle>();
			for (int i = 0; i < pubstylePathList.size(); i++) {
				pubstylePath = pubstylePathList.get(i);
				readAndAddPubstyle(pubstylePath);
			}
		} catch (Exception e) {
			throw new RuntimeException("cannot read resource: "+pubstylePath+"; "+e.getLocalizedMessage(), e);
		}
	}

	private void readAndAddPubstyle(Path pubstylePath) {
		LOG.debug("adding pubstyle "+pubstylePath);
		String name = pubstylePath.getFileName().toString();
		String pubstyleResource = pubstyleRoot+"/"+name+"/"+pubstyleFileName;
		LOG.debug("pubstyleResource "+pubstyleResource);
		SVGPubstyle pubstyle = null;
		try {
			InputStream is = getClass().getResourceAsStream(pubstyleResource);
			SVGElement svgElement = SVGElement.readAndCreateSVG(XMLUtil.parseQuietlyToDocument(is).getRootElement());
			pubstyle = new SVGPubstyle(svgElement, this);
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse pubstyleResource "+pubstyleResource, e);
		}
		String doi = pubstyle.getDoi();
		if (doi != null) {
			pubstyleByDoi.put(doi, pubstyle);
		} else {
			LOG.warn("no DOI");
		}
		pubstyleByPubstyleString.put(name, pubstyle);
	}
	
	public SVGPubstyle guessPubStyleFromFirstPage(List<File> svgFiles) {
		SVGPubstyle pubstyle = null;
		if (svgFiles != null && svgFiles.size() > 0) {
			File inputSvgFile = svgFiles.get(0);
			pubstyle = guessPubstyleFromFirstPage(inputSvgFile);
		}
		return pubstyle;
	}

	public SVGPubstyle guessPubstyleFromFirstPage(File inputSvgFile) {
		PageCache pageCache = new PageCache(null);
		pageCache.readGraphicsComponentsAndMakeCaches(inputSvgFile);
		
		HtmlElement htmlElement = pageCache.createHtmlElement();
//		LOG.debug(htmlElement.toXML());
		String textValue = htmlElement.getValue();
		Collection<SVGPubstyle> values = pubstyleByDoi.values();
		LOG.debug("VAL "+values);
		for (SVGPubstyle pubstyle : values) {
			String doi = pubstyle.getDoi();
			if (textValue.contains(doi)) {
				return pubstyle;
			}
		}
		return null;
	}
	
	/** create from XML, copy to SVGSVG.
	 * 
	 * @param element
	 * @return
	 */
	public SVGPubstyle createPubstyle(SVGElement element) {
		SVGPubstyle pubstyle = null;
		if (element != null) {
			pubstyle = new SVGPubstyle(element, this);
		}
		return pubstyle;
	}

	public SVGPubstyle getSVGPubstyleFromPubstyleString(String pubstyleString) {
		return pubstyleByPubstyleString.get(pubstyleString);
	}

	private void getOrCreateProperties() {
		svgHtmlProperties = SVGHtmlProperties.createSVGHtmlProperties();
		LOG.debug("KEYS "+svgHtmlProperties.keySet());
		String pubstyleResourceRootValue = svgHtmlProperties.getProperty(PUBSTYLE_RESOURCE_ROOT);
		String pubstylePropertiesValue = svgHtmlProperties.getProperty(PUBSTYLE_PROPERTIES);
		pubstyleRoot = svgHtmlProperties.getProperty(RESOURCE_ROOT)+"/"+pubstyleResourceRootValue;
		LOG.debug("pubstyleRoot: "+pubstyleRoot);
		pubstylePropertiesFile = pubstyleRoot+"/"+pubstylePropertiesValue;
		LOG.debug("pubstylePropertiesFile: "+pubstylePropertiesFile);
		jarRoot = MVN_ROOT+"/"+pubstyleRoot;
		pubstyleFileName = SVGHtmlProperties.createProperties(pubstylePropertiesFile).getProperty(PUBSTYLE_FILE_NAME);
		LOG.debug("pubstyleFileName: "+pubstyleFileName);
	}
	
}
