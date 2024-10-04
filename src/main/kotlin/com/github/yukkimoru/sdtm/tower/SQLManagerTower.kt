package com.github.yukkimoru.sdtm.tower

import org.bukkit.Location
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList

class SQLManagerTower private constructor() {
    private val sqlite: SQL = SQL()

    init {
        sqlite.createTableIfNotExists()
    }

    data class TowerData(val TowerID: Int, val TowerName: String, val TowerType: Int, val level: Int)

    data class Coordinates(val x: Int, val y: Int, val z: Int)

    fun writeTowerDatabase(TowerID: Int, TowerName: String, TowerType: Int, level: Int) {
        val sql = "INSERT INTO tower_data(TowerID, TowerName, TowerType, level) VALUES(?, ?, ?, ?)"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, TowerID)
            pstmt?.setString(2, TowerName)
            pstmt?.setInt(3, TowerType)
            pstmt?.setInt(4, level)
            pstmt?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
    }

    fun getTowerDatabase(TowerID: Int): TowerData? {
        val sql = "SELECT * FROM tower_data WHERE TowerID = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var towerData: TowerData? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, TowerID)
            rs = pstmt?.executeQuery()
            if (rs?.next() == true) {
                val TowerName = rs.getString("TowerName")
                val TowerType = rs.getInt("TowerType")
                val level = rs.getInt("level")
                towerData = TowerData(TowerID, TowerName, TowerType, level)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            rs?.close()
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
        return towerData
    }

    fun upgradeTower(TowerID: Int) {
        val sql = "UPDATE tower_data SET level = level + 1 WHERE TowerID = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, TowerID)
            pstmt?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
    }

    fun removeTower(towerId: Int) {
        val sql = "DELETE FROM tower_data WHERE TowerID = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, towerId)
            pstmt?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
    }

    fun writeCoordinates(TowerID: Int, EdgeLocation: Location, size: Construction.Size) {
        val sql = "INSERT INTO tower_coordinates(TowerID, x, y, z) VALUES(?, ?, ?, ?)"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            for (x in EdgeLocation.x.toInt() until EdgeLocation.x.toInt() + size.x) {
                for (y in EdgeLocation.y.toInt() until EdgeLocation.y.toInt() + size.y) {
                    for (z in EdgeLocation.z.toInt() until EdgeLocation.z.toInt() + size.z) {
                        pstmt?.setInt(1, TowerID)
                        pstmt?.setInt(2, x - 1)
                        pstmt?.setInt(3, y + 1)
                        pstmt?.setInt(4, z - 1)
                        pstmt?.executeUpdate()
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
    }

    fun getTowerID(location: Location): Int {
        val sql = "SELECT TowerID FROM tower_coordinates WHERE x = ? AND y = ? AND z = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var TowerID = 0
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, location.x.toInt())
            pstmt?.setInt(2, location.y.toInt())
            pstmt?.setInt(3, location.z.toInt())
            rs = pstmt?.executeQuery()
            if (rs?.next() == true) {
                TowerID = rs.getInt("TowerID")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            rs?.close()
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
        return TowerID
    }

    fun getCoordinates(TowerID: Int): List<Coordinates> {
        val sql = "SELECT * FROM tower_coordinates WHERE TowerID = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        val coordinatesList = ArrayList<Coordinates>()
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, TowerID)
            rs = pstmt?.executeQuery()
            while (rs?.next() == true) {
                val x = rs.getInt("x")
                val y = rs.getInt("y")
                val z = rs.getInt("z")
                coordinatesList.add(Coordinates(x, y, z))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            rs?.close()
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
        return coordinatesList
    }

    fun removeTowerCoordinates(TowerID: Int) {
        val sql = "DELETE FROM tower_coordinates WHERE TowerID = ?"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            pstmt?.setInt(1, TowerID)
            pstmt?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
    }

    fun getLastTowerID(): Int {
        val sql = "SELECT MAX(TowerID) AS LastTowerID FROM tower_data"
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var lastTowerID = 0
        try {
            sqlite.connect()
            conn = sqlite.getConnection()
            pstmt = conn?.prepareStatement(sql)
            rs = pstmt?.executeQuery()
            if (rs?.next() == true) {
                lastTowerID = rs.getInt("LastTowerID")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            rs?.close()
            pstmt?.close()
            conn?.close()
            sqlite.disconnect()
        }
        return lastTowerID
    }

    companion object {
        private var instance: SQLManagerTower? = null

        @JvmStatic
        fun getInstance(): SQLManagerTower {
            if (instance == null) {
                instance = SQLManagerTower()
            }
            return instance!!
        }
    }
}