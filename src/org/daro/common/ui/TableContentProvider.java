package org.daro.common.ui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class TableContentProvider<T> implements IStructuredContentProvider, IViewerList<T> {

private	TableViewer	viewer;
	
	public TableContentProvider(TableViewer viewer)
	{
		this.viewer = viewer;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		return ((IViewerList<T>)viewer.getInput()).getList().toArray();
	}

	@SuppressWarnings("unchecked")
	public void dispose() {
		IViewerList<T> input = (IViewerList<T>)viewer.getInput();
		if(input != null)
			input.removeChangeListener(this);
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		if (newInput != null)
			((IViewerList<T>) newInput).addChangeListener(this);
		if (oldInput != null)
			((IViewerList<T>) oldInput).removeChangeListener(this);
	}
	
	public void add(T arg) {
		viewer.add(arg);
	}

	public void remove(T arg) {
		viewer.remove(arg);
	}

	public void update(T arg) {
		viewer.update(arg, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> getList() {
		return ((IViewerList<T>)viewer.getInput()).getList();
	}

	public void addChangeListener(IViewerList<T> viewer) {}

	public void removeChangeListener(IViewerList<T> viewer) {}
}
