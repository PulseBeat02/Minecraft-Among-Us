package com.github.amongus.game.tasks;

import com.github.amongus.utility.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.amongus.game.Game;

import me.mattstudios.mfgui.gui.guis.PersistentGui;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import java.util.Arrays;
import java.util.List;

public class ChartCourse extends Task {
	
	private final PersistentGui gui;
	private final List<Integer> course = Arrays.asList(28, 20, 12, 22, 32, 24, 16);
	private int currentPos = 0;

	public ChartCourse(Game game, Location loc) {
		super(game, "Chart Course", loc);
		
		this.gui = new PersistentGui(5, "Chart Course");

		GuiItem clicked = new GuiItem(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).withName(ChatColor.BLUE + "").get());
		GuiItem path = new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName(ChatColor.RED + "").get(), event -> {
			int slot = event.getSlot();
			if (slot == course.get(currentPos)) {
				if (slot == 16) {
					callComplete((Player)event.getWhoClicked());
				}
				gui.setItem(course.get(currentPos), clicked);
				currentPos++;
			}
		});
		gui.setItem(course, path);

		setEmpty(gui);

	}

	

	@Override
	public void execute(Player p, PlayerArmorStandManipulateEvent e) {
		gui.open(p);
	}
	
	
}
