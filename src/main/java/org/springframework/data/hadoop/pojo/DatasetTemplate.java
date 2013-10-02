package org.springframework.data.hadoop.pojo;

import com.cloudera.cdk.data.Dataset;
import com.cloudera.cdk.data.DatasetDescriptor;
import com.cloudera.cdk.data.DatasetReader;
import com.cloudera.cdk.data.DatasetRepository;
import com.cloudera.cdk.data.DatasetWriter;
import com.cloudera.cdk.data.NoSuchDatasetException;
import com.cloudera.cdk.data.filesystem.FileSystemDatasetRepository;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class DatasetTemplate implements InitializingBean {

	Configuration conf;

	DatasetRepository repo;

	String basePath = "/";

	public DatasetTemplate() {
	}

	public DatasetTemplate(Configuration conf) {
		this.conf = conf;
	}

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

	public void write(Collection<?> pojos) {
		if (pojos == null && pojos.size() < 1) {
			return;
		}
		Class pojoClass = pojos.toArray()[0].getClass();
		Dataset dataset = getDataset(pojoClass);
		DatasetWriter<Object> writer = dataset.getWriter();
		try {
			writer.open();
			for (Object pojo : pojos) {
				writer.write(pojo);
			}
		} finally {
			writer.close();
		}

	}

	public <T, R> void read(Class<T> pojoClass, PojoCallback<T, R> callback) {
		DatasetReader<T> reader = getDataset(pojoClass).getReader();
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
		DatasetReader<T> reader = getDataset(pojoClass).getReader();
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

	public <T> void delete(Class<T> pojoClass) {
		repo.delete(getDatasetName(pojoClass));

	}

	private Dataset getDataset(Class pojoClass) {
		String repoName = getDatasetName(pojoClass);
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

	private String getDatasetName(Class pojoClass) {
		return pojoClass.getSimpleName().toLowerCase();
	}
}

