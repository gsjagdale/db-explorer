package com.shri.db.explorer.rest.service.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RSasJson {
	@XmlElement
	@Deprecated
	private final List<String> columnNames;
	@XmlElement
	private final List<String[]> data = new ArrayList<String[]>();

	public List<String[]> getData() {
		return data;
	}

	private final String[] rowDataSet;

	public RSasJson(List<String> columns) {
		columnNames = Collections.unmodifiableList(columns);
		rowDataSet = new String[columnNames.size()];
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	// public int getDataSize() {
	// return rowDataSet.length;
	// }

	private String[] getNewDataSet() {
		return rowDataSet.clone();
	}

	// public int copyToDataSet(String[] src) {
	// final String[] data = getNewDataSet();
	// System.arraycopy(src, 0, data, 0, src.length);
	// rsDatas.add(data);
	// return rsDatas.size() - 1;
	// }

	public String[] addNewData() {
		final String[] datas = getNewDataSet();
		data.add(datas);
		return datas;
	}
}
