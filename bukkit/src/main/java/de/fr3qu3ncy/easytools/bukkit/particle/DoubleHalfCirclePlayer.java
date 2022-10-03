package de.fr3qu3ncy.easytools.bukkit.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DoubleHalfCirclePlayer extends ParticlePlayer {

    private final ParticlePlayer halfCircle1, halfCircle2;

    public DoubleHalfCirclePlayer(JavaPlugin plugin, Location location, float radius,
                                  float duration, boolean loop, List<Color> colors) {
        super(plugin, 10, 0L, -1, duration, false);
        halfCircle1 = new HalfCirclePlayer(plugin, location, radius,
                duration, 0F, loop, colors);
        halfCircle2 = new HalfCirclePlayer(plugin, location, radius,
                duration, 180F, loop, colors);
    }

    @Override
    protected void calculateParticles() {
        halfCircle1.playParticles();
        halfCircle2.playParticles();
    }

    @Override
    public void stop() {
        super.stop();
        halfCircle1.stop();
        halfCircle2.stop();
    }
}
