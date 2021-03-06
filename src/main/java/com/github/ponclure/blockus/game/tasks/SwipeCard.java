package com.github.ponclure.blockus.game.tasks;

import com.github.ponclure.blockus.game.Game;
import com.github.ponclure.blockus.player.Participant;
import com.github.ponclure.blockus.utility.Utils;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PersistentGui;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import java.util.Arrays;

public class SwipeCard extends Task {

    private final PersistentGui gui;

    private long startTime;
    private long endTime;

    public SwipeCard(Game game, Location loc, Participant p) {
        super(game, "Swipe Card", loc, p);
        this.gui = new PersistentGui(5, "Swipe Card");
        init();
        setEmpty(gui);
    }

    private void init() {
        GuiItem start = new GuiItem(Utils.getSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTMwZWRiZDBjOTZkNmEwZWZlMzQ4YzFiMjcwMTc5YzMwMzFhODRiZjYyNjdiODU5N2FiOWZjZDNkZjZlMjFjMiJ9fX0=",
                ChatColor.GOLD + "Drag Card to Other Slot"), event -> {
            startTime = System.currentTimeMillis();
        });
        gui.setItem(28, start);
        GuiItem arrows = new GuiItem(Utils.getSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE5MmFkNDU2Zjc2ZWM3Y2FhMzU5NTkyMmQ1ZmNjMzhkY2E5MjhkYzY3MTVmNzUyZTc0YzhkZGRlMzQ0ZSJ9fX0=",
                ChatColor.AQUA + "Keep Dragging"));
        gui.setItem(Arrays.asList(20, 21, 22, 23, 24), arrows);
        gui.setDefaultClickAction(event -> {
            if (event.getSlot() == 34) {
                endTime = System.currentTimeMillis();
                double seconds = (endTime - startTime) / 1000.0;
                if (seconds >= 1 && seconds <= 1.5) {
                    GuiItem finished = new GuiItem(Utils.getSkull(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=",
                            ChatColor.GREEN + "Great Swipe!"));
                    gui.setItem(31, finished);
                    callComplete((Player) event.getWhoClicked(), gui);
                } else {
                    gui.setItem(28, start);
                    gui.setItem(34, null);
                    GuiItem wrong = new GuiItem(Utils.getSkull(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZjNjBkYTQxNGJmMDM3MTU5YzhiZThkMDlhOGVjYjkxOWJmODlhMWEyMTUwMWI1YjJlYTc1OTYzOTE4YjdiIn19fQ==",
                            ChatColor.GREEN + "Try Again (1 - 1.5 Seconds)"));
                    gui.setItem(31, wrong);
                }
            } else {
                event.setCancelled(true);
            }
        });
    }

    @Override
    public void execute(PlayerArmorStandManipulateEvent e) {
        gui.open(e.getPlayer());
    }

}
