package org.xmlcml.graphics.svg.util;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SVGHtmlProperties extends Properties {
	private static final Logger LOG = Logger.getLogger(SVGHtmlProperties.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	public final static String SVGHTML_PROPERTIES = "/org/xmlcml/svghtml.properties";
	public final static String PROPERTIES_XML = "/org/xmlcml/properties.xml";
	
	public static Properties createSVGHtmlProperties() {
		return createProperties(SVGHTML_PROPERTIES);
	}

	public static Properties createProperties(String propertiesResource) {
		if (propertiesResource == null) {
			throw new RuntimeException("null propertiesResource");
		}
		Properties properties = new Properties();
		try {
			properties.load(properties.getClass().getResourceAsStream(propertiesResource));
		} catch (InvalidPropertiesFormatException e) {
			throw new RuntimeException("Bad properties file: "+propertiesResource, e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read properties file: "+propertiesResource, e);
		}
		return properties;
	}
	
	/** this may have problems with DTDs and validation
	 * 
	 * @return
	 */
	public static Properties createSVGHtmlPropertiesXML() {
		return createPropertiesFeomXML(PROPERTIES_XML);
	}

	private static Properties createPropertiesFeomXML(String propertiesXML) {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(properties.getClass().getResourceAsStream(propertiesXML));
		} catch (InvalidPropertiesFormatException e) {
			throw new RuntimeException("Bad properties file: "+propertiesXML, e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read properties file: "+propertiesXML, e);
		}
		return properties;
	}
	
	public static void main(String[] args) {
		Properties properties = SVGHtmlProperties.createSVGHtmlProperties();
		LOG.debug("props "+properties);
	}
}
