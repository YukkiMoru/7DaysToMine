package com.github.yukkimoru.sdtm

import com.github.yukkimoru.sdtm.multi.PlayerJoinListener
import com.github.yukkimoru.sdtm.trade.DeployVillagers
import com.github.yukkimoru.sdtm.trade.ProtectVillagers
import com.github.yukkimoru.sdtm.utility.TabList
import com.github.yukkimoru.sdtm.utility.ability.DamageIndicator
import com.github.yukkimoru.sdtm.utility.ability.DrinkPotion
import com.github.yukkimoru.sdtm.utility.ability.GrapplingHook
import com.github.yukkimoru.sdtm.utility.commands.SDCommand
import com.github.yukkimoru.sdtm.utility.gui.GUIReceiver
import com.github.yukkimoru.sdtm.utility.items.DoubleJumper
import com.github.yukkimoru.sdtm.utility.items.EnderPack
import com.github.yukkimoru.sdtm.utility.items.ExplosiveSword
import com.github.yukkimoru.sdtm.world.DropOres
import com.github.yukkimoru.sdtm.world.FactoryTool
import com.github.yukkimoru.sdtm.world.MiningOres
import com.github.yukkimoru.sdtm.world.RegenerateBlocks
import com.github.yukkimoru.sdtm.world.enemy.ControlMobs
import com.github.yukkimoru.sdtm.world.enemy.DropMobs
import com.github.yukkimoru.sdtm.world.nexus.NexusScoreboard
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class SDTM : JavaPlugin() {
	private lateinit var nexusScoreboard: NexusScoreboard
	private lateinit var playerList: TabList
	private lateinit var drinkPotion: DrinkPotion

	override fun onEnable() {
		logger.info("SDTM plugin enabled")


		// プラグイン開始時に実行するマイクラコマンド
		executeCommand()
		// 各機能の初期化(諸事情によりinitWorld()を最初に実行)
		initWORLD()
		initMaster()
		initMulti()
		initTRADE()
		initUTILITY()
		scheduleTabListUpdates()
	}

	private fun executeCommand() {
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

	private fun initMaster() {
		// GameMasterを登録
//		server.pluginManager.registerEvents(GameMaster(this), this)
	}

	private fun initMulti() {
		// PlayerJoinListenerを登録
		server.pluginManager.registerEvents(PlayerJoinListener(nexusScoreboard), this)
	}

	private fun initTRADE() {
		// DeployVillagersを登録 (Villagersを召喚,トレードを設定)
		val summonVillagers = DeployVillagers(this)
		server.pluginManager.registerEvents(summonVillagers, this)
		summonVillagers.summonVillagers()

		// ProtectVillagersを登録
		server.pluginManager.registerEvents(ProtectVillagers(), this)
	}

	private fun initUTILITY() {
		initAbility()
		initItems()
		initCommands()
		playerList = TabList()
		playerList.initialize()

		// GUIを登録
		server.pluginManager.registerEvents(GUIReceiver(), this)
	}

	private fun initAbility() {
		// DamageIndicatorを登録 (プレイヤーがダメージを受けた際にダメージ量を表示)
		server.pluginManager.registerEvents(DamageIndicator(this), this)
		// GrapplingHookを登録 (グラップリングフック)
		server.pluginManager.registerEvents(GrapplingHook(this), this)
		// HealthIndicatorを登録 (プレイヤーのボスバーにモブの体力を表示)
//		server.pluginManager.registerEvents(HealthIndicator(this), this)
//		// DrinkPotionを登録 (ポーションの効果)
//		server.pluginManager.registerEvents(DrinkPotion(this), this)

		// Initialize DrinkPotion
		drinkPotion = DrinkPotion(this)
		// Register DrinkPotion as an event listener
		server.pluginManager.registerEvents(drinkPotion, this)
	}

	private fun initItems() {
		// EnderPackを登録 (エンダーチェストを開けるアイテム)
		server.pluginManager.registerEvents(EnderPack.OpenEnderChest(), this)
		// DoubleJumperを登録 (プレイヤーが2段ジャンプできる靴)
		server.pluginManager.registerEvents(DoubleJumper(this), this)

		// ExplosiveSwordを登録 (爆発する剣)
		server.pluginManager.registerEvents(ExplosiveSword(this), this)
	}

	private fun initCommands() {
		SDCommand(this)
		// ScanBlocksとSpawnMobsはSDCommandで呼び出されている
	}

	private fun initWORLD() {
		initEnemy()
		initNexus()

		// MiningOresを登録 (鉱石の採掘)
		server.pluginManager.registerEvents(DropOres(this, FactoryTool(this)), this)
		// ItemCreateは登録していない (アイテムの生成)、各インスタンスで使う(e.g.村人)
		// MiningOresを登録 (プレイヤーによるブロックの破壊速度の変更)
		server.pluginManager.registerEvents(MiningOres(this), this)
		// RegenerateBlocksを登録 (ブロックの再生)
		server.pluginManager.registerEvents(RegenerateBlocks(this), this)

		//
	}

	private fun initEnemy() {
		// ControlMobsを登録 (モブの移動)
		val mobController = ControlMobs(this)
		mobController.startMovingMobsToLocation()
		// DropMobsを登録 (モブのドロップアイテム)
		server.pluginManager.registerEvents(DropMobs(this), this)
	}

	private fun initNexus() {
		nexusScoreboard = NexusScoreboard()
		server.onlinePlayers.forEach { player ->
			player.scoreboard = nexusScoreboard.getScoreboard()
		}
		server.scheduler.scheduleSyncRepeatingTask(this, {
			nexusScoreboard.checkZombiesNearNexus()
		}, 0L, 20L) // 20 ticks = 1 second
	}

	private fun scheduleTabListUpdates() {
		Bukkit.getScheduler().runTaskTimer(this, Runnable {
			for (player in server.onlinePlayers) {
				val cooldowns = drinkPotion.getCooldowns(player)
				playerList.updatePotionCooldowns(player, cooldowns)
			}
		}, 0L, 20L)
	}

	override fun onDisable() {
		// Plugin shutdown logic
	}
}