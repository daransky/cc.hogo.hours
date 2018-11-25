package cc.hogo.hours.views;

import org.daro.common.ui.AbstractView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public abstract class HoursAbstractView extends AbstractView {

	protected TabFolder 	folder;

	@Override
	public void createPartControl(Composite parent) {
		folder = new TabFolder(parent, SWT.BORDER);

		TabItem item = newTab("Tabelle");
		item.setControl(createTable(folder));
		
		item = newTab("Übersicht");
		item.setControl(createChart(folder));
		
	}

	@Override
	public void setFocus() {
	}

	protected	abstract	Composite	createTable(Composite parent);
	
	protected	abstract	Composite	createChart(Composite parent);
	
	protected	TabItem	newTab(String title)
	{
		TabItem item = new TabItem(folder, SWT.BORDER);
		item.setText(title);
		return item;
	}
}
