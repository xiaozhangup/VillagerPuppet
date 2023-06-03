package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Container
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
            val blocks = mutableListOf<Block>()

            //位置判断和方块采集
            val location = puppet.getLocation()
            for (i in 1..level) {
                val block = location.clone().add(location.direction.multiply(i)).block
                if (block.type != Material.AIR) {
                    blocks.add(block)
                }
            }
            //采集结束

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