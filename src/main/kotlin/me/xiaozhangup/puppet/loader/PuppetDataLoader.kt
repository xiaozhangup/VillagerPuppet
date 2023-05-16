package me.xiaozhangup.puppet.loader

import me.xiaozhangup.puppet.VillagerPuppet.asPuppet
import me.xiaozhangup.puppet.VillagerPuppet.putElement
import me.xiaozhangup.puppet.VillagerPuppet.removeElement
import me.xiaozhangup.puppet.VillagerPuppet.toRawString
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

object PuppetDataLoader {

    private val puppets: ConcurrentHashMap<World, MutableList<Puppet>> = ConcurrentHashMap()

    @SubscribeEvent
    fun load(e: WorldLoadEvent) {
        submitAsync {
            e.world.setup()
        }
    }

    @SubscribeEvent
    fun unload(e: WorldUnloadEvent) {
        submitAsync {
            e.world.savePuppets()
        }
    }

    fun initAll() {
        for (world in Bukkit.getWorlds()) {
            world.setup()
        }
    }

    fun World.puppets(): MutableList<Puppet>? {
        return puppets[this]
    }

    private fun World.setup() {
        val world = this
        val dataPath = File(world.worldFolder.path + "/puppets")
        if (!dataPath.exists() || !dataPath.isDirectory) {
            dataPath.mkdirs()
        } else {
            dataPath.listFiles()?.let { files ->
                for (file in files) {
                    try {
                        file.readText().asPuppet().spawn()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun World.savePuppets() {
        val world = this
        val ps = puppets[world]

        val dataPath = File(world.worldFolder.path + "/puppets")
        if (!dataPath.exists() || !dataPath.isDirectory) {
            dataPath.mkdirs()
        } else {
            dataPath.listFiles()?.forEach { file ->
                file.delete()
            }
            ps?.forEach { puppet ->
                val pc = File(dataPath, puppet.uuid.toString())
                pc.createNewFile()
                pc.writeText(puppet.asJson())
            }
        }

        puppets.remove(world)
    }

    //---------------------------
    //Place, Break a puppet
    //---------------------------
    fun Player.tryPlacePuppet(itemStack: ItemStack?) {
        this.getTargetBlockExact(4)?.let { block ->
            if (block.isPassable) return

            val loc = block.location.clone().add(0.5, 1.0, 0.5)
            loc.yaw = this.faceTo()

            // TODO: 基于物品解析
            val puppet = Puppet(
                UUID.randomUUID(),
                this.uniqueId,
                loc.toRawString(),
                PuppetType.MINER,
                HashMap(),
                0,
                mutableListOf(),
                "",
                "",
                "",
                "",
                "",
                "${this.name} 的精灵",
            )
            puppet.create()
        }
    }

    fun Puppet.add() {
        val w = this.getLocation().world
        w?.let { ww ->
            puppets.putElement(ww, this)
        }
    }

    fun Puppet.delete() {
        val w = this.getLocation().world
        w?.let { ww ->
            puppets.removeElement(ww, this)
        }
    }

    private fun Player.faceTo(): Float {
        if (this.location.yaw in -135.0..-45.0) return 90.0f
        if (this.location.yaw in -45.0..45.0) return 180.0f
        if (this.location.yaw in 45.0..135.0) return -90.0f
        if (this.location.yaw in -135.0..135.0) return 0.0f
        return 0.0f
    }

}