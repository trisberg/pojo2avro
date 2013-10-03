package org.springframework.data.hadoop.pojo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 */
public class DatasetOperations implements InitializingBean {

	DatasetRepositoryFactory dsFactory;

	public void setDatasetRepositoryFactory(DatasetRepositoryFactory datasetRepositoryFactory) {
		this.dsFactory = datasetRepositoryFactory;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dsFactory, "The datasetRepositoryFactory property is required");
	}

	public DatasetWriter getDatasetWriter() {
		return new DatasetWriter(dsFactory.getDatasetRepository());
	}

	public DatasetReader getDatasetReader() {
		return new DatasetReader(dsFactory.getDatasetRepository());
	}

	public void execute(Class pojoClass, DatasetRepositoryCallback callback) {
		callback.doInRepository(dsFactory.getDatasetRepository(), getDatasetName(pojoClass));
	}

	protected static String getDatasetName(Class pojoClass) {
		return pojoClass.getSimpleName().toLowerCase();
	}
}
