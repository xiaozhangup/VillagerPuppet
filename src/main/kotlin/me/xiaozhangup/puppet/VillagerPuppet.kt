package me.xiaozhangup.puppet

import com.google.gson.Gson
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.entity.manager.ManagerType
import me.xiaozhangup.puppet.loader.PuppetData
import me.xiaozhangup.puppet.loader.PuppetData.savePuppets
import me.xiaozhangup.puppet.misc.Puppet
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray
import java.util.*
import java.util.concurrent.ConcurrentHashMap


object VillagerPuppet : Plugin() {

    val plugin: BukkitPlugin by lazy { BukkitPlugin.getInstance() }
    val gson: Gson by lazy { Gson() }
    val manager by lazy { Adyeshach.api().getPublicEntityManager(ManagerType.TEMPORARY) }
    val finder by lazy { Adyeshach.api().getEntityFinder() }

    val puppets: ConcurrentHashMap<World, MutableList<Puppet>> = ConcurrentHashMap()

    override fun onEnable() {
        PuppetData.initAll()
    }

    override fun onDisable() {
        Bukkit.getWorlds().forEach { world ->
            world.savePuppets()
        }
    }

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
        return gson.fromJson(this, Puppet::class.java)
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


}