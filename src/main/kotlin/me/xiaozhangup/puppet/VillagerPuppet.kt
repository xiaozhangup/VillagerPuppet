package me.xiaozhangup.puppet

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object VillagerPuppet : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
    }
}