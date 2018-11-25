package org.daro.common.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

public abstract class WizardPageObject extends WizardPage {

	protected	static	Object	object;
	protected	boolean			finish;
	
	public	class BlankListenner implements ModifyListener 
	{
		private	String	error;
		private boolean first=true;
		public	BlankListenner(String message)
		{
			error = message;
		}
		
		public void modifyText(ModifyEvent e) {
			if (e.widget instanceof Text) {
				if(((Text)e.widget).getText().length() == 0)
				{
					setErrorMessage(error);
					setPageComplete(false);
				} 
				else	
					{
						setErrorMessage(null);
						setPageComplete(true);
					}
			}
			 
			if(first)
			{
				first = false;
				return;
			}
			getContainer().updateButtons();
		}
	}
	
	protected WizardPageObject() {
		super("");
	}

	public	WizardPageObject(String value)
	{
		super(value);
	}
	
	public	Object	getObject()	{	return object; }
	
	public 	void	reset() { object = null; }
}
