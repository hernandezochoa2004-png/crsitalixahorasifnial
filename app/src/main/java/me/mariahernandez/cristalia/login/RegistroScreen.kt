package me.mariahernandez.cristalia.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import me.mariahernandez.cristalia.R
import me.mariahernandez.cristalia.celdaunitaria.Paleta

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onIrALogin: () -> Unit = onVolver,
    viewModel: RegistroViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                RegistroEventoUi.RegistroExitoso -> {
                    Toast.makeText(context, "¡Usuario creado exitosamente!", Toast.LENGTH_SHORT).show()
                    onIrALogin()
                }
                RegistroEventoUi.NavegarALogin -> {
                    onIrALogin()
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Barra superior
            HeaderBar(onVolver = onVolver)

            // Pill Badge con punto cian
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF131A29))
                    .border(BorderStroke(1.dp, Color(0xFF26334A)), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Paleta.Cian)
                )
            }

            // Título y Subtítulo
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Crear usuario",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ".",
                        color = Paleta.Cian,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Crea tu cuenta para guardar cálculos cristalográficos, parámetros de red y simulaciones 3D.",
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
                    // Campo 1: NOMBRE DE USUARIO
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "NOMBRE DE USUARIO",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        OutlinedTextField(
                            value = state.usuario,
                            onValueChange = viewModel::onUsuarioChange,
                            placeholder = {
                                Text(
                                    "ej. difraccion_lab",
                                    color = Color(0xFF4A5568),
                                    fontSize = 12.sp
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

                    // Campo 2: CORREO ELECTRÓNICO
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "CORREO ELECTRÓNICO",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        OutlinedTextField(
                            value = state.correo,
                            onValueChange = viewModel::onCorreoChange,
                            placeholder = {
                                Text(
                                    "ej. usuario@cristalia.io",
                                    color = Color(0xFF4A5568),
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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

                    // Campo 2: CREAR CONTRASEÑA
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "CREAR CONTRASEÑA",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        OutlinedTextField(
                            value = state.contrasena,
                            onValueChange = viewModel::onContrasenaChange,
                            placeholder = {
                                Text(
                                    "••••••••••••",
                                    color = Color(0xFF4A5568),
                                    fontSize = 12.sp
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

                    // Campo 3: CONFIRMAR CONTRASEÑA
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "CONFIRMAR CONTRASEÑA",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        OutlinedTextField(
                            value = state.confirmarContrasena,
                            onValueChange = viewModel::onConfirmarContrasenaChange,
                            placeholder = {
                                Text(
                                    "Repite tu contraseña",
                                    color = Color(0xFF4A5568),
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                                    Icon(
                                        imageVector = if (state.confirmPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                        contentDescription = "Ver confirmación de contraseña",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                    // Mensaje de error
                    state.error?.let { errorMsg ->
                        Text(
                            text = errorMsg,
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botón CREAR CUENTA
                    Button(
                        onClick = viewModel::onCrearCuentaClick,
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
                                    text = "CREAR CUENTA",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
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

            Spacer(modifier = Modifier.height(8.dp))

            // Footer: ¿Ya tienes una cuenta? Iniciar sesión
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = Paleta.TextoSecundario,
                    fontSize = 13.sp
                )
                Text(
                    text = "Iniciar sesión",
                    color = Paleta.Cian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { viewModel.onIrALoginClick() }
                )
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
        // Flecha de regreso
        IconButton(onClick = onVolver) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Paleta.Cian,
                modifier = Modifier.size(24.dp)
            )
        }

        // Logo y Nombre de la app
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF131926)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Cristalia",
                    modifier = Modifier.size(24.dp)
                )
            }
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
