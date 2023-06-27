package me.xiaozhangup.puppet.events.event

import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.block.Block
import taboolib.platform.type.BukkitProxyEvent

class PuppetBreakEvent(
    val puppet: Puppet,
    val block: Block
) : BukkitProxyEvent()
