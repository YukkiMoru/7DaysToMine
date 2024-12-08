package com.github.yukkimoru.sdtm.trade.villagers

import com.github.yukkimoru.sdtm.trade.gui.Interface
import com.github.yukkimoru.sdtm.world.FactoryTool
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerPickaxe(private val plugin: JavaPlugin) : Listener {

	init {
		Bukkit.getPluginManager().registerEvents(this, plugin)
	}

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
				villager.customName(Component.text("ツルハシの商人"))
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

				fun addRecipe(
					factoryTool: FactoryTool,
					recipes: MutableList<MerchantRecipe>,
					emeralds: Int,
					tier: Int
				) {
					val buyItem = ItemStack(Material.EMERALD, emeralds)
					val sellItem = when (tier) {
						1 -> factoryTool.createTier1Pickaxe()
						2 -> factoryTool.createTier2Pickaxe()
						3 -> factoryTool.createTier3Pickaxe()
						4 -> factoryTool.createTier1GemPickaxe()
						5 -> factoryTool.createTier2GemPickaxe()
						else -> throw IllegalArgumentException("Invalid tier")
					}
					val recipe = MerchantRecipe(sellItem, 9999999).apply {
						addIngredient(buyItem)
					}
					recipes.add(recipe)
				}

				addRecipe(factoryTool, recipes, 10, 1)
				addRecipe(factoryTool, recipes, 20, 2)
				addRecipe(factoryTool, recipes, 30, 3)
				addRecipe(factoryTool, recipes, 40, 4)
				addRecipe(factoryTool, recipes, 40, 5)

				villager.recipes = recipes
			}
		}.runTask(plugin)
	}

	@EventHandler
	fun onVillagerClick(event: PlayerInteractEntityEvent) {
		val entity = event.rightClicked
		if (entity is Villager) {
			Bukkit.getLogger().info("Villager clicked: ${entity.customName()}")
			if (entity.customName()?.equals(Component.text("ツルハシの商人")) == true) {
				Bukkit.getLogger().info("Opening GUI for player: ${event.player.name}")
				// Open the GUI menu
				// wait 1 tick
				Bukkit.getScheduler().runTask(plugin, Runnable {
					guiMenuPickaxe(event.player)
				})
			}
		}
	}

	private fun guiMenuPickaxe(player: Player) {
		val gui = Interface.shopPickaxe()
		player.openInventory(gui)
	}
}