package fr.droidfactory.cosmo.sdk.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal class Migration1To2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL(
                """
                    CREATE TABLE IF NOT EXISTS `TEMP_TABLE` (
                        `mac_address` TEXT NOT NULL, 
                        `brakeLight` INTEGER NOT NULL, 
                        `firmware_version` TEXT NOT NULL, 
                        `installation_mode` TEXT, 
                        `light_auto` INTEGER NOT NULL, 
                        `light_mode` TEXT, 
                        `light_value` INTEGER NOT NULL, 
                        `model` TEXT NOT NULL, 
                        `product` TEXT, 
                        `serial` TEXT, 
                        PRIMARY KEY(`mac_address`)
                    )
                """
            )

            execSQL(
                """
                   CREATE INDEX IF NOT EXISTS `index_PRODUCTS_MAC_ADDRESS` ON `TEMP_TABLE` (`mac_address`) 
                """)

            execSQL(
                """
                INSERT INTO `TEMP_TABLE`(
                     `mac_address`, 
                     `brakeLight`, 
                    `firmware_version`, 
                    `installation_mode`, 
                    `light_auto`, 
                    `light_mode`, 
                    `light_value`, 
                    `model`, 
                    `product`, 
                    `serial`
                ) 
                SELECT `mac_address`, 
                     `brakeLight`, 
                    `firmware_version`, 
                    `installation_mode`, 
                    `light_auto`, 
                    `light_mode`, 
                    `light_value`, 
                    `model`, 
                    `product`, 
                    `serial`
                    FROM `PRODUCTS`
            """
            )

            execSQL(
                """
                    DROP TABLE `PRODUCTS`
                """
            )

            execSQL(
                """
                ALTER TABLE `TEMP_TABLE` RENAME TO `PRODUCTS` 
            """
            )
        }
    }
}