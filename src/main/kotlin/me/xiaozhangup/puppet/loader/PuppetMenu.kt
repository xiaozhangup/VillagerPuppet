package me.xiaozhangup.puppet.loader

import ink.ptms.adyeshach.core.entity.EntityInstance
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PUtils.toBase64
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submitAsync
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Stored
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import java.util.*

object PuppetMenu {

    val air = ItemStack(Material.AIR)
    private val background = buildItem(Material.GRAY_STAINED_GLASS_PANE) {
        name = " "
    }
    private val background_b = buildItem(Material.BLACK_STAINED_GLASS_PANE) {
        name = " "
    }
    private val body = buildItem(Material.ARMOR_STAND) {
        name = "&f身体自定义"
        lore += "&7下方4格依次为:"
        lore += "&7胸甲"
        lore += "&7裤子"
        lore += "&7靴子"
        lore += "&7副手"
        lore += ""
        lore += "&e直接放入物品即可"
        colored()
    }
    private val item = buildItem(Material.PLAYER_HEAD) {
        name = "&f物品槽"
        lore += "&e单击物品取出"
        skullTexture = ItemBuilder.SkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5Yjk4ZjA0YzMyMjdkMzdkMzE5YmJjZmZjNTFmNTJlNzhkOTZhMDViMTI4NTJkMWI0NjRiYjc0MDhhNzgxMCJ9fX0=")
        colored()
    }
    private val moveable = listOf(10, 19, 28, 37)

    fun Puppet.openControl(player: Player, entity: EntityInstance) {
        val puppet = this
        player.openMenu<Stored>(title = "${puppet.type.cn}精灵操作页") {
            rows(6)

            handLocked(false)
            map(
                "xhxxxixxx",
                "x1x     x",
                "x2x     x",
                "x3x     x",
                "x4xxxxxxx",
                "xxxbbbbbx",
            )

            set('b', buildItem(Material.COMMAND_BLOCK_MINECART) {
                name = " "
            })
            set('x', background_b)
            set('h', body)
            set('i', item)

            set('1', puppet.getChest())
            set('2', puppet.getLeg())
            set('3', puppet.getBoot())
            set('4', puppet.getOffHand())

            onClick { event ->
                val slot = event.rawSlot
                if (slot in 0..53 && !moveable.contains(slot)) {
                    event.isCancelled = true
                }
            }

            onClose { event ->
                val inv = event.inventory

                entity as AdyArmorStand
                inv.getItem(10).let { im ->
                    puppet.chest = im.asNoNull().toBase64()
                    entity.setChestplate(im.asNoNull())
                }
                inv.getItem(19).let { im ->
                    puppet.leg = im.asNoNull().toBase64()
                    entity.setLeggings(im.asNoNull())
                }
                inv.getItem(28).let { im ->
                    puppet.boot = im.asNoNull().toBase64()
                    entity.setBoots(im.asNoNull())
                }
                inv.getItem(37).let { im ->
                    puppet.offhand = im.asNoNull().toBase64()
                    entity.setItemInOffHand(im.asNoNull())
                }
            }
        }
    }

    private fun ItemStack?.asNoNull(): ItemStack {
        if (this == null) {
            return air
        }
        return this
    }

}