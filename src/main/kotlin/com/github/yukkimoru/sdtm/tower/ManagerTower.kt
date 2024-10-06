package com.github.yukkimoru.sdtm.tower

import org.bukkit.Location

class ManagerTower {
	private val sqlite: SQL = SQL()

	init {
		sqlite.createTableIfNotExists()
	}

	data class TowerData(val TowerID: Int, val TowerName: String, val TowerType: Int, val level: Int)

	data class Coordinates(val x: Int, val y: Int, val z: Int)

	fun writeTowerDatabase(TowerID: Int, TowerName: String, TowerType: Int, level: Int) {
		val sql = "INSERT INTO tower_data(TowerID, TowerName, TowerType, level) VALUES(?, ?, ?, ?)"
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, TowerID)
				pstmt.setString(2, TowerName)
				pstmt.setInt(3, TowerType)
				pstmt.setInt(4, level)
				pstmt.executeUpdate()
			}
		}
		sqlite.disconnect()
	}

	fun getTowerDatabase(TowerID: Int): TowerData? {
		val sql = "SELECT * FROM tower_data WHERE TowerID = ?"
		var towerData: TowerData? = null
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, TowerID)
				pstmt.executeQuery().use { rs ->
					if (rs.next()) {
						val TowerName = rs.getString("TowerName")
						val TowerType = rs.getInt("TowerType")
						val level = rs.getInt("level")
						towerData = TowerData(TowerID, TowerName, TowerType, level)
					}
				}
			}
		}
		sqlite.disconnect()
		return towerData
	}

	fun upgradeTower(TowerID: Int) {
		val sql = "UPDATE tower_data SET level = level + 1 WHERE TowerID = ?"
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, TowerID)
				pstmt.executeUpdate()
			}
		}
		sqlite.disconnect()
	}

	fun removeTower(towerId: Int) {
		val sql = "DELETE FROM tower_data WHERE TowerID = ?"
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, towerId)
				pstmt.executeUpdate()
			}
		}
		sqlite.disconnect()
	}

	fun writeCoordinates(TowerID: Int, EdgeLocation: Location, size: Construction.Size) {
		val sql = "INSERT INTO tower_coordinates(TowerID, x, y, z) VALUES(?, ?, ?, ?)"
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				for (x in EdgeLocation.x.toInt() until EdgeLocation.x.toInt() + size.x) {
					for (y in EdgeLocation.y.toInt() until EdgeLocation.y.toInt() + size.y) {
						for (z in EdgeLocation.z.toInt() until EdgeLocation.z.toInt() + size.z) {
							pstmt.setInt(1, TowerID)
							pstmt.setInt(2, x - 1)
							pstmt.setInt(3, y + 1)
							pstmt.setInt(4, z - 1)
							pstmt.executeUpdate()
						}
					}
				}
			}
		}
		sqlite.disconnect()
	}

	fun getTowerID(location: Location): Int {
		val sql = "SELECT TowerID FROM tower_coordinates WHERE x = ? AND y = ? AND z = ?"
		var TowerID = 0
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, location.x.toInt())
				pstmt.setInt(2, location.y.toInt())
				pstmt.setInt(3, location.z.toInt())
				pstmt.executeQuery().use { rs ->
					if (rs.next()) {
						TowerID = rs.getInt("TowerID")
					}
				}
			}
		}
		sqlite.disconnect()
		return TowerID
	}

	fun getCoordinates(TowerID: Int): List<Coordinates> {
		val sql = "SELECT * FROM tower_coordinates WHERE TowerID = ?"
		val coordinatesList = ArrayList<Coordinates>()
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, TowerID)
				pstmt.executeQuery().use { rs ->
					while (rs.next()) {
						val x = rs.getInt("x")
						val y = rs.getInt("y")
						val z = rs.getInt("z")
						coordinatesList.add(Coordinates(x, y, z))
					}
				}
			}
		}
		sqlite.disconnect()
		return coordinatesList
	}

	fun removeTowerCoordinates(TowerID: Int) {
		val sql = "DELETE FROM tower_coordinates WHERE TowerID = ?"
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.setInt(1, TowerID)
				pstmt.executeUpdate()
			}
		}
		sqlite.disconnect()
	}

	fun GetTowerID(location: Location): Int {
		return getTowerID(location)
	}

	fun getLastTowerID(): Int {
		val sql = "SELECT MAX(TowerID) AS LastTowerID FROM tower_data"
		var lastTowerID = 0
		sqlite.connect()
		sqlite.getConnection()?.use { conn ->
			conn.prepareStatement(sql).use { pstmt ->
				pstmt.executeQuery().use { rs ->
					if (rs.next()) {
						lastTowerID = rs.getInt("LastTowerID")
					}
				}
			}
		}
		sqlite.disconnect()
		return lastTowerID
	}

	companion object {
		private var instance: ManagerTower? = null

		@JvmStatic
		fun getInstance(): ManagerTower {
			if (instance == null) {
				instance = ManagerTower()
			}
			return instance!!
		}
	}
}