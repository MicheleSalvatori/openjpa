package org.apache.openjpa.util.classes;
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.StateManager;

public final class ManageableClass implements PersistenceCapable {
	
	public ManageableClass(){
	}

	@Override
	public int pcGetEnhancementContractVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object pcGetGenericContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateManager pcGetStateManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pcReplaceStateManager(StateManager sm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcProvideField(int fieldIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcProvideFields(int[] fieldIndices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcReplaceField(int fieldIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcReplaceFields(int[] fieldIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcCopyFields(Object fromObject, int[] fields) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcDirty(String fieldName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object pcFetchObjectId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pcGetVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pcIsDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pcIsTransactional() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pcIsPersistent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pcIsNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pcIsDeleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean pcIsDetached() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistenceCapable pcNewInstance(StateManager sm, boolean clear) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistenceCapable pcNewInstance(StateManager sm, Object obj, boolean clear) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pcNewObjectIdInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pcNewObjectIdInstance(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pcCopyKeyFieldsToObjectId(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcCopyKeyFieldsToObjectId(FieldSupplier supplier, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pcCopyKeyFieldsFromObjectId(FieldConsumer consumer, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object pcGetDetachedState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pcSetDetachedState(Object state) {
		// TODO Auto-generated method stub
		
	}
	
}
