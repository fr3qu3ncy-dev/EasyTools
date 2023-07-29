package de.fr3qu3ncy.easytools.spigot.gui;

import de.fr3qu3ncy.easytools.spigot.gui.item.GUIItem;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIPagedInventory extends GUIInventory {

    static final List<Player> SWITCHING_PAGES = new ArrayList<>();

    @Getter
    private int currentPage;

    protected GUIPagedInventory(Player player, String title, int size) {
        super(player, title, size);

        this.currentPage = 0;
    }

    @Override
    protected void addItems() {
        populatePage(getCurrentPage());
        setupNavigator();
    }

    protected abstract void populatePage(int pageIndex);

    private void setupNavigator() {
        if (hasPage(currentPage - 1) && currentPage > 0) {
            setItem(getPreviousButtonSlot(), createPreviousButton()
                .setOnClick(ClickType.LEFT, () -> setPage(getCurrentPage() - 1)));
        }
        if (hasPage(currentPage + 1)) {
            setItem(getNextButtonSlot(), createNextButton()
                .setOnClick(ClickType.LEFT, () -> setPage(getCurrentPage() + 1)));
        }
    }

    public void setPage(int page) {
        if (page < 0) return;

        this.currentPage = page;
        SWITCHING_PAGES.add(getPlayer());
        update();
        SWITCHING_PAGES.remove(getPlayer());
    }

    protected abstract boolean hasPage(int page);

    protected abstract GUIItem createPreviousButton();
    protected int getPreviousButtonSlot() {
        return inventory.getSize() - 6;
    }

    protected abstract GUIItem createNextButton();
    protected int getNextButtonSlot() {
        return inventory.getSize() - 4;
    }
}
