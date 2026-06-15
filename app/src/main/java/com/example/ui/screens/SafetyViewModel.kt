package com.example.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiApiClient
import com.example.data.database.DatabaseProvider
import com.example.data.database.IncidentReport
import com.example.data.database.MedicalInfo
import com.example.data.database.SafetyAuditLog
import com.example.data.database.TrustedContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Screen state enumeration
 */
enum class AppScreen {
    SPLASH,
    ONBOARDING,
    AUTH,
    USER_SETUP,
    DASHBOARD,
    SOS_ACTIVATION,
    SILENT_EMERGENCY,
    AI_ASSISTANT,
    COMMUNITY_MAP,
    FAKE_CALL,
    NIGHT_WALK,
    MEDICAL_CARD,
    ADMIN_PANEL,
    SETTINGS
}

class SafetyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = DatabaseProvider.getDatabase(application)
    private val dao = database.dao()

    // --- Database Flow Listeners ---
    val contacts: StateFlow<List<TrustedContact>> = dao.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incidents: StateFlow<List<IncidentReport>> = dao.getAllIncidents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medicalInfo: StateFlow<MedicalInfo?> = dao.getMedicalInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val auditLogs: StateFlow<List<SafetyAuditLog>> = dao.getAuditLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Onboarding & Navigation States ---
    var currentScreen by mutableStateOf(AppScreen.SPLASH)
    var onboardingIndex by mutableStateOf(0)
    var biometricVerified by mutableStateOf(false)
    var isLoggedIn by mutableStateOf(false)

    // Temp inputs for setup
    var setupName by mutableStateOf("")
    var setupAge by mutableStateOf("23")
    var setupBloodGroup by mutableStateOf("B+")
    var setupAllergies by mutableStateOf("None")
    var setupConditions by mutableStateOf("None")
    var setupNotes by mutableStateOf("Emergency Contact: Primary Guardian")

    // Contact inputs
    var contactInputName by mutableStateOf("")
    var contactInputPhone by mutableStateOf("")
    var contactInputType by mutableStateOf("Family")

    // --- SOS Orchestrator States ---
    var isSosActivated by mutableStateOf(false)
    var sosLevel by mutableStateOf(3) // 1 = UNSAFE, 2 = DANGER, 3 = EMERGENCY
    var isSilentSosMode by mutableStateOf(false)
    var silentDecoyType by mutableStateOf("Calculator") // "Calculator", "Reader", "Notes"
    
    // Decoy features
    var calcDisplay by mutableStateOf("0")
    var isRecordingAudio by mutableStateOf(false)
    var recordedSeconds by mutableStateOf(0)
    private var audioRecordJob: Job? = null

    // Countdown and simulation
    var sosCountdownValue by mutableStateOf(10)
    var isSosConfirmed by mutableStateOf(false)
    val sosLiveLogs = mutableStateListOf<String>()
    
    private var countdownJob: Job? = null
    var locationLatitude by mutableStateOf(40.7128)
    var locationLongitude by mutableStateOf(-74.0060)

    // --- Fake Call Simulator States ---
    var fakeCallerName by mutableStateOf("Mom ❤")
    var fakeCallDelaySeconds by mutableStateOf(10)
    var isFakeRinging by mutableStateOf(false)
    var isFakeCallConnected by mutableStateOf(false)
    var fakeCallDuration by mutableStateOf(0)
    var isFakeAudioPlaying by mutableStateOf(false)
    private var fakeCallTriggerJob: Job? = null
    private var callTimerJob: Job? = null

    // --- AI Assistant Chat States ---
    val chatMessages = mutableStateListOf<Pair<String, Boolean>>(
        Pair("Hello, I am your Guardian AI. Text or speak to me for immediate distress screening, local safety risk diagnostics, or hands-free routes guidance.", false)
    )
    var chatInputText by mutableStateOf("")
    var isAiScreening by mutableStateOf(false)
    var voiceTranscriptMode by mutableStateOf(false)
    var voiceTranscriptStatusText by mutableStateOf("Click below to simulate speaking, e.g., 'Help me! Stop!'. Gemini will analyze your distress vocabulary...")

    // --- Night Walk & Check-In Guardian ---
    var isNightWalkActive by mutableStateOf(false)
    var isCheckInActive by mutableStateOf(false)
    var checkInDestination by mutableStateOf("Grand Central Station")
    var checkInEtaMinutes by mutableStateOf(15)
    var nightWalkDeviationCount by mutableStateOf(0)
    var checkInSecondsRemaining by mutableStateOf(900) // 15 mins
    private var nightWalkJob: Job? = null

    // --- Settings & UI Theme Options ---
    var isDarkModeEnabled by mutableStateOf(true)
    var simulatedBatteryLevel by mutableStateOf(85)
    var securityPinCode by mutableStateOf("1234")

    // --- Initial Default Data Generation ---
    init {
        viewModelScope.launch {
            // Seed defaults if empty
            delay(100) // Wait for DB load
            if (contacts.value.isEmpty()) {
                dao.insertContact(TrustedContact(name = "Audrey Miller (Mom)", phone = "+1 (555) 124-5678", escalationOrder = 1, groupType = "Family"))
                dao.insertContact(TrustedContact(name = "David Ross (Brother)", phone = "+1 (555) 987-6543", escalationOrder = 2, groupType = "Family"))
                dao.insertContact(TrustedContact(name = "Campus Security Desk", phone = "911-MOCK-LINE", escalationOrder = 3, groupType = "Emergency Service"))
            }
            if (incidents.value.isEmpty()) {
                dao.insertIncident(IncidentReport(type = "Dark Area", description = "Street lights broken on 4th Ave and Broadway. Extremely dark.", latitude = 40.7138, longitude = -74.0040, reporterName = "Anonymous", status = "Moderated"))
                dao.insertIncident(IncidentReport(type = "Suspicious Activity", description = "Catcalling outside local bar. Avoid walking alone here.", latitude = 40.7115, longitude = -74.0090, reporterName = "Rachel G.", status = "Moderated"))
                dao.insertIncident(IncidentReport(type = "Safe Zone", description = "24-Hour Cafe - verified safe staff always active.", latitude = 40.7150, longitude = -74.0068, reporterName = "Elena", status = "Moderated"))
            }
            dao.getMedicalInfo().collect { info ->
                if (info != null) {
                    setupName = info.name
                    setupAge = info.age.toString()
                    setupBloodGroup = info.bloodGroup
                    setupAllergies = info.allergies
                    setupConditions = info.medicalConditions
                    setupNotes = info.medicalNotes
                }
            }
        }
    }

    // --- Navigation Handlers ---
    fun navigateTo(screen: AppScreen) {
        currentScreen = screen
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "NAVIGATE", description = "Moved to screen: ${screen.name}"))
        }
    }

    fun handleOnboardingNext() {
        if (onboardingIndex < 4) {
            onboardingIndex++
        } else {
            navigateTo(AppScreen.AUTH)
        }
    }

    fun handleOnboardingPrev() {
        if (onboardingIndex > 0) {
            onboardingIndex--
        }
    }

    // --- Contact Handlers ---
    fun addTrustedContact() {
        if (contactInputName.isBlank() || contactInputPhone.isBlank()) return
        val currentCount = contacts.value.size
        viewModelScope.launch {
            dao.insertContact(
                TrustedContact(
                    name = contactInputName,
                    phone = contactInputPhone,
                    escalationOrder = currentCount + 1,
                    groupType = contactInputType
                )
            )
            dao.insertAuditLog(SafetyAuditLog(activityType = "CONTACT_ADD", description = "Added contact: $contactInputName"))
            contactInputName = ""
            contactInputPhone = ""
        }
    }

    fun deleteContact(contact: TrustedContact) {
        viewModelScope.launch {
            dao.deleteContact(contact)
            dao.insertAuditLog(SafetyAuditLog(activityType = "CONTACT_REMOVE", description = "Removed contact: ${contact.name}"))
        }
    }

    // --- Incident Reporting / Map Handlers ---
    fun fileIncidentReport(type: String, description: String, isAnonymous: Boolean) {
        viewModelScope.launch {
            val offsetLat = (Math.random() - 0.5) * 0.01
            val offsetLon = (Math.random() - 0.5) * 0.01
            val report = IncidentReport(
                type = type,
                description = description,
                latitude = locationLatitude + offsetLat,
                longitude = locationLongitude + offsetLon,
                reporterName = if (isAnonymous) "Anonymous" else (setupName.ifBlank { "User" }),
                status = "Moderated"
            )
            dao.insertIncident(report)
            dao.insertAuditLog(SafetyAuditLog(activityType = "INCIDENT_REPORTED", description = "Reported $type: $description"))
        }
    }

    fun discardIncident(incident: IncidentReport) {
        viewModelScope.launch {
            dao.deleteIncident(incident)
        }
    }

    // --- Profile Handlers ---
    fun saveUserProfile() {
        viewModelScope.launch {
            val medInfo = MedicalInfo(
                name = setupName,
                age = setupAge.toIntOrNull() ?: 23,
                bloodGroup = setupBloodGroup,
                allergies = setupAllergies,
                medicalConditions = setupConditions,
                medicalNotes = setupNotes
            )
            dao.saveMedicalInfo(medInfo)
            dao.insertAuditLog(SafetyAuditLog(activityType = "PROFILE_SAVED", description = "Medical Card & safety profile successfully committed."))
            isLoggedIn = true
            navigateTo(AppScreen.DASHBOARD)
        }
    }

    // --- Advanced SOS System Orchestrator ---
    fun pressSosButton(level: Int, silent: Boolean) {
        sosLevel = level
        isSilentSosMode = silent
        isSosActivated = true
        isSosConfirmed = false
        sosCountdownValue = 10
        sosLiveLogs.clear()
        
        sosLiveLogs.add("• Emergency Mode initialized [⏱ Countdown Active]")
        sosLiveLogs.add("• Location GPS tracker synced ($locationLatitude, $locationLongitude)")
        
        if (silent) {
            sosLiveLogs.add("• Silent Protocol Armed:decoying screen normal")
            // Instantly transition to silent decoy to avoid blocking
            confirmSosActivation()
        } else {
            // Regular activation fires countdown
            countdownJob?.cancel()
            countdownJob = viewModelScope.launch {
                while (sosCountdownValue > 0 && !isSosConfirmed) {
                    delay(1000)
                    sosCountdownValue--
                }
                if (sosCountdownValue == 0 && !isSosConfirmed) {
                    confirmSosActivation()
                }
            }
            navigateTo(AppScreen.SOS_ACTIVATION)
        }
    }

    fun cancelSosCountdown() {
        countdownJob?.cancel()
        isSosActivated = false
        isSosConfirmed = false
        stopAudioRecording()
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "SOS_CANCELLED", description = "SOS Countdown cancelled before propagation."))
        }
        navigateTo(AppScreen.DASHBOARD)
    }

    fun confirmSosActivation() {
        isSosConfirmed = true
        countdownJob?.cancel()
        
        // Start Evidence Capture
        startAudioRecording()

        viewModelScope.launch {
            dao.insertAuditLog(
                SafetyAuditLog(
                    activityType = if (isSilentSosMode) "SILENT_SOS" else "SOS_CONFIRMED",
                    description = "SOS confirmed. Level $sosLevel. Lat: $locationLatitude Lon: $locationLongitude",
                    evidenceMeta = "Audio transmission started. Encrypted timestamps recorded."
                )
            )
        }

        sosLiveLogs.add("• EVIDENCE CAPTURE ENABLED: Local audio encrypted on disk")
        sosLiveLogs.add("• SAFE PATH ENGINE ACTIVE: GPS tracking frequency maximum")
        
        // Multi-level escalation propagation simulation
        viewModelScope.launch {
            if (sosLevel >= 1) {
                delay(800)
                sosLiveLogs.add("• LEVEL 1 [UNSAFE] ARMED: Background audio and logs saved")
            }
            if (sosLevel >= 2) {
                delay(800)
                contacts.value.take(2).forEach { contact ->
                    sosLiveLogs.add("• LEVEL 2 SENT: SMS to ${contact.name} [\"I am in danger! Live tracking: maplink.com/track/user\"]")
                }
            }
            if (sosLevel >= 3) {
                delay(800)
                sosLiveLogs.add("• LEVEL 3 CRITICAL EXECUTING: Pushing data to emergency response server")
                contacts.value.firstOrNull()?.let { contact ->
                    sosLiveLogs.add("• Escalation Dialing: Initiating emergency backup call simulation to ${contact.name}")
                }
            }
        }

        if (isSilentSosMode) {
            navigateTo(AppScreen.SILENT_EMERGENCY)
        } else {
            navigateTo(AppScreen.SOS_ACTIVATION)
        }
    }

    fun deactivateSos() {
        isSosActivated = false
        isSosConfirmed = false
        stopAudioRecording()
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "SOS_DEACTIVATED", description = "Active SOS secure session gracefully closed."))
        }
        navigateTo(AppScreen.DASHBOARD)
    }

    private fun startAudioRecording() {
        isRecordingAudio = true
        recordedSeconds = 0
        audioRecordJob?.cancel()
        audioRecordJob = viewModelScope.launch {
            while (isRecordingAudio) {
                delay(1000)
                recordedSeconds++
            }
        }
    }

    private fun stopAudioRecording() {
        isRecordingAudio = false
        audioRecordJob?.cancel()
    }

    // --- Silent SOS Decoy Calcs ---
    fun onDecoyCalcButtonPress(char: Char) {
        if (char in '0'..'9' || char == '+' || char == '-' || char == '*' || char == '/') {
            if (calcDisplay == "0") {
                calcDisplay = char.toString()
            } else {
                calcDisplay += char
            }
        } else if (char == 'C') {
            calcDisplay = "0"
        } else if (char == '=') {
            // Fake calculation result
            try {
                calcDisplay = "Result: ${10 + (2..2002).random()}"
            } catch (e: Exception) {
                calcDisplay = "0"
            }
        }
    }

    // --- Fake Call Simulator Logic ---
    fun scheduleFakeCall(delaySeconds: Int = fakeCallDelaySeconds, caller: String = fakeCallerName) {
        fakeCallTriggerJob?.cancel()
        fakeCallerName = caller
        fakeCallDelaySeconds = delaySeconds
        
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "FAKE_CALL_SCHEDULE", description = "Scheduled fake incoming call from $caller in $delaySeconds seconds."))
        }

        if (delaySeconds == 0) {
            triggerFakeIncomingRing()
        } else {
            fakeCallTriggerJob = viewModelScope.launch {
                delay(delaySeconds * 1000L)
                triggerFakeIncomingRing()
            }
            // Navigate back to Dashboard to simulate natural delay
            navigateTo(AppScreen.DASHBOARD)
        }
    }

    private fun triggerFakeIncomingRing() {
        isFakeRinging = true
        isFakeCallConnected = false
        fakeCallDuration = 0
        navigateTo(AppScreen.FAKE_CALL)
    }

    fun acceptFakeCall() {
        isFakeRinging = false
        isFakeCallConnected = true
        isFakeAudioPlaying = true
        fakeCallDuration = 0
        
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (isFakeCallConnected) {
                delay(1000)
                fakeCallDuration++
            }
        }
    }

    fun rejectOrHangupFakeCall() {
        isFakeRinging = false
        isFakeCallConnected = false
        isFakeAudioPlaying = false
        callTimerJob?.cancel()
        fakeCallTriggerJob?.cancel()
        navigateTo(AppScreen.DASHBOARD)
    }

    // --- Night Walk Guardian & Check-In Core ---
    fun startNightWalk(destination: String, minutes: Int) {
        checkInDestination = destination
        checkInEtaMinutes = minutes
        checkInSecondsRemaining = minutes * 60
        isNightWalkActive = true
        isCheckInActive = true
        nightWalkDeviationCount = 0
        
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "NIGHT_WALK_START", description = "En route to $destination. Timer alert: $minutes mins."))
        }

        nightWalkJob?.cancel()
        nightWalkJob = viewModelScope.launch {
            while (isNightWalkActive && checkInSecondsRemaining > 0) {
                delay(1000)
                checkInSecondsRemaining--

                // Random deviation event simulator to show smart tracking strength
                if (checkInSecondsRemaining % 80 == 0 && Math.random() < 0.4) {
                    simulateWalkDeviation()
                }
            }
            if (checkInSecondsRemaining == 0 && isNightWalkActive) {
                // EXTRAPOLATE EMERGENCY: Check-In deadline missed!
                triggerCheckinAlarmFailure()
            }
        }
        navigateTo(AppScreen.NIGHT_WALK)
    }

    private fun simulateWalkDeviation() {
        nightWalkDeviationCount++
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "CHECKPOINT_DEVIATION", description = "Night Walk: Left safe route trace (event #$nightWalkDeviationCount). Auto check-in required."))
        }
    }

    fun endNightWalkPrematurely() {
        isNightWalkActive = false
        isCheckInActive = false
        nightWalkJob?.cancel()
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "NIGHT_WALK_SAFE", description = "Journey completed safely. Disarming sensors."))
        }
        navigateTo(AppScreen.DASHBOARD)
    }

    private fun triggerCheckinAlarmFailure() {
        isNightWalkActive = false
        isCheckInActive = false
        nightWalkJob?.cancel()
        
        // Propagate alarm
        pressSosButton(level = 3, silent = false)
        viewModelScope.launch {
            dao.insertAuditLog(SafetyAuditLog(activityType = "CHECKIN_FAIL", description = "FAILED to check-in at $checkInDestination. Triggered SOS escalation."))
        }
    }

    // --- AI Assistant - Voice Transcripts and Chat Interface ---
    fun sendChatMessage(messageText: String = chatInputText) {
        if (messageText.isBlank()) return
        chatInputText = ""
        chatMessages.add(Pair(messageText, true))
        
        isAiScreening = true
        viewModelScope.launch(Dispatchers.IO) {
            val safetyAuditList = dao.getAuditLogs().stateIn(viewModelScope).value.take(5).joinToString { "[${it.activityType}: ${it.description}]" }
            val systemInstructionPr = """
                You are the AI Safety brain for 'Guardian Angel' Women's Safety App. You must evaluate the user's micro questions with ultimate urgency, tactical brevity, and 100% focused safety intelligence.
                Avoid paragraphs of fluff. Direct the user to quick actions if they indicate feeling insecure. 
                Keep answers modern, clear, accessible, and structured. 
                Active Safety Logs in Session context:
                $safetyAuditList
            """.trimIndent()

            val aiAnswer = GeminiApiClient.getAiSafetyResponse(messageText, systemInstructionPr)
            withContext(Dispatchers.Main) {
                isAiScreening = false
                chatMessages.add(Pair(aiAnswer, false))
            }
        }
    }

    /**
     * Simulation of Voice/Speech Distress words evaluation with Gemini
     */
    fun performSimulatedSpeechScreening(distressText: String) {
        voiceTranscriptStatusText = "Analyzing audio pattern: '$distressText'..."
        isAiScreening = true
        viewModelScope.launch(Dispatchers.IO) {
            val checkPrompt = """
                The user's mobile microphone captured this ambient audio transcript: "$distressText".
                Is this utterance indicative of an emergency, physical assault, threat of danger, or distress?
                Evaluate and reply with a structured rating. Follow this exact format:
                [RATING: HIGH/MEDIUM/LOW] - Briefly explain why and give a 1-sentence recommended app action (e.g., 'Activate silent SOS', 'Trigger fake call decoy', 'No action needed').
            """.trimIndent()
            
            val analysisResult = GeminiApiClient.getAiSafetyResponse(checkPrompt)
            withContext(Dispatchers.Main) {
                isAiScreening = false
                voiceTranscriptStatusText = "Speech analysis complete."
                chatMessages.add(Pair("🎤 Distressed Audio Screen Captured: \"$distressText\"", true))
                chatMessages.add(Pair(analysisResult, false))
                
                // If rating is HIGH, prompt immediate SOS recommendation
                if (analysisResult.contains("HIGH", ignoreCase = true) || analysisResult.contains("EMERGENCY", ignoreCase = true)) {
                    chatMessages.add(Pair("⚠️ CRITICAL THREAT DETECTED. Recommending immediate Level 3 Guardian SOS. Press 'CONFIRM ACTIVATE' directly on your home screen or double-press the power key now.", false))
                }
            }
        }
    }

    fun switchTheme() {
        isDarkModeEnabled = !isDarkModeEnabled
    }
}
