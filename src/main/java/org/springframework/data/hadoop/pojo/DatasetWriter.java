package org.springframework.data.hadoop.pojo;

import com.cloudera.cdk.data.Dataset;
import com.cloudera.cdk.data.DatasetDescriptor;
import com.cloudera.cdk.data.DatasetRepository;
import com.cloudera.cdk.data.NoSuchDatasetException;

import java.util.Collection;

/**
 */
public class DatasetWriter {

	private DatasetRepository repo;

	public DatasetWriter(DatasetRepository repo) {
		this.repo = repo;
	}

	public void write(Collection<?> pojos) {
		if (pojos == null && pojos.size() < 1) {
			return;
		}
		Class pojoClass = pojos.toArray()[0].getClass();
		Dataset dataset = getDataset(pojoClass);
		com.cloudera.cdk.data.DatasetWriter<Object> writer = dataset.getWriter();
		try {
			writer.open();
			for (Object pojo : pojos) {
				writer.write(pojo);
			}
		} finally {
			writer.close();
		}

	}

	private Dataset getDataset(Class pojoClass) {
		String repoName = DatasetOperations.getDatasetName(pojoClass);
		Dataset dataset = null;
		try {
			dataset = repo.load(repoName);
		}
		catch (NoSuchDatasetException ex) {
			DatasetDescriptor descriptor = new DatasetDescriptor.Builder().schema(pojoClass).get();
			dataset = repo.create(repoName, descriptor);
		}
		return dataset;
	}

}
