package de.fr3qu3ncy.easytools.bukkit.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class HalfCirclePlayer extends ColoredParticlePlayer {

    private final Location location;
    private final float radius;
    private final float duration;
    private final float startAngle;

    private double currentAngle;

    public HalfCirclePlayer(JavaPlugin plugin, Location location, float radius, float duration,
                            float startAngle, boolean loop, List<Color> colors) {
        super(plugin, 10, 0L, 1L, duration, loop, colors);

        this.location = location;
        this.radius = radius;
        this.duration = duration;
        this.startAngle = startAngle;
    }

    @Override
    protected void resetValues() {
        currentAngle = startAngle;
    }

    @Override
    protected void calculateParticles() {
        int durationTicks = (int) duration * 20;
        float anglePerTick = 180F / durationTicks;

        double newX = radius * Math.cos(Math.toRadians(currentAngle)) + location.getX();
        double newZ = radius * Math.sin(Math.toRadians(currentAngle)) + location.getZ();

        Location spawnLocation = new Location(location.getWorld(), newX, location.getY(), newZ);
        Particle.DustOptions dustOptions = new Particle.DustOptions(
                Color.fromRGB(
                        Math.round(255F * (getCurrentColorRed() != 0f ? getCurrentColorRed() : 0.0001F)),
                         Math.round(255F * getCurrentColorGreen()),
                        Math.round(255F * getCurrentColorBlue())), 1F);

        for (Player player : getNearbyPlayers(location)) {
            player.spawnParticle(Particle.REDSTONE, spawnLocation, 0, dustOptions);
        }

        currentAngle += anglePerTick;
    }
}
