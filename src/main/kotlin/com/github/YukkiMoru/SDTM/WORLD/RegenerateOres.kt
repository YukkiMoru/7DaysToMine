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

		val ores = listOf(
			Material.COAL_ORE,
			Material.IRON_ORE,
			Material.DEEPSLATE_IRON_ORE,
			Material.RED_STAINED_GLASS,
			Material.RED_STAINED_GLASS_PANE
		)
		val gemstones = listOf(
			Material.RED_STAINED_GLASS
		)
		val shardGemstones = listOf(
			Material.RED_STAINED_GLASS_PANE
		)
		val breakableBlocks = ores + gemstones + shardGemstones

		if (breakableBlocks.contains(originalType)) {
			val temporaryType = when {
				ores.contains(originalType) -> Material.BEDROCK
				gemstones.contains(originalType) -> Material.GRAY_STAINED_GLASS
				shardGemstones.contains(originalType) -> Material.GRAY_STAINED_GLASS_PANE
				else -> Material.BEDROCK
			}
			val originalState: BlockState = block.state
			object : BukkitRunnable() {
				override fun run() {
					block.type = temporaryType
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