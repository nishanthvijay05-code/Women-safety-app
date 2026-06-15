package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlowBackground
import com.example.ui.components.PulsingSOSRing
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainAppContainer(viewModel: SafetyViewModel) {
    val currentScreen = viewModel.currentScreen
    val isDark = viewModel.isDarkModeEnabled

    GlowBackground(isDark = isDark) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Screen router with clean slide animations
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "screen_navigation"
            ) { screen ->
                when (screen) {
                    AppScreen.SPLASH -> SplashScreen(viewModel)
                    AppScreen.ONBOARDING -> OnboardingScreen(viewModel)
                    AppScreen.AUTH -> AuthenticationScreen(viewModel)
                    AppScreen.USER_SETUP -> UserSetupScreen(viewModel)
                    AppScreen.DASHBOARD -> DashboardScreen(viewModel)
                    AppScreen.SOS_ACTIVATION -> SosActivationScreen(viewModel)
                    AppScreen.SILENT_EMERGENCY -> SilentEmergencyScreen(viewModel)
                    AppScreen.AI_ASSISTANT -> AiAssistantScreen(viewModel)
                    AppScreen.COMMUNITY_MAP -> CommunityMapScreen(viewModel)
                    AppScreen.FAKE_CALL -> FakeCallScreen(viewModel)
                    AppScreen.NIGHT_WALK -> NightWalkScreen(viewModel)
                    AppScreen.MEDICAL_CARD -> MedicalCardScreen(viewModel)
                    AppScreen.ADMIN_PANEL -> AdminPanelScreen(viewModel)
                    AppScreen.SETTINGS -> SettingsScreen(viewModel)
                }
            }
        }
    }
}

// --- 1. Animated Splash Screen ---
@Composable
fun SplashScreen(viewModel: SafetyViewModel) {
    LaunchedEffect(Unit) {
        delay(2500)
        viewModel.navigateTo(AppScreen.ONBOARDING)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PulsingSOSRing(
                color = GuardPink,
                targetScale = 1.8f,
                modifier = Modifier.size(140.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            Brush.sweepGradient(listOf(GuardPink, SecurePurple)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning, 
                        contentDescription = "Shield",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "GUARDIAN ANGEL",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (viewModel.isDarkModeEnabled) Color.White else LightOnSurface,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "A I - P O W E R E D   S A F E T Y   N E T",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GuardPink,
                letterSpacing = 3.sp
            )
        }

        Text(
            text = "Always Alert. Always Safe.",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

// --- 2. Onboarding Carousel ---
@Composable
fun OnboardingScreen(viewModel: SafetyViewModel) {
    val stage = viewModel.onboardingIndex
    val isDark = viewModel.isDarkModeEnabled

    val titles = listOf(
        "AI Protection Shield",
        "Multi-Level Defense",
        "Silent Decoy Shroud",
        "Gemini Voice Sentinel",
        "Community Safety Grid"
    )

    val descriptions = listOf(
        "Always-on proactive analytics mapping. Monitors route integrity, emergency check-ins, and battery statuses securely on-device.",
        "Precision scaling. Level 1 silently tracks. Level 2 alerts trusted contacts. Level 3 initiates emergency calls and local rapid panic sirens.",
        "Need a disguise? Silent mode replicates an innocent math calculator on screen while secretly capturing location, audio, and transmitting to guard circles.",
        "Activate with voice macros like 'Help me!'. Gemini instantly screens audio transcripts for distress patterns and alerts support circles.",
        "Contribute anonymously. Report unsafe hotspots, dark alleys, bad lighting, or harassment triggers directly to the verified safe maps."
    )

    val icons = listOf(
        Icons.Default.Warning,
        Icons.Default.Add,
        Icons.Default.Lock,
        Icons.Default.Phone,
        Icons.Default.LocationOn
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = { viewModel.navigateTo(AppScreen.AUTH) },
                modifier = Modifier.testTag("skip_onboarding")
            ) {
                Text("Skip", color = Color.Gray, fontSize = 16.sp)
            }
        }

        // Feature display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0x11FF2C6D), CircleShape)
                    .border(1.dp, GuardPink.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icons[stage],
                    contentDescription = null,
                    tint = GuardPink,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = titles[stage],
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else LightOnSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = descriptions[stage],
                fontSize = 15.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 22.sp
            )
        }

        // Indicator and Buttons
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == stage) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (index == stage) GuardPink else Color.Gray.copy(alpha = 0.5f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (stage > 0) {
                    OutlinedButton(
                        onClick = { viewModel.handleOnboardingPrev() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Back", color = GuardPink)
                    }
                } else {
                    Box(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = { viewModel.handleOnboardingNext() },
                    colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("onboarding_next_button")
                ) {
                    Text(if (stage < 4) "Next" else "Verify Identity", color = Color.White)
                }
            }
        }
    }
}

// --- 3. Authentication & Locker ---
@Composable
fun AuthenticationScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Secure Verification",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else LightOnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Authenticate to sync local safety metrics",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Biometrics Mock Buttons
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = GuardPink,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Biometric Lock Engaged",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color.White else LightOnSurface
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.biometricVerified = true
                            viewModel.isLoggedIn = true
                            viewModel.navigateTo(AppScreen.USER_SETUP)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SecurePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Face", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Face Unlock", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.biometricVerified = true
                            viewModel.isLoggedIn = true
                            viewModel.navigateTo(AppScreen.USER_SETUP)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Fingerprint", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Fingerprint", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // PIN unlock
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("OR ENTER EMERGENCY SECURE PIN", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = enteredPin,
                onValueChange = {
                    if (it.length <= 4) enteredPin = it
                    if (it == viewModel.securityPinCode) {
                        viewModel.biometricVerified = true
                        viewModel.isLoggedIn = true
                        viewModel.navigateTo(AppScreen.USER_SETUP)
                    }
                },
                label = { Text("4-Digit PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .width(180.dp)
                    .testTag("auth_pin_input"),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = GuardPink,
                    focusedIndicatorColor = GuardPink
                )
            )
            if (pinError) {
                Text("Invalid Secure PIN", color = Color.Red, fontSize = 12.sp)
            }
        }

        Button(
            onClick = {
                viewModel.isLoggedIn = true
                viewModel.navigateTo(AppScreen.USER_SETUP)
            },
            colors = ButtonDefaults.textButtonColors(),
            modifier = Modifier.testTag("bypass_lock_button")
        ) {
            Text("Simulate Quick Guest Access (Developer Bypass)", color = Color.Gray, fontSize = 13.sp)
        }
    }
}

// --- 4. User Profile & Medical Configuration ---
@Composable
fun UserSetupScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Guardian Setup",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else LightOnSurface
        )
        Text(
            text = "Create secure emergency medical & profile metrics",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Text("1. PERSONAL METRICS", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.setupName,
                onValueChange = { viewModel.setupName = it },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("name_field"),
                colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = viewModel.setupAge,
                    onValueChange = { viewModel.setupAge = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
                )

                OutlinedTextField(
                    value = viewModel.setupBloodGroup,
                    onValueChange = { viewModel.setupBloodGroup = it },
                    label = { Text("Blood Group") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Text("2. ENCRYPTED EMERGENCY NOTES", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.setupAllergies,
                onValueChange = { viewModel.setupAllergies = it },
                label = { Text("Known Allergies") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.setupConditions,
                onValueChange = { viewModel.setupConditions = it },
                label = { Text("Medical Conditions") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.setupNotes,
                onValueChange = { viewModel.setupNotes = it },
                label = { Text("Emergency Hospital / Doctor Notes") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedLabelColor = GuardPink, focusedIndicatorColor = GuardPink)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveUserProfile() },
            colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("save_profile_button")
        ) {
            Text("Enter Guardian Dashboard", fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- 5. Dashboard / Main Workspace ---
@Composable
fun DashboardScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled
    val contactsList by viewModel.contacts.collectAsState()
    var showSosDialog by remember { mutableStateOf(false) }
    var selectedSosLevel by remember { mutableIntStateOf(3) }
    var flagSilentMode by remember { mutableStateOf(false) }

    val pulseTransition = rememberInfiniteTransition(label = "pulse_green_dot")
    val dotAlpha by pulseTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Sleek Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GUARDIAN ANGEL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecurePurple,
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Green.copy(alpha = dotAlpha), CircleShape)
                    )
                    Text(
                        text = "A.I. Shield Active",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFF1E1E24), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0x1BFFFFFF), RoundedCornerShape(14.dp))
                    .clickable { viewModel.navigateTo(AppScreen.SETTINGS) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = SecurePurple,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Safety Status Card (Current Location & Safety Score)
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            isDark = isDark
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Current Location",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "451 Oak Avenue, Sector 4",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) Color.White else LightOnSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "GPS: " + String.format("%.4f", viewModel.locationLatitude) + ", " + String.format("%.4f", viewModel.locationLongitude),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SecurePurple.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .border(1.dp, SecurePurple.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Score: 98",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecurePurple
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Main SOS Action Center (Center SOS Button)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PulsingSOSRing(
                    color = if (flagSilentMode) SecurePurple else GuardPink,
                    targetScale = 1.8f,
                    modifier = Modifier.size(190.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (flagSilentMode) {
                                        listOf(SecurePurple, Color(0xFF4F46E5), SecurePurple.copy(alpha = 0.8f))
                                    } else {
                                        listOf(Color(0xFFBE123C), GuardPink, Color(0xFFEC4899))
                                    }
                                ),
                                shape = CircleShape
                            )
                            .border(4.dp, Color(0x22FFFFFF), CircleShape)
                            .clickable { showSosDialog = true }
                            .testTag("sos_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SOS",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 34.sp,
                                letterSpacing = (-1).sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "TAP TO ARM",
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "LEVEL $selectedSosLevel",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Assistant Voice macro trigger
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(CircleShape)
                        .background(Color(0x0AFFFFFF))
                        .border(1.dp, Color(0x08FFFFFF), CircleShape)
                        .clickable { viewModel.navigateTo(AppScreen.AI_ASSISTANT) }
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(SecurePurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Voice Sentinel",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "\"Hey Angel, I'm heading home\"",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Medium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Quick Actions Grid (Row of 3 buttons)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButtonAction(
                icon = Icons.Default.Phone,
                label = "Fake Call",
                accentColor = SkyBlue,
                onClick = { viewModel.navigateTo(AppScreen.FAKE_CALL) },
                modifier = Modifier.weight(1f).testTag("action_fake_call")
            )

            IconButtonAction(
                icon = Icons.Default.LocationOn,
                label = "Live Track",
                accentColor = SecurePurple,
                onClick = { viewModel.navigateTo(AppScreen.NIGHT_WALK) },
                modifier = Modifier.weight(1f).testTag("action_walk")
            )

            IconButtonAction(
                icon = Icons.Default.Lock,
                label = "Silent Mode",
                accentColor = AlertUnsafeAmber,
                onClick = { viewModel.navigateTo(AppScreen.SILENT_EMERGENCY) },
                modifier = Modifier.weight(1f).testTag("action_map")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Sleek Bottom Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF070709), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0x0AFFFFFF), RoundedCornerShape(20.dp))
                .padding(vertical = 4.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard",
                    tint = SecurePurple,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.MEDICAL_CARD) },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Medical Card",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.ADMIN_PANEL) },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Alert Records",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // --- Active TAP SOS settings Dialog ---
        if (showSosDialog) {
            AlertDialog(
                onDismissRequest = { showSosDialog = false },
                title = { Text("Configure Threat Protection") },
                text = {
                    Column {
                        Text("Customize your SOS deployment before launching. Silent mode turns off sound & mimics calculator.", fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("1. CHOOSE ESCALATION SHIELD LEVEL:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(1, 2, 3).forEach { lvl ->
                                FilterChip(
                                    selected = selectedSosLevel == lvl,
                                    onClick = { selectedSosLevel = lvl },
                                    label = { Text("Level $lvl") }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Silent Decoy Shroud toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text("Silent Shroud Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Decoys device into math utility", fontSize = 11.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = flagSilentMode,
                                onCheckedChange = { flagSilentMode = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = GuardPink)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSosDialog = false
                            viewModel.pressSosButton(selectedSosLevel, flagSilentMode)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GuardPink)
                    ) {
                        Text("ARM GUARDIAN", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSosDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// Reusable action button
@Composable
fun IconButtonAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(95.dp)
            .background(Color(0xFF13131A), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFF1E1E24), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label.uppercase(),
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// --- 6. SOS Activation Screen ---
@Composable
fun SosActivationScreen(viewModel: SafetyViewModel) {
    val isConfirmed = viewModel.isSosConfirmed
    val counter = viewModel.sosCountdownValue
    val isDark = viewModel.isDarkModeEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (!isConfirmed) "ARMING PROTECTION" else "GUARDIAN ACTIVE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = if (!isConfirmed) AlertDangerOrange else AlertEmergencyRed
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (!isConfirmed) "Tactical confirmation countdown running" else "Broadcasting location and collecting real-time evidence now.",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        if (!isConfirmed) {
            // Countdown circle representation
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(Color(0x1AFFFFB3), CircleShape)
                    .border(2.dp, AlertUnsafeAmber, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = counter.toString(),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Light,
                    color = AlertUnsafeAmber
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.confirmSosActivation() },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertEmergencyRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("force_activate_sos"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("TRIGGER IMMEDIATE EMERGENCY", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { viewModel.cancelSosCountdown() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("cancel_sos_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("CANCEL SECURE SHIELD (I AM SAFE)", color = Color.Green)
                }
            }
        } else {
            // Screen during Active Emergency
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x33D50000)),
                border = BorderStroke(1.dp, AlertEmergencyRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Active Alert Logs", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("LIVE", color = AlertEmergencyRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.sosLiveLogs) { log ->
                            Text(text = log, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.deactivateSos() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .testTag("deactivate_sos_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("RESET SYSTEM SENSORS (ENTER PIN)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// --- 7. Silent Shroud Decoy Screen (Calculator) ---
@Composable
fun SilentEmergencyScreen(viewModel: SafetyViewModel) {
    val decoy = viewModel.silentDecoyType

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Innocent Calculator Display
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hidden micro indicator (tappable to reveal emergency panel / enter pin)
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color.Red.copy(alpha = 0.4f), CircleShape)
                            .clickable {
                                viewModel.deactivateSos()
                            }
                            .testTag("secret_exit_silent")
                    )

                    // Small indicator showing background audio is capturing
                    Text(
                        text = "Recording: ${viewModel.recordedSeconds}s",
                        color = Color.Green.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = viewModel.calcDisplay,
                    fontSize = 54.sp,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    maxLines = 1
                )
            }

            // Standard Calculator Keypad
            Column(modifier = Modifier.fillMaxWidth()) {
                val buttonRows = listOf(
                    listOf('7', '8', '9', '/'),
                    listOf('4', '5', '6', '*'),
                    listOf('1', '2', '3', '-'),
                    listOf('C', '0', '=', '+')
                )

                buttonRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { char ->
                            Button(
                                onClick = { viewModel.onDecoyCalcButtonPress(char) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (char in '0'..'9') Color(0xFF333333) else Color(0xFFFF9500)
                                ),
                                shape = CircleShape,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = char.toString(),
                                    fontSize = 24.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 8. Gemini Safety Assistant UI ---
@Composable
fun AiAssistantScreen(viewModel: SafetyViewModel) {
    val messages = viewModel.chatMessages
    val isDark = viewModel.isDarkModeEnabled
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // AI Assistant Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                    Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Guardian AI Assistant", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (isDark) Color.White else LightOnSurface)
                    Text("Hands-Free Distress Screening", fontSize = 11.sp, color = Color.Green)
                }
            }

            // Simulated Voice trigger button
            IconButton(
                onClick = { viewModel.voiceTranscriptMode = !viewModel.voiceTranscriptMode },
                modifier = Modifier.background(
                    if (viewModel.voiceTranscriptMode) GuardPink else Color(0x227B2CBF),
                    CircleShape
                )
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Voice Mode", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.voiceTranscriptMode) {
            // Voice transcription simulator area
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0x1B7B2CBF)),
                border = BorderStroke(1.dp, SecurePurple.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎤 VOICETRANSCRIPT SIMULATOR (Distress Screen)", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(viewModel.voiceTranscriptStatusText, fontSize = 13.sp, color = if (isDark) Color.White else LightOnSurface)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("CHOOSE TRANSCRIPT TO SIMULATE SPEECH CAPTURE:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.performSimulatedSpeechScreening("Help me! Stop! Leave me alone!") },
                            colors = ButtonDefaults.buttonColors(containerColor = AlertEmergencyRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simulate Distress: 'Help me! Stop!'", fontSize = 10.sp, color = Color.White)
                        }

                        Button(
                            onClick = { viewModel.performSimulatedSpeechScreening("I am walking back to the apartment now, it is fairly well lit here.") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simulate Normal: 'I am walking back...'", fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Chat timeline
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.second
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isUser) GuardPink else Color(0x22FFFFFF),
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomStart = if (isUser) 12.dp else 0.dp,
                                    bottomEnd = if (isUser) 0.dp else 12.dp
                                )
                            )
                            .padding(12.dp)
                            .widthIn(max = 280.dp)
                    ) {
                        Text(
                            text = msg.first,
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        if (viewModel.isAiScreening) {
            LinearProgressIndicator(color = GuardPink, modifier = Modifier.fillMaxWidth())
        }

        // Input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.chatInputText,
                onValueChange = { viewModel.chatInputText = it },
                placeholder = { Text("Ask Gemini: 'Synthesize safest route to Central Park...'") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_input_text")
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { viewModel.sendChatMessage() },
                modifier = Modifier
                    .background(GuardPink, CircleShape)
                    .testTag("ai_send_button")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

// --- 9. Community Safe Spots & Map Grid ---
@Composable
fun CommunityMapScreen(viewModel: SafetyViewModel) {
    val incidentsList by viewModel.incidents.collectAsState()
    val isDark = viewModel.isDarkModeEnabled
    var showReportDialog by remember { mutableStateOf(false) }
    var reportType by remember { mutableStateOf("Dark Area") }
    var reportDesc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                    Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Safety Intelligence Map", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (isDark) Color.White else LightOnSurface)
                    Text("Real-Time Community Incidents", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Button(
                onClick = { showReportDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GuardPink)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Report", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Report Spot", fontSize = 12.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual simulation grid of Safe Route / Hazards
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF1E1C22), RoundedCornerShape(16.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "🗺️ CROWDSOURCED TRACK PLOTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GuardPink
                )
                Spacer(modifier = Modifier.height(8.dp))

                incidentsList.forEach { spot ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val clr = when (spot.type) {
                                        "Dark Area" -> AlertUnsafeAmber
                                        "Crime" -> AlertEmergencyRed
                                        "Safe Zone" -> Color.Green
                                        else -> SkyBlue
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(clr, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(spot.type, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                }
                                Text(spot.description, fontSize = 11.sp, color = Color.LightGray)
                            }
                            IconButton(onClick = { viewModel.discardIncident(spot) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Generate safest routing
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Column {
                Text("SafeRoute AI Routing", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GuardPink)
                Text("Avoid danger zones & broken streetlights automatically.", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.chatInputText = "Synthesize safe bypass routing options near Downtown taking into account historical Dark Area reports."
                        viewModel.navigateTo(AppScreen.AI_ASSISTANT)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecurePurple),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Safest Path via AI", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // Incident Log dialog
        if (showReportDialog) {
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                title = { Text("Report Spot Hazard") },
                text = {
                    Column {
                        Text("Select incident category to register on maps", fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        val cats = listOf("Dark Area", "Harassment", "Suspicious Activity", "Safe Zone")
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            cats.forEach { cat ->
                                FilterChip(
                                    selected = reportType == cat,
                                    onClick = { reportType = cat },
                                    label = { Text(cat, fontSize = 10.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = reportDesc,
                            onValueChange = { reportDesc = it },
                            placeholder = { Text("Describe the hazard clearly...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.fileIncidentReport(reportType, reportDesc, true)
                            showReportDialog = false
                            reportDesc = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GuardPink)
                    ) {
                        Text("Submit anonymously", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReportDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// --- 10. Fake Call Screen UI ---
@Composable
fun FakeCallScreen(viewModel: SafetyViewModel) {
    val durationStr = String.format("%02d:%02d", viewModel.fakeCallDuration / 60, viewModel.fakeCallDuration % 60)

    if (viewModel.isFakeRinging) {
        // Incoming ringing interface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF141416)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("INCOMING SECURE SIMULATED CALL", color = GuardPink, fontSize = 11.sp, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(viewModel.fakeCallerName, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Safety Decoy Active...", color = Color.Gray, fontSize = 14.sp)
                }

                // Slider swipe UI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reject Button
                    Button(
                        onClick = { viewModel.rejectOrHangupFakeCall() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = CircleShape,
                        modifier = Modifier.size(75.dp).testTag("fake_call_reject")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Decline", tint = Color.White, modifier = Modifier.size(32.dp))
                    }

                    // Accept Button
                    Button(
                        onClick = { viewModel.acceptFakeCall() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        shape = CircleShape,
                        modifier = Modifier.size(75.dp).testTag("fake_call_accept")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Accept", tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    } else if (viewModel.isFakeCallConnected) {
        // Connected running chat decoy
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0E13)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(viewModel.fakeCallerName, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(durationStr, color = Color.Green, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }

                // Subtitle simulation for user to follow during conversation
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SIMULATED VOICE PROMPTING CHIP:", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Speak aloud and say: 'Yes dad, I am just leaving Broadway now. I will be home in 5 minutes.'",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }

                Button(
                    onClick = { viewModel.rejectOrHangupFakeCall() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp).testTag("fake_call_hangup")
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Hangup", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    } else {
        // Scheduler UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                    Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fake Call Generator", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (viewModel.isDarkModeEnabled) Color.White else LightOnSurface)
            }

            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                isDark = viewModel.isDarkModeEnabled
            ) {
                Text("1. CUSTOMIZE DECOY CALLER NAME", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GuardPink)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.fakeCallerName,
                    onValueChange = { viewModel.fakeCallerName = it },
                    label = { Text("Caller Identity") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("2. SCHEDULE RING TIMEOUT DELAY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GuardPink)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0, 10, 30, 60).forEach { sec ->
                        OutlinedButton(
                            onClick = { viewModel.fakeCallDelaySeconds = sec },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (viewModel.fakeCallDelaySeconds == sec) SecurePurple else Color.Transparent
                            )
                        ) {
                            Text(if (sec == 0) "Immediate" else "${sec}s", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.scheduleFakeCall() },
                colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .testTag("launch_fake_call"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("DISPATCH SIMULATED CALL", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 11. Night Walk Screen ---
@Composable
fun NightWalkScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled

    val remainingMin = viewModel.checkInSecondsRemaining / 60
    val remainingSec = viewModel.checkInSecondsRemaining % 60
    val timerStr = String.format("%02d:%02d", remainingMin, remainingSec)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Night Walk Companion", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) Color.White else LightOnSurface)
        }

        if (!viewModel.isNightWalkActive) {
            // Setup Walking target destination
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                isDark = isDark
            ) {
                Text("JOURNEY GOAL PARAMETERS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GuardPink)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.checkInDestination,
                    onValueChange = { viewModel.checkInDestination = it },
                    label = { Text("Target Safe Destination") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.checkInEtaMinutes.toString(),
                    onValueChange = { viewModel.checkInEtaMinutes = it.toIntOrNull() ?: 15 },
                    label = { Text("Estimated Arrival Minutes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { viewModel.startNightWalk(viewModel.checkInDestination, viewModel.checkInEtaMinutes) },
                colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .testTag("start_walk_button")
            ) {
                Text("ACTIVATE WALK RADAR", fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            // Running Watch Layout
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .background(Color(0x1A00B0FF), CircleShape)
                        .border(2.dp, SkyBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timerStr,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyBlue
                        )
                        Text("ARRIVING CHECKPOINT", fontSize = 9.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Destination: ${viewModel.checkInDestination}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else LightOnSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Sensors status logs
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Micro-Movement Sensor", fontSize = 12.sp, color = Color.LightGray)
                            Text("OK", color = Color.Green, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Route Deviations Tracked", fontSize = 12.sp, color = Color.LightGray)
                            Text("${viewModel.nightWalkDeviationCount} events", color = if (viewModel.nightWalkDeviationCount == 0) Color.Green else AlertUnsafeAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.endNightWalkPrematurely() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CHECK-IN SAFE (DISARM)", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 12. Medical Card lock-screen representation ---
@Composable
fun MedicalCardScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Emergency Medical ID", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) Color.White else LightOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lockscreen Outer Emulator Outline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, AlertEmergencyRed, RoundedCornerShape(24.dp))
                .background(Color(0xFF13101E), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("EMERGENCY MEDICAL CARD", fontWeight = FontWeight.Bold, color = AlertEmergencyRed, fontSize = 14.sp)
                    Icon(Icons.Default.Warning, contentDescription = "Med", tint = AlertEmergencyRed)
                }

                Divider(color = AlertEmergencyRed.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                Text("PATIENT NAME", fontSize = 11.sp, color = Color.Gray)
                Text(viewModel.setupName.ifBlank { "Unregistered User" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("AGE", fontSize = 11.sp, color = Color.Gray)
                        Text(viewModel.setupAge, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("BLOOD GROUP", fontSize = 11.sp, color = Color.Gray)
                        Text(viewModel.setupBloodGroup, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = AlertEmergencyRed)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("KNOWN ALLERGIES", fontSize = 11.sp, color = Color.Gray)
                Text(viewModel.setupAllergies.ifBlank { "No Known Allergies" }, fontSize = 14.sp, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Text("CHRONIC CONDITIONS", fontSize = 11.sp, color = Color.Gray)
                Text(viewModel.setupConditions.ifBlank { "No Chronic Conditions" }, fontSize = 14.sp, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Text("SPECIAL ESCALATION BACKUP NOTES", fontSize = 11.sp, color = Color.Gray)
                Text(viewModel.setupNotes.ifBlank { "None" }, fontSize = 14.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Trusted Contacts Section
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Text("TRUSTED CONTACT CIRCLES", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))

            val contactsVal by viewModel.contacts.collectAsState()

            contactsVal.forEach { tc ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(tc.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isDark) Color.White else LightOnSurface)
                        Text("Order Hierarchy: Priority #${tc.escalationOrder} [${tc.groupType}]", fontSize = 11.sp, color = Color.Gray)
                    }
                    IconButton(onClick = { viewModel.deleteContact(tc) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick add contact form
            Text("ADD REGISTERED CONTACT:", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.contactInputName,
                onValueChange = { viewModel.contactInputName = it },
                label = { Text("Contact Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = viewModel.contactInputPhone,
                onValueChange = { viewModel.contactInputPhone = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.addTrustedContact() },
                colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Guardian Circle", color = Color.White)
            }
        }
    }
}

// --- 13. Admin logs and general audit tracking ---
@Composable
fun AdminPanelScreen(viewModel: SafetyViewModel) {
    val auditList by viewModel.auditLogs.collectAsState()
    val incidentsList by viewModel.incidents.collectAsState()
    val isDark = viewModel.isDarkModeEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Security Admin Core", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) Color.White else LightOnSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("1. ACTIVE SESSION TELEMETRY AUDIT", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF15121B), RoundedCornerShape(12.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(auditList) { audit ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0x19FFFFFF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(audit.activityType, fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 11.sp)
                                Text("${audit.timestamp % 100000}", fontSize = 10.sp, color = Color.Gray)
                            }
                            Text(audit.description, fontSize = 12.sp, color = Color.White)
                            if (audit.evidenceMeta.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Evidence: ${audit.evidenceMeta}", fontSize = 11.sp, color = SkyBlue)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick moderation action
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Text("MODERATION ANALYTICS", fontSize = 12.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Hotspots Reported: ${incidentsList.size}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else LightOnSurface)
                Text("Moderated: 100%", color = Color.Green, fontSize = 14.sp)
            }
        }
    }
}

// --- 14. Settings configuration ---
@Composable
fun SettingsScreen(viewModel: SafetyViewModel) {
    val isDark = viewModel.isDarkModeEnabled

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) }) {
                Icon(Icons.Default.Home, contentDescription = "Back", tint = GuardPink)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardian Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) Color.White else LightOnSurface)
        }

        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            isDark = isDark
        ) {
            Text("SYSTEM AND THEME OPTIONS", fontWeight = FontWeight.Bold, color = GuardPink, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Dark theme switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Background Atmosphere", color = if (isDark) Color.White else LightOnSurface)
                Switch(checked = viewModel.isDarkModeEnabled, onCheckedChange = { viewModel.isDarkModeEnabled = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Battery mock value setup
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Battery Level (Mock)", color = if (isDark) Color.White else LightOnSurface)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${viewModel.simulatedBatteryLevel}%", fontWeight = FontWeight.Bold, color = GuardPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (viewModel.simulatedBatteryLevel > 5) viewModel.simulatedBatteryLevel -= 10
                    }) {
                        Text("-", fontSize = 18.sp, color = GuardPink)
                    }
                    IconButton(onClick = {
                        if (viewModel.simulatedBatteryLevel < 95) viewModel.simulatedBatteryLevel += 10
                    }) {
                        Text("+", fontSize = 18.sp, color = GuardPink)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PIN configuration
            Text("EMERGENCY SECURITY PIN", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.securityPinCode,
                onValueChange = { if (it.length <= 4) viewModel.securityPinCode = it },
                label = { Text("Secure Pin") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
            colors = ButtonDefaults.buttonColors(containerColor = GuardPink),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text("Save & Complete Settings", color = Color.White)
        }
    }
}
