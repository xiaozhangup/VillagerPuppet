package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.xiaozhangup.puppet.VillagerPuppet
import me.xiaozhangup.puppet.events.PuppetInteract.asPuppet
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PEntity.tryPlacePuppet
import org.bukkit.NamespacedKey
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import taboolib.common.platform.event.SubscribeEvent
import java.util.*
import kotlin.collections.HashMap

object PuppetInteract {

    @SubscribeEvent
    fun place(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.blockFace != BlockFace.UP) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        val itemInMainHand = e.player.inventory.itemInMainHand

        if (itemInMainHand.getMetaString("type").isNotEmpty()) {
            val block = e.clickedBlock
            if (block == null || block.isPassable) return
            e.player.tryPlacePuppet(itemInMainHand.asPuppet(e.player), block)
            e.isCancelled = true
            itemInMainHand.amount -= 1
        }
    }

    @SubscribeEvent
    fun inter(e: AdyeshachEntityInteractEvent) {
        if (e.isMainHand) {
            if (e.entity.id.startsWith("puppet-")) {
                val id = e.entity.id.replaceFirst("puppet-", "")
                id.asPuppet(e.entity.world)?.let {  puppet: Puppet ->
                    if (puppet.owner == e.player.uniqueId) {
                        // TODO: OPEN Menu
                    }
                }
            }
        }
    }

    fun ItemStack.asPuppet(player: Player): Puppet {
        val pdc = this.itemMeta!!.persistentDataContainer
        val type = PuppetType.valueOf(pdc.getOrDefault(NamespacedKey(VillagerPuppet.plugin, "type"), PersistentDataType.STRING, "MINER"))
        return Puppet (
            UUID.randomUUID(),
            player.uniqueId,
            "",
            type,
            HashMap(),
            this.getMetaInt("level", 1),
            mutableListOf(),
            this.getMetaString("chest"),
            this.getMetaString("leg"),
            this.getMetaString("boot"),
            this.getMetaString("offhand"),
        )
    }

    fun ItemStack.getMetaString(key: String): String {
        val pdc = this.itemMeta?.persistentDataContainer ?: return ""
        return pdc.getOrDefault(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.STRING, "")
    }

    fun ItemStack.getMetaInt(key: String, int: Int): Int {
        val pdc = this.itemMeta?.persistentDataContainer ?: return int
        return pdc.getOrDefault(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.INTEGER, int)
    }
}