package com.test.image.model.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntArrayListTest {
    private IntList underTest;
    private int[] values = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Before
    public void setUp() {
        underTest = new IntArrayList(4, 2);
    }

    private void populate() {
        for (int value : values) {
            underTest.add(value);
        }
    }

    @Test
    public void testEmptySize() {
        Assert.assertTrue(underTest.isEmpty());
        Assert.assertEquals(0, underTest.size());
    }

    @Test
    public void testEmptyIndexes() {
        Assert.assertFalse(underTest.contains(0));
        Assert.assertEquals(-1, underTest.indexOf(0));
        Assert.assertEquals(-1, underTest.lastIndexOf(0));
    }

    @Test
    public void testEmptyIterator() {
        final IntIterator iterator = underTest.iterator();
        Assert.assertNotNull(iterator);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyArray() {
        final int[] array = underTest.toArray();
        Assert.assertNotNull(array);
        Assert.assertEquals(0, array.length);
    }

    @Test
    public void testEmptyGet() {
        try {
            underTest.get(0);
            Assert.fail();
        } catch (IndexOutOfBoundsException iobe) {
            Assert.assertEquals("Index 0 out of bounds: IntArrayList is empty.", iobe.getMessage());
        }
    }

    @Test
    public void testEmptySet() {
        try {
            underTest.set(0, 42);
            Assert.fail();
        } catch (IndexOutOfBoundsException iobe) {
            Assert.assertEquals("Index 0 out of bounds: IntArrayList is empty.", iobe.getMessage());
        }
    }

    @Test
    public void testSize() {
        populate();

        Assert.assertFalse(underTest.isEmpty());
        Assert.assertEquals(values.length, underTest.size());
    }

    @Test
    public void testIndexes() {
        populate();

        for (int i=0 ; i<values.length ; i++) {
            Assert.assertTrue(underTest.contains(values[i]));
            Assert.assertEquals(i, underTest.indexOf(values[i]));
            Assert.assertEquals(i, underTest.lastIndexOf(values[i]));
        }
    }

    @Test
    public void testIterator() {
        populate();

        final IntIterator iterator = underTest.iterator();
        Assert.assertNotNull(iterator);

        for (int i=0 ; i<values.length ; i++) {
            Assert.assertTrue(iterator.hasNext());
            Assert.assertEquals(values[i], iterator.next());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testArray() {
        populate();

        final int[] array = underTest.toArray();
        Assert.assertNotNull(array);
        Assert.assertEquals(values.length, array.length);
        for (int i=0 ; i<values.length ; i++) {
            Assert.assertEquals(values[i], array[i]);
        }
    }

    @Test
    public void testGet() {
        populate();

        for (int i=0 ; i<values.length ; i++) {
            Assert.assertEquals(values[i], underTest.get(i));
        }
    }

    @Test
    public void testSet() {
        populate();

        final int index = 7;
        final int value = 42;

        Assert.assertNotEquals(value, underTest.get(index));
        underTest.set(index, value);
        Assert.assertEquals(value, underTest.get(index));
    }

    @Test
    public void testClear() {
        populate();

        Assert.assertFalse(underTest.isEmpty());
        Assert.assertEquals(values.length, underTest.size());
        underTest.clear();
        Assert.assertTrue(underTest.isEmpty());
        Assert.assertEquals(0, underTest.size());
    }

    @Test
    public void testOutOfBoundsGet() {
        populate();
        final int index = values.length + 1;

        try {
            underTest.get(index);
            Assert.fail();
        } catch (IndexOutOfBoundsException iobe) {
            Assert.assertEquals("Index " + index + " out of bounds: [0, " + values.length + ").", iobe.getMessage());
        }
    }

    @Test
    public void testOutOfBoundsSet() {
        populate();
        final int index = -1;

        try {
            underTest.set(index, 42);
            Assert.fail();
        } catch (IndexOutOfBoundsException iobe) {
            Assert.assertEquals("Index " + index + " out of bounds: [0, " + values.length + ").", iobe.getMessage());
        }
    }

    @Test
    public void testAdd() {
        underTest.add(42);
        Assert.assertEquals(1, underTest.size());
        Assert.assertEquals(42, underTest.get(0));
    }

    @Test
    public void testAddAll() {
        final IntCollection intCollection = new IntArrayList(3);
        intCollection.add(3);
        intCollection.add(2);
        intCollection.add(1);

        underTest.addAll(intCollection);
        Assert.assertEquals(3, underTest.size());
        final IntIterator expected = intCollection.iterator();
        final IntIterator actual = underTest.iterator();
        while (expected.hasNext()) {
            Assert.assertTrue(actual.hasNext());
            Assert.assertEquals(expected.next(), actual.next());
        }
    }
}
