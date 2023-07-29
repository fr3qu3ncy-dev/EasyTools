package de.fr3qu3ncy.easytools.spigot.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BukkitScoreboard {

    @Getter
    private final Scoreboard scoreboard;
    private final Player player;

    public BukkitScoreboard(Player player) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.player = player;
    }

    public Team registerTeam(String name) {
        return this.scoreboard.registerNewTeam(name);
    }

    public Objective registerObjective(String name, String displayName, DisplaySlot displaySlot) {
        Objective obj = this.scoreboard.registerNewObjective(name, "dummy", displayName);
        obj.setDisplaySlot(displaySlot);
        return obj;
    }

    public void setScoreboard() {
        this.player.setScoreboard(this.scoreboard);
    }

    public void removeScoreboard() {
        this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
