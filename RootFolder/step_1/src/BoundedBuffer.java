



public class BoundedBuffer {
    int first, last, size, maxSize;
    Object content[];

    BoundedBuffer (int maxSize) {
        this.first = 0;
        this.last = 0;
        this.size = 0;
        this.maxSize = maxSize;
        this.content = new Object[maxSize];
    }

    public int getSize(){
    	return size;
    }
    Object get() throws InterruptedException {
        Object value = content[first];
        first = (first + 1) % maxSize;
        size = size - 1;
        return value;
    }

    void put(Object value) throws InterruptedException {
        content[last] = value;
        last = (last + 1) % maxSize;
        size = size + 1;
    }
}