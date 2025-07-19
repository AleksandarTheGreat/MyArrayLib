package Array;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyArray<Type> implements Iterable<Type>{

    private static final int DEFAULT_CAPACITY = 5;
    private int size;
    private int capacity;
    private Type[] array;

    public MyArray() {
        this.size = 0;
        this.capacity = DEFAULT_CAPACITY;
        this.array = (Type[]) new Object[capacity];
    }

    public MyArray(Type[] array){
        this.size = array.length;
        this.capacity = size * 2;
        this.array = (Type[]) new Object[capacity];
        System.arraycopy(array, 0, this.array, 0, array.length);
    }

    private void grow() {
        capacity *= 2;
        Type [] newArray = (Type[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    private void shrink() {
        if (capacity < DEFAULT_CAPACITY)
            return;

        capacity /= 2;
        Type [] newArray = (Type[]) new Object[capacity];
        for (int i=0;i<size;i++){
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(Type value) {
        if (size >= capacity)
            grow();

        array[size++] = value;
    }

    public void addAt(Type value, int position) {
        if (position < 0 || position > size)
            throw new ArrayIndexOutOfBoundsException(position);

        if (size >= capacity)
            grow();

        for (int i=size;i>position;i--){
            array[i] = array[i-1];
        }

        array[position] = value;
        size++;
    }

    public Type delete(int position) {
        if (position < 0 || position >= size)
            throw new ArrayIndexOutOfBoundsException(position);

        if (isEmpty())
            throw new RuntimeException("Array is empty");

        Type value = array[position];
        for (int i=position;i<size-1;i++){
            array[i] = array[i+1];
        }

        array[--size] = null;

        if (size <= capacity / 3)
            shrink();
        return value;
    }

    public Type delete(Type value) {
        if (isEmpty())
            return null;

        Type ret = null;
        for (int i=0;i<size;i++){
            if (array[i].equals(value)){
                ret = array[i];
                for (int j=i;j<size-1;j++){
                    array[j] = array[j+1];
                }

                array[--size] = null;
                i--;
            }
        }

        if (size <= capacity / 3)
            shrink();

        return ret;
    }

    public Type delete(){
        if (isEmpty())
            return null;

        Type value = array[--size];
        array[size] = null;

        if (size <= capacity / 3)
            shrink();

        return value;
    }

    public boolean contains(Type value) {
        for (int i=0;i<size;i++){
            if (array[i].equals(value))
                return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Type get(int position) {
        if (position < 0 || position >= size)
            throw new ArrayIndexOutOfBoundsException(position);

        return array[position];
    }

    public void set(Type value, int position) {
        if (position < 0 || position >= size)
            throw new ArrayIndexOutOfBoundsException(position);

        array[position] = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() == null || obj.getClass() == null)
            return false;
        else if (getClass() != obj.getClass())
            return false;
        return array == ((MyArray<?>) obj).array;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "[]";

        if (size == 1)
            return "[" + array[0] + "]";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                builder.append("[").append(array[i]);
            } else if (i == size - 1) {
                builder.append(", ").append(array[i]).append("]");
            } else {
                builder.append(", ").append(array[i]);
            }
        }

        return builder.toString();
    }


    // ITERATOR MAGIC HERE
    @Override
    public Iterator<Type> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Type> {

        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Type next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[currentIndex++];
        }
    }
}
