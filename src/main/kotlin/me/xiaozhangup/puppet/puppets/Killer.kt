package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.entity.Animals
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import taboolib.common.platform.event.SubscribeEvent

object Killer {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.KILLER) {
            val puppet = e.puppet
            val level = puppet.level.toDouble()
            val entity =
                puppet.getLocation().getNearbyEntities(level, 1.0, level).filter { it is Monster || it is Animals }

            if (entity.isNotEmpty()) {
                for (mob in entity.take(16)) {
                    mob as LivingEntity
                    mob.damage(level * 2)
                }
            }
        }
    }

}