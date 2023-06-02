package me.xiaozhangup.puppet

import com.google.gson.Gson
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.entity.manager.ManagerType
import me.xiaozhangup.puppet.loader.PuppetData
import me.xiaozhangup.puppet.loader.PuppetData.savePuppets
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.slimecargo.events.prot.control.ActionType
import me.xiaozhangup.slimecargo.events.prot.control.PermCheck
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ConcurrentHashMap


object VillagerPuppet : Plugin() {

    val plugin: BukkitPlugin by lazy { BukkitPlugin.getInstance() }
    val gson: Gson by lazy { Gson() }
    val manager by lazy { Adyeshach.api().getPublicEntityManager(ManagerType.TEMPORARY) }
    val finder by lazy { Adyeshach.api().getEntityFinder() }
    val lobby by lazy { Bukkit.getServer().name == "lobby" }

    val puppets: ConcurrentHashMap<World, MutableList<Puppet>> = ConcurrentHashMap()

    override fun onEnable() {
        PuppetData.initAll()
    }

    override fun onDisable() {
        Bukkit.getWorlds().forEach { world ->
            world.savePuppets()
        }
    }

    fun Player.hasPerm(location: Location): Boolean {
        return if (lobby) this.isOp
        else !PermCheck.quick(this, location, ActionType.OPEN)
    }

}