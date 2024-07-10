package es.paytef.cepsastandalone.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.paytef.cepsastandalone.BuildConfig

object Migrations {
    //Create DB_SELLS
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `${BuildConfig.DB_SELLS_NAME}`")
        }
    }
    //DB_SESSION
    val MIGRATION_2_3: Migration = object : Migration(2,3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `${BuildConfig.DB_SESSION_NAME}`")
        }
    }
    //Create DB_SHIFT
    val MIGRATION_3_4: Migration = object : Migration(3,4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `${BuildConfig.DB_SHIFT_NAME}` (id INTEGER, PRIMARY KEY (`id`), checkInDate TEXT)")
        }
    }
    val MIGRATION_4_5: Migration = object : Migration(4,5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SHIFT_NAME} ADD COLUMN idShift TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SHIFT_NAME} ADD COLUMN virtualTicket INTEGER")
        }
    }
    val MIGRATION_5_6: Migration = object: Migration(5,6){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN acquirerID TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN amountWithSign TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN approved INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN authorisationCode TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN authorisationMode TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN commerceCSB TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN commerceText TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN commerceCode TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN emvARC TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN failed INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN isDCC INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN message TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN needsSignature INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN opType TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN operationCommission INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN operationTypeName TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN paytefOperationNumber INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN pinpadSerialNumber TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN requestedAmount INTEGER")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN resultCode TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN resultText TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN sessionDate TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN signatureData TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN signatureType TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN tcod TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN title TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN transactionReference TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN verificationMode TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN timestamp TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN receipts TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN cardInformation TEXT")
        }
    }
    val MIGRATION_6_7: Migration = object : Migration(6,7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE `${BuildConfig.DB_PRODUCTS_NAME}`")
            database.execSQL("CREATE TABLE `${BuildConfig.DB_PRODUCTS_NAME}` (time TEXT, PRIMARY KEY (`productID`), name TEXT NOT NULL, product_price TEXT, maxPrice TEXT, minPrice TEXT, type_sell TEXT, type_product TEXT NOT NULL, imageB64 TEXT, taxPercent TEXT NOT NULL, flagID TEXT NOT NULL, accepted INTEGER, amountSell TEXT, code INTEGER NOT NULL)")
        }
    }
    val MIGRATION_7_8: Migration = object: Migration(7,8){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SESSION_NAME} ADD COLUMN terminalName TEXT")
        }
    }
    val MIGRATION_8_9: Migration = object: Migration(8,9){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN productID TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN imageB64 TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN taxPercent TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN flagID TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN accepted TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN code TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN paytefUnit TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN maxPrice TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN minPrice TEXT")
            database.execSQL("ALTER TABLE ${BuildConfig.DB_SELLS_NAME} ADD COLUMN amountSell TEXT")
        }
    }
    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Create a new table with the auto-generated primary key
            database.execSQL("""
            CREATE TABLE new_SellData (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                time TEXT NOT NULL,
                name TEXT NOT NULL,
                productID TEXT NOT NULL,
                // ... (add all other columns from SellData here)
            )
            """.trimIndent()
            )

            // 2. Copy data from the old table to the new table
            database.execSQL(
                """
            INSERT INTO new_SellData (time, name, productID, /* ... other columns */)
            SELECT time, name, productID, /* ...other columns */
            FROM DB_SELLS_NAME
            """.trimIndent()
            )

            // 3. Drop the old table
            database.execSQL("DROP TABLE DB_SELLS_NAME")

            // 4. Rename the new table to the original name
            database.execSQL("ALTER TABLE new_SellData RENAME TO DB_SELLS_NAME")
        }
    }
    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table with the modified schema
            database.execSQL(
                """
            CREATE TABLE new_SellData (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                time TEXT,
                name TEXT,
                productID TEXT,
                imageB64 TEXT, // Fixed typo here
                taxPercent TEXT,
                flagID TEXT,
                accepted INTEGER,
                code INTEGER,
                paytefUnit TEXT,
                maxPrice TEXT,
                minPrice TEXT,
                amountSell TEXT,
                quantity TEXT,
                unitPrice TEXT,
                totalPrice TEXT,
                type_sell TEXT,
                type_product TEXT,
                product_price TEXT,
                processorTextValue TEXT,
                acquirerID TEXT,
                amountWithSign TEXT,
                approved INTEGER,
                authorisationCode TEXT,authorisationMode TEXT,
                cardInformation TEXT, 
                commerceCSB TEXT,
                commerceCode TEXT,
                commerceText TEXT,
                emvARC TEXT,
                failed INTEGER,
                isDCC INTEGER,
                message TEXT,
                needsSignature INTEGER,
                opType TEXT,
                operationCommission INTEGER,
                operationTypeName TEXT,
                paytefOperationNumber INTEGER,
                pinpadSerialNumber TEXT,
                receipts TEXT, 
                requestedAmount INTEGER,
                resultCode TEXT,
                resultText TEXT,
                sessionDate TEXT,
                signatureData TEXT,
                signatureType TEXT,
                tcod TEXT,
                timestamp TEXT,
                title TEXT,
                transactionReference TEXT,
                verificationMode TEXT
            )
            """.trimIndent()
            )

            // Copy data from the old table to the new table
            database.execSQL(
                """
            INSERT INTO new_SellData (id, time, name, productID, imageB64, taxPercent, flagID, accepted, code, paytefUnit, maxPrice, minPrice, amountSell, quantity, unitPrice, totalPrice, type_sell,type_product, product_price, processorTextValue, acquirerID, amountWithSign, approved, authorisationCode, authorisationMode, cardInformation, commerceCSB, commerceCode, commerceText, emvARC, failed, isDCC, message, needsSignature, opType, operationCommission, operationTypeName, paytefOperationNumber, pinpadSerialNumber, receipts, requestedAmount, resultCode, resultText, sessionDate, signatureData, signatureType, tcod, timestamp, title, transactionReference, verificationMode)
            SELECT id, time, name, productID, imageB64, taxPercent, flagID, accepted, code, paytefUnit, maxPrice, minPrice, amountSell, quantity, unitPrice, totalPrice, type_sell, type_product, product_price, processorTextValue, acquirerID, amountWithSign, approved, authorisationCode, authorisationMode, cardInformation, commerceCSB, commerceCode, commerceText, emvARC, failed, isDCC, message, needsSignature, opType, operationCommission, operationTypeName, paytefOperationNumber, pinpadSerialNumber, receipts, requestedAmount, resultCode, resultText, sessionDate, signatureData, signatureType, tcod, timestamp, title, transactionReference, verificationMode
            FROM DB_SELLS_NAME
            """.trimIndent()
            )

            // Drop the old table
            database.execSQL("DROP TABLE DB_SELLS_NAME")

            // Rename the new table to the original name
            database.execSQL("ALTER TABLE new_SellData RENAME TO DB_SELLS_NAME")
        }
    }
}