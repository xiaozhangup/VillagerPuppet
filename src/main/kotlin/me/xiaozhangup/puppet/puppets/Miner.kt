package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import taboolib.common.platform.event.SubscribeEvent

object Miner {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MINER) {
            e.entity.setCustomName(System.currentTimeMillis().toString())
            if (!e.entity.isCustomNameVisible()) {
                e.entity.setCustomNameVisible(true)
            }
        }
    }

}