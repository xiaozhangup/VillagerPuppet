package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityDamageEvent
import me.xiaozhangup.puppet.VillagerPuppet.hasPerm
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.dropAt
import me.xiaozhangup.puppet.utils.PMessage.error
import taboolib.common.platform.event.SubscribeEvent

object PuppetBreak {

    @SubscribeEvent
    fun inter(e: AdyeshachEntityDamageEvent) {
        if (e.entity.id.startsWith("puppet-")) {
            val id = e.entity.id.replaceFirst("puppet-", "")
            id.asPuppet(e.entity.world)?.let { puppet: Puppet ->
                if (puppet.owner == e.player.name || e.player.hasPerm(puppet.getLocation())) {
                    val data = puppet.getData("opened")
                    if (!data.isNullOrEmpty()) {
                        e.player.error("这个人偶正在被 $data 操作,无法拆除!")
                        return
                    }
                    puppet.asItemStack().dropAt(puppet.getLocation())
                    puppet.remove()
                }
            }
        }

    }

}