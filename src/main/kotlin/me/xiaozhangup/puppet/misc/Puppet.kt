package me.xiaozhangup.puppet.misc

import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.VillagerPuppet.gson
import me.xiaozhangup.puppet.VillagerPuppet.manager
import me.xiaozhangup.puppet.VillagerPuppet.toBase64
import me.xiaozhangup.puppet.VillagerPuppet.toItemStack
import me.xiaozhangup.puppet.VillagerPuppet.toLocation
import me.xiaozhangup.puppet.loader.PuppetDataLoader.add
import me.xiaozhangup.puppet.loader.PuppetDataLoader.delete
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
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
        if (head.isEmpty()) return ItemStack(Material.AIR)
        return head.toItemStack()
    }
    fun getChest(): ItemStack {
        if (chest.isEmpty()) return ItemStack(Material.AIR)
        return chest.toItemStack()
    }
    fun getLeg(): ItemStack {
        if (leg.isEmpty()) return ItemStack(Material.AIR)
        return leg.toItemStack()
    }
    fun getBoot(): ItemStack {
        if (boot.isEmpty()) return ItemStack(Material.AIR)
        return boot.toItemStack()
    }

    fun getHand(): ItemStack {
        if (hand.isEmpty()) return ItemStack(Material.AIR)
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

    fun spawn() {
        val spawn = this.getLocation()

        manager.create(EntityTypes.ARMOR_STAND, spawn) {
            it as AdyArmorStand
            it.id = this.uuid.toString()
            it.setCustomName(this.name) // TODO: 使用多行来显示多内容
            it.setCustomNameVisible(true)
            it.setSmall(true)
            it.setArms(true)
            it.setBasePlate(false)
            it.setEquipment(EquipmentSlot.HAND, this.getHand())
            it.setEquipment(EquipmentSlot.HEAD, this.getHead())
            it.setEquipment(EquipmentSlot.CHEST, this.getChest())
            it.setEquipment(EquipmentSlot.LEGS, this.getLeg())
            it.setEquipment(EquipmentSlot.FEET, this.getBoot())
        } as AdyArmorStand

        this.add()
    }

    fun despawn() { //这个更类似于打掉某个精灵，通常不必调用
        manager.getEntityById(this.uuid.toString()).forEach { it.remove() }
        this.delete()
    }

    fun create() { //注意！此方法仅仅在创建一个新的精灵时调用，如果要生成，请直接用 spawn()
        // TODO: 创建对应类型的对象
        this.spawn()
    }

    fun asJson(): String {
        return gson.toJson(this)
    }

}
