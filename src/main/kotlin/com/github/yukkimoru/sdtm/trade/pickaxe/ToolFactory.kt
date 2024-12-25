package com.github.yukkimoru.sdtm.trade.pickaxe

import com.github.yukkimoru.sdtm.utility.ItemFactory
import com.github.yukkimoru.sdtm.utility.RarityUtil
import com.github.yukkimoru.sdtm.utility.Translate
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class ToolFactory(private val plugin: JavaPlugin) {

	// データ構造の定義
	// ピッケルの情報を格納するデータクラス
	data class OreData(
		val material: Material,
		val miningSpeed: Double,
		val dropRate: Double,
		val isShard: Boolean = false // シャード(ガラス板)かどうか
	)

	// ピッケルの価格を格納するデータクラス
	data class PickaxeData(
		val pickaxeName: String,
		val rarity: String = "NULL",
		val miningOres: List<OreData>,
		val pickaxeCosts: Map<Material, Int> = mapOf(Material.EMERALD to 1)
	)

	// ピッケルの情報を書き込む
	val pickaxes = mapOf(
		200 to PickaxeData(
			"§7§l木のツルハシ",
			"common",
			listOf(
				OreData(Material.COAL_ORE, 0.5, 1.0),
				OreData(Material.IRON_ORE, 0.4, 1.0)
			),
			mapOf(
				Material.EMERALD to 1,
				Material.DIAMOND to 1
			)
		),
		201 to PickaxeData(
			"§7§l石のツルハシ",
			"uncommon",
			listOf(
				OreData(Material.COAL_ORE, 0.8, 1.0),
				OreData(Material.IRON_ORE, 0.7, 1.0),
				OreData(Material.DEEPSLATE_COAL_ORE, 0.5, 2.0),
				OreData(Material.DEEPSLATE_IRON_ORE, 0.4, 2.0),
			)
		),
		202 to PickaxeData(
			"§7§l鉄のつるはし",
			"rare",
			listOf(
				OreData(Material.COAL_ORE, 1.1, 1.0),
				OreData(Material.IRON_ORE, 1.0, 1.0),
				OreData(Material.DEEPSLATE_COAL_ORE, 0.8, 2.0),
				OreData(Material.DEEPSLATE_IRON_ORE, 0.7, 2.0),
				OreData(Material.COAL_BLOCK, 0.5, 4.0),
				OreData(Material.RAW_IRON_BLOCK, 0.4, 4.0)
			)
		),


		300 to PickaxeData(
			"§d§lルビーのツルハシ",
			"epic",
			listOf(
				OreData(Material.RED_STAINED_GLASS, 0.4, 1.0),
				OreData(Material.RED_STAINED_GLASS_PANE, 0.4, 0.5, true),
				OreData(Material.ORANGE_STAINED_GLASS, 0.6, 1.0),
			)
		),
		301 to PickaxeData(
			"§d§lサファイアのツルハシ",
			"legendary",
			listOf(
				OreData(Material.RED_STAINED_GLASS, 0.6, 1.0),
				OreData(Material.RED_STAINED_GLASS_PANE, 0.6, 0.5, true),
				OreData(Material.ORANGE_STAINED_GLASS, 0.7, 1.0),
				OreData(Material.BLUE_STAINED_GLASS, 0.4, 1.0),
				OreData(Material.BLUE_STAINED_GLASS_PANE, 0.4, 0.5, true),
			)
		)
	)

	// ここから下は、FactoryToolクラスのプロパティとして定義されている
	// 破壊可能な鉱石(鉱石ブロック)
	val allBreakableOreMaterials: List<Material> =
		(200..299).mapNotNull {
			pickaxes[it]?.miningOres?.map { ore -> ore.material }
		}
			.flatten()
			.toSet()
			.toList()

	val allBreakableGemMaterials: List<Material> =
		(300..399).mapNotNull {
			pickaxes[it]?.miningOres?.filter { ore -> !ore.isShard }?.map { ore -> ore.material }
		}
			.flatten()
			.toSet()
			.toList()

	val allBreakableGemShardMaterials: List<Material> =
		(300..399).mapNotNull {
			pickaxes[it]?.miningOres?.filter { ore -> ore.isShard }?.map { ore -> ore.material }
		}
			.flatten()
			.toSet()
			.toList()

	//破壊可能な全てのブロックリスト
	val allBreakableMaterials: List<Material> =
		allBreakableOreMaterials + allBreakableGemMaterials + allBreakableGemShardMaterials

	fun createPickaxe(
		customModelID: Int,
		displayMode: Boolean
	): ItemStack {
		val pickaxeData = pickaxes[customModelID] ?: throw IllegalArgumentException("Invalid customModelID")
		val destroyableBlocks = pickaxeData.miningOres.joinToString(",") {
			"minecraft:${it.material.name.lowercase()}:${it.miningSpeed}:${it.dropRate}"
		}

		val itemFactory = ItemFactory(plugin)
		val lore = if (displayMode) {
			listOf(
				"§f鉱石が掘れそうだ",
				"§a必要素材:",
				*(pickaxeData.pickaxeCosts.entries.map { "§a${Translate.transEN2JP(it.key.name)}§r x${it.value}§r" }
					.toTypedArray()),
				RarityUtil.getInfo(pickaxeData.rarity).name
			)
		} else {
			listOf(
				"§f鉱石が掘れそうだ",
				RarityUtil.getInfo(pickaxeData.rarity).name
			)
		}

		val itemStack = itemFactory.createItemStack(
			Material.NETHERITE_PICKAXE,
			1,
			pickaxeData.pickaxeName,
			lore,
			pickaxeData.rarity,
			customModelID
		)
		val meta: ItemMeta = itemStack.itemMeta
		meta.isUnbreakable = true
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
		val container = meta.persistentDataContainer
		val key = NamespacedKey(plugin, "destroyable_blocks")
		container.set(key, PersistentDataType.STRING, destroyableBlocks)
		itemStack.itemMeta = meta
		return itemStack
	}
}