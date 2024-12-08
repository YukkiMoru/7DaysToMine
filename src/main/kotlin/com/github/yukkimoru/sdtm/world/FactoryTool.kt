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
		val disableLore: Boolean = false
	)

	// 普通のピッケル
	fun createWoodenPickaxe() = createPickaxe("common", 200, woodenPickaxe, "§f§l木のツルハシ")
	fun createStonePickaxe() = createPickaxe("uncommon", 201, stonePickaxe, "§f§l石のツルハシ")
	fun createIronPickaxe() = createPickaxe("rare", 202, ironPickaxe, "§f§l鉄のツルハシ")

	// ジェムストーン用のピッケル
	fun createRubyPickaxe() = createPickaxe("epic", 300, rubyPickaxe, "§d§lルビーのツルハシ")
	fun createSapphirePickaxe() = createPickaxe("legendary", 301, sapphirePickaxe, "§d§lサファイアのツルハシ")

	private val woodenPickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 0.5, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 0.4, 1.0),
	)

	private val stonePickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 0.8, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 0.7, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.5, 2.0),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.4, 2.0),
	)

	private val ironPickaxe: Map<Material, OreData> = mapOf(
		Material.COAL_ORE to OreData("石炭鉱石", 1.1, 1.0),
		Material.IRON_ORE to OreData("鉄鉱石", 1.0, 1.0),
		Material.DEEPSLATE_COAL_ORE to OreData("深層石炭鉱石", 0.8, 2.0),
		Material.DEEPSLATE_IRON_ORE to OreData("深層鉄鉱石", 0.7, 2.0),
		Material.COAL_BLOCK to OreData("石炭の塊", 0.5, 4.0),
		Material.RAW_IRON to OreData("鉄鉱石の塊", 0.4, 4.0),
	)

	private val rubyPickaxe: Map<Material, OreData> = mapOf(
		Material.RED_STAINED_GLASS to OreData("ルビー", 0.4, 1.0),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー", 0.4, 0.5, true),
		Material.ORANGE_STAINED_GLASS to OreData("アンバー", 0.6, 1.0),
	)

	private val sapphirePickaxe: Map<Material, OreData> = mapOf(
		Material.RED_STAINED_GLASS to OreData("ルビー", 0.6, 1.0),
		Material.RED_STAINED_GLASS_PANE to OreData("ルビー", 0.6, 0.5, true),
		Material.ORANGE_STAINED_GLASS to OreData("アンバー", 0.7, 1.0),
		Material.BLUE_STAINED_GLASS to OreData("サファイア", 0.4, 1.0),
		Material.BLUE_STAINED_GLASS_PANE to OreData("サファイア", 0.4, 0.5, true),
	)

	// ここから下は、FactoryToolクラスのプロパティとして定義されている
	// 破壊可能な鉱石(鉱石ブロック)
	val allBreakableOreMaterials: List<Material> =
		(woodenPickaxe.keys + stonePickaxe.keys + ironPickaxe.keys).toSet().toList()

	//破壊可能な宝石リスト(ガラスブロック)
	val allBreakableGemMaterials: List<Material> =
		(rubyPickaxe.keys + sapphirePickaxe.keys).filter { !it.name.endsWith("_PANE") }.toSet().toList()

	//破壊可能な宝石リスト(ガラス板)
	val allBreakableGemShardMaterials: List<Material> =
		(rubyPickaxe.keys + sapphirePickaxe.keys).filter { it.name.endsWith("_PANE") }.toSet().toList()

	//破壊可能な全てのブロックリスト
	val allBreakableMaterials: List<Material> =
		allBreakableOreMaterials + allBreakableGemMaterials + allBreakableGemShardMaterials

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
		rarity: String,
		customModelData: Int,
		pickaxeData: Map<Material, OreData>,
		name: String
	): ItemStack {
		val destroyableBlocks = pickaxeData.entries.joinToString(",") {
			"minecraft:${it.key.name.lowercase()}:${it.value.miningSpeed}:${it.value.dropRate}"
		}
		// ピッケルの採掘速度などを表示する。表示量が大きすぎるため、loreはコメントアウトする。
//		val lore = pickaxeData.entries.map {
//			"§a⛏${it.value.miningSpeed} ☘${it.value.dropRate} ${it.value.displayName}"
//		}
		return createUnbreakableTool(
			Material.NETHERITE_PICKAXE,
			name,
			listOf("§f鉱石が掘れそうだ"),
			rarity,
			destroyableBlocks,
			customModelData
		)
	}
}