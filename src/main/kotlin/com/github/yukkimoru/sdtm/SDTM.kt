package com.github.yukkimoru.sdtm

import com.github.yukkimoru.sdtm.core.ControlMobs
import com.github.yukkimoru.sdtm.core.DropMobs
import com.github.yukkimoru.sdtm.core.NexusScoreboard
import com.github.yukkimoru.sdtm.multi.PlayerJoinListener
import com.github.yukkimoru.sdtm.trade.DeployVillagers
import com.github.yukkimoru.sdtm.trade.ProtectVillagers
import com.github.yukkimoru.sdtm.utility.Items.EnderPack
import com.github.yukkimoru.sdtm.utility.SDCommand
import com.github.yukkimoru.sdtm.utility.ability.DamageIndicator
import com.github.yukkimoru.sdtm.utility.ability.DoubleJump
import com.github.yukkimoru.sdtm.utility.ability.GrapplingHook
import com.github.yukkimoru.sdtm.world.DropOres
import com.github.yukkimoru.sdtm.world.MiningOres
import com.github.yukkimoru.sdtm.world.RegenerateBlocks
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
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

		server.pluginManager.registerEvents(DropMobs(this), this)
	}

	private fun initializeTRADE() {
		val summonVillagers = DeployVillagers(this)
		server.pluginManager.registerEvents(summonVillagers, this)
		summonVillagers.summonVillagers()
	}

	private fun initializeUTILITY() {
		server.pluginManager.registerEvents(ProtectVillagers(), this)
		SDCommand(this).registerCommands()

		// プレイヤーのボスバーにモブの体力を表示
//		HealthIndicator(this).startHealthIndicator()

		// ダメージを受けた際にダメージ量を表示
		server.pluginManager.registerEvents(DamageIndicator(this), this)

		server.pluginManager.registerEvents(DoubleJump(this), this)

		server.pluginManager.registerEvents(EnderPack.OpenEnderChest(), this)

		server.pluginManager.registerEvents(GrapplingHook(), this)
	}

	private fun initializeWORLD() {
		val mobController = ControlMobs(this)
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