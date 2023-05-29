package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import taboolib.common.platform.event.SubscribeEvent

object Example {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.BREAKER) {
            //这只是一个例子，用来快速开搓的【
        }
    }

}