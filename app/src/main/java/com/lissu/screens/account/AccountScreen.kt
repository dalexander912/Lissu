package com.lissu.screens.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lissu.AppScaffold
import com.lissu.R
import com.lissu.Routes
import com.lissu.ui.theme.Lissu_LightPurple
import com.lissu.ui.theme.Lissu_Purple

@Composable
fun AccountScreen(
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToAddList: () -> Unit = {},
    onNavigateToMaps: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val bgPage = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textMain = if (isDark) Color.White else Color(0xFF111827)
    val textHint = if (isDark) Color(0xFF9CA3AF) else Color(0xFF6B7280)
    val borderColor = if (isDark) Color(0xFF374151) else Color(0xFFE5E7EB)
    val iconBg = if (isDark) Color(0xFF2D2D2D) else Color(0xFFF3F4F6)
    val redText = Color(0xFFDC2626)

    AppScaffold (
        title = "Cuenta",
        currentScreen = Routes.Account,
        onNavigateToHome = onNavigateToHome,
        onNavigateToAddList = onNavigateToAddList,
        onNavigateToMaps = onNavigateToMaps,
        onNavigateToAccount = onNavigateToAccount,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgPage)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBg)
                    .padding(vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.bag_icon),
                        contentDescription = "perfil",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(Modifier.height(10.dp))

                Text(
                    text = if (isLoggedIn) "Usuario1" else "Modo Invitado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMain
                )
                if (!isLoggedIn) {
                    Text(
                        text = "Inicia sesión o registrate",
                        fontSize = 13.sp,
                        color = textHint,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (isLoggedIn) {
                InfoSection(label = "Información personal", textHint = textHint, cardBg = cardBg, borderColor = borderColor) {
                    InfoRow(icon = Icons.Outlined.Person, label = "Nombre", value = "Usuario1", textMain = textMain, textHint = textHint)

                    InfoRow(icon = Icons.Outlined.Email, label = "Correo", value = "usuario1@email.com", textMain = textMain, textHint = textHint)
                }

                Spacer(Modifier.height(12.dp))

                InfoSection(label = "Seguridad", textHint = textHint, cardBg = cardBg, borderColor = borderColor) {
                    InfoRow(icon = Icons.Outlined.Lock, label = "Contraseña", value = "••••••••", textMain = textMain, textHint = textHint)
                }

                Spacer(Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = cardBg,
                    onClick = onLogout
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = redText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = redText
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Lissu_Purple)
                    ) {
                        Text(text = "Iniciar Sesión", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, Lissu_Purple),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isDark) Color.White else Lissu_Purple)
                    ) {
                        Text(text = "Crear una cuenta", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoSection(label: String, textHint: Color, cardBg: Color, borderColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            color = textHint,
            letterSpacing = 0.06.sp,
            modifier = Modifier.padding(start = 2.dp, bottom = 6.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = cardBg,
            border = BorderStroke(0.5.dp, borderColor)
        ) {
            Column(modifier = Modifier.fillMaxWidth(), content = content)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String, textMain: Color, textHint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Lissu_LightPurple, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Lissu_Purple,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(text = label, fontSize = 11.sp, color = textHint)
            Spacer(Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textMain)
        }
    }
}
