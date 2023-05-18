package me.xiaozhangup.puppet.misc

import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.PCommand.setMetaInt
import me.xiaozhangup.puppet.PCommand.setMetaString
import me.xiaozhangup.puppet.VillagerPuppet.gson
import me.xiaozhangup.puppet.VillagerPuppet.manager
import me.xiaozhangup.puppet.VillagerPuppet.toBase64
import me.xiaozhangup.puppet.VillagerPuppet.toItemStack
import me.xiaozhangup.puppet.VillagerPuppet.toLocation
import me.xiaozhangup.puppet.loader.PuppetData.add
import me.xiaozhangup.puppet.loader.PuppetData.delete
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import java.util.*

data class Puppet(
    val uuid: UUID,
    var owner: UUID,
    var loc: String,
    val type: PuppetType,
    val data: HashMap<String, String>,
    var level: Int,
    var items: MutableList<String>,
    var chest: String,
    var leg: String,
    var boot: String,
    var offhand: String
) {
    //可能会有姿态的类型，但现在不是实现的时候
    fun getHead(): ItemStack {
        return buildItem(Material.PLAYER_HEAD) {
            skullTexture = ItemBuilder.SkullTexture(type.skull)
        }
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

    fun getOffHand(): ItemStack {
        if (offhand.isEmpty()) return ItemStack(Material.AIR)
        return offhand.toItemStack()
    }

    fun getHand(): ItemStack {
        return type.hand
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
            it.id = "puppet-" + this.uuid.toString()
            it.setSmall(true)
            it.setArms(true)
            it.setBasePlate(false)
            it.setEquipment(EquipmentSlot.HAND, this.getHand())
            it.setEquipment(EquipmentSlot.OFF_HAND, this.getOffHand())
            it.setEquipment(EquipmentSlot.HEAD, this.getHead())
            it.setEquipment(EquipmentSlot.CHEST, this.getChest())
            it.setEquipment(EquipmentSlot.LEGS, this.getLeg())
            it.setEquipment(EquipmentSlot.FEET, this.getBoot())
        }

        this.add()
    }

    fun despawn() { //让某个精灵消失
        manager.getEntityById("puppet-" + this.uuid.toString()).forEach { it.remove() }
    }

    fun asItemStack(): ItemStack {
        val item = buildItem(getHead()) {
            name = "&f${type.cn}人偶"
            lore += "&7右键放置到地上"
            lore += "&7来使其工作"
            lore += ""
            lore += "&e等级: &f$level"
            colored()
        }
        item.setMetaString("type", type.toString())
        item.setMetaString("chest", chest)
        item.setMetaString("leg", leg)
        item.setMetaString("boot", boot)
        item.setMetaString("offhand", offhand)
        item.setMetaInt("level", level)
        return item
    }

    fun remove() { //打掉
        this.despawn()
        this.delete()
    }

    fun asJson(): String {
        return gson.toJson(this)
    }

}
