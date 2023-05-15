package me.xiaozhangup.puppet.misc

import me.xiaozhangup.puppet.VillagerPuppet.toBase64
import me.xiaozhangup.puppet.VillagerPuppet.toItemStack
import me.xiaozhangup.puppet.VillagerPuppet.toLocation
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.UUID

data class Puppet(
    val uuid: UUID,
    val owner: UUID,
    val loc: String,
    val type: PuppetType,
    val data: HashMap<String, String>,
    var level: Int,
    var items: MutableList<String>,
    var head: String,
    var chest: String,
    var leg: String,
    var boot: String,
    var hand: String,
    var name: String
) {
    //可能会有姿态的类型，但现在不是实现的时候
    fun getHead(): ItemStack {
        return head.toItemStack()
    }
    fun getChest(): ItemStack {
        return chest.toItemStack()
    }
    fun getLeg(): ItemStack {
        return leg.toItemStack()
    }
    fun getBoot(): ItemStack {
        return boot.toItemStack()
    }

    fun getHand(): ItemStack {
        return hand.toItemStack()
    }

    fun getItemsStore(): List<ItemStack> {
        return items.map { it.toItemStack() }.asReversed()
    }

    fun addItem(itemStack: ItemStack) {
        items.add(itemStack.toBase64())
    }

    fun removeItem(itemStack: ItemStack): Boolean {
        return items.remove(itemStack.toBase64())
    }

    fun setData(key: String, value: String) {
        data[key] = value
    }

    fun removeData(key: String) {
        data.remove(key)
    }

    fun getData(key: String): String? {
        return data[key]
    }

    fun getLocation(): Location {
        return loc.toLocation()
    }

}
