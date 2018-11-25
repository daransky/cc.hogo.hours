package cc.hogo.hours.views.hoursimport;

import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import org.daro.common.ui.AbstractView;
import org.daro.common.ui.TableContentProvider;
import org.daro.common.ui.UIError;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.core.MonthCombo;
import cc.hogo.hours.db.DB;
import cc.hogo.hours.db.HourEntry;

public class HoursImportView extends AbstractView {

	public static final String ID = "cc.hogo.hours.views.hoursimport.HoursImportView";
	private HoursImportModel model;
	private TableViewer viewer;
	private Image refreshImg = Activator.getImageDescriptor("icons/refresh.gif").createImage();
	private Image addImg = Activator.getImageDescriptor("icons/add.gif").createImage();

	Combo month;
	Spinner year;

	@Override
	public void refresh() {
		viewer.getTable().removeAll();
		try {
			List<HourEntry> list = model.read(month.getSelectionIndex(), year.getSelection());
			viewer.add(list.toArray());
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}
	}

	@Override
	public String getViewID() {
		return ID;
	}

	@Override
	public void createPartControl(Composite parent) {

		try {
			model = HoursImportModel.open();
		} catch (SQLException e1) {
			UIError.showError("DB Fehler", e1);
		}

		parent.setLayout(new GridLayout(1, false));

		GridLayout topLayout = new GridLayout(3, false);

		Group top = new Group(parent, SWT.NONE);
		top.setLayout(topLayout);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createFilterComposite(top, "Jahr", p -> {
			year = new Spinner(p, SWT.NONE);
			final int value = Year.now().getValue();
			year.setMaximum(value + 11);
			year.setMinimum(value - 11);
			year.setSelection(value);

			return year;
		});

		createFilterComposite(top, "Monat", p -> month = MonthCombo.create(p));
		month.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				refresh();
			}
		});

		Button refresh = new Button(top, SWT.NONE);
		refresh.setImage(refreshImg);
		refresh.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				refresh();
			}
		});

		Button add = new Button(top, SWT.NONE);
		add.setImage(addImg);
		add.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HourEntry entry = new HourEntry();
				HoursEditDialog dialog = new HoursEditDialog(Display.getCurrent().getActiveShell(), entry);
				if (dialog.open() == Window.OK) {
					entry = dialog.getEntry();
					model.add(entry);
					refresh();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Table table = new Table(parent, SWT.FULL_SELECTION);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) { //
			}

			@Override
			public void mouseDown(MouseEvent e) { //
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem item = table.getItem(table.getSelectionIndex());
				HourEntry entry = (HourEntry) item.getData();
				try {
					entry = model.read(entry.getId());
					HoursEditDialog dialog = new HoursEditDialog(table.getShell(), entry);
					if (dialog.open() == Window.OK) {
						entry = dialog.getEntry();
						model.update(entry);
						item.setData(entry);
						viewer.refresh(entry);
					}
					;
				} catch (SQLException e1) {
					try {
						UIError.showError("DB Fehler", e1);
						DB.instance().getConnection().rollback();
					} catch (SQLException e2) {
						UIError.showError("DB Fehler", e2);
					}
				}
			}
		});

		viewer = new TableViewer(table);
		addColumn("Disponent", 200);
		addColumn("Kundenname", 300);
		addColumn("Stunden", 100, SWT.RIGHT);
		addColumn("Info", 300);

		viewer.setContentProvider(new TableContentProvider<>(viewer));
		viewer.setLabelProvider(new AbstractTableLabelProvider() {

			@Override
			public String getColumnText(Object element, int columnIndex) {
				HourEntry e = (HourEntry) element;
				switch (columnIndex) {
				case 0:
					return model.getDisponentName(e.getDisponentId());
				case 1:
					return e.getKundenName();
				case 2:
					return String.format(Locale.GERMAN, "%,.2f", e.getFakturStunden());
				case 3:
					return e.getInfo();
				default:
					return null;
				}
			}
		});
	}

	@Override
	public void setFocus() { //

	}

	private void addColumn(String text, int width) {
		addColumn(text, width, SWT.NONE);
	}

	private void addColumn(String text, int width, int swt) {
		TableViewerColumn c = new TableViewerColumn(viewer, swt);
		c.getColumn().setText(text);
		c.getColumn().setWidth(width);
	}

	private Composite createFilterComposite(Composite parent, String label,
			Function<Composite, ? extends Control> controlCreator) {
		Composite filter = new Composite(parent, SWT.NONE);
		filter.setLayout(new GridLayout(2, false));
		filter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label l = new Label(filter, SWT.NONE);
		l.setText(label);

		Control control = controlCreator.apply(filter);
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return filter;
	}

	@Override
	public void dispose() {
		refreshImg.dispose();
		addImg.dispose();
		super.dispose();
	}
}
