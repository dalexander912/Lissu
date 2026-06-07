package com.lissu.screens.register

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lissu.R
import com.lissu.ui.theme.Lissu_DarkPurple
import com.lissu.ui.theme.Lissu_Purple


@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Lissu_Purple)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo Lissu",
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = "¡Únete a Lissu!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Text(
                text = "Regístrate para empezar\na gestionar tus compras",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Lissu_DarkPurple,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp),
                lineHeight = 22.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )


            Text(
                text = "Usuario",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Lissu_DarkPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                placeholder = { Text("Crea un nombre de usuario", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedBorderColor = Lissu_Purple
                ),
                singleLine = true
            )

            Spacer(Modifier.height(14.dp))


            Text(
                text = "Correo electrónico",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Lissu_DarkPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                placeholder = { Text("ejemplo@correo.com", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedBorderColor = Lissu_Purple
                ),
                singleLine = true
            )

            Spacer(Modifier.height(14.dp))


            Text(
                text = "Contraseña",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Lissu_DarkPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                placeholder = { Text("Crea una contraseña", fontSize = 13.sp) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedBorderColor = Lissu_Purple
                ),
                singleLine = true
            )

            Spacer(Modifier.height(14.dp))


            Text(
                text = "Confirmar contraseña",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Lissu_DarkPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                placeholder = { Text("Repite tu contraseña", fontSize = 13.sp) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    focusedBorderColor = Lissu_Purple
                ),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))


            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Lissu_DarkPurple)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Lissu_DarkPurple),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Ya tengo cuenta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
//@Composable
//fun RegisterPreview() {
//    RegisterScreen()
//}