package org.apache.openjpa.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class CacheMapRemoveTest {

	private CacheMap cacheMap;
	private Object key;
	private Object value;
	private boolean pinQuery;
	private boolean keyIsFree;

	private Object previousObjectInKey;

	public CacheMapRemoveTest(Object key, boolean keyIsFree, boolean pinQuery) {
		this.key = key;
		this.keyIsFree = keyIsFree;
		this.pinQuery = pinQuery;
	}

	@Before
	public void setUp() {
		this.cacheMap = new CacheMap(); 
		
		value = null;
		if (!keyIsFree) {
			value = new Object();
			cacheMap.put(key, value);
		}
			
		if (pinQuery) {
			cacheMap.pin(key);
		}
		
		
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		int nullSizeCache = 0;
		boolean pinKey = true;

		return Arrays.asList(new Object[][] {
				
			// Test suite già migliorata per coverage
				{ new Object(), false, false},
				{ new Object(), false, pinKey},
				{ new Object(), true, false},					// cacheMap is empty so remove return null
				{ new Object(), true, pinKey},					// Viene pinnata la key ma senza value
		});
	}

	@Test
	public void testRemove() {
		Object deletedValue = cacheMap.remove(key);
		
		assertEquals(deletedValue, value);
	}

}

/* REMOVE
 * Removes the mapping for a key from this map if it is present(optional operation). More formally, if this map contains a 
 * mappingfrom key k to value v such that Objects.equals(key, k), that mappingis removed. (The map can contain at most one such mapping.) 

Returns the value to which this map previously associated the key,or null if the map contained no mapping for the key. 

If this map permits null values, then a return value of null does not necessarily indicate that the mapcontained no mapping for the key; 
it's also possible that the mapexplicitly mapped the key to null. 

The map will not contain a mapping for the specified key once thecall returns.
*/
