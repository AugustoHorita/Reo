package nl.cwi.reo.runtime.java;

@SuppressWarnings("initialization")
public class PortFIFO1<T> implements Port<T> {
	private Component producer;
	private Component consumer;
	private volatile T put;
	public void setProducer(Component p) { producer = p; }
	public void setConsumer(Component c) { consumer = c; }	
	public void setGet() { }
	public void setPut(T datum) { put = datum; }
	public T take() { T datum = put; put = null; return datum; }
	public boolean hasGet() { return put == null; }
	public boolean hasPut() { return put != null; }
	public void activateProducer() { producer.activate(); }
	public void activateConsumer() { consumer.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		put = datum; 
		consumer.activate();
		while (put != null) { }
	}
	public T get() {
		producer.activate();
		while (put == null) { }
		T datum = put;
		put = null; 
		producer.activate();
		return datum;
	}
}