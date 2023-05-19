package me.xiaozhangup.puppet.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent

object PCheck {

    fun Player.canBreak(location: Location): Boolean {
        val e = BlockBreakEvent(location.block, this)
        Bukkit.getPluginManager().callEvent(e)
        return !e.isCancelled
    }

}