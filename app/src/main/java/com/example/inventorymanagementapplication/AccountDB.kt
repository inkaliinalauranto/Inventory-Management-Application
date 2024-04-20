package com.example.inventorymanagementapplication

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

/* This is why the kapt dependency was installed. When creating a room
database table, the @Entity annotation is needed. id and accessToken
are the columns of the account table.
 */
@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accessToken: String
)

// Dao means Data abstract object. All queries are defined inside a Dao.
@Dao
abstract class AccountDao {
    @Insert
    abstract suspend fun addToken(entity: AccountEntity)

    @Query("SELECT accessToken FROM account ORDER BY id DESC LIMIT 1;")
    abstract suspend fun getToken(): String?

    @Query("DELETE FROM account")
    abstract suspend fun removeTokens()
}

/* The actual database. When the structure of this database is changed,
the version number must be exchanged. If not, the database structure
won't update.
 */
@Database(entities = [AccountEntity::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}