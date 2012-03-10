public class SemBoundedBuffer extends BoundedBuffer {
    Semaphore emptySlots, fullSlots;

    public SemBoundedBuffer (int maxSize) {
        super(maxSize);
        emptySlots = new Semaphore(maxSize);
        fullSlots  = new Semaphore(0);

    }

    public Object get() throws InterruptedException {
        Object value;
            // Suspend until a full slot is available
        	fullSlots.acquire();
        	value = super.get();
        	System.out.println("OBJECT "+value+" TAKEN FROM THE BUFFER");
            emptySlots.release();
            // Release an empty slot

        return value;
    }


    public void put(Object value) throws InterruptedException {
    		// Suspend until an empty slot is available
    		emptySlots.acquire();
    		System.out.println("OBJECT "+value+" PUT IN THE BUFFER");
            super.put(value);
        	fullSlots.release();
        	// Release an empty slot

    }
}