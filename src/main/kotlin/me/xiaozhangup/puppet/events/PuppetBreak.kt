package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityDamageEvent
import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.dropAt
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.giveItem

object PuppetBreak {

    @SubscribeEvent
    fun inter(e: AdyeshachEntityDamageEvent) {
        if (e.entity.id.startsWith("puppet-")) {
            val id = e.entity.id.replaceFirst("puppet-", "")
            id.asPuppet(e.entity.world)?.let { puppet: Puppet ->
                if (puppet.owner == e.player.uniqueId) {
                    puppet.asItemStack().dropAt(puppet.getLocation())
                    puppet.remove()
                }
            }
        }

    }

}