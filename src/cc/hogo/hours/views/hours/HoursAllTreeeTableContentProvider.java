package cc.hogo.hours.views.hours;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.hogo.hours.views.hours.HoursAllTableEntry.Office;

public class HoursAllTreeeTableContentProvider implements ITreeContentProvider {

	Collection<HoursAllTableEntry> root; 
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			root = ((Collection<HoursAllTableEntry>)inputElement);
			return root.toArray();
		}
		if (inputElement instanceof HoursAllTableEntry) {
			HoursAllTableEntry entry = (HoursAllTableEntry)inputElement;
			final Float[] ovalues = new Float[13];
			final float[] pvalues = entry.getValues();
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
		if (parentElement instanceof HoursAllTableEntry) {
			HoursAllTableEntry entry = (HoursAllTableEntry)parentElement;			
			return entry.office;
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Collection<?>) {
			return root;
		}
		
		if (element instanceof Office) {
			Office o = (Office)element;
			return o.parent;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			return !((Collection<?>)inputElement).isEmpty();
		}
		if (inputElement instanceof HoursAllTableEntry) {
			HoursAllTableEntry entry = (HoursAllTableEntry)inputElement;
			return entry.office != null && entry.office.length != 0;
		}
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
