package me.xiaozhangup.puppet.puppets

import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.utils.PCheck.getUnderBlocks
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import kotlin.random.Random

object Miner {

    private val ores = arrayOf(
        Pair(Material.COAL_ORE, 10),
        Pair(Material.IRON_ORE, 5),
        Pair(Material.GOLD_ORE, 2),
        Pair(Material.REDSTONE_ORE, 5),
        Pair(Material.LAPIS_ORE, 3),
        Pair(Material.DIAMOND_ORE, 1),
        Pair(Material.EMERALD_ORE, 1)
    )
    private val pickaxe = ItemStack(Material.DIAMOND_PICKAXE)

    @SubscribeEvent
    fun e(e: PuppetWorkEvent) {
        if (e.type == PuppetType.MINER) {
            val puppet = e.puppet
            if (!puppet.getData("opened").isNullOrEmpty()) return
            val blocks = puppet.getUnderBlocks(puppet.level, -1.0)
            val stones = blocks.stream().filter { it.type == Material.STONE }
                .toList()
            if (stones.isNotEmpty()) {
                stones.random().type = getRandomOre(puppet.level)
            } else {
                val ores =
                    blocks.stream().filter { it.type.toString().endsWith("_ORE") }
                        .toList()
                if (ores.isEmpty()) {
                    puppet.display("&c范围内没有石头或矿物 :(".applyColor())
                    return
                } else {
                    val block = ores.random()
                    for (drop in block.getDrops(pickaxe)) {
                        if (!puppet.addItem(drop)) {
                            puppet.display("&c人偶背包已满 :(".applyColor())
                            puppet.setData("full", "true")
                            break
                        } else {
                            puppet.display("")
                            block.type = Material.STONE
                        }
                    }
                }
            }
        }
    }

    private fun getRandomOre(level: Int): Material {

        val ableores = ores.take(ores.size - (5 - level))

        var totalWeight = 0
        for (ore in ableores) {
            totalWeight += ore.second
        }
        var randomWeight = Random.nextInt(totalWeight)
        for (ore in ableores) {
            randomWeight -= ore.second
            if (randomWeight < 0) {
                return ore.first
            }
        }
        return Material.STONE // fallback option
    }

}