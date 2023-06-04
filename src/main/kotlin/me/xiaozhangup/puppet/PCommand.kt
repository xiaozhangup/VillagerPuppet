package me.xiaozhangup.puppet

import me.xiaozhangup.puppet.events.PuppetInteract.limit
import me.xiaozhangup.puppet.loader.PuppetData.puppets
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.misc.asItemStack
import me.xiaozhangup.puppet.utils.PMessage.info
import me.xiaozhangup.puppet.utils.PMessage.minimessage
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.common.platform.function.submitAsync
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
                submitAsync {
                    val puppets = sender.world.puppets()
                    val pi = puppets?.size ?: 0

                    sender.sendMessage(minimessage.deserialize("<newline><newline><newline><newline>"))
                    sender.sendMessage(minimessage.deserialize(" <color:#e9d5c9><b>当前世界内人偶数量统计:</b></color><newline>"))
                    if (pi > 0) {
                        for (type in PuppetType.values()) {
                            val num = puppets?.filter { it.type == type }?.size ?: 0
                            if (num > 0) {
                                sender.sendMessage(minimessage.deserialize("   <white>${type.cn}类型</white> <gray>*$num</gray>"))
                            }
                        }
                    } else {
                        sender.sendMessage(minimessage.deserialize("   <gray>没有放置过任何人偶</gray>"))
                    }

                    sender.sendMessage(minimessage.deserialize("<newline>   <color:#e9d5c9>人偶数量信息概括</color>"))
                    sender.sendMessage(minimessage.deserialize("   <gray>总计 $pi 个人偶已加载,最大数量为 $limit 个</gray><newline><newline>"))
                }
            }
        }
    }

}