package de.fr3qu3ncy.easytools.spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;

public class ThreadUtils {

    private ThreadUtils() {}

    public static void sync(JavaPlugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static void async(JavaPlugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void spreadOverTicks(JavaPlugin plugin, int ticks, IntConsumer action) {
        AtomicInteger currentTick = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (currentTick.get() >= ticks) {
                    cancel();
                    return;
                }

                action.accept(currentTick.getAndIncrement());
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public static void spreadOverTicks(JavaPlugin plugin, IntConsumer action, BooleanSupplier terminate, @Nullable Runnable onFinished) {
        AtomicInteger currentTick = new AtomicInteger();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (terminate.getAsBoolean()) {
                    if (onFinished != null) onFinished.run();
                    cancel();
                    return;
                }

                action.accept(currentTick.getAndIncrement());
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
