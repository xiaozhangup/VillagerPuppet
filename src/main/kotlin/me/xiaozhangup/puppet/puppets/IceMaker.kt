package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object IceMaker {

    val ice = listOf(Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE)

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.ICE_MAKER) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return
            val blocks = puppet.getUnderBlocks(2, -1.0)

            val waters = blocks.stream().filter { it.type == Material.WATER }
                .toList()
            if (waters.isNotEmpty()) {
                waters.random().type = getRandomIce(puppet.level)
            } else {
                val ices = blocks.stream().filter { it.type.toString().endsWith("_ICE") || it.type == Material.ICE }
                    .toList()
                if (ices.isNotEmpty()) {
                    val block = ices.random()
                    if (!puppet.addItem(ItemStack(block.type))) {
                        puppet.display("&c人偶背包已满 :(".applyColor())
                        puppet.setData("full", "true")
                    } else {
                        puppet.display("")
                    }
                    block.type = Material.WATER
                } else {
                    puppet.display("&c范围内没有水或冰 :(".applyColor())
                    return
                }
            }
        }
    }

    private fun getRandomIce(level: Int): Material {
        return ice.take(level).random()
    }

}