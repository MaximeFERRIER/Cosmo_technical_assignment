package fr.droidfactory.cosmo.sdk.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ProductEntity(
    @PrimaryKey @ColumnInfo(name = "mac_address") override val _id: String,
    @ColumnInfo(name = "brakeLight") val brakeLight: Boolean,
    @ColumnInfo(name = "firmware_version") val firmwareVersion: String,
    @ColumnInfo(name = "installation_mode") val installationMode: String,
    @ColumnInfo(name = "light_auto") val lightAuto: Boolean,
    @ColumnInfo(name = "light_mode") val lightMode: String,
    @ColumnInfo(name = "light_value") val lightValue: Int,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "product") val product: String,
    @ColumnInfo(name = "serial") val serial: String
): CosmoEntity
