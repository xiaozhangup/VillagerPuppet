package me.xiaozhangup.puppet

import me.xiaozhangup.puppet.loader.PuppetData.puppets
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.misc.asItemStack
import me.xiaozhangup.puppet.utils.PMessage.error
import me.xiaozhangup.puppet.utils.PMessage.info
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
                suggestion<Player> { _, _ ->
                    PuppetType.values().map { it.toString() }
                }
                execute<Player> { player, _, argument ->
                    val type = PuppetType.valueOf(argument)
                    player.giveItem(type.asItemStack())
                    player.info("你得到了一个 ${type.cn}人偶!")
                }
            }
        }

        command("puppetall", permissionDefault = PermissionDefault.OP) {
            execute<Player> { sender, _, _ ->
                for (type in PuppetType.values()) {
                    sender.giveItem(type.asItemStack())
                }
            }
        }

        command("puppetinfo", permissionDefault = PermissionDefault.TRUE) {
            execute<Player> { sender, _, _ ->
                val puppets = sender.world.puppets()
                sender.error("这个世界目前放置了 ${puppets?.size ?: 0} 个人偶,详细如下:")
                for (type in PuppetType.values()) {
                    sender.info("- ${type.cn}: 总计 ${puppets?.filter { it.type == type }?.size ?: 0} 个")
                }
            }
        }
    }

}