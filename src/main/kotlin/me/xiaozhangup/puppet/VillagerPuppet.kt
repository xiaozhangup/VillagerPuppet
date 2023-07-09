package me.xiaozhangup.puppet

import com.google.gson.Gson
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.entity.manager.ManagerType
import me.xiaozhangup.puppet.loader.PuppetData
import me.xiaozhangup.puppet.loader.PuppetData.savePuppets
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.slimecargo.listeners.protect.utils.ActionType
import me.xiaozhangup.slimecargo.listeners.protect.utils.CheckUtils.hasPermission
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ConcurrentHashMap


object VillagerPuppet : Plugin() {

    val plugin: BukkitPlugin by lazy { BukkitPlugin.getInstance() }
    val gson: Gson by lazy { Gson() }
    val manager by lazy { Adyeshach.api().getPublicEntityManager(ManagerType.TEMPORARY) }
    val finder by lazy { Adyeshach.api().getEntityFinder() }

    @Config
    lateinit var config: Configuration
        private set

    val slimecargo by lazy { config.getBoolean("hook.slimecargo") }

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
        return if (slimecargo) !hasPermission(location, ActionType.OPEN)
        else return this.isOp
    }

}