package Frontend;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Sequencer {
	private AtomicLong sequenceNumber;
	private ConcurrentHashMap<Long, String> queue;
	
    public Sequencer() {
        sequenceNumber = new AtomicLong(0);
        setQueue(new ConcurrentHashMap<Long, String>());
    }

    public long getNextSequenceNumber() {
        return sequenceNumber.getAndIncrement();
    }

	public ConcurrentHashMap<Long, String> getQueue() {
		return queue;
	}

	public void setQueue(ConcurrentHashMap<Long, String> queue) {
		this.queue = queue;
	}
	
	public void addQueue(long sequenceNumber, String msg) {
		this.queue.put(sequenceNumber, msg);
	}
}
