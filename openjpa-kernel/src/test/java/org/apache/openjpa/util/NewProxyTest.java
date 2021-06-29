package org.apache.openjpa.util;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.util.classes.FinalClass;
import org.apache.openjpa.util.classes.ManageableClass;
import org.apache.openjpa.util.classes.NonProxableClass;
import org.apache.openjpa.util.classes.ProxableClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(value = Parameterized.class)
public class NewProxyTest {
	
	private ProxyManagerImpl proxyManager;
	private Object object;
	private boolean autoOff;
	
	private Object expectedObjected;
	
	public NewProxyTest(Object object, boolean autoOff) {
		this.object = object;
		this.autoOff = autoOff;
		
		if (object instanceof NonProxableClass || object instanceof FinalClass || object instanceof PersistenceCapable) {
			expectedObjected = null;
		}else expectedObjected = object;
	}
	
	@Parameters
	public static Collection<?> getParameters(){
		ProxableClass proxableClass = new ProxableClass();
		proxableClass.setValue("test");
		
		Proxy proxy = new ProxyManagerImpl().newDateProxy(Date.class);
		HashMap<Integer, String> hashMap = new HashMap<>();
		hashMap.put(new Integer(1), "integer_1");
		hashMap.put(new Integer(2), "integer_2");
		hashMap.put(new Integer(3), "integer_3");
		
		Timestamp timestamp = new Timestamp(123L);
		GregorianCalendar calendar = new GregorianCalendar();
		List<String> strings = new ArrayList<>();
		strings.add("string_1");
		
		TreeSet<String> set = new TreeSet<>(strings);
		TreeMap<Integer, String> treeMap = new TreeMap<>(hashMap);
		
		
		return Arrays.asList(new Object[][] {
			// Test Suite minimale
				{ proxableClass, false},
				{ new NonProxableClass(), true},
				{ null, true},
				
			// Adeguacy
//				
				{proxy, true},
				{hashMap, true},
				{new Date(), true},
				{timestamp, true},
				{calendar, true},
				{strings, true},
				{set, true},
				{treeMap, true},
				{new FinalClass(), true},
				{new ManageableClass(), true},							//Dummy implementation of PersistenceCapable
		});
	}
	
	@Before
	public void configure() {
		proxyManager = new ProxyManagerImpl();
	}
	
	@Test
	public void testNewProxy() {
		Object proxedObject = proxyManager.newCustomProxy(object, autoOff);
			
		System.out.println(object);
		System.out.println(proxedObject);
		assertEquals(expectedObjected
				, proxedObject);
		
	}
	
}
