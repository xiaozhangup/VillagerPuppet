package me.xiaozhangup.puppet.utils

import me.xiaozhangup.puppet.VillagerPuppet
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PMessage.error
import me.xiaozhangup.puppet.utils.PUtils.toRawString
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object PEntity {


    //---------------------------
    //Place, Break a puppet
    //---------------------------
    fun Player.tryPlacePuppet(puppet: Puppet, block: Block) {
        val loc = block.location.clone().add(0.5, 1.0, 0.5)

        //防止重复放置代码
        VillagerPuppet.finder.getNearestEntity(loc)?.let {
            if (it.id.startsWith("puppet-") && it.getLocation().distance(loc) < 3) {
                this.error("这个位置附近已经有一个人偶了!")
                return
            }
        }

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