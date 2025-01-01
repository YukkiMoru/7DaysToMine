package com.github.yukkimoru.sdtm.multi

import com.github.yukkimoru.sdtm.world.nexus.NexusScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val nexusScoreboard: NexusScoreboard) : Listener {

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		player.scoreboard = nexusScoreboard.getScoreboard()

		//com/github/yukkimoru/sdtm/utility/items/DoubleJumper.kt
		//プレイヤーのクリエ飛行を無効化する
		player.allowFlight = false
		player.isFlying = false

	}
}