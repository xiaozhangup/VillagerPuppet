package me.xiaozhangup.puppet.loader

import me.xiaozhangup.puppet.VillagerPuppet.asPuppet
import me.xiaozhangup.puppet.VillagerPuppet.toRawString
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.misc.PuppetType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.setEquipment
import taboolib.type.BukkitEquipment
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object PuppetDataLoader {

    private val puppets: HashMap<World, MutableList<Puppet>> = HashMap()

    @SubscribeEvent
    fun load(e: WorldLoadEvent) {
        submitAsync {
            e.world.init()
        }
    }

    @SubscribeEvent
    fun unload(e: WorldUnloadEvent) {
        submitAsync {
            e.world.savePuppets()
        }
    }

    fun World.puppets(): MutableList<Puppet>? {
        return puppets[this]
    }

    fun initAll() {
        submitAsync {
            for (world in Bukkit.getWorlds()) {
                world.init()
            }
        }
    }

    private fun World.init() {
        val world = this
        val dataPath = File(world.worldFolder.path + "/puppets")
        if (!dataPath.exists() || !dataPath.isDirectory) {
            dataPath.mkdirs()
        } else {
            val ps = mutableListOf<Puppet>()
            dataPath.listFiles()?.forEach { file ->
                ps += file.readText().asPuppet()
            }
            puppets[world] = ps
            ps.forEach { it.spawn() }
        }
    }

    private fun World.savePuppets() {
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
        puppets[this.getLocation().world]?.let {
            it += this
        }
    }

    fun Puppet.delete() {
        puppets[this.getLocation().world]?.removeIf { uuid == this.uuid }
    }

    private fun Player.faceTo(): Float {
        if (this.location.yaw in -135.0..-45.0) return 90.0f
        if (this.location.yaw in -45.0..45.0) return 180.0f
        if (this.location.yaw in 45.0..135.0) return -90.0f
        if (this.location.yaw in -135.0..135.0) return 0.0f
        return 0.0f
    }

}