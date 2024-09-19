package com.github.YukkiMoru.SDTM.TRADE

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerPickaxe(private val plugin: JavaPlugin) {

	fun summonVillagerPickaxe(location: Location) {
		object : BukkitRunnable() {
			override fun run() {
				val world = location.world ?: return
				val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

				// Set villager properties
				villager.profession = Villager.Profession.TOOLSMITH
				villager.villagerLevel = 2
				villager.villagerType = Villager.Type.PLAINS
				villager.isCustomNameVisible = true
				villager.customName = "Pickaxe Master"
				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face south
				villager.location.yaw = 180f

				// Create trades
				val recipes = mutableListOf<MerchantRecipe>()

				// Trade 1: 10 emeralds for 1 unbreakable iron pickaxe that can break specific blocks
				val buyItem1 = ItemStack(Material.EMERALD, 10)
				val sellItem1 = ItemStack(Material.IRON_PICKAXE, 1)
				val meta: ItemMeta = sellItem1.itemMeta

				// Set the item to be unbreakable
				meta.isUnbreakable = true

				// Set the item to be able to break specific blocks using PersistentDataContainer
				val container = meta.persistentDataContainer
				val key = NamespacedKey(plugin, "destroyable_blocks")
				val destroyableBlocks =
					"minecraft:coal_ore:1.0:1.0,minecraft:iron_ore:1.0:1.0,minecraft:deepslate_iron_ore:0.8:1.5,minecraft:red_stained_glass:0.5:1.0,minecraft:red_stained_glass_pane:0.7:0.5"
				container.set(key, PersistentDataType.STRING, destroyableBlocks)

				// Add lore to display destroyable blocks
				val lore = listOf(
					"${ChatColor.GREEN}破壊可能なブロック:",
					"${ChatColor.GREEN}⛏1.0 ☘1.0 石炭鉱石",
					"${ChatColor.GREEN}⛏1.0 ☘1.0 鉄鉱石",
					"${ChatColor.GREEN}⛏0.8 ☘1.5 深層鉄鉱石",
					"${ChatColor.GREEN}⛏0.5(0.7) ☘1.0(0.5) ルビー鉱石",
				)
				meta.lore = lore

				sellItem1.itemMeta = meta

				val recipe1 = MerchantRecipe(sellItem1, 9999999)
				recipe1.addIngredient(buyItem1)
				recipes.add(recipe1)

				// Trade 2: 2 diamond blocks for 20 emerald blocks
				val buyItem2 = ItemStack(Material.DIAMOND_BLOCK, 2)
				val sellItem2 = ItemStack(Material.EMERALD_BLOCK, 20)
				val recipe2 = MerchantRecipe(sellItem2, 9999999)
				recipe2.addIngredient(buyItem2)
				recipes.add(recipe2)


// Trade 3: 1 unbreakable iron pickaxe for 10 emeralds
				val buyItem3 = ItemStack(Material.IRON_PICKAXE, 1)
				val meta3: ItemMeta = buyItem3.itemMeta

// Set the item to be unbreakable
				meta3.isUnbreakable = true

// Set the item to be able to break specific blocks using PersistentDataContainer
				val container3 = meta3.persistentDataContainer
				val key3 = NamespacedKey(plugin, "destroyable_blocks")
				val destroyableBlocks3 =
					"minecraft:coal_ore:1.0:1.0,minecraft:iron_ore:1.0:1.0,minecraft:deepslate_iron_ore:0.8:1.5,minecraft:red_stained_glass:0.5:1.0,minecraft:red_stained_glass_pane:0.7:0.5"
				container3.set(key3, PersistentDataType.STRING, destroyableBlocks3)

// Add lore to display destroyable blocks
				val lore3 = listOf(
					"${ChatColor.GREEN}破壊可能なブロック:",
					"${ChatColor.GREEN}⛏1.0 ☘1.0 石炭鉱石",
					"${ChatColor.GREEN}⛏1.0 ☘1.0 鉄鉱石",
					"${ChatColor.GREEN}⛏0.8 ☘1.5 深層鉄鉱石",
					"${ChatColor.GREEN}⛏0.5(0.7) ☘1.0(0.5) ルビー鉱石",
				)
				meta3.lore = lore3

				buyItem3.itemMeta = meta3
				val sellItem3 = ItemStack(Material.EMERALD, 10)

				val recipe3 = MerchantRecipe(sellItem3, 9999999)
				recipe3.addIngredient(buyItem3)
				recipes.add(recipe3)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}
}