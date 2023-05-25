package me.xiaozhangup.puppet

import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.misc.asItemStack
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.platform.util.giveItem

object PCommand {

    @Awake(LifeCycle.ENABLE)
    fun reg() {
        command("puppet", permissionDefault = PermissionDefault.OP) {
            dynamic(optional = true) {
                suggestion<Player> { player, _ ->
                    PuppetType.values().map { it.toString() }
                }
                execute<Player> { player, _, argument ->
                    player.giveItem(PuppetType.valueOf(argument).asItemStack())
                }
            }
        }
    }

}