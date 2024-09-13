package com.github.YukkiMoru.SDTM

import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import com.github.YukkiMoru.SDTM.TRADE.DeployVillagers
import com.github.YukkiMoru.SDTM.UTILITY.SDCommand
import com.github.YukkiMoru.SDTM.UTILITY.protectVillager
import com.github.YukkiMoru.SDTM.WORLD.ControlMobs
import com.github.YukkiMoru.SDTM.WORLD.DropOres
import com.github.YukkiMoru.SDTM.WORLD.MiningOres
import com.github.YukkiMoru.SDTM.WORLD.RegenerateOres
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SDTM : JavaPlugin() {

	override fun onEnable() {
		logger.info("SDTM plugin enabled")
// COMMANDS
		// プレイヤーのブロック破壊速度を0に設定
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "attribute @p player.block_break_speed base set 0")
		// プレイヤー以外のエンティティを全て削除
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!player]")
		// ネクサスのエンドストーンを召喚
		Bukkit.dispatchCommand(
			Bukkit.getConsoleSender(),
			"summon block_display 100.1 11.1 100.1 {transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],scale:[0.8f,0.8f,0.8f],translation:[0f,0f,0f]},block_state:{Name:end_stone}}"
		)
// CORE
		val nexusScoreboard = NexusScoreboard()
		server.onlinePlayers.forEach { player ->
			player.scoreboard = nexusScoreboard.getScoreboard()
		}
// TRADE
		val summonVillagers = DeployVillagers(this)
		server.pluginManager.registerEvents(summonVillagers, this)
		summonVillagers.summonVillagers()
// UTILITY
		server.pluginManager.registerEvents(protectVillager(this), this)

		SDCommand(this).registerCommands()
// WORLD
		val mobController = ControlMobs(this, nexusScoreboard)
		mobController.startMovingMobsToLocation()

		server.scheduler.scheduleSyncRepeatingTask(this, {
			nexusScoreboard.checkZombiesNearNexus()
		}, 0L, 20L) // 20 ticks = 1 second

		server.pluginManager.registerEvents(MiningOres(this), this)
		server.pluginManager.registerEvents(RegenerateOres(this), this)
		server.pluginManager.registerEvents(DropOres(this), this)
	}

	override fun onDisable() {
		// Plugin shutdown logic
	}
}