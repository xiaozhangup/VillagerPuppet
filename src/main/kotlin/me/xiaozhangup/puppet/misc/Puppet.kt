package me.xiaozhangup.puppet.misc

import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.entity.type.AdyArmorStand
import me.xiaozhangup.puppet.VillagerPuppet.gson
import me.xiaozhangup.puppet.VillagerPuppet.manager
import me.xiaozhangup.puppet.VillagerPuppet.puppets
import me.xiaozhangup.puppet.events.event.PuppetWorkEvent
import me.xiaozhangup.puppet.loader.PuppetData.add
import me.xiaozhangup.puppet.loader.PuppetData.delete
import me.xiaozhangup.puppet.utils.PEntity.dropAt
import me.xiaozhangup.puppet.utils.PUtils.setMetaInt
import me.xiaozhangup.puppet.utils.PUtils.setMetaString
import me.xiaozhangup.puppet.utils.PUtils.toBase64
import me.xiaozhangup.puppet.utils.PUtils.toItemStack
import me.xiaozhangup.puppet.utils.PUtils.toLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import java.util.*

data class Puppet(
    val uuid: UUID,
    var owner: String,
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

//    fun getItemsStore(): List<ItemStack> {
//        return items.map { it.toItemStack() }.asReversed()
//    }
//
//    fun addItem(itemStack: ItemStack) {
//        items.add(itemStack.toBase64())
//    }
//
//    fun removeItem(itemStack: ItemStack): Boolean {
//        return items.remove(itemStack.toBase64())
//    }

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

    fun display(string: String) {
        manager.getEntityById("puppet-" + this.uuid.toString()).forEach {
            if (it.getCustomName() == string) return
            it.setCustomName(string)
            it.setCustomNameVisible(string.isNotEmpty())
        }
    }

    fun despawn() { //让某个人偶消失
        manager.getEntityById("puppet-" + this.uuid.toString()).forEach { it.remove() }
    }

    fun asItemStack(): ItemStack {
        val item = buildItem(Material.PLAYER_HEAD) {
            skullTexture = ItemBuilder.SkullTexture(type.skull)
            name = "&f${type.cn}人偶 &7(等级: $level)"
            lore += "&7右键放置到地上来使用"
            lore += "&7左键查看说明书"
            lore += ""
            lore += "&x&d&c&c&4&4&c类型: &f${type.cn}"
            lore += "&x&d&c&c&4&4&c等级: &f$level"
            lore += "&x&d&c&c&4&4&c描述:"
            lore += "&7${type.doc}"
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

    //Inventory
    fun getInventorySlots(): Int {
        return level * 3
    }

    fun getInventory(): Inventory {
        val inventory = Bukkit.createInventory(null, InventoryType.CHEST)
        for (i in items) {
            inventory.addItem(i.toItemStack())
        }

        return inventory
    }

    fun setInventory(inventory: Inventory) {
        items.clear()
        for (s in 0 until getInventorySlots()) {
            inventory.getItem(s)?.let { items.add(it.toBase64()) }
        }
    }

    fun addItem(itemStack: ItemStack): Boolean {
        if (!getData("full").isNullOrEmpty()) return false //如果满了直接返回

        val inv = getInventory()
        inv.addItem(itemStack)

        if (inv.getItem(getInventorySlots()) != null) {
            return false
        }
        setInventory(inv)
        return true
        //添加成功时，返回true，失败则返回false
    }

    fun addItem(itemStack: List<ItemStack>): Boolean {
        if (!getData("full").isNullOrEmpty()) return false //如果满了直接返回

        val inv = getInventory()
        for (item in itemStack) {
            inv.addItem(item)
        }

        if (inv.getItem(getInventorySlots()) != null) {
            return false
        }
        setInventory(inv)
        return true
        //添加成功时，返回true，失败则返回false
    }

    fun dropAll() {
        val loc = getLocation()
        items.forEach { it.toItemStack().dropAt(loc) }
        items.clear()
        removeData("full")
        display("")
    }

    fun isLive(): Boolean {
        val list = puppets[getLocation().world]
        if (list != null) {
            return list.contains(this)
        }
        return false
    }

    fun workNow() {
        val entity = manager.getEntityById("puppet-$uuid").firstOrNull() as AdyArmorStand
        PuppetWorkEvent(this, type, entity).apply {
            call()
        }
    }

}
