package cc.hogo.hours.views.hours.year;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cc.hogo.hours.views.hours.year.HoursYearTableEntry.Office;

public class HoursYearTreeeTableContentProvider implements ITreeContentProvider {

	Collection<HoursYearTableEntry> root; 
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			root = ((Collection<HoursYearTableEntry>)inputElement);
			return root.toArray();
		}
		if (inputElement instanceof HoursYearTableEntry) {
			HoursYearTableEntry entry = (HoursYearTableEntry)inputElement;
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
		if (parentElement instanceof HoursYearTableEntry) {
			HoursYearTableEntry entry = (HoursYearTableEntry)parentElement;			
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
		if (inputElement instanceof HoursYearTableEntry) {
			HoursYearTableEntry entry = (HoursYearTableEntry)inputElement;
			return entry.office != null && entry.office.length != 0;
		}
		return false;
	}
}
