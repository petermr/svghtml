package org.xmlcml.graphics.svg.util;

import java.awt.Color;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.graphics.svg.util.ColorStore.ColorizerType;

public class ColorStoreTest {
	private static final Logger LOG = Logger.getLogger(ColorStoreTest.class);
	
	static {
		LOG.setLevel(Level.DEBUG);
	}

	@Test
	public void testColorStore() {
		ColorStore colorStore = ColorStore.createColorizer(ColorizerType.CONTRAST);
		Iterator<String> colorIterator = colorStore.getColorIterator();
		int count = 0;
		while (count++ < 20 && colorIterator.hasNext()) {
			LOG.debug(">> "+colorIterator.next());;
		}
	}
	
	@Test
	public void testJavaColorStore() {
		ColorStore colorStore = ColorStore.createColorizer(ColorizerType.CONTRAST);
		Iterator<Color> colorIterator = colorStore.getJavaColorIterator();
		int count = 0;
		while (count++ < 20 && colorIterator.hasNext()) {
			LOG.debug(">> "+colorIterator.next());;
		}
	}
	@Test
	public void testJavaColorStore1() {
		Iterator<Color> colorIterator = ColorStore.getJavaColorIterator(ColorizerType.CONTRAST);
		int count = 0;
		while (count++ < 20 && colorIterator.hasNext()) {
			LOG.debug(">> "+colorIterator.next());;
		}
	}
}
