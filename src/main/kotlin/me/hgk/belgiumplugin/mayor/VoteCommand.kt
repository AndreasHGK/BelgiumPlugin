package me.hgk.belgiumplugin.mayor

import me.hgk.belgiumplugin.utils.matchArgument
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.math.E

class VoteCommand : Command(
    "vote",
    "vote for a mayor",
    "/vote [candidate]",
    listOf()
) {
    init {
        permission = "belgium.election.vote"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (!Election.enabled) {
            sender.sendMessage("§6§lVOTE §r§7The elections are not currently open.")
            return true
        }

        if (args.isEmpty()) {
            val vote = Election.getVote(sender)
            if (vote != null) {
                sender.sendMessage("§6§lVOTE §r§7You have currently voted for §e§l${vote}§r§7. You can still change your vote by re-running the command. §8(Do /vote list to show all candidates)")
            } else {
                sender.sendMessage("§6§lVOTE §r§7You have not voted yet. Do /vote list to show all candidates.")
            }
            return true
        } else if (args.size > 1) {
            return false
        }
        if (args[0].lowercase() == "list") {
            var s = "§6§lVOTE §r§7Current mayoral candidates:"
            for (name in Election.candidates) {
                s += "\n §r§8- §r§e$name"
            }
            sender.sendMessage(s)
            return true
        }

        val vote = args[0]
        if (!Election.isCandidate(vote)) {
            sender.sendMessage("§4§lVOTE §r§7Unknown mayoral candidate: §c§l${vote}§r§7.")
            return true
        }

        val res = Election.vote(sender, vote)
        if (!res) {
            sender.sendMessage("§4§lVOTE §r§7Unable to vote for §c§l${vote}§r§7. Please report this to an admin.")
            return true
        }

        sender.sendMessage("§6§lVOTE §r§7You have voted for §e§l${vote}§r§7. Thank you for your vote!")
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        if (!Election.enabled) return emptyList()

        val l = Election.candidates.toMutableList()
        l.add("list")
        return when (args.size) {
            0 -> l
            1 -> {
                val arg = args[0].lowercase()
                matchArgument(arg, l)
            }
            else -> emptyList()
        }
    }
}