package org.apache.openjpa.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.openjpa.enhance.PersistenceCapable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(value = Parameterized.class)
public class CopyArrayProxyTest {
	
	private ProxyManagerImpl proxyManager;
	private Object array;
	
	private Class<? extends Exception> expectedException;
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	public CopyArrayProxyTest(Object array, Class<? extends Exception> expectedException) {
		this.array = array;
		this.expectedException = expectedException;
	}
	
	@Parameters
	public static Collection<?> getParameters(){
		
		String[] list = new String[] {"1", "2"};
		
		return Arrays.asList(new Object[][] {
			// Test Suite minimale
			{null, null},
			{new String(), UnsupportedException.class},
			{list, null}
		});
	}
	
	@Before
	public void configure() {
		proxyManager = new ProxyManagerImpl();
	}
	
	@Test
	public void testNewProxy() {
		
		if (expectedException != null)
			exceptionRule.expect(expectedException);
		
		Object clonedArray = proxyManager.copyArray(array);
		System.out.println(clonedArray);
		System.out.println(array);
		
		if (array != null) {
			assertArrayEquals((Object[]) array, (Object[])clonedArray);
		}else{
			assertNull(clonedArray);
		}
		
	}
	
}
