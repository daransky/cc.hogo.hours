package cc.hogo.hours;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import cc.hogo.hours.views.disponent.DisponentView;
import cc.hogo.hours.views.hours.HoursViewAll;
import cc.hogo.hours.views.hours.disponent.HoursViewDisponent;
import cc.hogo.hours.views.hoursimport.HoursImportView;
import cc.hogo.hours.views.log.LogView;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, (float)0.15, editorArea); //$NON-NLS-1$
	
		leftFolder.addView(DisponentView.ID);
		leftFolder.addPlaceholder(DisponentView.ID);
		
		IFolderLayout topFolder = layout.createFolder("top", IPageLayout.TOP, (float)0.85, editorArea); //$NON-NLS-1$
		topFolder.addView(HoursViewAll.ID);
		topFolder.addPlaceholder(HoursViewDisponent.ID);
		topFolder.addPlaceholder(HoursViewAll.ID);
		topFolder.addPlaceholder(HoursViewDisponent.ID);
		topFolder.addPlaceholder(HoursImportView.ID);
		topFolder.addPlaceholder(LogView.ID);
		
	}

}
