package org.springframework.data.hadoop.pojo;

import com.cloudera.cdk.data.Dataset;
import com.cloudera.cdk.data.DatasetRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class DatasetReader {

	private DatasetRepository repo;

	public DatasetReader(DatasetRepository repo) {
		this.repo = repo;
	}

	public <T, R> void read(Class<T> pojoClass, PojoCallback<T, R> callback) {
		com.cloudera.cdk.data.DatasetReader<T> reader = getDataset(pojoClass).getReader();
		try {
			reader.open();
			for (T t : reader) {
				callback.doInPojo(t);
			}
		} finally {
			reader.close();
		}
	}

	public <T> Collection<T> read(Class<T> pojoClass) {
		com.cloudera.cdk.data.DatasetReader<T> reader = getDataset(pojoClass).getReader();
		List<T> results = new ArrayList<T>();
		try {
			reader.open();
			for (T t : reader) {
				results.add(t);
			}
		} finally {
			reader.close();
		}
		return results;
	}

	private Dataset getDataset(Class pojoClass) {
		String repoName = DatasetOperations.getDatasetName(pojoClass);
		Dataset dataset = null;
		dataset = repo.load(repoName);
		return dataset;
	}

}
