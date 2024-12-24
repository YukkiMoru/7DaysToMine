package com.github.yukkimoru.sdtm.trade.potion

import com.github.yukkimoru.sdtm.trade.gui.Interface
import net.kyori.adventure.text.Component
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

class VillagerPotion(private val plugin: JavaPlugin) : Listener {

	init {
		plugin.server.pluginManager.registerEvents(this, plugin)
	}
	
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
				villager.customName(Component.text("ポーションの商人"))

				villager.isPersistent = true

				// Disable AI
				villager.setAI(false)

				// Set villager to face
				val newLocation = villager.location
				newLocation.yaw = yaw
				villager.teleport(newLocation)

				// Create trades (deprecated)
//				val recipes = createPotionTrades()

				//岩盤を取引する
				val recipes = mutableListOf<MerchantRecipe>()
				val bedrockTrade = MerchantRecipe(ItemStack(Material.BARRIER), 1, 1, false)
				bedrockTrade.addIngredient(ItemStack(Material.BARRIER, 1))
				recipes.add(bedrockTrade)
				villager.recipes = recipes
			}
		}.runTask(plugin)
	}

	@EventHandler
	fun onVillagerClick(event: PlayerInteractEntityEvent) {
		val entity = event.rightClicked
		if (entity is Villager) {
//			Bukkit.getLogger().info("Villager clicked: ${entity.customName()}")
			if (entity.customName()?.equals(Component.text("ポーションの商人")) == true) {
//				Bukkit.getLogger().info("Opening GUI for player: ${event.player.name}")
				event.isCancelled = true
				openPotionShop(event.player)
			}
		}
	}

	private fun openPotionShop(player: Player) {
		val gui = Interface.shopPotion()
		player.openInventory(gui)
	}

//	 Create trades (deprecated)
//	private fun createPotionTrades(): List<MerchantRecipe> {
//		val potionFactory = PotionFactory(plugin)
//		val recipes = mutableListOf<MerchantRecipe>()
//
//		// Trade 1: Healing Potion for 5 emeralds
//		val buyItem1 = ItemStack(Material.EMERALD, 5)
//		val sellItem1 = potionFactory.getPotion("healing", 1)
//		val recipe1 = MerchantRecipe(sellItem1, 9999999)
//		recipe1.addIngredient(buyItem1)
//		recipes.add(recipe1)
//
//		// Trade 2: Strength Potion for 8 emeralds
//		val buyItem2 = ItemStack(Material.EMERALD, 8)
//		val sellItem2 = potionFactory.getPotion("strength", 1)
//		val recipe2 = MerchantRecipe(sellItem2, 9999999)
//		recipe2.addIngredient(buyItem2)
//		recipes.add(recipe2)
//
//		// Trade 3: Speed Potion for 6 emeralds
//		val buyItem3 = ItemStack(Material.EMERALD, 6)
//		val sellItem3 = potionFactory.getPotion("speed", 1)
//		val recipe3 = MerchantRecipe(sellItem3, 9999999)
//		recipe3.addIngredient(buyItem3)
//		recipes.add(recipe3)
//
//		// Trade 4: Giant Potion for 10 emeralds
//		val buyItem4 = ItemStack(Material.EMERALD, 10)
//		val sellItem4 = potionFactory.getPotion("giant", 1)
//		val recipe4 = MerchantRecipe(sellItem4, 9999999)
//		recipe4.addIngredient(buyItem4)
//		recipes.add(recipe4)
//
//		// Trade 5: Midget Potion for 3 emeralds
//		val buyItem5 = ItemStack(Material.EMERALD, 3)
//		val sellItem5 = potionFactory.getPotion("midget", 1)
//		val recipe5 = MerchantRecipe(sellItem5, 9999999)
//		recipe5.addIngredient(buyItem5)
//		recipes.add(recipe5)
//
//		return recipes
//	}
}