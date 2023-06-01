package me.xiaozhangup.puppet.puppets

import ink.ptms.adyeshach.module.editor.meta.impl.MetaItem
import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import taboolib.common.platform.event.SubscribeEvent

object Fletcher {

    private val tulip = listOf(Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP)

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.FLETCHER) {
            val puppet = e.puppet
            val air = puppet.getUnderBlocks(puppet.level, 0.0, Material.AIR)
            val grass = puppet.getUnderBlocks(puppet.level, -1.0, Material.GRASS_BLOCK)

            if (grass.isEmpty() || air.isEmpty()) {
                puppet.display("&c可用位置放满了郁金香 :(".applyColor())
            } else {
                val block = air.random()
                if (block.location.add(0.0, -1.0, 0.0).block.type == Material.GRASS_BLOCK) {
                    block.setType(getRandomTulip(), false)
                }
                puppet.display("")
            }
        }
    }

    private fun getRandomTulip(): Material {
        return tulip.random()
    }

}