package me.xiaozhangup.puppet.utils

import me.xiaozhangup.puppet.VillagerPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray
import java.util.*

object PUtils {

    fun ItemStack.toBase64(): String {
        return Base64.getEncoder().encodeToString(serializeToByteArray())
    }

    fun String.toItemStack(): ItemStack {
        return Base64.getDecoder().decode(this).deserializeToItemStack()
    }

    fun Location.toRawString(): String {
        val loc = this
        return loc.world!!.name + ":" + loc.x + ":" + loc.y + ":" + loc.z + ":" + loc.yaw + ":" + loc.pitch
    }

    fun String.toLocation(): Location {
        val locData = this.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val world: World? = Bukkit.getWorld(locData[0])
        val x = locData[1].toDouble()
        val y = locData[2].toDouble()
        val z = locData[3].toDouble()
        val yaw = locData[4].toFloat()
        val pitch = locData[5].toFloat()
        return Location(world, x, y, z, yaw, pitch)
    }

    fun String.asPuppet(): Puppet {
        return VillagerPuppet.gson.fromJson(this, Puppet::class.java)
    }

    fun <K, T> MutableMap<K, MutableList<T>>.putElement(key: K, element: T) {
        val value = this[key]
        if (value != null) {
            value += element
        } else this[key] = arrayListOf(element)
    }

    fun <K, T> MutableMap<K, MutableList<T>>.removeElement(key: K, element: T) {
        val value = this[key]
        if (value != null) {
            value -= element
        }
    }

    fun ItemStack.asPuppet(player: Player): Puppet {
        val pdc = this.itemMeta!!.persistentDataContainer
        val type = PuppetType.valueOf(pdc.getOrDefault(NamespacedKey(VillagerPuppet.plugin, "type"), PersistentDataType.STRING, "MINER"))
        return Puppet (
            UUID.randomUUID(),
            player.name,
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

    fun ItemStack.setMetaString(key: String, value: String) {
        val pdc = this.itemMeta
        pdc!!.persistentDataContainer.set(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.STRING, value)

        this.itemMeta = pdc
    }

    fun ItemStack.setMetaInt(key: String, value: Int) {
        val pdc = this.itemMeta
        pdc!!.persistentDataContainer.set(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.INTEGER, value)

        this.itemMeta = pdc
    }
}