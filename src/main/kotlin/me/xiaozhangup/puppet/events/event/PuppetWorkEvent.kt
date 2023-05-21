package me.xiaozhangup.puppet.events.event

import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import taboolib.platform.type.BukkitProxyEvent

class PuppetWorkEvent(
    val puppet: Puppet,
    val type: PuppetType,
    val entity: AdyArmorStand
) : BukkitProxyEvent()
