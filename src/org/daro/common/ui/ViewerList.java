package org.daro.common.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ViewerList<T> implements IViewerList<T> {
	private	List<T>	impl;
	private Set<IViewerList<T>>listeners;
	
	public	ViewerList() {
		impl = new ArrayList<T>();
		listeners = new HashSet<IViewerList<T>>();
	}
	
	public ViewerList(int init)
	{
		impl = new ArrayList<T>(init);
	}
	
	public List<T>	getList() { return impl; }
	
	public int length() { return impl.size(); }
	
	public void add(T arg) {
		impl.add(arg);
		Iterator<IViewerList<T>> iterator = listeners.iterator();
		while (iterator.hasNext())
			iterator.next().add(arg);
	}

	/**
	 * @param Pair
	 */
	public void remove(T arg) {
		impl.remove(arg);
		Iterator<IViewerList<T>> iterator = listeners.iterator();
		while (iterator.hasNext())
			iterator.next().remove(arg);
	}

	/**
	 * @param Pair
	 */
	public void update(T arg) {
		Iterator<IViewerList<T>> iterator = listeners.iterator();
		while (iterator.hasNext())
			iterator.next().update(arg);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IViewerList<T> viewer) {
		listeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IViewerList<T> viewer) {
		listeners.add(viewer);
	}
}
