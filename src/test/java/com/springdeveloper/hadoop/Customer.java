package com.springdeveloper.hadoop;

import java.util.List;

/**
 */
public class Customer {

	private Long id;
	private String name;
	private List<Address> addresses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "Customer [" + id + "] " + name + " :: " + addresses;
	}
}
