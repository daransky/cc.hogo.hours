package cc.hogo.hours.views.log;

import java.time.format.DateTimeFormatter;

import org.eclipse.swt.graphics.Image;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.AbstractTableLabelProvider;

public class LogTableLabelProvider extends AbstractTableLabelProvider {
	private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy,hh:mm:ss");
	private Image info, warning, error;
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		LogEntry e = (LogEntry) element;
		switch (columnIndex) {
		case 1:
			return FORMATTER.format(e.getTime());
		case 2:
			return e.getMessage();
		}
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			LogEntry e = (LogEntry) element;
			switch (e.getType()) {
			case INFO:
				if( info == null )
					info = Activator.getImageDescriptor("icons/info_tsk.png").createImage();
				return info;
			case WARNING:
				if( warning == null )
					warning = Activator.getImageDescriptor("icons/warn_tsk.png").createImage();
				return warning;
			case ERROR:
				if( error == null )
					error = Activator.getImageDescriptor("icons/error_tsk.png").createImage();
				return error;
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
		if( info != null )
			info.dispose();
		if( warning != null )
			warning.dispose();
		if( error != null )
			error.dispose();
	}
}
