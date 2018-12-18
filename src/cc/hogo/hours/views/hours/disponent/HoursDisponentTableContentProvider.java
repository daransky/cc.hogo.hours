package cc.hogo.hours.views.hours.disponent;

import java.sql.SQLException;
import java.util.Collection;

import org.daro.common.ui.UIError;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.hogo.hours.views.hours.disponent.HoursDisponentTableEntry.SubEntry;

public class HoursDisponentTableContentProvider implements ITreeContentProvider {

	Collection<HoursDisponentTableEntry> root; 
	final HoursViewDisponentModel	model;
	
	public HoursDisponentTableContentProvider(HoursViewDisponentModel model) {
		this.model = model;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			root = ((Collection<HoursDisponentTableEntry>)inputElement);
			return root.toArray();
		}
		if (inputElement instanceof HoursDisponentTableEntry) {
			HoursDisponentTableEntry entry = (HoursDisponentTableEntry)inputElement;
			final Float[] ovalues = new Float[13];
			final float[] pvalues = entry.current;
			for(int i = 0; i < 13; i++ ) {
				ovalues[i] = pvalues[i];
			}
			return ovalues;
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Collection<?>) {
			return ((Collection<?>)parentElement).toArray();
		}
		if (parentElement instanceof HoursDisponentTableEntry) {
			HoursDisponentTableEntry entry = (HoursDisponentTableEntry)parentElement;
			if( entry.childs == null || entry.childs.length == 0) {
				try {
					model.loadSubEntries(entry, 2018);
					return entry.childs;
				} catch (SQLException e) {
					UIError.showError("DB Fehler", e.getMessage());
				}
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Collection<?>) {
			return root;
		}
		
		if (element instanceof SubEntry) {
			SubEntry o = (SubEntry)element;
			return o.parent;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			return !((Collection<?>)inputElement).isEmpty();
		}
		if (inputElement instanceof SubEntry) {
			return false;
		}
		return true;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { //
	}

}
