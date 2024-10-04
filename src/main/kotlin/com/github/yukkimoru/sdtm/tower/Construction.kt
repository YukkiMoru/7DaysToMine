package com.github.yukkimoru.sdtm.tower

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.WorldEditException
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import com.sk89q.worldedit.util.io.Closer
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class Construction {

    // create size class
    class Size {
        var x: Int = 0
        var y: Int = 0
        var z: Int = 0
    }

    fun summonStructure(location: Location, structureName: String) {
        // offset the structure
        val offset = Location(location.world, location.x, location.y, location.z)
        offset.x = offset.x - 2
        offset.y = offset.y + 1
        offset.z = offset.z - 2
        val schematic = File("plugins/WorldEdit/schematics/$structureName.schem")
        val worldEdit = WorldEdit.getInstance()
        val format = ClipboardFormats.findByFile(schematic)
        try {
            Closer.create().use { closer ->
                val fis = closer.register(FileInputStream(schematic))
                val bis = closer.register(BufferedInputStream(fis))
                val reader = closer.register(format?.getReader(bis))

                val clipboard = reader?.read()

                try {
                    worldEdit.editSessionFactory.getEditSession(BukkitWorld(offset.world), -1).use { editSession ->
                        val operation: Operation = ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(offset.x, offset.y, offset.z))
                            .ignoreAirBlocks(false)
                            .build()
                        Operations.complete(operation)
                    }
                } catch (e: WorldEditException) {
                    throw RuntimeException(e)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getSizeStructure(structureName: String): Size {
        val schematic = File("plugins/WorldEdit/schematics/$structureName.schem")
        val worldEdit = WorldEdit.getInstance()
        val format = ClipboardFormats.findByFile(schematic)
        val size = Size()
        // Get the size of the structure
        try {
            Closer.create().use { closer ->
                val fis = closer.register(FileInputStream(schematic))
                val bis = closer.register(BufferedInputStream(fis))
                val reader = closer.register(format?.getReader(bis))

                val clipboard = reader?.read()

                size.x = clipboard?.dimensions?.blockX ?: 0
                size.y = clipboard?.dimensions?.blockY ?: 0
                size.z = clipboard?.dimensions?.blockZ ?: 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return size
    }

    fun removeStructure(towerID: Int) {
        val towerManager = SQLManagerTower.Companion.getInstance()
        val coordinatesList = towerManager.getCoordinates(towerID)

        for (coordinates in coordinatesList) {
            val world: World? = Bukkit.getWorld("TD_world") // Replace with your world name
            val location = Location(world, coordinates.x.toDouble(), coordinates.y.toDouble(), coordinates.z.toDouble())
            location.block.type = Material.AIR
        }
    }
}