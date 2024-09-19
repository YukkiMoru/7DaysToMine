package com.github.yukkimoru.sdtm.world

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class RegenerateBlocks(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val originalState = block.state
		val temporaryState = block.state

		val ores = listOf(
			Material.COAL_ORE,
			Material.IRON_ORE,
			Material.DEEPSLATE_IRON_ORE,
		)
		val gemstones = listOf(
			Material.RED_STAINED_GLASS
		)
		val shardGemstones = listOf(
			Material.RED_STAINED_GLASS_PANE
		)
		val breakableBlocks = ores + gemstones + shardGemstones

		if (breakableBlocks.contains(originalState.type)) {
			temporaryState.type = when {
				ores.contains(originalState.type) -> Material.BEDROCK
				gemstones.contains(originalState.type) -> Material.GRAY_STAINED_GLASS
				shardGemstones.contains(originalState.type) -> Material.GRAY_STAINED_GLASS_PANE
				else -> Material.BEDROCK
			}
			if (ores.contains(originalState.type) || gemstones.contains(originalState.type)) {
				object : BukkitRunnable() {
					override fun run() {
						block.type = temporaryState.type
					}
				}.runTaskLater(plugin, 1L)

				object : BukkitRunnable() {
					override fun run() {
						block.type = originalState.type
					}
				}.runTaskLater(plugin, 60L)

			} else if (shardGemstones.contains(originalState.type)) {
				object : BukkitRunnable() {
					override fun run() {
//						block.type = temporaryState.type
						temporaryState.update(true, true)
					}
				}.runTaskLater(plugin, 1L)

				object : BukkitRunnable() {
					override fun run() {
						block.type = originalState.type
						originalState.update(true, false)
					}
				}.runTaskLater(plugin, 30L)
			}
		}
	}
}