package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.xiaozhangup.puppet.loader.PuppetDataLoader.asPuppet
import me.xiaozhangup.puppet.loader.PuppetDataLoader.tryPlacePuppet
import me.xiaozhangup.puppet.misc.Puppet
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Sheep
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.setEquipment
import taboolib.type.BukkitEquipment

object PuppetOperates {

    @SubscribeEvent
    fun place(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.blockFace != BlockFace.UP) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return

        e.player.tryPlacePuppet(null)
    }

    @SubscribeEvent
    fun inter(e: AdyeshachEntityInteractEvent) {
        if (e.isMainHand) {
            if (e.entity.id.startsWith("puppet-")) {
                val id = e.entity.id.replaceFirst("puppet-", "")
                id.asPuppet(e.entity.world)?.let {  puppet: Puppet ->
                    puppet.remove()
                }
            }
        }
    }

}