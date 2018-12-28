package cc.hogo.hours.views.log;

import org.daro.common.ui.AbstractView;
import org.daro.common.ui.TableContentProvider;
import org.daro.common.ui.UIError;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.CustomAction;

public class LogView extends AbstractView {

	public static final String ID = "cc.hogo.hours.views.log.LogView";
	private LogModel	model;
	private LogTable	viewer = null;
	private Table		table = null;
	
	@Override
	public void refresh() {
		table.removeAll();
		
		try {
			viewer.add(model.select().toArray());
		} catch (Exception e) {
			UIError.showError("DB Fehler", e);
		}
	}

	@Override
	public String getViewID() {
		return ID;
	}

	@Override
	public void createPartControl(Composite parent) {
		table = new Table(parent,SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new LogTable(table);
		viewer.setContentProvider(new TableContentProvider<>(viewer));
		
		CustomAction refresh = new CustomAction("Laden", Activator.getImageDescriptor("icons/refresh.gif"), 
				() -> refresh());
		
		IToolBarManager toolBar = getViewSite().getActionBars().getToolBarManager(); 
		toolBar.add(refresh);

		try {
			model = LogModel.open();
			refresh();
		} catch (Exception e) {
			UIError.showError("DB Fehler", e);
		}
	}

	@Override
	public void setFocus() {//
	}
	
	@Override
	public void dispose() {
		super.dispose();
		try {
			model.close();
		} catch (Exception e) {
			UIError.showError("DB Fehler", e);
		}
	}

}
