package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Sheep
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import kotlin.random.Random

object Shepherd {

    val dyes: HashMap<DyeColor, Material> = HashMap()

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.SHEPHERD) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return

            val level = puppet.level.toDouble()
            val sheep = puppet.getLocation().getNearbyEntities(level, 1.0, level).filter { it.type == EntityType.SHEEP }

            if (sheep.isEmpty()) {
                puppet.display("&c范围内没有羊 :(".applyColor())
                return
            }

            for (entity in sheep) {
                if (entity is Sheep) {
                    if (!entity.isSheared) {
                        entity.color?.let {
                            val wool = ItemStack(dyes[entity.color]!!, Random.nextInt(3))
                            if (!puppet.addItem(wool)) {
                                puppet.display("&c人偶背包已满 :(".applyColor())
                                puppet.setData("full", "true")
                            } else {
                                puppet.display("")
                                entity.isSheared = true
                            }
                        }
                        break
                    }
                }
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun cacheDye() {
        for (dyeColor in DyeColor.values()) {
            dyes[dyeColor] = Material.valueOf("${dyeColor}_WOOL")
        }
    }

}