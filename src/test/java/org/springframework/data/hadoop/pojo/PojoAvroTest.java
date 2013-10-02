package org.springframework.data.hadoop.pojo;

import com.springdeveloper.hadoop.Address;
import com.springdeveloper.hadoop.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/application-context.xml")
public class PojoAvroTest {

	@Autowired
	DatasetTemplate dsTemplate;

	@Test
	public void testWritePojos() {
		Customer c1 = new Customer();
		c1.setId(23L);
		c1.setName("Sven");
		Address a1 = new Address();
		a1.setStreet("123 Main St");
		a1.setCity("London");
		a1.setPostalCode("12345");
		c1.setAddresses(Collections.singletonList(a1));
		Customer c2 = new Customer();
		c2.setId(46L);
		c2.setName("John");
		Address a2 = new Address();
		a2.setStreet("123 South St");
		a2.setCity("Philadelphia");
		a2.setPostalCode("19147");
		c2.setAddresses(Collections.singletonList(a2));
		System.out.println("WRITE: " + c1.getClass().getName() + " :: " + c1);
		System.out.println("WRITE: " + c2.getClass().getName() + " :: " + c2);
		dsTemplate.write(Arrays.asList(new Customer[] {c1, c2}));
	}

	@Test
	public void testReadPojos() {
		for (Customer c : dsTemplate.read(Customer.class)) {
			System.out.println("READ: " + c.getClass().getName() + " :: " + c);
		}
	}

	@Test
	public void testReadWithCallback() {
	    dsTemplate.read(Customer.class, new PojoCallback<Customer, Object>() {
		    public Object doInPojo(Customer c) {
			    System.out.println("CALLBACK: " + c.getClass().getName() + " :: " + c);
				return null;
		    }
	    });
	}

	@Test
	public void testDeletePojos() {
		dsTemplate.delete(Customer.class);
	}

}