package org.xmlcml.graphics.svg.layout;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGPath;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;

import nu.xom.Attribute;

/** a footer (can be on any/every page
 * 
 * @author pm286
 *
 */
public class SVGPubstyleColumn extends AbstractPubstyle {

	private static final Logger LOG = Logger.getLogger(SVGPubstyleColumn.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	public enum ColumnPosition{
		ANY,
		LEFT,
		MIDDLE,
		RIGHT,
		WIDE;
	}
	
	private static final String XPATH = "xpath";
	public final static String SVG_CLASSNAME = "column";

	private ColumnPosition columnPosition;
	
	public SVGPubstyleColumn() {
		super();
		init();
	}
	
	public SVGPubstyleColumn(SVGElement element) {
		super(element);
	}

	public SVGPubstyleColumn(SVGElement element, ColumnPosition columnPosition) {
		this(element);
		this.columnPosition = columnPosition;
	}

	protected void init() {
		super.init();
		columnPosition = ColumnPosition.ANY;
	}
	
	@Override
	protected String getPubstyleClassName() {
		return SVG_CLASSNAME;
	}

	public void setXPath(String columnXpath) {
		this.addAttribute(new Attribute(XPATH, columnXpath));
	}

	/** this is out of place here
	 * 
	 * @param svgElement TODO
	 * @return
	 */
	public List<DocumentChunk> extractDocumentChunksInBox(SVGElement svgElement) {
		List<DocumentChunk> documentChunks1;
		if (svgElement == null) {
			SVGElement.LOG.error("Null SVGElement");
			throw new RuntimeException("NULL svgElement");
		}
		
		List<SVGText> texts = extractTextsContainedInBox(svgElement);
		containingPubstyle.setExtractedTexts(texts);
		List<SVGPath> paths = extractPathsContainedInBox(svgElement);
		containingPubstyle.setExtractedPaths(paths);
		documentChunks1 = matchDocumentChunks();
		// this is just debug
//		int ipage = containingPubstyle.currentPage;
//		String dirRoot = containingPubstyle.dirRoot;
//		SVGSVG.wrapAndWriteAsSVG(texts, new File("target/pubstyle/"+dirRoot+"/page"+ipage+".texts.svg"));
//		SVGSVG.wrapAndWriteAsSVG(paths, new File("target/pubstyle/"+dirRoot+"/page"+ipage+".paths.svg"));
		return documentChunks1;
	}

//	public void setContainingPubstyle(SVGPubstyle svgPubstyle) {
//		this.containingPubstyle = svgPubstyle;
//	}


}
