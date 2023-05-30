package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object Fisher {

    private val fish = listOf(Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH)

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.FISHER) {
            val puppet = e.puppet
            val waters = puppet.getUnderBlocks(3, -1.0, Material.WATER)

            if (waters.size > 6) {
                if (!puppet.addItem(getRandomFish())) {
                    puppet.display("&c人偶背包已满 :(".applyColor())
                    puppet.setData("full", "true")
                } else {
                    puppet.display("")
                    waters.random().location.playSplashParticle()
                }
            } else {
                puppet.display("&c范围内没有足够的水 :(".applyColor())
            }
        }
    }

    private fun getRandomFish(): ItemStack {
        return ItemStack(fish.random())
    }

    private fun Location.playSplashParticle() {
        // 播放水花渐起的粒子效果
        this.world.spawnParticle(Particle.WATER_SPLASH, this, 100, 0.5, 0.5, 0.5, 0.1)
    }

}