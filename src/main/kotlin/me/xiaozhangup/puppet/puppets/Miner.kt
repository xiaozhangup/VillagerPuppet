package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import taboolib.common.platform.event.SubscribeEvent

object Miner {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MINER) {
            if (e.entity.getCustomName().isNotEmpty()) {
                e.puppet.display("")
            } else {
                e.puppet.display("&c你好，我现在正在工作".applyColor())
            }
        }
    }

}