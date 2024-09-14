package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class RegenerateOres(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val originalType = block.type

		// Check if the block is one of the ores
		if (originalType == Material.COAL_ORE || originalType == Material.IRON_ORE || originalType == Material.DEEPSLATE_IRON_ORE || originalType == Material.RED_STAINED_GLASS || originalType == Material.RED_STAINED_GLASS_PANE) {
			val temporaryType = when (originalType) {
				Material.RED_STAINED_GLASS -> Material.GRAY_STAINED_GLASS
				Material.RED_STAINED_GLASS_PANE -> Material.GRAY_STAINED_GLASS_PANE
				else -> Material.BEDROCK
			}
			val originalState: BlockState = block.state

			object : BukkitRunnable() {
				override fun run() {
					block.type = temporaryType
//					block.state.update(true, true) // Force block update to ensure it connects with surrounding blocks
				}
			}.runTaskLater(plugin, 1L)

			// Schedule a task to restore the original block after 3 seconds (60 ticks)
			object : BukkitRunnable() {
				override fun run() {
					block.type = originalType
					originalState.update(true, true) // Restore the original state and force update
				}
			}.runTaskLater(plugin, 60L)
		}
	}
}