package cc.hogo.hours.wizard.imp;

import java.io.File;
import java.io.IOException;
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

	private Spinner year;
	private Combo month;
	private Text path;
	private final ImportContext context;
	private int eval = 0;

	protected ImportPage(ImportContext ctx) {
		super("Stundenimport", "Stundensätze importieren", Activator.getImageDescriptor("icons/import_48.gif"));
		context = ctx;
		setTitle("Stundensätze importieren");
		setDescription("Säzte werden aus einer Datei imortiert ... ");
		setPageComplete(false);
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

		CLabel label = new CLabel(composite, SWT.NULL);
		label.setText("Jahr:");
		label.setLayoutData(left);

		year = new Spinner(composite, SWT.BORDER);
		int now = Year.now().getValue();
		year.setMinimum(now - 50);
		year.setMaximum(now + 50);
		year.setIncrement(1);
		year.setLayoutData(right);
		year.setSelection(now);
		year.addModifyListener( m -> { if( checkFields() ) context.setYear(year.getSelection()); } );

		context.setYear(now);
		
		label = new CLabel(composite, SWT.NULL);
		label.setText("Monat:");
		label.setLayoutData(left);

		month = MonthCombo.create(composite);
		month.setLayoutData(right);
		month.select(0);
		month.addModifyListener( m -> { if( checkFields() ) context.setMonth(month.getSelectionIndex()); } ); 
		
		label = new CLabel(composite, SWT.NULL);
		label.setText("Datei:");
		label.setLayoutData(left);

		GridData ld = new GridData();
		ld.horizontalAlignment = GridData.FILL;
		ld.grabExcessHorizontalSpace = true;

		path = new Text(composite, SWT.BORDER);
		path.setLayoutData(ld);
		path.addModifyListener( m -> { if(checkFields()) context.setPath(path.getText()); } );
		
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
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		setControl(composite);
		eval++;
	}
	
	private boolean checkFields() { 

		if (year.getSelection() <= 0) {
			setErrorMessage("Jahr ist ungültig.");
			return false;
		}
		if (month.getSelectionIndex() == -1 ) {
			setErrorMessage("Monat ist ungültig.");
			return false;
		}
		
		if (path.getText().isEmpty()) {
			setErrorMessage("Datei für import darf nicht leer sein.");
			return false;
		}
		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	public boolean canFlipToNextPage() {
		setErrorMessage(null);
		if( eval++ > 1 ) {
			try {
				context.load();
			} catch (IOException e) {
				setErrorMessage(e.getMessage());
			}
			return true;
		} 
		return false;
	}

	@Override
	public void reload() {
	}
}
