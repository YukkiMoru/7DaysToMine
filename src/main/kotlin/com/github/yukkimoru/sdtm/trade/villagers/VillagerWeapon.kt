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

class VillagerWeapon(private val plugin: JavaPlugin) {

	fun summonVillagerWeapon(location: Location, yaw: Float) {
		object : BukkitRunnable() {
			override fun run() {
				val world = location.world ?: return
				val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

				// Set villager properties
				villager.profession = Villager.Profession.WEAPONSMITH
				villager.villagerLevel = 2
				villager.villagerType = Villager.Type.PLAINS
				villager.isCustomNameVisible = true
				villager.customName(Component.text("Weapon Master"))
				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face
				val newLocation = villager.location
				newLocation.yaw = yaw
				villager.teleport(newLocation)

				// Create trades
				val recipes = mutableListOf<MerchantRecipe>()

				// Example trade: 10 emeralds for 1 diamond sword
				val buyItem = ItemStack(Material.EMERALD, 10)
				val sellItem = ItemStack(Material.DIAMOND_SWORD, 1)
				val recipe = MerchantRecipe(sellItem, 9999999)
				recipe.addIngredient(buyItem)
				recipes.add(recipe)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}
}