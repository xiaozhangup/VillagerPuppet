package me.xiaozhangup.puppet.utils

import ink.ptms.adyeshach.core.bukkit.BukkitRotation
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.VillagerPuppet.manager
import me.xiaozhangup.puppet.VillagerPuppet.puppets
import org.bukkit.util.EulerAngle
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import kotlin.math.sin

object PActions {

    @Awake(LifeCycle.ENABLE)
    fun a() {
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

}