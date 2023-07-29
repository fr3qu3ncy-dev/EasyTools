package de.fr3qu3ncy.easytools.spigot.particle;

import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class ColoredParticlePlayer extends ParticlePlayer {

    private final List<Color> colors;

    private int currentColorIndex;

    public ColoredParticlePlayer(JavaPlugin plugin, int playRadius, long delay, long interval, float duration, boolean loop,
                                 List<Color> colors) {
        super(plugin, playRadius, delay, interval, duration, loop);

        this.colors = colors;
    }

    protected List<Color> getColors() {
        return colors;
    }

    protected Color getCurrentColor() {
        return colors.get(currentColorIndex);
    }

    protected float getCurrentColorRed() {
        return (float) getCurrentColor().getRed() / 255F;
    }

    protected float getCurrentColorGreen() {
        return (float) getCurrentColor().getGreen() / 255F;
    }

    protected float getCurrentColorBlue() {
        return (float) getCurrentColor().getBlue() / 255F;
    }

    public void nextColor() {
        if (currentColorIndex < colors.size() - 1) {
            currentColorIndex++;
        } else {
            currentColorIndex = 0;
        }
    }

    @Override
    public void onParticleTick() {
        nextColor();
    }
}
