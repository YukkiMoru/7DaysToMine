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
		Material.IRON_ORE to OreData("鉄鉱石", 1.1, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.8, 2.5),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.8, 2.5),
		Material.COAL_BLOCK to OreData("石炭の塊", 0.7, 3.0),
		Material.RAW_IRON to OreData("鉄鉱石の塊", 0.7, 3.0),
	)

	private val tier4Pickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.6, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.2, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.9, 2.0),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.9, 2.5),
		Material.COAL_BLOCK to OreData("石炭の塊", 0.9, 3.0),
		Material.RAW_IRON to OreData("鉄鉱石の塊", 0.9, 3.0),
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

	private fun createPickaxe(
		tier: Int,
		rarity: String,
		customModelData: Int,
		pickaxeData: Map<Material, OreData>
	): ItemStack {
		val destroyableBlocks = pickaxeData.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		val lore = pickaxeData.entries.map {
			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			"§f§l普通のピッケル",
			listOf("破壊可能なブロック:") + lore,
			rarity,
			destroyableBlocks,
			customModelData
		)
	}

	fun createTier1Pickaxe() = createPickaxe(1, "common", 200, tier1Pickaxe)
	fun createTier2Pickaxe() = createPickaxe(2, "rare", 201, tier2Pickaxe)
	fun createTier3Pickaxe() = createPickaxe(3, "epic", 202, tier3Pickaxe)
	fun createTier4Pickaxe() = createPickaxe(4, "legendary", 202, tier4Pickaxe)
}