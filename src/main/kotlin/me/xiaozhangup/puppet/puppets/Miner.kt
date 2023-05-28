package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Miner {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MINER) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return
            val stones = puppet.getUnderBlocks(puppet.level, -1.0, Material.STONE)
            if (stones.isNotEmpty()) {
                stones.random().type = Material.COAL_ORE
            } else {
                val ores =
                    puppet.getUnderBlocks(puppet.level, -1.0).stream().filter { it.type.toString().endsWith("_ORE") }
                        .toList()
                if (ores.isEmpty()) {
                    puppet.display("&c范围内没有石头或矿物!".applyColor())
                    return
                }
                ores.random().type = Material.STONE
                if (!puppet.addItem(ItemStack(Material.STONE, 64))) {
                    puppet.display("&c人偶背包已满!".applyColor())
                }
            }
        }
    }

}