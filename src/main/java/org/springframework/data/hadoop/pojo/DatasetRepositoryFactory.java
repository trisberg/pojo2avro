package org.springframework.data.hadoop.pojo;

import com.cloudera.cdk.data.DatasetRepository;
import com.cloudera.cdk.data.filesystem.FileSystemDatasetRepository;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.net.URI;

/**
 */
public class DatasetRepositoryFactory implements InitializingBean {

	Configuration conf;

	DatasetRepository repo;

	String basePath = "/";

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(conf, "The configuration property is required");
		this.repo = new FileSystemDatasetRepository.Builder()
				.rootDirectory(new URI(basePath)).configuration(conf).get();
	}

	public DatasetRepository getDatasetRepository() {
		return repo;
	}
}
