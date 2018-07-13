package com.dakkra.wavetableeditor.util;

public class ArrayBuilder<E> {

    private final int SIZE;
    private int index;
    private E[] array;

    /**
     * Creates and array builder for the provided data type
     *
     * @param size size of the backing array
     */
    public ArrayBuilder(int size) {
        SIZE = size;
        index = 0;
    }

    /**
     * Adds the input data to the next available index in the backing array
     */
    public void add(E data) {
        if (isFull())
            throw new IndexOutOfBoundsException("Cannot add another object to this array builder. Exceeding fixed size of " + SIZE);
        array[index++] = data;
    }

    /**
     * Gets the item at the provided index from the backing array.
     * <p>
     * Keep in mind this can be done even when the builder is not finished.
     *
     * @param index index of the desired item
     * @return item from the backing array at the index
     */
    public E get(int index) {
        if (index < 0 || index >= SIZE)
            throw new IndexOutOfBoundsException("Index is not defined by the backing array");
        return array[index];
    }

    /**
     * Determines if this array builder is full (meaning the backing array is full)
     */
    public boolean isFull() {
        return (index >= (SIZE - 1));
    }

    /**
     * Returns the backing array for this array builder.
     * <p>
     * Keep in mind that it is possible to call this method before the array builder is finished.
     */
    public E[] getArray() {
        return array;
    }

    /**
     * Returns the size of the backing array
     */
    public int getSize() {
        return SIZE;
    }

}
