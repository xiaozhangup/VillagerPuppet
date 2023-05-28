package me.xiaozhangup.puppet.loader

import ink.ptms.adyeshach.core.entity.EntityInstance
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.misc.Puppet
import me.xiaozhangup.puppet.utils.PEntity.dropAt
import me.xiaozhangup.puppet.utils.PMessage.info
import me.xiaozhangup.puppet.utils.PUtils.toBase64
import me.xiaozhangup.puppet.utils.PUtils.toItemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Stored
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem

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
    private val locked = buildItem(Material.WHITE_STAINED_GLASS_PANE) {
        name = "&f锁定的物品栏"
        colored()
    }
    private val echest = buildItem(Material.PLAYER_HEAD) {
        name = "&6容器槽 &7(也就是等级)"
        lore += "&7在这里放入&f运输矿车"
        lore += "&7人偶便会拥有更多空间"
        skullTexture =
            ItemBuilder.SkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5Yjk4ZjA0YzMyMjdkMzdkMzE5YmJjZmZjNTFmNTJlNzhkOTZhMDViMTI4NTJkMWI0NjRiYjc0MDhhNzgxMCJ9fX0=")
        colored()
    }
    private val item = buildItem(Material.PLAYER_HEAD) {
        name = "&f物品槽"
        lore += "&7在本菜单被开启时"
        lore += "&7人偶将停止手头的工作"
        lore += ""
        lore += "&e单击物品取出"
        skullTexture =
            ItemBuilder.SkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg5Yjk4ZjA0YzMyMjdkMzdkMzE5YmJjZmZjNTFmNTJlNzhkOTZhMDViMTI4NTJkMWI0NjRiYjc0MDhhNzgxMCJ9fX0=")
        colored()
    }
    private val moveable = listOf(10, 19, 28, 37, 49, 50, 51, 52)
    private val stores = listOf(12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34)

    fun Puppet.openControl(player: Player, entity: EntityInstance) {
        val puppet = this
        puppet.setData("opened", player.name)
        player.openMenu<Stored>(title = "${puppet.type.cn}人偶操作页 (等级 ${puppet.level})") {
            rows(6)

            handLocked(false)
            map(
                "xhxxxixxx",
                "x1x     x",
                "x2x     x",
                "x3x     x",
                "x4xxxxxxx",
                "xxxe%%%%x",
            )

            set('b', buildItem(Material.COMMAND_BLOCK_MINECART) {
                name = " "
            })
            set('x', background_b)
            set('h', body)
            set('i', item)
            set('e', echest)

            set('1', puppet.getChest())
            set('2', puppet.getLeg())
            set('3', puppet.getBoot())
            set('4', puppet.getOffHand())
            set(' ', locked)

            //填入矿车
            for (level in 2..puppet.level) {
                set(level + 47, ItemStack(Material.CHEST_MINECART))
            }

            //放入内部物品
            for ((i, item) in puppet.items.withIndex()) {
                set(stores[i], item.toItemStack()) {
                    if (puppet.isLive()) {
                        player.closeInventory()
                        player.info("物品已洒落到地上!")
                        puppet.dropAll()
                    }
                }
            }

            //填入可用物品栏
            for (slot in 0..(-1 + puppet.level * 3)) {
                set(stores[slot], ItemStack(Material.AIR))
            }

            onClick { event ->
                val slot = event.rawSlot
                if (slot in 0..53 && !moveable.contains(slot)) {
                    event.isCancelled = true
                }
            }

            onClose { event ->
                puppet.removeData("opened")
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

                //计算等级也就是矿车数
                var level = 1
                for (slot in 49..52) {
                    inv.getItem(slot)?.let { itemStack ->
                        if (itemStack.type == Material.CHEST_MINECART) {
                            level++
                        } else {
                            itemStack.dropAt(puppet.getLocation())
                        }
                    }
                }
                puppet.level = level
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