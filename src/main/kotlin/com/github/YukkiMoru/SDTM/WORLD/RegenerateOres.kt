package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
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
		if (originalType == Material.COAL_ORE || originalType == Material.IRON_ORE || originalType == Material.DEEPSLATE_IRON_ORE || originalType == Material.RED_STAINED_GLASS) {
//			event.isCancelled = true
			object : BukkitRunnable() {
				override fun run() {
					block.type = Material.BEDROCK
				}
			}.runTaskLater(plugin, 1L)

			// Schedule a task to restore the original block after 3 seconds (60 ticks)
			object : BukkitRunnable() {
				override fun run() {
					block.type = originalType
				}
			}.runTaskLater(plugin, 60L)
		}
	}
}