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
	)

	private val earlyStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0),
	)

	private val midStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 1.5),
		Material.RED_STAINED_GLASS to OreData("ルビー鉱石", 0.5, 1.0),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー鉱石", 0.7, 0.5),
	)

	private val endStagePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 1.5),
		Material.RED_STAINED_GLASS to OreData("ルビー鉱石", 0.5, 1.0),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー鉱石", 0.7, 0.5),
	)

	val allBreakableMaterials: List<Material> =
		(earlyStagePickaxe.keys + midStagePickaxe.keys + endStagePickaxe.keys).toSet().toList()

	private fun createUnbreakableTool(
		material: Material,
		name: String,
		lore: List<String>,
		rarity: String,
		destroyableBlocks: String,
		customModelData: Int? = null
	): ItemStack {
		val factoryItem = FactoryItem(plugin)
		val itemStack = factoryItem.createItemStack(material, 1, name, lore, rarity, customModelData)
		val meta: ItemMeta = itemStack.itemMeta
		meta.isUnbreakable = true
		val container = meta.persistentDataContainer
		val key = NamespacedKey(plugin, "destroyable_blocks")
		container.set(key, PersistentDataType.STRING, destroyableBlocks)
		itemStack.itemMeta = meta
		return itemStack
	}

	fun createEarlyStagePickaxe(): ItemStack {
		val destroyableBlocks = earlyStagePickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = earlyStagePickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"序盤のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"common",
			destroyableBlocks,
			200
		)
	}

	fun createMidStagePickaxe(): ItemStack {
		val destroyableBlocks = midStagePickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = midStagePickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"中盤のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"rare",
			destroyableBlocks,
			201
		)
	}

	fun createLateStagePickaxe(): ItemStack {
		val destroyableBlocks = endStagePickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = endStagePickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"終盤のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"legendary",
			destroyableBlocks,
			202
		)
	}
}