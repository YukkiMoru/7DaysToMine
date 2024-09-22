package com.github.yukkimoru.sdtm.world.nexus

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Zombie
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

class NexusScoreboard {

	private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
	private val objective: Objective =
		scoreboard.registerNewObjective("nexusHealth", Criteria.DUMMY, Component.text("§aNexus Health"))
	private var nexusHealth: Int = 10000
	private val targetLocation = Location(Bukkit.getWorld("world"), 100.0, 10.1, 100.0)

	init {
		objective.displaySlot = DisplaySlot.SIDEBAR
		updateNexusHealth(nexusHealth)
	}

	private fun updateNexusHealth(health: Int) {
		nexusHealth = health
		objective.getScore("§cNexus Health:").score = nexusHealth
	}

	private fun getNexusHealth(): Int {
		return nexusHealth
	}

	fun getScoreboard(): Scoreboard {
		return scoreboard
	}

	fun checkZombiesNearNexus() {
		val world = targetLocation.world

		// ネクサスを中心とした半径5ブロックの円を描画する
//        val radius = 5.0
//        val points = 100
//        for (i in 0 until points) {
//            val angle = 2 * Math.PI * i / points
//            val x = radius * Math.cos(angle)
//            val z = radius * Math.sin(angle)
//            val particleLocation = targetLocation.clone().add(Vector(x+0.5, 0.0, z+0.5))
//            world?.spawnParticle(Particle.COMPOSTER, particleLocation, 1)
//        }

		world?.entities?.forEach { entity ->
			if (entity is Zombie) {
				if (entity.location.distance(targetLocation) <= 5) {
					entity.swingMainHand()
					world.playSound(entity.location, "minecraft:block.anvil.place", 1.0f, 0.1f)

					val currentHealth = getNexusHealth()
					updateNexusHealth(currentHealth - 1)
				}
			}
		}
	}
}