package com.github.yukkimoru.sdtm.tower

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class SQL {
	private var connection: Connection? = null

	init {
		connect()
		createTableIfNotExists()
	}

	fun connect() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:TdSql.db")
		} catch (e: SQLException) {
			e.printStackTrace()
		}
	}

	fun disconnect() {
		try {
			connection?.close()
		} catch (e: SQLException) {
			e.printStackTrace()
		}
	}

	fun getConnection(): Connection? {
		return connection
	}

	fun createTableIfNotExists() {
		val sql1 =
			"CREATE TABLE IF NOT EXISTS tower_data(TowerID INTEGER, TowerName TEXT, TowerType INTEGER, level INTEGER)"
		val sql2 = "CREATE TABLE IF NOT EXISTS tower_coordinates(TowerID INTEGER, x INTEGER, y INTEGER, z INTEGER)"
		executeUpdate(sql1)
		executeUpdate(sql2)
	}

	fun deleteAllData() {
		val sql1 = "DELETE FROM tower_data"
		val sql2 = "DELETE FROM tower_coordinates"
		executeUpdate(sql1)
		executeUpdate(sql2)
	}

	private fun executeUpdate(sql: String) {
		try {
			connect()
			getConnection()?.use { conn ->
				conn.prepareStatement(sql).use { stmt ->
					stmt.executeUpdate()
				}
			}
		} catch (e: SQLException) {
			e.printStackTrace()
		} finally {
			disconnect()
		}
	}
}