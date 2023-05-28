package me.xiaozhangup.puppet.utils

import ink.ptms.adyeshach.core.bukkit.BukkitRotation
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.VillagerPuppet.manager
import me.xiaozhangup.puppet.VillagerPuppet.puppets
import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import org.bukkit.util.EulerAngle
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import kotlin.math.sin
import kotlin.random.Random

object PActions {

    @Awake(LifeCycle.ENABLE)
    fun action() {
        submitAsync(period = 8) {
            val e = EulerAngle(24 * sin(System.currentTimeMillis() / 300.0) - 16, 0.0, 0.0)
            for (list in puppets.values) {
                for (puppet in list) {
                    val entity =
                        manager.getEntityById("puppet-" + puppet.uuid.toString()).firstOrNull() as AdyArmorStand
                    if (entity.hasViewer()) {
                        entity.setRotation(BukkitRotation.RIGHT_ARM, e)
                    }
                }
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun event() {
        submitAsync(period = 60) {
            for (list in puppets.values) {
                for (puppet in list) {
                    val entity =
                        manager.getEntityById("puppet-" + puppet.uuid.toString()).firstOrNull() as AdyArmorStand

                    //多tick处理
                    submit(delay = Random.nextInt(1, 30).toLong()) {
                        if (entity.getLocation().chunk.isLoaded) {
                            PuppetWorkEvent(puppet, puppet.type, entity).apply {
                                call()
                            }
                        }
                    }
                }
            }
        }
    }

}