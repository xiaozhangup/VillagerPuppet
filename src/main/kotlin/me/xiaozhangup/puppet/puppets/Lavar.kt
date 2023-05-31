package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Lavar {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.LAVA) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return
            val blocks = puppet.getUnderBlocks(2, -1.0)

            val lavas = blocks.filter { it.type == Material.LAVA }
            if (lavas.isNotEmpty()) {
                lavas.random().type = Material.OBSIDIAN
            } else {
                val obsidian = blocks.filter { it.type == Material.OBSIDIAN }
                if (obsidian.isNotEmpty()) {
                    val block = obsidian.random()
                    if (!puppet.addItem(ItemStack(Material.OBSIDIAN))) {
                        puppet.display("&c人偶背包已满 :(".applyColor())
                        puppet.setData("full", "true")
                    } else {
                        puppet.display("")
                        block.type = Material.AIR
                    }
                } else {
                    puppet.display("&c范围内没有岩浆或黑耀石 :(".applyColor())
                    return
                }
            }
        }
    }

}