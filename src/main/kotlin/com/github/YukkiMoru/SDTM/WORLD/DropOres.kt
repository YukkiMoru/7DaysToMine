package com.github.YukkiMoru.SDTM.WORLD

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.random.Random

class DropOres(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val player = event.player
		val itemInHand: ItemStack = player.inventory.itemInMainHand

		if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE || block.type == Material.DEEPSLATE_IRON_ORE || block.type == Material.RED_STAINED_GLASS) {
			event.isDropItems = false // Prevent default drops

			val key = NamespacedKey(plugin, "destroyable_blocks")
			val luck = MiningOres.getBlockFortune(itemInHand, key, block.type) ?: 0.0

			// message to player
			player.sendMessage("Luck: $luck")

			if (block.type == Material.RED_STAINED_GLASS) {
				player.sendMessage("You have found a Ruby Gemstone!")
			}
			val dropCount = calculateDropCount(luck)
			val dropItem = when (block.type) {
				Material.COAL_ORE -> ItemStack(Material.COAL, dropCount)
				Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> ItemStack(Material.RAW_IRON, dropCount)
				Material.RED_STAINED_GLASS -> {
					val customDrop = ItemStack(Material.PLAYER_HEAD, dropCount)
					val meta = customDrop.itemMeta as SkullMeta
					meta.setDisplayName("Perfect Ruby Gemstone")
					meta.lore = listOf(
						"Custom Head ID: 48269",
						"www.minecraft-heads.com"
					)
					val profile = GameProfile(UUID.fromString("b1a8b1c7-8b8b-4b8b-8f8b-8f8b8f8b8f8b"), "RubyGemstone")
					profile.properties.put(
						"textures",
						Property(
							"textures",
							"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzliNmUwNDdkM2IyYmNhODVlOGNjNDllNTQ4MGY5Nzc0ZDhhMGVhZmU2ZGZhOTU1OTUzMDU5MDI4MzcxNTE0MiJ9fX0="
						)
					)
					meta.owningPlayer = player
					customDrop.itemMeta = meta
					customDrop
				}

				else -> return
			}

			block.world.dropItemNaturally(block.location, dropItem)
		}
	}

	private fun calculateDropCount(luck: Double): Int {
		val baseDropCount = luck.toInt()
		val additionalDropChance = luck - baseDropCount

		return if (Random.nextDouble() < additionalDropChance) {
			baseDropCount + 1
		} else {
			baseDropCount
		}
	}
}