package com.github.yukkimoru.sdtm.world

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class FactoryTool(private val plugin: JavaPlugin) {

	data class OreData(
		val displayName: String,
		val miningSpeed: Double,
		val dropRate: Double,
		val magicFind: Double
	)

	private val EarlyStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0, 0.01),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0, 0.01),
	)

	private val MidStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0, 0.02),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0, 0.02),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 1.5, 0.02),
		Material.RED_STAINED_GLASS to OreData("ルビー鉱石", 0.5, 1.0, 0.02),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー鉱石", 0.7, 0.5, 0.02),
	)

	private val EndStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0, 0.03),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0, 0.03),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 1.5, 0.03),
		Material.RED_STAINED_GLASS to OreData("ルビー鉱石", 0.5, 1.0, 0.03),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー鉱石", 0.7, 0.5, 0.03),
	)


	fun createUnbreakableTool(
		material: Material,
		name: String,
		lore: List<String>,
		rarity: String,
		destroyableBlocks: String
	): ItemStack {
		val factoryItem = FactoryItem(plugin)
		val itemStack = factoryItem.createItemStack(material, 1, name, lore, rarity)
		val meta: ItemMeta = itemStack.itemMeta
		meta.isUnbreakable = true
		val container = meta.persistentDataContainer
		val key = NamespacedKey(plugin, "destroyable_blocks")
		container.set(key, PersistentDataType.STRING, destroyableBlocks)
		itemStack.itemMeta = meta
		return itemStack
	}

	fun createEarlyStagePickaxe(): ItemStack {
		val destroyableBlocks = "minecraft:coal_ore:1.0:1.0"
		return createUnbreakableTool(
			Material.IRON_PICKAXE,
			"序盤のピッケル",
			listOf("破壊可能なブロック:", "§a⛏1.0 ☘1.0 石炭鉱石"),
			"common",
			destroyableBlocks
		)
	}

	fun createMidStagePickaxe(): ItemStack {
		val destroyableBlocks = "minecraft:coal_ore:1.0:1.0,minecraft:iron_ore:1.0:1.0"
		return createUnbreakableTool(
			Material.DIAMOND_PICKAXE,
			"中盤のピッケル",
			listOf("破壊可能なブロック:", "§a⛏1.0 ☘1.0 石炭鉱石", "§a⛏1.0 ☘1.0 鉄鉱石"),
			"rare",
			destroyableBlocks
		)
	}

	fun createLateStagePickaxe(): ItemStack {
		val destroyableBlocks =
			"minecraft:coal_ore:1.0:1.0,minecraft:iron_ore:1.0:1.0,minecraft:deepslate_iron_ore:0.8:1.5,minecraft:red_stained_glass:0.5:1.0,minecraft:red_stained_glass_pane:0.7:0.5"
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"終盤のピッケル",
			listOf(
				"破壊可能なブロック:",
				"§a⛏1.0 ☘1.0 石炭鉱石",
				"§a⛏1.0 ☘1.0 鉄鉱石",
				"§a⛏0.8 ☘1.5 深層鉄鉱石",
				"§a⛏0.5(0.7) ☘1.0(0.5) ルビー鉱石"
			),
			"legendary",
			destroyableBlocks
		)
	}
}