public class ArrayList<T> implements List<T> {
    private Object[] elements;
    private int size;

    public ArrayList() {
        this.elements = new Object[10];
        this.size = 0;
    }

    public void add(T element) {
        if (size == elements.length) {
            // Increase the size of the array by creating a new array with double the capacity
            Object[] newElements = new Object[elements.length * 2];
            // Copy the elements from the old array to the new array
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
        elements[size++] = element;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void grow_array() {
        Object[] newElements = new Object[elements.length * 2];
        // Copy the elements from the old array to the new array
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == elements.length) {
            // Increase the size of the array by creating a new array with double the capacity
            grow_array();
        }
        // Shift elements to the right to make space for the new element
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        // Insert the new element at the specified index
        elements[index] = element;
        size++;
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }
    
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T removedElement = (T) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // Set the last element to null
        size--;
        return removedElement;
    }
    /* public ListIterator<T> listIterator() {
        return new ArrayListIterator(0);
    }

    private class ArrayListIterator implements ListIterator<T> {
        private int cursor;
        private int lastRet = -1;

        public ArrayListIterator(int index) {
            this.cursor = index;
        }

        public boolean hasNext() {
            return cursor < size;
        }

        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastRet = cursor;
            cursor++;
            return (T) elements[lastRet];
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            cursor--;
            lastRet = cursor;
            return (T) elements[lastRet];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            ArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }

        public void set(T element) {
            if (lastRet < 0)
                throw new IllegalStateException();
            ArrayList.this.elements[lastRet] = element;
        }

        public void add(T element) {
            int i = cursor;
            ArrayList.this.add(i, element);
            cursor = i + 1;
            lastRet = -1;
        }
    }*/
    
    
}


