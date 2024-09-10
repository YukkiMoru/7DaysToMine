package com.github.YukkiMoru.SDTM.TRADE

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class ItemShop(private val plugin: JavaPlugin) {

    fun summonCustomVillager(location: Location) {
        object : BukkitRunnable() {
            override fun run() {
                val world = location.world ?: return
                val villager = world.spawnEntity(location, EntityType.VILLAGER) as Villager

                // Set villager properties
                villager.profession = Villager.Profession.FARMER
                villager.villagerLevel = 2
                villager.villagerType = Villager.Type.PLAINS
                villager.isCustomNameVisible = true
                villager.customName = "test"
                villager.isPersistent = true

                // Create custom trade
                val buyItem = ItemStack(Material.IRON_INGOT, 10)
                val sellItem = ItemStack(Material.BEDROCK, 19)
                val meta = sellItem.itemMeta
                meta?.setDisplayName("a")
                meta?.lore = listOf("a")
                sellItem.itemMeta = meta

                val recipe = MerchantRecipe(sellItem, 9999999)
                recipe.addIngredient(buyItem)
                villager.recipes = listOf(recipe)
            }
        }.runTask(plugin)
    }
}