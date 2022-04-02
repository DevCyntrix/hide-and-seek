package de.helixdevs.hideandseek.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.function.Consumer;

public final class Events {

    public static <T extends Event> Closeable registerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Consumer<T> consumer) {
        return registerEvent(plugin, eventClass, consumer, EventPriority.NORMAL);
    }

    public static <T extends Event> Closeable registerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Consumer<T> consumer, @NotNull EventPriority priority) {
        return registerEvent(plugin, eventClass, consumer, priority, false);
    }

    public static <T extends Event> Closeable registerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Consumer<T> consumer, @NotNull EventPriority priority, boolean ignoreCancelled) {
        Listener listener = new Listener() {
        };
        EventExecutor executor = (listener1, event) -> {
            if (!eventClass.isInstance(event)) {
                return;
            }
            //noinspection unchecked
            consumer.accept((T) event);
        };
        Bukkit.getPluginManager().registerEvent(eventClass, listener, priority, executor, plugin, ignoreCancelled);
        return () -> HandlerList.unregisterAll(listener);
    }

    public static <T extends PlayerEvent> Closeable registerPlayerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Player player, @NotNull Consumer<T> consumer) {
        return registerPlayerEvent(plugin, eventClass, player, consumer, EventPriority.NORMAL);
    }

    public static <T extends PlayerEvent> Closeable registerPlayerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Player player, @NotNull Consumer<T> consumer, @NotNull EventPriority priority) {
        return registerPlayerEvent(plugin, eventClass, player, consumer, priority, false);
    }

    public static <T extends PlayerEvent> Closeable registerPlayerEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Player player, @NotNull Consumer<T> consumer, @NotNull EventPriority priority, boolean ignoreCancelled) {
        return registerEvent(plugin, eventClass, (Consumer<T>) t -> {
            if (!player.equals(t.getPlayer())) {
                return;
            }
            consumer.accept(t);
        }, priority, ignoreCancelled);
    }

    public static <T extends EntityEvent> Closeable registerEntityEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Entity entity, @NotNull Consumer<T> consumer) {
        return registerEntityEvent(plugin, eventClass, entity, consumer, EventPriority.NORMAL, false);
    }

    public static <T extends EntityEvent> Closeable registerEntityEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Entity entity, @NotNull Consumer<T> consumer, @NotNull EventPriority priority) {
        return registerEntityEvent(plugin, eventClass, entity, consumer, priority, false);
    }

    public static <T extends EntityEvent> Closeable registerEntityEvent(@NotNull Plugin plugin, @NotNull Class<? extends T> eventClass, @NotNull Entity entity, @NotNull Consumer<T> consumer, @NotNull EventPriority priority, boolean ignoreCancelled) {
        return registerEvent(plugin, eventClass, (Consumer<T>) t -> {
            if (!entity.equals(t.getEntity())) {
                return;
            }
            consumer.accept(t);
        }, priority, ignoreCancelled);
    }

}
