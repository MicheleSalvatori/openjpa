package org.apache.openjpa.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class test {

    private Object key;
    private Object value;
    private Object previousValue;
    private CacheMap cacheMap;
    private boolean hasPreviousValue;
    private boolean pinned;
    private Integer cachedMaxMapSize;
    private Integer numObjectToInsert;


    public test(TestInput testInput) {
        this.key = testInput.getKey();
        this.value = testInput.getValue();
        this.hasPreviousValue = testInput.isAlreadyExist();
        this.pinned = testInput.isPinned();
        this.cachedMaxMapSize = testInput.getCacheMaxMapSize();
        this.numObjectToInsert = testInput.getNumObjectToInsert();
        if (this.hasPreviousValue) {
            this.previousValue = new Object();
        } else {
            this.previousValue = null;
        }
    }


    @Parameterized.Parameters
    public static Collection<TestInput> getParameters(){
        List<TestInput> testInputs = new ArrayList<>();

//        testInputs.add(new TestInput(null, null, false,false, 0, 1));
//        testInputs.add(new TestInput(new Object(), null, false, true, 1, 2));
//        testInputs.add(new TestInput(new Object(), new Object(), false, false, 1, 1));
//        testInputs.add(new TestInput(new Object(), new Object(), true, false, 2,0));
        testInputs.add(new TestInput(new Object(), new Object(), true, false, 1, 1));
//        testInputs.add(new TestInput(new Object(), new Object(), true, true, 1, 1));
        return testInputs;

    }

    private static class TestInput {
        private Object key;
        private Object value;
        private boolean alreadyExist;
        private boolean pinned;
        private Integer cacheMaxMapSize;
        private Integer numObjectToInsert;

        public TestInput(Object key, Object value, boolean alreadyExist, boolean pinned, Integer cacheMaxMapSize, Integer numObjectToInsert) {
            this.key = key;
            this.value = value;
            this.alreadyExist = alreadyExist;
            this.pinned = pinned;
            this.cacheMaxMapSize = cacheMaxMapSize;
            this.numObjectToInsert = numObjectToInsert;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public boolean isAlreadyExist() {
            return alreadyExist;
        }

        public boolean isPinned() {
            return pinned;
        }

        public Integer getCacheMaxMapSize() {
            return cacheMaxMapSize;
        }

        public Integer getNumObjectToInsert() {
            return numObjectToInsert;
        }
    }

    @Before
    public void setUp(){
        this.cacheMap = new CacheMap(true, this.cachedMaxMapSize, this.cachedMaxMapSize + 1, 1L, 1);
        if (this.hasPreviousValue) {
            this.cacheMap.put(this.key, this.previousValue);
        }
        for (int i = 0; i < this.numObjectToInsert; i++) {
            this.cacheMap.put(new Object(), new Object());
        }

        if (this.pinned) {
            this.cacheMap.pin(this.key);
        }


    }

    @Test
    public void putTest() {
        Object previousValue = this.cacheMap.put(this.key, this.value);

        if (this.hasPreviousValue && this.cachedMaxMapSize != 0) {
            Assert.assertEquals(this.previousValue, previousValue);
        } else {
            Assert.assertNull(previousValue);
        }

        Object getValue = this.cacheMap.get(this.key);

        Assert.assertEquals(this.value, getValue);


    }

}