package cc.hogo.hours.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractTableLabelProvider implements ITableLabelProvider {

	private List<ILabelProviderListener> listeners = new ArrayList<>();

	@Override
	public void addListener(ILabelProviderListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}
