package me.xiaozhangup.puppet.utils

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

object PMessage {
    val minimessage = MiniMessage.miniMessage()
    val prefix = "<dark_gray>[<color:#e9d5c9>人偶</color>]</dark_gray>"

    fun Player.info(string: String) {
        this.sendMessage(minimessage.deserialize("$prefix <color:#f7efde>$string</color>"))
    }

    fun Player.error(string: String) {
        this.sendMessage(minimessage.deserialize("$prefix <color:#ad5d5d>$string</color>"))
    }
}