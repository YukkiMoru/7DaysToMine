package com.github.yukkimoru.sdtm.trade.villagers

import com.github.yukkimoru.sdtm.world.FactoryPotion
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

	fun summonVillagerPotion(location: Location, yaw: Float) {
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

				// Set villager to face
				val newLocation = villager.location
				newLocation.yaw = yaw
				villager.teleport(newLocation)

				// Create trades
				val recipes = createPotionTrades()
				villager.recipes = recipes
			}
		}.runTask(plugin)
	}

	private fun createPotionTrades(): List<MerchantRecipe> {
		val factoryPotion = FactoryPotion(plugin)
		val recipes = mutableListOf<MerchantRecipe>()

		// Trade 1: Healing Potion for 5 emeralds
		val buyItem1 = ItemStack(Material.EMERALD, 5)
		val sellItem1 = factoryPotion.distroPotion("healing", 1)
		val recipe1 = MerchantRecipe(sellItem1, 9999999)
		recipe1.addIngredient(buyItem1)
		recipes.add(recipe1)

		// Trade 2: Strength Potion for 8 emeralds
		val buyItem2 = ItemStack(Material.EMERALD, 8)
		val sellItem2 = factoryPotion.distroPotion("strength", 1)
		val recipe2 = MerchantRecipe(sellItem2, 9999999)
		recipe2.addIngredient(buyItem2)
		recipes.add(recipe2)

		// Trade 3: Speed Potion for 6 emeralds
		val buyItem3 = ItemStack(Material.EMERALD, 6)
		val sellItem3 = factoryPotion.distroPotion("speed", 1)
		val recipe3 = MerchantRecipe(sellItem3, 9999999)
		recipe3.addIngredient(buyItem3)
		recipes.add(recipe3)

		// Trade 4: Giant Potion for 10 emeralds
		val buyItem4 = ItemStack(Material.EMERALD, 10)
		val sellItem4 = factoryPotion.distroPotion("giant", 1)
		val recipe4 = MerchantRecipe(sellItem4, 9999999)
		recipe4.addIngredient(buyItem4)
		recipes.add(recipe4)

		// Trade 5: Midget Potion for 3 emeralds
		val buyItem5 = ItemStack(Material.EMERALD, 3)
		val sellItem5 = factoryPotion.distroPotion("midget", 1)
		val recipe5 = MerchantRecipe(sellItem5, 9999999)
		recipe5.addIngredient(buyItem5)
		recipes.add(recipe5)

		return recipes
	}
}