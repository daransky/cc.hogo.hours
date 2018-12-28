package cc.hogo.hours.wizard.imp;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class ImportAllWizard extends Wizard {

	public static final String ID = "cc.hogo.hours.wizard.ImportAllWizard";

	private ImportContext context = new ImportContext(true);
	private IWizardPage	last;
	private IWizardPage next = null;
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page.canFlipToNextPage()) {
			next = super.getNextPage(page);
			((IImportPage) next).reload();
		}
		return next;
	}

	@Override
	public boolean canFinish() {
		return next == last;
	}


	@Override
	public boolean performFinish() {
		try {
			context.importRecords();
		} catch (Exception e) {
			DialogPage page = (DialogPage) getContainer().getCurrentPage();
			page.setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void addPages() {
		last = new ImportPage2(context);
		addPage(new ImportPage(context));
		addPage(last);
	}

}
