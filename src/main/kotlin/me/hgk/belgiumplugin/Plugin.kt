package me.hgk.belgiumplugin

import me.hgk.belgiumplugin.mayor.Election
import me.hgk.belgiumplugin.mayor.ElectionCommand
import me.hgk.belgiumplugin.mayor.ElectionListener
import me.hgk.belgiumplugin.mayor.VoteCommand
import org.bukkit.plugin.java.JavaPlugin

class Plugin : JavaPlugin() {
    override fun onEnable() {
        Election.load("mayors.yml")

        val listeners = listOf(
            ElectionListener()
        )
        for (listener in listeners) {
            server.pluginManager.registerEvents(listener, this)
        }
        logger.info("registered ${listeners.size} listeners")

        val commands = listOf(
            VoteCommand(),
            ElectionCommand(),
        )
        server.commandMap.registerAll("belgium", commands)
        logger.info("registered ${commands.size} commands")
    }

    override fun onDisable() {
        Election.save()
    }
}