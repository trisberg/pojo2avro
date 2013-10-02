package org.springframework.data.hadoop.pojo;

/**
 */
public interface PojoCallback<T, R> {

	R doInPojo(T pojo);

}
