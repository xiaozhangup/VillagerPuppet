package me.xiaozhangup.puppet.loader

import me.xiaozhangup.puppet.VillagerPuppet.puppets
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PUtils.asPuppet
import me.xiaozhangup.puppet.utils.PUtils.putElement
import me.xiaozhangup.puppet.utils.PUtils.removeElement
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import java.io.File

object PuppetData {

    @SubscribeEvent
    fun load(e: WorldLoadEvent) {
        submitAsync {
            e.world.setup()
        }
    }

    @SubscribeEvent
    fun unload(e: WorldUnloadEvent) {
        submitAsync {
            puppets[e.world]?.forEach { it.despawn() }
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

    //对缓存的操作系列

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

    fun Puppet.update() {
        val w = this.getLocation().world
        val puppet = this
        w?.let { ww ->
            puppets[ww]?.removeIf { it.uuid == puppet.uuid }
            puppets.putElement(ww, puppet)
        }
    }

    //获取等操作

    fun getPuppet(id: String, world: World): Puppet? {
        return puppets[world]?.firstOrNull { it.uuid.toString() == id }
    }

    fun World.getPuppet(id: String): Puppet? {
        return puppets[this]?.firstOrNull { it.uuid.toString() == id }
    }

    fun String.asPuppet(world: World): Puppet? {
        return puppets[world]?.firstOrNull { it.uuid.toString() == this }
    }
}
