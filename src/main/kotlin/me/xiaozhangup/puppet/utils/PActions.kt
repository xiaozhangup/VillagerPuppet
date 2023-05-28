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

    val normal = EulerAngle(0.0, 0.0, 10.0)

    @Awake(LifeCycle.ENABLE)
    fun action() {
        submitAsync(period = 8) {
            val e = EulerAngle(24 * sin(System.currentTimeMillis() / 300.0) - 16, 0.0, 10.0)
            for (list in puppets.values) {
                for (puppet in list) {
                    val entity =
                        manager.getEntityById("puppet-" + puppet.uuid.toString()).firstOrNull() as AdyArmorStand
                    if (entity.hasViewer()) {
                        if (puppet.getData("full").isNullOrEmpty()) {
                            entity.setRotation(BukkitRotation.RIGHT_ARM, e)
                        } else {
                            entity.setRotation(BukkitRotation.RIGHT_ARM, normal)
                        }
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
                            if (puppet.getData("full").isNullOrEmpty()) {
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

}