package de.helixdevs.hideandseek.api;

import com.google.common.base.Preconditions;

import java.io.Closeable;
import java.io.IOException;
import java.util.ListIterator;

public class GameSequence implements Closeable {

    private final ListIterator<GamePhase> iterator;
    private GamePhase current;

    private boolean running;

    public GameSequence(ListIterator<GamePhase> iterator) {
        Preconditions.checkArgument(iterator.hasNext());
        this.iterator = iterator;
    }

    public void start() {
        if(running)
            return;
        this.running = true;
        this.current = iterator.next();
        this.current.start();
    }


    public void next() throws IOException {
        boolean next = iterator.hasNext();
        if (!next)
            throw new IllegalStateException("The sequence doesn't have a next phase");
        this.current.close();
        this.current = iterator.next();
        this.current.start();
    }

    public void previous() throws IOException {
        boolean previous = iterator.hasPrevious();
        if (!previous)
            throw new IllegalStateException("The sequence doesn't have a previous phase");
        this.current.close();
        this.current = iterator.previous();
        this.current.start();
    }

    @Override
    public void close() throws IOException {
        if (!running)
            return;

        if (this.current != null) {
            this.current.close();
        }
        this.running = false;
    }


    public GamePhase getCurrent() {
        return current;
    }
}
