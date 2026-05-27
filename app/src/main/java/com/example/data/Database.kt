package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface NeetDao {

    // --- User Session ---
    @Query("SELECT * FROM user_sessions ORDER BY lastLoginTimestamp DESC LIMIT 1")
    fun getActiveSessionFlow(): Flow<UserSessionEntity?>

    @Query("SELECT * FROM user_sessions WHERE phoneNumber = :phoneNumber")
    suspend fun getSessionByPhone(phoneNumber: String): UserSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: UserSessionEntity)

    @Query("UPDATE user_sessions SET isPremium = :isPremium WHERE phoneNumber = :phoneNumber")
    suspend fun updatePremiumStatus(phoneNumber: String, isPremium: Boolean)

    @Query("DELETE FROM user_sessions")
    suspend fun clearSessions()


    // --- Question Attempts ---
    @Query("SELECT * FROM question_attempts")
    fun getAllAttemptsFlow(): Flow<List<AttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAttempt(attempt: AttemptEntity)

    @Query("DELETE FROM question_attempts")
    suspend fun clearAttempts()


    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessagesFlow(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()
}

@Database(
    entities = [
        UserSessionEntity::class,
        AttemptEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun neetDao(): NeetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neet_ai_pro_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
