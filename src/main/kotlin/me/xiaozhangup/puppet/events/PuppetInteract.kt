package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.xiaozhangup.puppet.VillagerPuppet.hasPerm
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.loader.PuppetMenu.openControl
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.tryPlacePuppet
import me.xiaozhangup.puppet.utils.PUtils.asPuppet
import me.xiaozhangup.puppet.utils.PUtils.getMetaString
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent

object PuppetInteract {

    @SubscribeEvent
    fun place(e: PlayerInteractEvent) {
        val item = e.item
        if (item != null && item.getMetaString("type").isNotEmpty()) {
            e.isCancelled = true

            if (e.hand != EquipmentSlot.HAND) return
            if (e.blockFace != BlockFace.UP) return
            if (e.action != Action.RIGHT_CLICK_BLOCK) return

            val block = e.clickedBlock
            if (block == null || block.isPassable) return
            e.player.tryPlacePuppet(item.asPuppet(e.player), block)
            item.amount -= 1
        }
    }

    @SubscribeEvent
    fun inter(e: AdyeshachEntityInteractEvent) {
        if (e.isMainHand) {
            if (e.entity.id.startsWith("puppet-")) {
                val id = e.entity.id.replaceFirst("puppet-", "")
                id.asPuppet(e.entity.world)?.let { puppet: Puppet ->
                    val player = e.player
                    if (puppet.owner == player.name || player.hasPerm(puppet.getLocation())) {
                        puppet.openControl(player, e.entity)
                        player.playSound(player.location, Sound.BLOCK_ANVIL_BREAK, 1f, 1f)
                    }
                }
            }
        }
    }

}