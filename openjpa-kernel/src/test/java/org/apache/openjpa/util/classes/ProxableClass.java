package org.apache.openjpa.util.classes;

public class ProxableClass {
	
	private String value;
	
	public ProxableClass() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	// Nedded for assertEquals
	@Override
	public boolean equals(Object obj) {
		return this.value.equals(((ProxableClass)obj).getValue());
	}

		
}
