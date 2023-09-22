package com.example.appvacaciones

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.appvacaciones.db.AppDatabase
import com.example.appvacaciones.db.Lugar
import com.example.appvacaciones.db.LugarDao
import com.example.appvacaciones.ui.theme.AppVacacionesTheme
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    private lateinit var lugarDao: LugarDao
    private lateinit var lugaresLiveData: LiveData<List<Lugar>>

    private lateinit var mapView: MapView
    private lateinit var mapOverlay: Marker

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar OSMDroid
        Configuration.getInstance()
            .load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        lugarDao = AppDatabase.getInstance(this).lugarDao()
        lugaresLiveData = lugarDao.getAllLugares()

        // Solicitar permiso para la ubicación
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION)
        }

        setContent {
            AppVacacionesTheme {
                val lugares by rememberUpdatedState(newValue = lugaresLiveData.value ?: emptyList())
                var showFormulario by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            Modifier.background(Color.Blue),
                        )
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Mapa
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                MapViewContainer()
                            }

                            LugarList(lugares = lugares)

                            Button(
                                onClick = {
                                    showFormulario = true
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "+ Agregar Lugar")
                            }

                            if (showFormulario) {
                                FormularioAgregarLugar(
                                    onGuardarClick = {
                                        agregarLugar(it)
                                        showFormulario = false
                                    },
                                    onCancelarClick = {
                                        showFormulario = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun agregarLugar(lugar: Lugar) {
        val nuevoId = lugarDao.insertar(lugar)
        if (nuevoId > 0) {

        }
    }
}

@Composable
fun LugarList(lugares: List<Lugar>) {
    LazyColumn {
        items(lugares) { lugar ->
            LugarItem(lugar = lugar)
        }
    }
}

@Composable
fun LugarItem(lugar: Lugar) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = lugar.nombre)
            Text(text = "Costo x Noche: $${lugar.costoAlojamiento}")
            Text(text = "Costo Traslados: $${lugar.costoTraslados}")
            // Agrega otros detalles según tus necesidades
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAgregarLugar(
    onGuardarClick: (Lugar) -> Unit,
    onCancelarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var imagenRef by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }
    var orden by remember { mutableStateOf("") }
    var costoAlojamiento by remember { mutableStateOf(0) }
    var costoTraslados by remember { mutableStateOf(0) }
    var comentarios by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = imagenRef,
            onValueChange = { imagenRef = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = latitud.toString(),
            onValueChange = { latitud = it.toDoubleOrNull() ?: 0.0 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = longitud.toString(),
            onValueChange = { longitud = it.toDoubleOrNull() ?: 0.0 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = orden,
            onValueChange = { orden = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = costoAlojamiento.toString(),
            onValueChange = { costoAlojamiento = it.toIntOrNull() ?: 0 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = costoTraslados.toString(),
            onValueChange = { costoTraslados = it.toIntOrNull() ?: 0 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = comentarios,
            onValueChange = { comentarios = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val nuevoLugar = Lugar(
                        nombre = nombre,
                        imagenRef = imagenRef,
                        latitud = latitud,
                        longitud = longitud,
                        orden = orden,
                        costoAlojamiento = costoAlojamiento,
                        costoTraslados = costoTraslados,
                        comentarios = comentarios,
                        realizada = false
                    )
                    onGuardarClick(nuevoLugar)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }

            Button(
                onClick = {
                    onCancelarClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MapViewContainer() {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context)
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    )
}

const val PERMISSIONS_REQUEST_LOCATION = 123
