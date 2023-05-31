package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.Container
import org.bukkit.block.data.Ageable
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Breaker {

    private val tool: ItemStack = ItemStack(Material.NETHERITE_PICKAXE)

    //Still have bug!!!
    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.BREAKER) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return

            val level = puppet.level
            val loc = puppet.getLocation()

            val blocks = mutableListOf<Block>()

            when (e.entity.pitch.toInt()) {
                0 -> {
                    for (i in 1..level) {
                        val b = loc.clone().add(0.0, 0.0, i.toDouble()).block
                        blocks.add(b)
                    }
                }
                -90 -> {
                    for (i in 1..level) {
                        val b = loc.clone().add(i.toDouble(), 0.0, 0.0).block
                        blocks.add(b)
                    }
                }
                180, -180 -> {
                    for (i in -level..-1) {
                        val b = loc.clone().add(0.0, 0.0, i.toDouble()).block
                        blocks.add(b)
                    }
                }
                90 -> {
                    for (i in -level..-1) {
                        val b = loc.clone().add(i.toDouble(), 0.0, 0.0).block
                        blocks.add(b)
                    }
                }
            }

            for (block in blocks.filter { it.state !is Container }) {
                    if (!puppet.addItem(block.getDrops(tool).toList())) {
                        puppet.display("&c人偶背包已满 :(".applyColor())
                        puppet.setData("full", "true")
                        break
                    } else {
                        puppet.display("")
                        block.type = Material.AIR
                    }
            }
        }
    }
}