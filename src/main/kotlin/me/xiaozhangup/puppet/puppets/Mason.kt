package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Mason {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MASON) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return
            val blocks = puppet.getUnderBlocks(2, -1.0, Material.AIR)

            if (blocks.isEmpty()) {
                if (!puppet.addItem(ItemStack(Material.COBBLESTONE))) {
                    puppet.display("&c人偶背包已满 :(".applyColor())
                    puppet.setData("full", "true")
                } else {
                    puppet.display("")
                }
            } else {
                blocks.random().type = Material.COBBLESTONE
            }
        }
    }
}