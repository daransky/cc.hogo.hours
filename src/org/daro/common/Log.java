package org.daro.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class Log {
	
	public class LogFormater 
	{
		protected	StringBuffer	buffer;
		
		public	LogFormater()	{	buffer = new StringBuffer(256); }
		
		public 	String	format(int severity, String message)
		{
			
			Calendar c = Calendar.getInstance();
			buffer.delete(0, buffer.length());
			int i = c.get(Calendar.DAY_OF_MONTH);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append('/');
			i = c.get(Calendar.MONTH);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append('/');
			i = c.get(Calendar.DAY_OF_MONTH);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append(' ');
			i = c.get(Calendar.HOUR_OF_DAY);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append(':');
			i = c.get(Calendar.MINUTE);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append(':');
			i = c.get(Calendar.SECOND);
				if( i < 10 ) buffer.append('0');
			buffer.append(i).append(' ').append((char)severity).append(' ').append(message);		
			
			return buffer.toString();
		}
		
		public	String	format(Exception exception)	
		{
			return format(Level.SEVERE, exception.getMessage());
		}
	}
	
	public interface Level {
		public static final int SEVERE 	= 'E';
		public static final int WARNING = 'W';
		public static final int INFO 	= 'I';
	}
	
	private static final byte[]	crlf	= { '\n' };
	private OutputStream	stream;
	private LogFormater		format;
	private static Log		singelton; 
	
	public Log(OutputStream os) {
		stream = os;
		format = new LogFormater();
	}
	
	public 	static boolean initialized() { return singelton != null; }
	
	public 	static Log getInstance() { return singelton; }
	
	public 	static Log	createInstance(OutputStream os)
	{
		if(singelton != null)
		{
			singelton.close();
		}
		singelton = new Log(os);
		return singelton;
	}
	
	public	synchronized void	log(Exception	exception)
	{
		try {
			stream.write(format.format(exception).getBytes());
			stream.write(crlf);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public	synchronized void	log(int level, String message)
	{
		try {
			stream.write(format.format(level, message).getBytes());
			stream.write(crlf);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public	synchronized void 	close()
	{
		try {
			if( stream != null )
			{	
				stream.close();
				stream = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
