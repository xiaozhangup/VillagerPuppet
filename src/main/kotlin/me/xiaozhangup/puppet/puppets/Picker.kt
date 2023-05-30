package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.entity.Item
import taboolib.common.platform.event.SubscribeEvent

object Picker {

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.PICKER) {
            val puppet = e.puppet
            val level = puppet.level.toDouble()
            val entity = puppet.getLocation().getNearbyEntities(level, 1.0, level).stream().filter { it is Item }
                .toList()

            if (entity.isNotEmpty()) {
                for (item in entity) {
                    item as Item
                    if (!item.canMobPickup() || !item.canPlayerPickup()) continue
                    if (!puppet.addItem(item.itemStack)) {
                        puppet.display("&c人偶背包已满 :(".applyColor())
                        puppet.setData("full", "true")
                        break
                    } else {
                        puppet.display("")
                        item.remove()
                    }
                }
            }
        }
    }

}