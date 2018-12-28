package cc.hogo.hours.wizard.imp;

import org.daro.common.ui.TableContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.Months;

public class ImportPage2 extends WizardPage implements IImportPage {

	final ImportContext ctx;
	TableViewer viewer;
	final ImportLabelProvider provider = new ImportLabelProvider();

	protected ImportPage2(ImportContext ctx) {
		super("Import Übersicht", "", Activator.getImageDescriptor("icons/import_48.gif"));
		this.ctx = ctx;

		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new FillLayout());

		Table table = new Table(composite, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn id = new TableColumn(table, SWT.NONE);
		id.setWidth(40);

		TableColumn kundenName = new TableColumn(table, SWT.NONE);
		kundenName.setText("Kundenname");
		kundenName.setWidth(300);

		TableColumn disponentId = new TableColumn(table, SWT.None);
		disponentId.setText("Disponent");
		disponentId.setWidth(80);

		TableColumn name = new TableColumn(table, SWT.None);
		name.setText("Name");
		name.setWidth(200);

		TableColumn fakturStunden = new TableColumn(table, SWT.RIGHT);
		fakturStunden.setText("Fakturstunden");
		fakturStunden.setWidth(80);

		TableColumn lohnStunden = new TableColumn(table, SWT.RIGHT);
		lohnStunden.setText("Lohnstunden");
		lohnStunden.setWidth(80);

		viewer = new TableViewer(table);
		viewer.setContentProvider(new TableContentProvider<>(viewer));
		viewer.setLabelProvider(provider);

		setControl(composite);
	}

	public void reload() {

		if (ctx.isLoadHistory()) {
			setTitle("Import Historie Übersicht");
			setDescription(String.format("Es werden %d Sätze importiert.", ctx.getHours().size()));
		} else {
			setTitle("Import Übersicht");
			setDescription(String.format("Für Monat '%s/%d' werden %d Sätze importiert.", Months.NAMES[ctx.getMonth()],
					ctx.getYear(), ctx.getHours().size()));
		}
		viewer.getTable().removeAll();
		provider.reset();
		final Object[] entries = ctx.getHours().toArray();
		if (entries != null)
			viewer.add(entries);
	}

}
