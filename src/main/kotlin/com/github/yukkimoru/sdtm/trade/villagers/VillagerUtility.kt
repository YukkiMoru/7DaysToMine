package com.github.yukkimoru.sdtm.trade.villagers

import com.github.yukkimoru.sdtm.world.FactoryItem
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerUtility(private val plugin: JavaPlugin) {

	fun summonVillagerUtility(location: Location, yaw: Float) {
		object : BukkitRunnable() {
			override fun run() {
				val world = location.world ?: return
				val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

				// Set villager properties
				villager.profession = Villager.Profession.LEATHERWORKER
				villager.villagerLevel = 2
				villager.villagerType = Villager.Type.PLAINS
				villager.isCustomNameVisible = true
				villager.customName(Component.text("Utility Master"))
				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face
				val newLocation = villager.location
				newLocation.yaw = yaw
				villager.teleport(newLocation)

				// Create trades
				val recipes = mutableListOf<MerchantRecipe>()

				// Create an instance of ItemCreate
				val itemCreate = FactoryItem(plugin)

				// Trade 1: 10 emeralds for 1 Ender Pack
				val buyItem1 = ItemStack(Material.EMERALD, 10)
				val sellItem1 = itemCreate.createItemStack(
					Material.RECOVERY_COMPASS, 1, "§r§1§lEnder Pack", listOf(
						"§7右クリックでエンダーチェストを開く"
					),
					"uncommon",
					300
				)
				val meta: ItemMeta = sellItem1.itemMeta
				sellItem1.itemMeta = meta

				val recipe1 = MerchantRecipe(sellItem1, 9999999)
				recipe1.addIngredient(buyItem1)
				recipes.add(recipe1)

				val buyItem2 = ItemStack(Material.EMERALD, 5)
				val sellItem2 = itemCreate.createItemStack(
					Material.IRON_BOOTS, 1, "§r§2§lDouble Jumper", listOf(
						"§7２段ジャンプができる"
					),
					"rare",
					301
				)
				val recipe2 = MerchantRecipe(sellItem2, 9999999)
				recipe2.addIngredient(buyItem2)
				recipes.add(recipe2)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}
}