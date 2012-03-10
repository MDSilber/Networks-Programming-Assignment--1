



public class Semaphore {
    int count;

    Semaphore (int count) {
        this.count = count;
    }

    public synchronized void acquire() throws InterruptedException {
        count--;
        if(count<0)
        	wait();
    }

    public synchronized void release() throws InterruptedException {
        count++;
        if (count<=0)
        	notify();
    }
}