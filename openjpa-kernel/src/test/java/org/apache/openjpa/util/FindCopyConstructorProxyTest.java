package org.apache.openjpa.util;

import static org.junit.Assert.assertEquals;

import java.awt.List;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;

import org.apache.openjpa.util.classes.FinalClass;
import org.apache.openjpa.util.classes.SuperClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(value = Parameterized.class)
public class FindCopyConstructorProxyTest {
	
	private ProxyManagerImpl proxyManager;
	private Class<?> cls;
	Class<? extends Exception> expectedException;
	Constructor<?> expectedConstructor;
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	public FindCopyConstructorProxyTest(Class<?> cls, Class<? extends Exception> expectedException, Constructor<?> expectedConstructor) {
		this.cls = cls;
		this.expectedException = expectedException;
		this.expectedConstructor = expectedConstructor;
	}
	
	@Parameters
	public static Collection<?> getParameters() throws NoSuchMethodException, SecurityException{
		
		// Otteniamo il costruttore che ha come per parametro la classe stessa (o la sua superclasse) ovvero un copy constructor
		Constructor<?> hashMapConstructor = HashMap.class.getConstructor(Map.class);
		Constructor<?> stringConstructor = String.class.getConstructor(String.class);
		Constructor<?> superClassConstructor = SuperClass.class.getConstructor(List.class);
		
		return Arrays.asList(new Object[][] {
			// Test Suite minimale
			{FinalClass.class, null, null},
			{String.class, null, stringConstructor},
			{null, NullPointerException.class, null},
			
			// Adequacy
			{HashMap.class, null, hashMapConstructor},
			{SuperClass.class, null, superClassConstructor},
		});
	}
	
	@Before
	public void configure() {
		proxyManager = new ProxyManagerImpl();
	}
	
	@Test
	public void testNewProxy(){
		if (expectedException!=null)
			exceptionRule.expect(expectedException);
		
		
		Constructor<?> copyConstructorFinded = proxyManager.findCopyConstructor(cls);
		assertEquals(expectedConstructor, copyConstructorFinded);
	}
	
}
