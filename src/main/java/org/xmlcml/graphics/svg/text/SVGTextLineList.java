package org.xmlcml.graphics.svg.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.graphics.svg.SVGG;

public class SVGTextLineList implements List<SVGTextLine> {
	private static final Logger LOG = Logger.getLogger(SVGTextLineList.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private List<SVGTextLine> textLineList;
	
	public SVGTextLineList() {
		textLineList = new ArrayList<SVGTextLine>();
	}

	public SVGTextLineList(List<SVGTextLine> textLineList) {
		this.textLineList = new ArrayList<SVGTextLine>(textLineList);
	}

	public int size() {
		return textLineList.size();
	}

	public boolean isEmpty() {
		return textLineList.isEmpty();
	}

	public boolean contains(Object o) {
		return textLineList.contains(o);
	}

	public Iterator<SVGTextLine> iterator() {
		return textLineList.iterator();
	}

	public Object[] toArray() {
		return textLineList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return textLineList.toArray(a);
	}

	public boolean add(SVGTextLine e) {
		return textLineList.add(e);
	}

	public boolean remove(Object o) {
		return textLineList.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return textLineList.containsAll(c);
	}

	public boolean addAll(Collection<? extends SVGTextLine> c) {
		return textLineList.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends SVGTextLine> c) {
		return textLineList.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return textLineList.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return textLineList.retainAll(c);
	}

	public void clear() {
		textLineList.clear();
	}

	public SVGTextLine get(int index) {
		return textLineList.get(index);
	}

	public SVGTextLine set(int index, SVGTextLine element) {
		return textLineList.set(index, element);
	}

	public void add(int index, SVGTextLine element) {
		textLineList.add(element);
	}

	public SVGTextLine remove(int index) {
		return textLineList.remove(index);
	}

	public int indexOf(Object o) {
		return textLineList.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return textLineList.lastIndexOf(o);
	}

	public ListIterator<SVGTextLine> listIterator() {
		return textLineList.listIterator();
	}

	public ListIterator<SVGTextLine> listIterator(int index) {
		return textLineList.listIterator(index);
	}

	public List<SVGTextLine> subList(int fromIndex, int toIndex) {
		return textLineList.subList(fromIndex, toIndex);
	}

	public RealArray calculateIndents(int ndecimal) {
		RealArray indents= new RealArray();
		for (SVGTextLine textLine : textLineList) {
			double x = textLine.getLeftX();
			indents.addElement(x);
		}
		indents.format(ndecimal);
		return indents;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (SVGTextLine textLine : textLineList) {
			sb.append(textLine.toString()+"\n");
		}
		return sb.toString();
	}

	public SVGG createSVGElement() {
		SVGG g = new SVGG();
		for (SVGTextLine textLine : textLineList) {
			g.appendChild(textLine.createSVGElement().copy());
		}
		return g;
	}

	public List<Double> getYCoords() {
		List<Double> yCoordList = new ArrayList<Double>();
		for (SVGTextLine textLine : textLineList) {
			yCoordList.add((Double)textLine.getY());
		}
		return yCoordList;
	}
}
