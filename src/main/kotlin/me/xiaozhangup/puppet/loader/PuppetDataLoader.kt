package me.xiaozhangup.puppet.loader

import me.xiaozhangup.puppet.misc.Puppet
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import java.io.File

object PuppetDataLoader {

    val puppets: HashMap<World, MutableList<Puppet>> = HashMap()

    @SubscribeEvent
    fun load(e: WorldLoadEvent) {
        submitAsync {
            e.world.init()
        }
    }

    @SubscribeEvent
    fun unload(e: WorldUnloadEvent) {
        submitAsync {
            e.world.saveAll()
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
            // TODO: LOAD
        }
    }

    private fun World.saveAll() {
        // TODO: SAVE
    }

}