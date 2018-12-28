package org.daro.common.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class TreeContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if(parentElement == null) return new Object[0];
		TreeNodeData[] childs  = ((TreeNodeData)parentElement).getChildren();
		return childs == null ? new Object[0]: (Object[])childs;
	}

	public Object getParent(Object element) {
		return ((TreeNodeData)element).getParent();
	}

	public boolean hasChildren(Object element) {
		if(element==null) return false;
		return getChildren(element).length>0;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
