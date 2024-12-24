package com.github.yukkimoru.sdtm.world

import com.github.yukkimoru.sdtm.trade.pickaxe.ToolFactory
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class RegenerateBlocks(private val toolFactory: ToolFactory, private val plugin: JavaPlugin) : Listener {

	private val ores: Set<Material> = toolFactory.allBreakableOreMaterials.toSet()
	private val gems: Set<Material> = toolFactory.allBreakableGemMaterials.toSet()
	private val gemShards: Set<Material> = toolFactory.allBreakableGemShardMaterials.toSet()

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val originalState = block.state
		val temporaryState = block.state

		if (toolFactory.allBreakableMaterials.contains(originalState.type)) {
			temporaryState.type = when {
				ores.contains(originalState.type) -> Material.BEDROCK
				gems.contains(originalState.type) -> Material.GRAY_STAINED_GLASS
				gemShards.contains(originalState.type) -> Material.GRAY_STAINED_GLASS_PANE
				else -> Material.BEDROCK
			}
			if (ores.contains(originalState.type) || gems.contains(originalState.type)) {
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

			} else if (gemShards.contains(originalState.type)) {
				object : BukkitRunnable() {
					override fun run() {
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