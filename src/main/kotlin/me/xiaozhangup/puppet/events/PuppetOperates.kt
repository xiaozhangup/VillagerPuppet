package me.xiaozhangup.puppet.events

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

        e.player.placePuppet()
    }

    fun Player.placePuppet() {
        this.getTargetBlockExact(4)?.let { block ->
            if (block.isPassable) return

            val loc = block.location.clone().add(0.5, 1.0, 0.5)
            loc.yaw = this.faceTo()

            val armorStand = loc.world!!.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand

            armorStand.isSmall = true
            armorStand.setBasePlate(false)
            armorStand.setArms(true)
            armorStand.setEquipment(BukkitEquipment.HAND, ItemStack(Material.ACACIA_BOAT))
        }
    }

    fun Player.faceTo(): Float {
        if (this.location.yaw in -135.0..-45.0) return 90.0f
        if (this.location.yaw in -45.0..45.0) return 180.0f
        if (this.location.yaw in 45.0..135.0) return -90.0f
        if (this.location.yaw in -135.0..135.0) return 0.0f
        return 0.0f
    }

}