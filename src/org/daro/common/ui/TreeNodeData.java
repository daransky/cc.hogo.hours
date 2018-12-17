package org.daro.common.ui;

import java.util.LinkedList;

public class TreeNodeData {
	public static final int NULL_NODE = -1;
	protected int type;
	protected String name;
	protected LinkedList<TreeNodeData> children;
	protected TreeNodeData parent;
	protected Object data;

	public TreeNodeData() {
		children = new LinkedList<>();
	}

	public TreeNodeData(String name, int type) {
		children = new LinkedList<>();
		this.name = name;
		this.type = type;
	}

	public TreeNodeData(String name, TreeNodeData parent, int type) {
		children = new LinkedList<>();
		this.name = name;
		this.parent = parent;
		this.type = type;
	}

	public TreeNodeData[] getChildren() {
		TreeNodeData[] ch = new TreeNodeData[children.size()];
		System.arraycopy(children.toArray(), 0, ch, 0, children.size());
		return ch;
	}

	public boolean hasChildern() {
		return children.size() > 0;
	}

	public void addChild(TreeNodeData childs) {
		childs.parent = this;
		this.children.add(childs);
	}

	public void removeChild(TreeNodeData node) {
		children.remove(node);
	}

	public void removeChilds() {
		children.clear();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeNodeData getParent() {
		return parent;
	}

	public void setParent(TreeNodeData parent) {
		this.parent = parent;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}

}
