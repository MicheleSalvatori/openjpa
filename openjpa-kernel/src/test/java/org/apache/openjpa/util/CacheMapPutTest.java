package org.apache.openjpa.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class CacheMapPutTest {

	private CacheMap cacheMap;
	private Object key;
	private Object value;
	private boolean keyIsFree;
	private int cacheMaxSize = 1000;
	private boolean pinQuery;

	private Object previousObjectInKey;

	public CacheMapPutTest(Object key, Object value, boolean keyIsFree, Object cacheMaxSize, boolean pinQuery) {
		this.key = key;
		this.value = value;
		this.keyIsFree = keyIsFree;
		if (cacheMaxSize != null) {
			this.cacheMaxSize = (int) cacheMaxSize;
		}
		this.pinQuery = pinQuery;
	}

	@Before
	public void setUp() {
		this.cacheMap = new CacheMap(); 
		
		if (cacheMaxSize == 0) {
			cacheMap.setCacheSize(cacheMaxSize);
		}
		
		if (!keyIsFree) {
			previousObjectInKey = new Object();							
			cacheMap.put(key, previousObjectInKey);
			if (cacheMaxSize == 1) {
				cacheMap.setCacheSize(cacheMaxSize);				// put another element on cache so remove method on line 409 will return non null value
				cacheMap.put(new Object(), new Object());
			}
		}

		if (pinQuery) {
			if(cacheMaxSize == 1) {									// Trigger for populate cache
				previousObjectInKey = new Object();							
				cacheMap.put(key, previousObjectInKey);
			}
			cacheMap.pin(key);
		}
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		int nullSizeCache = 0;
		boolean pinKey = true;

		return Arrays.asList(new Object[][] {

				// Test suit minimale
				{ new Object(), new Object(), false, null, false},
				{ new Object(), null, true, null, false },
				{ null, new Object(), true, null, false },

				// Added for increase code coverage
				{ new Object(), new Object(), true, nullSizeCache, false}, // cache map size = 0
				{ new Object(), new Object(), true, null, pinKey }, // cache map size = 0
				{ new Object(), new Object(), true, 1, pinKey }, // cache map size = 0
				{ new Object(), new Object(), false, 1, false}
		});
	}

	@Test
	public void test() {
		Object previousValue = cacheMap.put(key, value);
		System.out.println(previousValue);

		if (!keyIsFree) {
			assertEquals(previousObjectInKey, previousValue);			// check if put method return the right value (previous object in the cache)
		} 

		Object insertedValue = cacheMap.get(key);
		if (cacheMaxSize == 0) {
			assertEquals(null, insertedValue); // put operation fails
		} else {
			assertEquals(value, insertedValue);
		}
	}

}
