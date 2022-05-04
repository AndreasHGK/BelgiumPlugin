package me.hgk.belgiumplugin.mayor

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.*

object Election {
    var enabled = false

    private var confPath: String? = null
    private val conf = YamlConfiguration()

    var candidates = mutableSetOf<String>()
        private set
    var votes = mutableMapOf<UUID, String>()
        private set

    /**
     * Returns whether the provided username is a candidate to be chosen as mayor.
     */
    fun isCandidate(c: String) : Boolean {
        val c = c.lowercase()
        for (candidate in candidates) {
            if (candidate.lowercase() == c) return true
        }
        return false
    }

    /**
     * Returns who the player has voted for, and null if they havent voted for anyone yet.
     */
    fun getVote(p: Player) : String? {
        if (!votes.containsKey(p.uniqueId)) return null

        val v = votes[p.uniqueId]!!
        if (!isCandidate(v)) return null
        return v
    }

    /**
     * Make a player cast a vote on a certain mayor. Returns whether the vote was successful.
     */
    fun vote(p: Player, mayor: String) : Boolean {
        if (!isCandidate(mayor)) return false
        votes[p.uniqueId] = mayor

        return true
    }

    /**
     * Load all elections data.
     */
    fun load(path: String) {
        if (confPath != null) throw RuntimeException("trying to load the election more than once")
        confPath = path
        conf.load(confPath!!)

        val s = mutableSetOf<String>()
        for (name in conf.getStringList("candidates")) {
            s.add(name)
        }
        candidates = s

        enabled = conf.getBoolean("enabled")

        // Load all vote data
        val votesSection = conf.getConfigurationSection("votes")
        val keys = votesSection?.getKeys(false)
        if (keys != null) {
            for (name in keys) {
                votes[UUID.fromString(name)] = votesSection.get(name) as String
            }
        }
    }

    /**
     * Saves all data regarding mayors. This saves everything, including the candidates.
     */
    fun save() {
        conf.set("enabled", enabled)
        conf.set("candidates", candidates.toList())
        conf.set("votes", listOf<String>())
        for (vote in votes) {
            conf.set("votes." + vote.component1().toString(), vote.component2())
        }
        conf.save(confPath!!)
    }
}