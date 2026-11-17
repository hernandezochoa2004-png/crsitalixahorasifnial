package me.mariahernandez.cristalia.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import me.mariahernandez.cristalia.R
import me.mariahernandez.cristalia.celdaunitaria.Paleta

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onLoginExitoso: (String) -> Unit = { _ -> onVolver() },
    onCrearCuenta: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is LoginEventoUi.LoginExitoso -> {
                    Toast.makeText(context, "¡Sesión iniciada con éxito!", Toast.LENGTH_SHORT).show()
                    onLoginExitoso(evento.usuario)
                }
                LoginEventoUi.NavegarACrearCuenta -> {
                    onCrearCuenta()
                }
                LoginEventoUi.OlvideContrasena -> {
                    Toast.makeText(context, "Recuperar contraseña próximamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paleta.Fondo)
            .drawBehind {
                val step = 28.dp.toPx()
                val strokeWidth = 0.5.dp.toPx()
                val gridColor = Color(0xFF182030).copy(alpha = 0.35f)

                var x = 0f
                while (x < size.width) {
                    drawLine(gridColor, start = Offset(x, 0f), end = Offset(x, size.height), strokeWidth = strokeWidth)
                    x += step
                }
                var y = 0f
                while (y < size.height) {
                    drawLine(gridColor, start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = strokeWidth)
                    y += step
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Barra superior
            HeaderBar(onVolver = onVolver)

            // Título y Subtítulo
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Paleta.Cian)
                    )
                    Text(
                        text = "Iniciar sesión",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Accede a tus herramientas de cristalografía, simulaciones 3D y cálculos de celda.",
                    color = Paleta.TextoSecundario,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            // Tarjeta del Formulario
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Paleta.Tarjeta,
                border = BorderStroke(1.dp, Color(0xFF1E2638)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo Usuario
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Nombre de usuario",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = state.usuario,
                            onValueChange = viewModel::onUsuarioChange,
                            placeholder = {
                                Text(
                                    "ej. usuario@cristalia.io",
                                    color = Color(0xFF4A5568),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF0D121D),
                                unfocusedContainerColor = Color(0xFF0D121D),
                                focusedBorderColor = Paleta.Cian,
                                unfocusedBorderColor = Color(0xFF1E2738),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Campo Contraseña
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Contraseña",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = state.contrasena,
                            onValueChange = viewModel::onContrasenaChange,
                            placeholder = {
                                Text(
                                    "••••••••",
                                    color = Color(0xFF4A5568),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = viewModel::togglePasswordVisibility) {
                                    Icon(
                                        imageVector = if (state.passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                        contentDescription = "Ver contraseña",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF0D121D),
                                unfocusedContainerColor = Color(0xFF0D121D),
                                focusedBorderColor = Paleta.Cian,
                                unfocusedBorderColor = Color(0xFF1E2738),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Link Olvidé mi contraseña
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Olvidé mi contraseña",
                            color = Paleta.Cian,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { viewModel.onOlvideContrasenaClick() }
                        )
                    }

                    // Mensaje de error si hay
                    state.error?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botón Iniciar sesión
                    Button(
                        onClick = viewModel::onIniciarSesionClick,
                        enabled = !state.cargando,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Paleta.Cian,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (state.cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Iniciar sesión",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Separador O CONTINÚA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF1E2638)
                )
                Text(
                    text = "O CONTINÚA",
                    color = Color(0xFF4A5568),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF1E2638)
                )
            }

            // Tarjeta Botón Crear cuenta
            Surface(
                onClick = viewModel::onCrearCuentaClick,
                shape = RoundedCornerShape(12.dp),
                color = Paleta.Tarjeta,
                border = BorderStroke(1.dp, Color(0xFF1E2638)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes una cuenta? ",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Crear cuenta",
                        color = Paleta.Cian,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderBar(onVolver: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón atrás
        Surface(
            onClick = onVolver,
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF131825),
            border = BorderStroke(1.dp, Color(0xFF263044))
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Logo y Nombre de la app
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Cristalia",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Cristalia",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
