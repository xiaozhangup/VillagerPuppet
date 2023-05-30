package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Creeper
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Hunter {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.HUNTER) {
            val puppet = e.puppet
            val level = puppet.level.toDouble()
            val entity = puppet.getLocation().getNearbyEntities(level, 1.0, level).stream().filter { it is Monster }
                .toList()

            if (entity.isNotEmpty()) {
                val monster = entity.random() as LivingEntity
                monster.damage(level * 4)
            }
        }
    }

}