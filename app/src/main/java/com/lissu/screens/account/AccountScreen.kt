package com.lissu.screens.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.lissu.ui.theme.Lissu_DarkPurple
import com.lissu.ui.theme.Lissu_LightPurple
import com.lissu.ui.theme.Lissu_Purple

private val BgPage     = Color(0xFFF5F5F5)
private val BorderColor = Color(0xFFE5E7EB)
private val TextHint   = Color(0xFF6B7280)
private val TextMain   = Color(0xFF111827)
private val RedLight   = Color(0xFFFCA5A5)
private val RedText    = Color(0xFFDC2626)

@Composable
fun AccountScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAddList: () -> Unit,
    onNavigateToMaps: () -> Unit,
    onNavigateToAccount: () -> Unit

) {
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
                .background(BgPage)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFF3F4F6), CircleShape),
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
                    text = "Usuario1",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMain
                )
            }

            Spacer(Modifier.height(12.dp))

            // Información personal
            InfoSection(label = "Información personal") {
                InfoRow(icon = Icons.Outlined.Person,  label = "Nombre",   value = "Usuario1")
                HorizontalDivider(color = BorderColor, thickness = 0.5.dp)
                InfoRow(icon = Icons.Outlined.Email,   label = "Correo",   value = "usuario1@email.com")

            }

            Spacer(Modifier.height(12.dp))

            // Seguridad
            InfoSection(label = "Seguridad") {
                InfoRow(icon = Icons.Outlined.Lock, label = "Contraseña", value = "••••••••")
            }

            Spacer(Modifier.height(16.dp))

            // Cerrar sesión
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
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
                        tint = RedText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = RedText
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoSection(label: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            color = TextHint,
            letterSpacing = 0.06.sp,
            modifier = Modifier.padding(start = 2.dp, bottom = 6.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(0.5.dp, BorderColor)
        ) {
            Column(modifier = Modifier.fillMaxWidth(), content = content)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
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
            Text(text = label, fontSize = 11.sp, color = TextHint)
            Spacer(Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextMain)
        }
    }
}