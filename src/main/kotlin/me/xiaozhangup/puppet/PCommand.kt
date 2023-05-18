package me.xiaozhangup.puppet

import me.xiaozhangup.puppet.PCommand.setMetaString
import me.xiaozhangup.puppet.events.PuppetInteract.getMetaInt
import me.xiaozhangup.puppet.misc.PuppetType
import me.xiaozhangup.puppet.misc.asItemStack
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem

object PCommand {

    @Awake(LifeCycle.ENABLE)
    fun reg() {
        command("puppet", permissionDefault = PermissionDefault.TRUE) {
            execute<Player> {sender, _, _ ->
                sender.giveItem(PuppetType.BREAKER.asItemStack())
            }
        }
    }

    fun ItemStack.setMetaString(key: String, value: String) {
        val pdc = this.itemMeta
        pdc!!.persistentDataContainer.set(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.STRING, value)

        this.itemMeta = pdc
    }

    fun ItemStack.setMetaInt(key: String, value: Int) {
        val pdc = this.itemMeta
        pdc!!.persistentDataContainer.set(NamespacedKey(VillagerPuppet.plugin, key), PersistentDataType.INTEGER, value)

        this.itemMeta = pdc
    }

}