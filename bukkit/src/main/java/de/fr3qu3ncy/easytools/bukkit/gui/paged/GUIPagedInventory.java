package de.fr3qu3ncy.easytools.bukkit.gui.paged;

import de.fr3qu3ncy.easytools.bukkit.gui.GUIInventory;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class GUIPagedInventory extends GUIInventory {

    private final PagedGUINavigator navigator;

    @Getter
    private int currentPage;

    public GUIPagedInventory(Player player, String title, int size) {
        super(player, title, size);

        this.navigator = getNavigator();
        this.currentPage = 0;

        setupNavigator();
    }

    public abstract PagedGUINavigator getNavigator();

    private void setupNavigator() {
        setItem(this.navigator.getPreviousButtonSlot(), this.navigator.getNavigatePrevious());
        setItem(this.navigator.getNextButtonSlot(), this.navigator.getNavigateNext());
    }

    public void setPage(int page) {
        if (page < 0) return;

        this.currentPage = page;
        update();
    }
}
