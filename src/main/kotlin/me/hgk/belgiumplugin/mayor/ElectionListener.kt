package me.hgk.belgiumplugin.mayor

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ElectionListener : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!Election.enabled || Election.getVote(e.player) != null || !e.player.hasPermission("belgium.election.vote")) return

        e.player.sendMessage("§3§lNOTICE §r§7You have not yet voted for the mayoral elections. Vote with §b§l/vote [player]§r§7!")
    }
}