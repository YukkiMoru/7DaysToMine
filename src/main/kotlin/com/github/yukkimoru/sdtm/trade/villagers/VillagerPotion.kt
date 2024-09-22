package com.github.yukkimoru.sdtm.trade.villagers

import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerPotion(private val plugin: JavaPlugin) {

	fun summonVillagerPotion(location: Location) {
		object : BukkitRunnable() {
			override fun run() {
				val world = location.world ?: return
				val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

				// Set villager properties
				villager.profession = Villager.Profession.CLERIC
				villager.villagerLevel = 2
				villager.villagerType = Villager.Type.PLAINS
				villager.isCustomNameVisible = true
				villager.customName(Component.text("Potion Master"))

				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face south
				villager.location.yaw = 180f

				// Create trades
				val recipes = mutableListOf<MerchantRecipe>()

				// Trade 1: 10 iron blocks for 4 emerald blocks
				val buyItem1 = ItemStack(Material.IRON_BLOCK, 10)
				val sellItem1 = ItemStack(Material.EMERALD_BLOCK, 4)
				val recipe1 = MerchantRecipe(sellItem1, 9999999)
				recipe1.addIngredient(buyItem1)
				recipes.add(recipe1)

				// Trade 2: 2 diamond blocks for 20 emerald blocks
				val buyItem2 = ItemStack(Material.DIAMOND_BLOCK, 2)
				val sellItem2 = ItemStack(Material.EMERALD_BLOCK, 20)
				val recipe2 = MerchantRecipe(sellItem2, 9999999)
				recipe2.addIngredient(buyItem2)
				recipes.add(recipe2)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}
}