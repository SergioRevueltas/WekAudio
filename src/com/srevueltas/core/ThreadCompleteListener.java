package com.srevueltas.core;

/**
 * Interface to be notified when a thread has been end.
 * http://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished/702460#702460
 * @author Sergio Revueltas
 *
 */
public interface ThreadCompleteListener {
    void notifyOfThreadComplete(final Thread thread);

}
