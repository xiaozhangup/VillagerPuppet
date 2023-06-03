package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import taboolib.common.platform.event.SubscribeEvent

object Hunter {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.HUNTER) {
            val puppet = e.puppet
            val level = puppet.level.toDouble()
            val entity = puppet.getLocation().getNearbyEntities(level, 1.0, level).filter { it is Monster }

            if (entity.isNotEmpty()) {
                val monster = entity.random() as LivingEntity
                monster.damage(level * 4)
            }
        }
    }

}