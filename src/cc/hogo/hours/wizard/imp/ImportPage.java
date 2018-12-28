package cc.hogo.hours.wizard.imp;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.MonthCombo;

public class ImportPage extends WizardPage implements IImportPage {

	private Text path;
	private final ImportContext context;
	private final int YEAR = Year.now().getValue();
	private final int MONTH = Month.from(LocalDate.now()).getValue()-1;

	protected ImportPage(ImportContext ctx) {
		super("Stundenimport", "Stundensätze importieren", Activator.getImageDescriptor("icons/import_48.gif"));
		context = ctx;
		setTitle("Stundensätze importieren");
		setDescription("Säzte werden aus einer Datei imortiert ... ");
		setPageComplete(true);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3, false));

		GridData left = new GridData();
		left.horizontalAlignment = GridData.FILL;

		GridData right = new GridData();
		right.horizontalSpan = 2;
		right.horizontalAlignment = SWT.LEFT;

		CLabel label = null;
		if (!context.isLoadHistory()) {

			label = new CLabel(composite, SWT.NULL);
			label.setText("Jahr:");
			label.setLayoutData(left);

			Spinner year = new Spinner(composite, SWT.BORDER);

			year.setMinimum(YEAR - 50);
			year.setMaximum(YEAR + 50);
			year.setIncrement(1);
			year.setLayoutData(right);
			year.setSelection(YEAR);
			year.addModifyListener(m -> context.setYear(year.getSelection()));

			context.setYear(YEAR);
			context.setMonth(MONTH);
			
			label = new CLabel(composite, SWT.NULL);
			label.setText("Monat:");
			label.setLayoutData(left);

			Combo month = MonthCombo.create(composite);
			month.setLayoutData(right);
			month.select(context.getMonth());
			month.addModifyListener(m -> context.setMonth(month.getSelectionIndex()));
		}

		label = new CLabel(composite, SWT.NULL);
		label.setText("Datei:");
		label.setLayoutData(left);

		GridData ld = new GridData();
		ld.horizontalAlignment = GridData.FILL;
		ld.grabExcessHorizontalSpace = true;

		path = new Text(composite, SWT.BORDER);
		path.setLayoutData(ld);
		path.addModifyListener(m -> context.setPath(path.getText()) );

		Button choose = new Button(composite, SWT.NONE);
		choose.setText("...");
		choose.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SINGLE);

				dialog.setFilterExtensions(new String[] { "*.txt", "*.csv", "*.*" });
				if (dialog.open() != null) {
					String file = dialog.getFileName();
					String parent = dialog.getFilterPath();

					File f = new File(parent, file);
					path.setText(f.getAbsolutePath());
					canFlipToNextPage();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) { //
			}
		});
		setControl(composite);
	}

	@Override
	public void reload() { //
	}
	
	@Override
	public boolean canFlipToNextPage() {
		setErrorMessage(null);
		try{ 
			context.load();
			return true;
		} catch(Exception e ) {
			setErrorMessage("Datei '"+context.getPath() +"' konnte nicht geladen werden.\n"+e.getMessage());
			return false;
		}
	}
}
