package org.springframework.data.hadoop.pojo;

import com.cloudera.cdk.data.DatasetRepository;

/**
 */
public interface DatasetRepositoryCallback {

	void doInRepository(DatasetRepository datasetRepository, String datasetName);

}
