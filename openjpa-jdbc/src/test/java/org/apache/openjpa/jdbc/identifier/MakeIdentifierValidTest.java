package org.apache.openjpa.jdbc.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.openjpa.jdbc.identifier.DBIdentifier.DBIdentifierType;
import org.apache.openjpa.jdbc.schema.NameSet;
import org.apache.openjpa.jdbc.schema.SchemaGroup;
import org.apache.openjpa.lib.identifier.IdentifierConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class MakeIdentifierValidTest {
	
	private static DBIdentifierUtilImpl utilImpl;
	private static IdentifierConfiguration conf;
	private SchemaGroup nameSet;
	
	private InputParameters inputEntity;
	private int identifierLenght;
	private Class<? extends Exception> exceptionExpected;
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	// public DBIdentifier makeIdentifierValid(DBIdentifier sname, NameSet set, int
	// maxLen, boolean checkForUniqueness)
	public MakeIdentifierValidTest(InputParameters input) {
		this.inputEntity = input;
		if (this.inputEntity.isNull()) {
			exceptionExpected = NullPointerException.class;
		}
	}

	@Parameters
	public static Collection<InputParameters> getParameters() {
		
		DBIdentifier validDbIdentifierWithReserverWord = DBIdentifier.newTable("TESTINGTABLEISW2");
		DBIdentifier validDbIdentifier = DBIdentifier.newTable("testingTable");
		DBIdentifier notValidDbIdentifier = DBIdentifier.NULL;
		
		List<InputParameters> parameters = new ArrayList<>();
		
		// Test suite minimale
		parameters.add(new InputParameters(validDbIdentifier, null, validDbIdentifier.getName().length()-1, false, "TESTINGTABL"));
		InputParameters simulateTenVersions = new InputParameters(validDbIdentifier, true, validDbIdentifier.getName().length()+1, true, "TESTINGTAB10");
		simulateTenVersions.setTenVersionBit();
		parameters.add(simulateTenVersions);
		parameters.add(new InputParameters(validDbIdentifierWithReserverWord, true, validDbIdentifierWithReserverWord.getName().length(), false, "TESTINGTABLEISW0"));
		parameters.add(new InputParameters(notValidDbIdentifier, false, 255, true, null));		// DBIdentifier.NULL
		parameters.add(new InputParameters(null, false, 10, false, null));						// null
//
//		// Adequacy
		DBIdentifier sequenceIdentifier = DBIdentifier.newSequence("testingSequence");
//		
		parameters.add(new InputParameters(sequenceIdentifier, true, sequenceIdentifier.getName().length()+1, true, "TESTINGSEQUENCE1"));
		parameters.add(new InputParameters(DBIdentifier.newColumn("testingColumn"), true, 255, true, "TESTINGCOLUMN1"));		// viene normalizzato quando si appende 1
		parameters.add(new InputParameters(DBIdentifier.newColumn("\"testingColumn\""), false, 255, false, "\"testingcolumn\"", "LOWER_CASE"));
		parameters.add(new InputParameters(DBIdentifier.newColumn("\"testingColumn\""), false, 255, false, "\"TESTINGCOLUMN\"", "UPPER_CASE"));
		
		parameters.add(new InputParameters(DBIdentifier.newColumn("\"testingColumn\""), false, 255, false, "\"testingColumn\""));	// Preserve_case
		parameters.add(new InputParameters(DBIdentifier.newTable("tableISW2"), true, "tableISW2".length()+1, false, "TABLEISW20"));
		
		
		return parameters;
	}

	@SuppressWarnings("deprecation")
	@Before
	public void configure() {
		nameSet = null;
		conf = new IdConfigurationTestImpl(inputEntity.getFormat());
		
		// Setting configuration with a reserverWord 
		Set<String> reservedWords = new HashSet<String>(Arrays.asList("TABLEISW2", "TESTINGTABLEISW2"));
		conf.getDefaultIdentifierRule().setReservedWords(reservedWords);
		
		if (inputEntity.getSet() != null) {
			// se non entra in nessun if, nameSet sarà null in inputEntity
			if (inputEntity.isSetFilled()){
				nameSet = new SchemaGroup();
				DBIdentifier schemaID = DBIdentifier.newSchema("testingSchema");
				nameSet.addSchema(schemaID);
				
				if(inputEntity.getIdentifier().getType() == DBIdentifierType.SEQUENCE)
					nameSet.getSchema(schemaID).addSequence(inputEntity.getIdentifier());
				else {
				nameSet.getSchema(schemaID).addTable(inputEntity.getIdentifier());
				if (inputEntity.tenVersionBit) {
					generateTenVersionsOf(schemaID, inputEntity.getIdentifier().getName());
				}
				}
			}else {
				nameSet = new SchemaGroup();
			}
			
			if (inputEntity.isCheckForUniqueness()) {
				inputEntity.setNameSet(nameSet);
			}
		}
		
		utilImpl = new DBIdentifierUtilImpl(conf);
	}
	
	@SuppressWarnings("deprecation")
	private void generateTenVersionsOf(DBIdentifier schemaID, String tableName) {
		nameSet.getSchema(schemaID).addTable(tableName+"1");
		for (int i=2; i<10; i++) {
			nameSet.getSchema(schemaID).addTable(DBIdentifier.newTable("testingTabl"+i));
		}
	}

	@Test
	public void testMakeIdentifierValid() {
		
		if (exceptionExpected != null) {
			exceptionRule.expect(exceptionExpected);
		}
		
		if (inputEntity.getIdentifier() != null)
			identifierLenght = inputEntity.getIdentifier().length();
		
		DBIdentifier validatedIdentifier = utilImpl.makeIdentifierValid(inputEntity.getIdentifier(), inputEntity.getSet(), inputEntity.getMaxLen(), inputEntity.isCheckForUniqueness());
		System.out.println(inputEntity.getExpected() + " =?= " + validatedIdentifier.getName());
		
		assertEquals(inputEntity.getExpected(), validatedIdentifier.getName());
		assertTrue(validatedIdentifier.getName().length() <= inputEntity.getMaxLen());
		
	}
	
	
	private static class InputParameters{
		private DBIdentifier identifier;
		private NameSet set;
		private int maxLen;
		private boolean checkForUniqueness;
		private boolean setFilled;
		private boolean tenVersionBit = false;
		private String format = null;
		
		private String expectedValidatedName;
		
		public InputParameters(DBIdentifier identifier, NameSet set, int maxLen, boolean checkForUniqueness, String expectedValidatedName) {
			this.identifier = identifier;
			this.set = set;
			this.maxLen = maxLen;
			this.checkForUniqueness = checkForUniqueness;
			this.expectedValidatedName = expectedValidatedName;
		}
		

		public InputParameters(DBIdentifier identifier, boolean setFilled, int maxLen, boolean checkForUniqueness, String expectedValidatedName) {
			this.identifier = identifier;
			this.setFilled = setFilled;
			this.maxLen = maxLen;
			this.checkForUniqueness = checkForUniqueness;
			this.expectedValidatedName = expectedValidatedName;
			this.set = new SchemaGroup();
		}
		
		public void setTenVersionBit() {
			this.tenVersionBit = true;
		}
		
		public boolean isTenVersion() {
			return this.tenVersionBit;
		}
		public InputParameters(DBIdentifier identifier, boolean setFilled, int maxLen, boolean checkForUniqueness, String expectedValidatedName, String format) {
			this.identifier = identifier;
			this.setFilled = setFilled;
			this.maxLen = maxLen;
			this.checkForUniqueness = checkForUniqueness;
			this.expectedValidatedName = expectedValidatedName;
			this.set = new SchemaGroup();
			this.format = format;
		}
		
		public String getFormat() {
			return this.format;
		}
		
		public void setNameSet(SchemaGroup nameTakenInSet) {
			this.set = nameTakenInSet;
		}
		
		public boolean isSetFilled() {
			return this.setFilled;
		}

		public boolean isNull() {
			return (identifier == null || identifier.getType() == DBIdentifierType.NULL);
		}
		
		public String getExpected() {
			return this.expectedValidatedName;
		}

		public DBIdentifier getIdentifier() {
			return identifier;
		}

		public NameSet getSet() {
			return set;
		}

		public int getMaxLen() {
			return maxLen;
		}

		public boolean isCheckForUniqueness() {
			return checkForUniqueness;
		}
		
	}

}
