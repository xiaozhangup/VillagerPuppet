package me.xiaozhangup.puppet.utils

import net.minecraft.server.v1_9_R1.Block
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

object PCheck {

    fun Player.canBreak(location: Location): Boolean {
        val e = BlockBreakEvent(location.block, this)
        Bukkit.getPluginManager().callEvent(e)
        return !e.isCancelled
    }

}