package com.github.yukkimoru.sdtm.trade.pickaxe

import com.github.yukkimoru.sdtm.trade.gui.Interface
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
				//岩盤を取引する
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
			if (entity.customName()?.equals(Component.text("ツルハシの商人")) == true) {
//				Bukkit.getLogger().info("Opening GUI for player: ${event.player.name}")
				event.isCancelled = true
				openPickaxeShop(event.player)
			}
		}
	}

	private fun openPickaxeShop(player: Player) {
		val gui = Interface.shopPickaxe()
		player.openInventory(gui)
	}
}