package org.apache.openjpa.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class CacheMapPinTest {

	private CacheMap cacheMap;
	private Object key;
	private boolean expectedResult;
	private boolean cacheEmpty;
	private boolean alreadyPinned;


	public CacheMapPinTest(Object key, boolean expectedResult, boolean cacheEmpty, boolean alreadyPinned) {
		this.key = key;
		this.expectedResult = expectedResult;
		this.cacheEmpty = cacheEmpty;
		this.alreadyPinned = alreadyPinned;
	}

	@Before
	public void setUp() {
		this.cacheMap = new CacheMap(); 
		
		if (!cacheEmpty) {
			this.cacheMap.put(key, new Object());			// Populate cache
		}
		
		if (alreadyPinned) {
			this.cacheMap.pin(key);
		}
		
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		boolean alreadyPinned = true;
		boolean cacheEmpty = true;

		return Arrays.asList(new Object[][] {

				// Test suit minimale
				{ new Object(), true, false, false},
				{ null, true, false, false},
				
				// Added
				{ new Object(), false, cacheEmpty, false},				// cacheEmpty -> viene pinnata lo stesso la query con value = null
				{ new Object(), true, false, alreadyPinned}	,			// se la key è già pinnata ritorna true 
				{ null, false, cacheEmpty, alreadyPinned}				// for cover statement 296 (= null)
				});
	}

	@Test
	public void testPin() {
		boolean result = cacheMap.pin(key);
		assertEquals(expectedResult, result);
		
		Set<?> pinnedKeys = cacheMap.getPinnedKeys();
		boolean contains = pinnedKeys.contains(this.key);
		System.out.println(pinnedKeys);
		System.out.println(contains);
		assertTrue(contains);
	}

}
