package com.github.YukkiMoru.SDTM

import PlayerJoinListener
import com.github.YukkiMoru.SDTM.CORE.ControlMobs
import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import com.github.YukkiMoru.SDTM.TRADE.DeployVillagers
import com.github.YukkiMoru.SDTM.UTILITY.SDCommand
import com.github.YukkiMoru.SDTM.UTILITY.protectVillager
import com.github.YukkiMoru.SDTM.WORLD.DropOres
import com.github.YukkiMoru.SDTM.WORLD.MiningOres
import com.github.YukkiMoru.SDTM.WORLD.RegenerateBlocks
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SDTM : JavaPlugin() {
	private lateinit var nexusScoreboard: NexusScoreboard

	override fun onEnable() {
		logger.info("SDTM plugin enabled")
		initializeCommands()
		initializeCORE()
		initializeTRADE()
		initializeUTILITY()
		initializeWORLD()
	}

	private fun initializeCommands() {
		// プレイヤーのブロック破壊速度を0に設定
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "attribute @p player.block_break_speed base set 0")
		// プレイヤー以外のエンティティを全て削除
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!player]")
		// ネクサスのエンドストーンを召喚
		Bukkit.dispatchCommand(
			Bukkit.getConsoleSender(),
			"summon block_display 100.1 11.1 100.1 {transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],scale:[0.8f,0.8f,0.8f],translation:[0f,0f,0f]},block_state:{Name:end_stone}}"
		)
	}

	private fun initializeCORE() {
		nexusScoreboard = NexusScoreboard()
		server.onlinePlayers.forEach { player ->
			player.scoreboard = nexusScoreboard.getScoreboard()
		}
		server.pluginManager.registerEvents(PlayerJoinListener(nexusScoreboard), this)
	}

	private fun initializeTRADE() {
		val summonVillagers = DeployVillagers(this)
		server.pluginManager.registerEvents(summonVillagers, this)
		summonVillagers.summonVillagers()
	}

	private fun initializeUTILITY() {
		server.pluginManager.registerEvents(protectVillager(this), this)
		SDCommand(this).registerCommands()
	}

	private fun initializeWORLD() {
		val mobController = ControlMobs(this, nexusScoreboard)
		mobController.startMovingMobsToLocation()

		server.scheduler.scheduleSyncRepeatingTask(this, {
			nexusScoreboard.checkZombiesNearNexus()
		}, 0L, 20L) // 20 ticks = 1 second

		server.pluginManager.registerEvents(MiningOres(this), this)
		server.pluginManager.registerEvents(RegenerateBlocks(this), this)
		server.pluginManager.registerEvents(DropOres(this), this)
	}

	override fun onDisable() {
		// Plugin shutdown logic
	}
}