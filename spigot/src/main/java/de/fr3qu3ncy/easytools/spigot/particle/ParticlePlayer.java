package de.fr3qu3ncy.easytools.spigot.particle;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticlePlayer {

    private final JavaPlugin plugin;
    private final int playRadius;
    private final boolean loop;
    private final long delay, interval;
    private final float duration;

    private BukkitTask task;

    private long ticksPassed;

    public ParticlePlayer(JavaPlugin plugin, int playRadius, long delay, long interval, float duration, boolean loop) {
        this.plugin = plugin;
        this.playRadius = playRadius;
        this.delay = delay;
        this.interval = interval;
        this.duration = duration;
        this.loop = loop;
    }

    private void reset() {
        ticksPassed = 0;
        resetValues();
    }

    protected void resetValues() {}

    public void playParticles() {
        reset();
        prepare();

        if (interval == -1) {
            calculateParticles();
            return;
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (ticksPassed > duration * 20) {
                    if (loop) reset(); else {
                        cancel();
                        return;
                    }
                }

                if (ticksPassed % interval == 0) {
                    onParticleTick();
                    calculateParticles();
                }

                ticksPassed++;
            }
        }.runTaskTimer(plugin, delay, 1L);
    }

    public void onParticleTick() {}

    protected abstract void calculateParticles();

    protected void prepare() {}

    protected JavaPlugin getPlugin() {
        return plugin;
    }

    protected float getDuration() {
        return duration;
    }

    protected int getPlayRadius() {
        return playRadius;
    }

    protected List<Player> getNearbyPlayers(Location loc) {
        List<Player> list = new ArrayList<>();
        for (Entity ent : loc.getWorld().getNearbyEntities(loc, getPlayRadius(), getPlayRadius(), getPlayRadius())) {
            if (!(ent instanceof Player)) continue;
            Player packetPlayer = (Player) ent;
            list.add(packetPlayer);
        }
        return list;
    }

    public void stop() {
        if (task != null) task.cancel();
        task = null;
    }
}
