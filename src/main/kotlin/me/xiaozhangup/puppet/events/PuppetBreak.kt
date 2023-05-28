package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityDamageEvent
import me.xiaozhangup.puppet.VillagerPuppet.hasPerm
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.dropAt
import taboolib.common.platform.event.SubscribeEvent

object PuppetBreak {

    @SubscribeEvent
    fun inter(e: AdyeshachEntityDamageEvent) {
        if (e.entity.id.startsWith("puppet-")) {
            val id = e.entity.id.replaceFirst("puppet-", "")
            id.asPuppet(e.entity.world)?.let { puppet: Puppet ->
                if (puppet.owner == e.player.name || e.player.hasPerm(puppet.getLocation())) {
                    if (!puppet.getData("opened").isNullOrEmpty()) return
                    // TODO: 被打开无法拆除的提示
                    puppet.asItemStack().dropAt(puppet.getLocation())
                    puppet.remove()
                }
            }
        }

    }

}