package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.block.data.Ageable
import taboolib.common.platform.event.SubscribeEvent

object Farmer {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.FARMER) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return

            val level = puppet.level
            val blocks = puppet.getUnderBlocks(level, 0.0).filter { it.blockData is Ageable }

            if (blocks.isEmpty()) {
                puppet.display("&c范围内没有作物 :(".applyColor())
            } else {
                for (block in blocks) {
                    val age = block.blockData as Ageable
                    if (age.maximumAge == age.age) {
                        for (drop in block.drops) {
                            if (!puppet.addItem(drop)) {
                                puppet.display("&c人偶背包已满 :(".applyColor())
                                puppet.setData("full", "true")
                                break
                            } else {
                                puppet.display("")
                                age.age = 0
                                block.blockData = age
                            }
                        }
                    }
                }
            }
        }
    }
}