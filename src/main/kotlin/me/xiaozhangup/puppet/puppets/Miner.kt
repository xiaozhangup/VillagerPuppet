package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.asList
import kotlin.random.Random

object Miner {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MINER) {
            val puppet = e.puppet
            val stones = puppet.getUnderBlocks(puppet.level, -1.0, Material.STONE)
            if (stones.isNotEmpty()) {
                stones.random().type = Material.COAL_ORE
            } else {
                val ores = puppet.getUnderBlocks(puppet.level, -1.0).stream().filter { it.type.toString().endsWith("_ORE") }.toList()
                if (ores.isEmpty()) {
                    puppet.display("&c范围内没有石头或矿物!".applyColor())
                    return
                }
                ores.random().type = Material.STONE
            }
        }
    }

}