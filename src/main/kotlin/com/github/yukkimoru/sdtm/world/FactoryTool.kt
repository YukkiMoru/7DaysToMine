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

	private val tier1Pickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.0, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 0.9, 1.0),
	)

	private val tier2Pickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.2, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.7, 2.5),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.7, 2.5),
	)

	private val tier3Pickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.4, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.2, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.8, 2.5),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 2.5),
		Material.COAL_BLOCK to OreData("石炭の塊", 0.7, 3.0),
		Material.RAW_IRON to OreData("鉄鉱石の塊", 0.7, 3.0),
	)

	private val tier4Pickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.6, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 1.4, 2.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.9, 2.5),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.9, 2.5),
		Material.COAL_BLOCK to OreData("石炭の塊", 0.8, 3.0),
		Material.RAW_IRON to OreData("鉄鉱石の塊", 0.8, 3.0),
	)

	val allBreakableMaterials: List<Material> =
		(tier1Pickaxe.keys + tier2Pickaxe.keys + tier3Pickaxe.keys).toSet().toList()

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

	fun createTier1Pickaxe(): ItemStack {
		val destroyableBlocks = tier1Pickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = tier1Pickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"tier1のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"common",
			destroyableBlocks,
			200
		)
	}

	fun createTier2Pickaxe(): ItemStack {
		val destroyableBlocks = tier2Pickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = tier2Pickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"tier2のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"rare",
			destroyableBlocks,
			201
		)
	}

	fun createTier3Pickaxe(): ItemStack {
		val destroyableBlocks = tier3Pickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = tier3Pickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"Tier3のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"epic",
			destroyableBlocks,
			202
		)
	}

	fun createTier4Pickaxe(): ItemStack {
		val destroyableBlocks = tier3Pickaxe.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = tier4Pickaxe.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"Tier4のピッケル",
			listOf("破壊可能なブロック:") + lore,
			"legendary",
			destroyableBlocks,
			202
		)
	}
}