import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val nexusScoreboard: NexusScoreboard) : Listener {

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		player.scoreboard = nexusScoreboard.getScoreboard()
	}
}