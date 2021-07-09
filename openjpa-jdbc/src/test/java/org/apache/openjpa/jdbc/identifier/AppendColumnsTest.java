package org.apache.openjpa.jdbc.identifier;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.identifier.DBIdentifierUtilImpl;
import org.apache.openjpa.jdbc.schema.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class AppendColumnsTest {
	private DBIdentifierUtilImpl utilImpl;
	private List<String> names;
	
	private Column[] columns;
	private boolean emptyArray;
	
	public AppendColumnsTest(Column[] columns, boolean emptyArray) {
		this.columns = columns;
		this.emptyArray = emptyArray;
	}
	
	@Parameters
	public static Collection<?> getParameters(){
		int num_cols = 5;
		Column[] validColumns = new Column[num_cols];
		Column[] emptyColumns = new Column[0];
		Column[] oneColumn = {new Column()};
		for (int i=0; i<num_cols; i++) {
			validColumns[i] = new Column();
		}
		
		boolean emptyArray = true;
		boolean  notEmptyAray= false;
		
		return Arrays.asList(new Object[][] {
				
			// Test Suite minimale
				{validColumns, notEmptyAray},
				{null,false},
				{emptyColumns, emptyArray},
				
			// Adequacy
				{oneColumn, notEmptyAray}
				
		});
	}
	
	@Before
	public void configure() {
		utilImpl = new DBIdentifierUtilImpl(new IdConfigurationTestImpl());
		names = new ArrayList<>();
		
		if (columns != null && !emptyArray) {
			for (int i=0; i<columns.length; i++) {
					names.add("col_"+i);
					columns[i].setIdentifier(DBIdentifier.newColumn(names.get(i)));
			}
		}
	}

	
	@Test
	public void testAppend() {
		String finalString = utilImpl.appendColumns(columns);
		
		StringBuilder expectedString = new StringBuilder();
		
		if (columns != null && !emptyArray) {
			expectedString.append(names.get(0));
			names.remove(0);
			for (String s: names) {
				expectedString.append(", "+s);
			}
		}
		
		assertEquals(expectedString.toString(), finalString);
		System.out.println(finalString );
		System.out.println(expectedString.toString());
		
	}

}
