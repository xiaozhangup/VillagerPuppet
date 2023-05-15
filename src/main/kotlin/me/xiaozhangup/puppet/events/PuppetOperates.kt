package me.xiaozhangup.puppet.events

import me.xiaozhangup.puppet.loader.PuppetDataLoader.tryPlacePuppet
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

}