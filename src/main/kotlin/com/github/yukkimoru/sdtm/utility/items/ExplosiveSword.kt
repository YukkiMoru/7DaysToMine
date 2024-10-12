package com.github.yukkimoru.sdtm.utility.items

import com.github.yukkimoru.sdtm.world.FactoryItem
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class ExplosiveSword(private val plugin: JavaPlugin) : Listener {

	private val cooldownTime = 5000 // 5 seconds
	private val lastUseTime = mutableMapOf<Player, Long>()
	private val factoryItem = FactoryItem(plugin)

	private fun isSpecialWeapon(item: ItemStack?): Boolean {
		return item?.let { factoryItem.isItemWithCustomModelData(it, 302) } ?: false
	}

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		val damager = event.damager
		val entity = event.entity

		if (damager is Player) {
			val itemInHand = damager.inventory.itemInMainHand

			if (isSpecialWeapon(itemInHand)) {
				val currentTime = System.currentTimeMillis()
				val lastUse = lastUseTime[damager] ?: 0

				if (currentTime - lastUse >= cooldownTime) {
					damager.sendMessage("§aSpecial ability activated!")
					entity.world.createExplosion(entity.location, 2.0f, false, false)
					lastUseTime[damager] = currentTime

					// ノックバックを受けるようにする
					damager.velocity = damager.location.direction.multiply(-1)

					object : BukkitRunnable() {
						override fun run() {
							damager.sendMessage("§aSpecial ability is ready again!")
						}
					}.runTaskLater(plugin, (cooldownTime / 50).toLong()) // Convert milliseconds to ticks
				} else {
					damager.sendMessage("§cSpecial ability is on cooldown!")
				}
			}
		}
	}
}