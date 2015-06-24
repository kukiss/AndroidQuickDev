package weiy.app.basic.tools;

/** Created by kukiss on 2015/6/15. */
public class WYThread extends Thread {

	volatile boolean stop;

	public void terminate() {
		stop = true;
	}

	@Override
	public synchronized void start() {
		stop = false;
		super.start();
	}

	public boolean isTerminated() {
		return stop;
	}
}