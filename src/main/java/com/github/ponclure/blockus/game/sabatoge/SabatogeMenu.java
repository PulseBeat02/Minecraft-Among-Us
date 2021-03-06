package com.github.ponclure.blockus.game.sabatoge;

import com.github.ponclure.blockus.BlockUsPlugin;
import com.github.ponclure.blockus.arena.components.Door;
import com.github.ponclure.blockus.game.Game;
import com.github.ponclure.blockus.player.Imposter;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PersistentGui;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SabatogeMenu {

    private final Game game;
    private final PersistentGui gui;
    private final Imposter imposter;

    private final Map<Door, Boolean> canCloseDoor;
    private final boolean canSabatogeMain;

    public SabatogeMenu(Game game, Imposter imp) {
        this.game = game;
        this.gui = new PersistentGui(5, "Sabatoge Menu");
        this.imposter = imp;
        this.canCloseDoor = new HashMap<>();
        this.canSabatogeMain = false;
        for (Door doors : game.getArena().getDoors()) {
            canCloseDoor.put(doors, true);
        }
    }

    private Door get = null;

    public void initDoors() {
        List<Integer> doorSlots = Arrays.asList(3, 4, 5, 6, 7, 12, 13, 14, 15, 16, 17); // Added extra door slots for later support
        GuiItem sabatogeDoors = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Sabatoge Doors:").asGuiItem();
        gui.setItem(1, sabatogeDoors);
        Door[] doors = game.getArena().getDoors();
        int index = 0;
        for (Door door : doors) {
            GuiItem item = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + door.getDoorName()).asGuiItem(event -> {
                if (doorSlots.contains(event.getSlot())) {
                    Door[] allDoors = game.getArena().getDoors();
                    for (Door d : allDoors) {
                        if (gui.getGuiItem(event.getSlot()).getItemStack().getItemMeta().getDisplayName().contains(d.getDoorName())) {
                            get = d;
                        }
                    }
                    if (canCloseDoor.get(get)) {
                        get.shutDoors();
                        canCloseDoor.put(get, false);
                        BlockUsPlugin.getBlockUs().plugin().getServer().getScheduler().scheduleSyncDelayedTask(BlockUsPlugin.getBlockUs().plugin(), new Runnable() {
                            public void run() {
                                canCloseDoor.put(get, true);
                            }
                        }, 100);
                    }
                } else {
                    event.setCancelled(true);
                }
            });
            gui.setItem(doorSlots.get(index), item);
            index++;
        }
    }

    public void initMain() {
        List<Integer> mainSlots = Arrays.asList(21, 22, 23, 24, 25, 26);
        GuiItem mainSabatoge = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Main Sabatoges:").asGuiItem();
        gui.setItem(19, mainSabatoge);
        int index = 0;
        for (Sabatoge.SabatogeType type : Sabatoge.SabatogeType.values()) {
            GuiItem item = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Sabatoge " + type.getName()).asGuiItem(event -> {
                int slot = event.getSlot();
                if (mainSlots.contains(slot)) {
                    String displayName = gui.getGuiItem(slot).getItemStack().getItemMeta().getDisplayName();
                    if (displayName.contains("Oxygen")) {

                    } else if (displayName.contains("Reactors")) {

                    } else if (displayName.contains("Communications")) {

                    } else if (displayName.contains("Lights")) {

                    }
                } else {
                    event.setCancelled(true);
                }
            });
            gui.setItem(mainSlots.get(index), item);
            index++;
        }
    }

    private void sabatogeOxygen() {

    }

    private void sabatogeReactors() {

    }

    private void sabatogeCommunications() {

    }

    private void sabatogeLights() {

    }

}


