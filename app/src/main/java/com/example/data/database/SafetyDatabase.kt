package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Entities ---

@Entity(tableName = "contacts")
data class TrustedContact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val escalationOrder: Int, // Priority order (1, 2, 3...)
    val groupType: String // "Family", "Friend", "Emergency Service"
)

@Entity(tableName = "incidents")
data class IncidentReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "Harassment", "Dark Area", "Crime", "Safe Zone", "Suspicious Activity"
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val reporterName: String = "Anonymous",
    val status: String = "Pending Review" // For Admin moderation
)

@Entity(tableName = "location_logs")
data class LocationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "medical_info")
data class MedicalInfo(
    @PrimaryKey val id: Int = 1, // Singleton row
    val name: String = "",
    val photoBytes: String = "", // Base64 encoding of avatar if present
    val age: Int = 25,
    val bloodGroup: String = "O+",
    val allergies: String = "",
    val medicalConditions: String = "",
    val medicalNotes: String = ""
)

@Entity(tableName = "safety_audit_logs")
data class SafetyAuditLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val activityType: String, // "SOS_PREP", "SOS_ACTIVATED", "SILENT_SOS", "FAKE_CALL", "NIGHT_WALK_START", "CHECKIN_FAIL"
    val description: String,
    val evidenceMeta: String = "" // Metadata e.g. "Audio recorded: audio_sos_01.mp4"
)

// --- DAOs ---

@Dao
interface SafetyDao {
    // Contacts
    @Query("SELECT * FROM contacts ORDER BY escalationOrder ASC")
    fun getAllContacts(): Flow<List<TrustedContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: TrustedContact)

    @Delete
    suspend fun deleteContact(contact: TrustedContact)

    @Query("UPDATE contacts SET escalationOrder = :newOrder WHERE id = :id")
    suspend fun updateEscalationOrder(id: Int, newOrder: Int)

    // Incidents
    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentReport)

    @Delete
    suspend fun deleteIncident(incident: IncidentReport)

    @Query("UPDATE incidents SET status = :status WHERE id = :id")
    suspend fun updateIncidentStatus(id: Int, status: String)

    // Location Logs
    @Query("SELECT * FROM location_logs WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getLogsForSession(sessionId: String): Flow<List<LocationLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationLog(log: LocationLog)

    // Medical Info
    @Query("SELECT * FROM medical_info WHERE id = 1 LIMIT 1")
    fun getMedicalInfo(): Flow<MedicalInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMedicalInfo(info: MedicalInfo)

    // Audit logs
    @Query("SELECT * FROM safety_audit_logs ORDER BY timestamp DESC")
    fun getAuditLogs(): Flow<List<SafetyAuditLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: SafetyAuditLog)
}

// --- App Database ---

@Database(
    entities = [
        TrustedContact::class,
        IncidentReport::class,
        LocationLog::class,
        MedicalInfo::class,
        SafetyAuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SafetyDatabase : RoomDatabase() {
    abstract fun dao(): SafetyDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: SafetyDatabase? = null

    fun getDatabase(context: android.content.Context): SafetyDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = androidx.room.Room.databaseBuilder(
                context.applicationContext,
                SafetyDatabase::class.java,
                "safety_database"
            )
            .fallbackToDestructiveMigration()
            .build()
            INSTANCE = instance
            instance
        }
    }
}
