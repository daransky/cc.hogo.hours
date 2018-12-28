package org.daro.common.ui;

import java.util.List;

public interface IViewerList<T> {

	public	void add(T arg);
	
	public 	void remove(T arg);
	
	public 	void update(T arg);
	
	public	List<T>	getList();
	
	public void removeChangeListener(IViewerList<T> viewer);

	public void addChangeListener(IViewerList<T> viewer);

}
