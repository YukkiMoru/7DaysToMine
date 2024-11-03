package com.github.yukkimoru.sdtm.trade.villagers

import com.github.yukkimoru.sdtm.world.FactoryTool
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerPickaxe(private val plugin: JavaPlugin) {

	fun summonVillagerPickaxe(location: Location, yaw: Float) {
		object : BukkitRunnable() {
			override fun run() {
				val world = location.world ?: return
				val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

				// Set villager properties
				villager.profession = Villager.Profession.TOOLSMITH
				villager.villagerLevel = 2
				villager.villagerType = Villager.Type.PLAINS
				villager.isCustomNameVisible = true
				villager.customName(Component.text("Pickaxe Master"))
				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face
				val newLocation = villager.location
				newLocation.yaw = yaw
				villager.teleport(newLocation)

				// Create trades
				val recipes = mutableListOf<MerchantRecipe>()

				// Create an instance of ToolFactory
				val factoryTool = FactoryTool(plugin)

				// Trade 1
				val buyItem1 = ItemStack(Material.EMERALD, 10)
				val sellItem1 = factoryTool.createTier1Pickaxe()
				val recipe1 = MerchantRecipe(sellItem1, 9999999)
				recipe1.addIngredient(buyItem1)
				recipes.add(recipe1)

				// Trade 2
				val buyItem2 = ItemStack(Material.EMERALD, 20)
				val sellItem2 = factoryTool.createTier2Pickaxe()
				val recipe2 = MerchantRecipe(sellItem2, 9999999)
				recipe2.addIngredient(buyItem2)
				recipes.add(recipe2)

				// Trade 3
				val buyItem3 = ItemStack(Material.EMERALD, 30)
				val sellItem3 = factoryTool.createTier3Pickaxe()
				val recipe3 = MerchantRecipe(sellItem3, 9999999)
				recipe3.addIngredient(buyItem3)
				recipes.add(recipe3)

				// Trade 4
				val buyItem4 = ItemStack(Material.EMERALD, 40)
				val sellItem4 = factoryTool.createTier4Pickaxe()
				val recipe4 = MerchantRecipe(sellItem4, 9999999)
				recipe4.addIngredient(buyItem4)
				recipes.add(recipe4)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}
}