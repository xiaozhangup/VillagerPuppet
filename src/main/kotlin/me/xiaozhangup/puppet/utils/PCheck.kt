package me.xiaozhangup.puppet.utils

import me.xiaozhangup.puppet.misc.Puppet
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent

object PCheck {

    fun Player.canBreak(location: Location): Boolean {
        val e = BlockBreakEvent(location.block, this)
        Bukkit.getPluginManager().callEvent(e)
        return !e.isCancelled
    }

    fun Puppet.getUnderBlocks(range: Int, offsetY: Double, material: Material): List<Block> {
        val loc = this.getLocation().add(0.0, offsetY, 0.0)

        val blocks = mutableListOf<Block>()
        for (offsetX in -range..range) {
            for (offsetZ in -range..range) {
                val l = loc.clone().add(offsetX.toDouble(), 0.0, offsetZ.toDouble())
                val b = l.block
                if (b.type == material) {
                    blocks.add(b)
                }
            }
        }

        return blocks
    }

    fun Puppet.getUnderBlocks(range: Int, offsetY: Double): List<Block> {
        val loc = this.getLocation().add(0.0, offsetY, 0.0)

        val blocks = mutableListOf<Block>()
        for (offsetX in -range..range) {
            for (offsetZ in -range..range) {
                val l = loc.clone().add(offsetX.toDouble(), 0.0, offsetZ.toDouble())
                blocks.add(l.block)
            }
        }

        return blocks
    }

}