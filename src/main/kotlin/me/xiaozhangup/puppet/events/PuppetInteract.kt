package me.xiaozhangup.puppet.events

import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.xiaozhangup.puppet.VillagerPuppet
import me.xiaozhangup.puppet.VillagerPuppet.hasPerm
import me.xiaozhangup.puppet.loader.PuppetData.asPuppet
import me.xiaozhangup.puppet.loader.PuppetData.puppets
import me.xiaozhangup.puppet.loader.PuppetMenu.openControl
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.tryPlacePuppet
import me.xiaozhangup.puppet.utils.PMessage.error
import me.xiaozhangup.puppet.utils.PUtils.applyColor
import me.xiaozhangup.puppet.utils.PUtils.asPuppet
import me.xiaozhangup.puppet.utils.PUtils.getMetaString
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.buildBook

object PuppetInteract {

    private const val limit = 32

    @SubscribeEvent
    fun place(e: BlockPlaceEvent) {
        val item = e.itemInHand
        if (!e.player.hasPerm(e.player.location)) return
        if (item.getMetaString("type").isNotEmpty()) {
            e.isCancelled = true
            val puppet = item.asPuppet(e.player)

            if (e.hand != EquipmentSlot.HAND) return

            val block = e.blockPlaced

            val loc = block.location.clone().add(0.5, 0.0, 0.5)
            //防止重复放置代码
            val nearestEntity = VillagerPuppet.finder.getNearestEntity(loc)
            if (nearestEntity != null) {
                if (nearestEntity.id.startsWith("puppet-") && nearestEntity.getLocation().distance(loc) < 3) {
                    e.player.error("这个位置附近已经有一个人偶了!")
                    return
                }
            }
            place(e, puppet, loc, item)
        }

        @SubscribeEvent
        fun e(e: PlayerInteractEvent) {
            val item = e.item
            if (item != null && item.getMetaString("type").isNotEmpty()) {
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
                val puppet = item.asPuppet(e.player)
                e.isCancelled = true
                e.player.openBook(buildBook {
                    title = "人偶文档"
                    author = "VillagerPuppet"

                    write(
                        """&0&l${puppet.type.cn}的说明书
                        |&7基本教程
                        |
                        |&0${puppet.type.book}
                    """.trimMargin().applyColor()
                    )
                })
            }
        }

        @SubscribeEvent
        fun inter(e: AdyeshachEntityInteractEvent) {
            if (e.isMainHand) {
                if (e.entity.id.startsWith("puppet-")) {
                    val id = e.entity.id.replaceFirst("puppet-", "")
                    id.asPuppet(e.entity.world)?.let { puppet: Puppet ->
                        val player = e.player
                        if (puppet.owner == player.name || player.hasPerm(puppet.getLocation())) {
                            val data = puppet.getData("opened")
                            if (!data.isNullOrEmpty()) {
                                e.player.error("这个人偶正在被 $data 操作,无法交互!")
                                return
                            }
                            puppet.openControl(player, e.entity)
                            player.playSound(player.location, Sound.BLOCK_ANVIL_BREAK, 1f, 1f)
                        }
                    }
                }
            }
        }

    }

    private fun place(
        e: BlockPlaceEvent,
        puppet: Puppet,
        loc: Location,
        item: ItemStack
    ) {
        if ((e.player.world.puppets()?.size ?: 0) < limit) {
            e.player.tryPlacePuppet(puppet, loc)
            item.amount -= 1
        } else {
            e.player.error("这个世界已经放置了 $limit 给人偶了,不能再放置更多了!")
        }
    }
}