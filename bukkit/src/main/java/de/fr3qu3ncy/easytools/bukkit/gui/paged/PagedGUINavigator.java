package de.fr3qu3ncy.easytools.bukkit.gui.paged;

import de.fr3qu3ncy.easytools.bukkit.gui.item.GUIItem;
import lombok.Getter;
import org.bukkit.event.inventory.ClickType;

public abstract class PagedGUINavigator {

    private final GUIPagedInventory inventory;

    @Getter
    private final GUIItem navigatePrevious, navigateNext;

    public PagedGUINavigator(GUIPagedInventory inventory) {
        this.inventory = inventory;

        this.navigatePrevious = createPreviousButton();
        this.navigateNext = createNextButton();

        setNavigateActions();
    }

    protected void setNavigateActions() {
        this.navigatePrevious.setOnClick(ClickType.LEFT, () -> inventory.setPage(inventory.getCurrentPage() - 1));
        this.navigateNext.setOnClick(ClickType.LEFT, () -> inventory.setPage(inventory.getCurrentPage() + 1));
    }

    protected abstract GUIItem createPreviousButton();
    protected abstract int getPreviousButtonSlot();

    protected abstract GUIItem createNextButton();
    protected abstract int getNextButtonSlot();
}
