package me.xiaozhangup.puppet.utils

import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PUtils.toRawString
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object PEntity {


    //---------------------------
    //Place, Break a puppet
    //---------------------------
    fun Player.tryPlacePuppet(puppet: Puppet, loc: Location) {

        loc.yaw = this.faceTo()

        puppet.loc = loc.toRawString()
        puppet.spawn()
    }

    fun ItemStack.dropAt(location: Location) {
        location.world?.dropItem(location, this)
    }

    private fun Player.faceTo(): Float {
        if (this.location.yaw in -135.0..-45.0) return 90.0f
        if (this.location.yaw in -45.0..45.0) return 180.0f
        if (this.location.yaw in 45.0..135.0) return -90.0f
        if (this.location.yaw in -135.0..135.0) return 0.0f
        return 0.0f
    }

}