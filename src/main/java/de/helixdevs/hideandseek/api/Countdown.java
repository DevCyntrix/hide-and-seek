package de.helixdevs.hideandseek.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.Closeable;
import java.io.IOException;

public abstract class Countdown implements Closeable {

    private final Plugin plugin;
    private int counter;
    private final int start;

    private BukkitTask task;

    public Countdown(Plugin plugin, int start) {
        this.plugin = plugin;
        this.counter = this.start = start;
    }

    public abstract void notify(int counter);

    public abstract void finish();

    public void start() {
        if (isRunning())
            return;
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (counter == 0) {
                    Bukkit.getScheduler().runTask(plugin, Countdown.this::finish);
                    reset();
                    cancel();
                    return;
                }
                Countdown.this.notify(counter);
                counter--;
            }
        }.runTaskTimerAsynchronously(this.plugin, 20, 20);
    }

    public int getCounter() {
        return counter;
    }

    public int getStart() {
        return start;
    }

    public void reset() {
        if (this.task != null) {
            this.task.cancel();
        }
        this.counter = this.start;
    }

    public boolean isRunning() {
        return task != null && plugin.getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
    }

    @Override
    public void close() throws IOException {
        reset();
    }
}
