package me.xiaozhangup.puppet

import me.xiaozhangup.puppet.loader.PuppetDataLoader
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray
import java.util.*


object VillagerPuppet : Plugin() {

    val plugin: BukkitPlugin by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        PuppetDataLoader.initAll()
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

}