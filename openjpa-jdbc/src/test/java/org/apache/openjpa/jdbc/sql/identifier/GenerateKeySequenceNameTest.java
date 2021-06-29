package org.apache.openjpa.jdbc.sql.identifier;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.identifier.DBIdentifierUtilImpl;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Schema;
import org.apache.openjpa.jdbc.schema.SchemaGroup;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.lib.identifier.IdentifierConfiguration;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith (value = Parameterized.class)
public class GenerateKeySequenceNameTest {
	private static DBIdentifierUtilImpl utilImpl;
	private static IdentifierConfiguration config;
	private static String SEQ = "SEQ";
	
	private Column col;
	private int maxLen;
	private static Table table;
	private Class<? extends Exception> expectedException;
	
	private boolean validInstance;
	
	private String columnName = "columnTest";
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	public GenerateKeySequenceNameTest(Column col, int maxLen, Class<? extends Exception> expectedException, boolean validInstance) {
		this.col = col;
		this.maxLen = maxLen;
		this.expectedException = expectedException;
		this.validInstance = validInstance;
	}
	
	@Parameters
	public static Collection<?> getParameters(){
		int rightLen = 24;		// lenght("tableTest_columnTest_SEQ")
		boolean validInstance = true;
		return Arrays.asList(new Object[][] {
				
			// Test Suite minimale
				{new Column(), 1, StringIndexOutOfBoundsException.class, true},
				{null, -1, NullPointerException.class, false},
				{new Column(), 0, NullPointerException.class, false},
				{new Column(), rightLen, null, true},
				{new Column(), rightLen+1, null, true},
				
			// Adequacy
				{new Column(), rightLen-1, null, true},
				
		});
	}
	
	@BeforeClass
	public static void configure() {
		config = new IdConfigurationTestImpl();
		utilImpl = new DBIdentifierUtilImpl(config);
		
		SchemaGroup group = new SchemaGroup();
		Schema schema = new Schema(DBIdentifier.newSchema("schemaTest"), group);
		table = new Table(DBIdentifier.newTable("tableTest"), schema);
	}
	
	@Test
	public void testGenerateSequence() {
		
		if (expectedException != null) {
			exceptionRule.expect(expectedException);
		}
		
		if (col != null) {
			col.setTableIdentifier(table.getIdentifier());
			if (validInstance) {
				col.setName(columnName);
			}
		}
		
		
		String tableName = table.getIdentifier().getName();
		String columnName = col.getIdentifier().getName();
		
		DBIdentifier sequenceGenerated = utilImpl.getGeneratedKeySequenceName(col, maxLen);
		String expectedSequenceName = String.format("%s_%s_%s", tableName, columnName, SEQ);
		
		if (maxLen < expectedSequenceName.length()) {				// jacoco
			tableName = tableName.substring(0, tableName.length()-(expectedSequenceName.length() - maxLen));
			expectedSequenceName = String.format("%s_%s_%s", tableName, columnName, SEQ);
		}
		
		System.out.println(sequenceGenerated + " =?= " + expectedSequenceName);
		assertEquals(expectedSequenceName, sequenceGenerated.getName());
		
	}

}
