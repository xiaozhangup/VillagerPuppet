package me.xiaozhangup.puppet.misc

import me.xiaozhangup.puppet.utils.PUtils.setMetaString
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem

enum class PuppetType(val cn: String, val skull: String, val hand: ItemStack, val doc: String) {
    MINER(
        "矿工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI5Njc2ZWU2ZGE1YTM1MjFjYjQ3ODQ2Mzc3OWI4NzExZTMxODg3Y2Q5YTZkOWZkZWNmY2JjNTUwODNlNTUxNSJ9fX0=",
        buildItem(Material.IRON_PICKAXE),
        "生成并挖掘矿物"
    ),
    BREAKER(
        "挖工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ3YjIyMjJlZDZmZTFkNDMxM2MzY2IzNDJiYTk2YTU1YTg5Yjc2ZTYyZDZiYTdhMTU4Y2QzZGU5NDNkZTNlZSJ9fX0=",
        buildItem(Material.GOLDEN_PICKAXE),
        "挖掘面前的方块"
    ),
    FARMER(
        "农夫",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxZTAzNWEzZDhkNjEyNjA3MmJjYmU1MmE5NzkxM2FjZTkzNTUyYTk5OTk1YjVkNDA3MGQ2NzgzYTMxZTkwOSJ9fX0=",
        buildItem(Material.STONE_HOE),
        "种植和收割附近(5*5)的作物"
    ),
    FISHER(
        "渔夫",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzlhOWViZGQyYzFiZjJkMGE2MWMzYjk4YzBiYzQyNzc0NDRhMWI4ZmVkYmIxNmNjYTRmYWFjYTlmN2VjMDU5MiJ9fX0=",
        buildItem(Material.FISHING_ROD),
        "从附近的水源钓鱼"
    ),
    //可能有更多,目前就这几个足够
}

fun PuppetType.asItemStack(): ItemStack {
    val type = this
    val item = buildItem(Material.PLAYER_HEAD) {
        skullTexture = ItemBuilder.SkullTexture(type.skull)
        name = "&f${type.cn}人偶"
        lore += "&7右键放置到地上来使用"
        lore += ""
        lore += "&x&d&c&c&4&4&c类型: &f${type.cn}"
        lore += "&x&d&c&c&4&4&c等级: &f1"
        lore += "&x&d&c&c&4&4&c描述:"
        lore += "&7${type.doc}"
        colored()
    }
    item.setMetaString("type", type.toString())
    return item
}