package org.apache.openjpa.util.classes;

import java.awt.Component;
import java.awt.List;

import org.apache.openjpa.util.ImplHelper;

/*
 * Classe creata per FindCopyConstructorProxyTest
 * Implementa diversi costruttori per simulare un determinato caso gestito dal metodo testato
 * Creata per aumentare risultati coverage
 */
@SuppressWarnings("serial")
public class SuperClass extends List{
	private int val;
	
	public SuperClass(String s) {}
	
	public SuperClass(Object s) {
	}
	
	public SuperClass(List cls) {
	}

	public SuperClass(Component cls) {
	}
	
	public int getVal() {
		return this.val;
	}
	
	
	public static void main(String[] args) {
		boolean result = ImplHelper.isAssignable(Component.class, List.class);
		System.out.println(result);
	}
}

