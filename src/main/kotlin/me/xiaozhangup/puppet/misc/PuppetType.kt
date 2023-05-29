package me.xiaozhangup.puppet.misc

import me.xiaozhangup.puppet.utils.PUtils.setMetaString
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem

enum class PuppetType(val cn: String, val skull: String, val hand: ItemStack, val doc: String, val book: String) {
    MINER(
        "矿工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI5Njc2ZWU2ZGE1YTM1MjFjYjQ3ODQ2Mzc3OWI4NzExZTMxODg3Y2Q5YTZkOWZkZWNmY2JjNTUwODNlNTUxNSJ9fX0=",
        buildItem(Material.IRON_PICKAXE),
        "生成并挖掘矿物",
        "在脚下半径为N的石头上生成矿物方块或挖掘矿物,类型随等级提高而增多 (N为人偶等级)."
    ),
    BREAKER( // TODO:
        "挖工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ3YjIyMjJlZDZmZTFkNDMxM2MzY2IzNDJiYTk2YTU1YTg5Yjc2ZTYyZDZiYTdhMTU4Y2QzZGU5NDNkZTNlZSJ9fX0=",
        buildItem(Material.GOLDEN_PICKAXE),
        "挖掘面前的方块",
        "挖掘面前N格的方块 (N为精灵等级)."
    ),
    FARMER( // TODO:
        "农夫",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAxZTAzNWEzZDhkNjEyNjA3MmJjYmU1MmE5NzkxM2FjZTkzNTUyYTk5OTk1YjVkNDA3MGQ2NzgzYTMxZTkwOSJ9fX0=",
        buildItem(Material.STONE_HOE),
        "种植和收割附近的作物",
        "种植和收割附近N格内的成熟作物 (N为人偶等级)."
    ),
    SHEPHERD(
        "羊毛工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=",
        buildItem(Material.SHEARS),
        "裁剪附近的绵羊羊毛",
        "裁剪附近N格内的绵羊羊毛 (N为人偶等级)."
    ),
    FISHER( // TODO:
        "渔夫",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzlhOWViZGQyYzFiZjJkMGE2MWMzYjk4YzBiYzQyNzc0NDRhMWI4ZmVkYmIxNmNjYTRmYWFjYTlmN2VjMDU5MiJ9fX0=",
        buildItem(Material.FISHING_ROD),
        "从附近的水源钓鱼",
        ""
    ),
    ICE_MAKER(
        "冰工",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJlNjlhNjVhOTVlZjNmZjRkMzc4YTBjODlkNzFhMmUyZDc5MDZiNDViYmU2NTQ4OGRiNmVhMTQzYzJjMDQzOCJ9fX0=",
        buildItem(Material.ICE),
        "冻结水源并收集冰块",
        "将脚下半径为2范围内的水变成各种冰块,并在全部变为冰块后挖掘冰块并收集 (种类和人偶等级相关)."
    )
    //可能有更多,目前就这几个足够
}

fun PuppetType.asItemStack(): ItemStack {
    val type = this
    val item = buildItem(Material.PLAYER_HEAD) {
        skullTexture = ItemBuilder.SkullTexture(type.skull)
        name = "&f${type.cn}人偶 &7(等级: 1)"
        lore += "&7右键放置到地上来使用"
        lore += "&7左键查看说明书"
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