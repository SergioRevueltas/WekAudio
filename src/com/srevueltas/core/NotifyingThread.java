package com.srevueltas.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * Thread with callback. You have to override doRun() instead of run() method. 
 * http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished/702460#702460
 * @author Sergio Revueltas
 */
public abstract class NotifyingThread extends Thread {

	private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

	public final void addListener(final ThreadCompleteListener listener) {
		listeners.add(listener);
	}

	public final void removeListener(final ThreadCompleteListener listener) {
		listeners.remove(listener);
	}

	private final void notifyListeners() {
		for (ThreadCompleteListener listener : listeners) {
			listener.notifyOfThreadComplete(this);
		}
	}

	@Override
	public final void run() {
		try {
			doRun();
		} finally {
			notifyListeners();
		}
	}

	public abstract void doRun();
}
