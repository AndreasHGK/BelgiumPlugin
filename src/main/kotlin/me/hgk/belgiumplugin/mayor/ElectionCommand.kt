package me.hgk.belgiumplugin.mayor

import me.hgk.belgiumplugin.utils.matchArgument
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ElectionCommand : Command(
    "election",
    "view info regarding the election",
    "/election [subcommand]",
    listOf()
) {
    init {
        permission = "belgium.election"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false

        // Election managing stuff
        if (sender.hasPermission("belgium.election.admin") && args.isNotEmpty()) {
            return when (val subcmd = args[0]) {
                "enable", "disable" -> {
                    val e = subcmd == "enable"
                    Election.enabled = e

                    sender.sendMessage("§6§lELECTION §r§7Is now ${
                        if (e) "§a§lenabled"
                        else "§c§ldisabled"
                    }§r§7.")
                    true
                }
                "register" -> {
                    if (args.size < 2) {
                        sender.sendMessage("§4§lELECTION §r§7Please enter who you want to sign up for the elections.")
                        return true
                    }
                    val name = args[1]
                    Election.candidates.add(name)
                    sender.sendMessage("§6§lELECTION §r§7You have signed up §e§l$name §r§7for the elections.")
                    true
                }
                "unregister" -> {
                    if (args.size < 2) {
                        sender.sendMessage("§4§lELECTION §r§7Please enter who you want to sign up for the elections.")
                        return true
                    }

                    val name = args[1]
                    if (!Election.candidates.contains(name)) {
                        sender.sendMessage("§4§lELECTION §r§7This person is not signed up for the elections.")
                        return true
                    }
                    Election.candidates.remove(name)
                    sender.sendMessage("§6§lELECTION §r§7You have removed §e§l$name §r§7from the elections.")
                    true
                }
                "result" -> {
                    var s = "§6§lELECTION §r§7Vote leaderboard:"

                    val m = mutableMapOf<String, Int>()
                    for (vote in Election.votes) {
                        if (!Election.isCandidate(vote.component2())) continue

                        val name = vote.component2().lowercase()
                        if (!m.containsKey(name)) m[name] = 0
                        m[name] = m[name]!! + 1
                    }

                    var first = true
                    for (can in m.toList().sortedBy { (_, v) -> - v }) {
                        s += "\n §r§8- §e${can.first}§r§7: §f${can.second}"
                        if (first) {
                            s += " §6§l✦"
                            first = false
                        }
                    }
                    sender.sendMessage(s)
                    true
                }
                else -> {
                    sender.sendMessage("§4§lELECTION §r§7Unknown subcommand.")
                    true
                }
            }
        }

        sender.sendMessage("§6§lELECTION §r§7Election management command.")
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        val subcommands = mutableListOf<String>()

        if (sender.hasPermission("belgium.election.admin")) {
            subcommands.add("enable")
            subcommands.add("disable")
            subcommands.add("result")
            subcommands.add("register")
            subcommands.add("unregister")
        }
        return when (args.size) {
            0 -> subcommands
            1 -> {
                val arg = args[0].lowercase()
                matchArgument(arg, subcommands)
            }
            2 -> {
                when (args[0]) {
                    "unregister" -> {
                        val arg = args[1].lowercase()
                        matchArgument(arg, Election.candidates)
                    }
                    else -> emptyList()
                }
            }
            else -> emptyList()
        }
    }
}