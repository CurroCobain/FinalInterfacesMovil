package com.example.proyectofinalintmov.krankenwagen.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyectofinalintmov.R
import com.example.proyectofinalintmov.barralateral.BarraLateral
import com.example.proyectofinalintmov.bienvenida.Bienvenida
import com.example.proyectofinalintmov.krankenwagen.model.Routes
import com.example.proyectofinalintmov.krankenwagen.viewModels.AmbulancesViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.HospitalViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.KrankenwagenViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.SesionViewModel

/**
 * Scaffold que alberga la página de hospitales
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun Hospitals(
    navController: NavHostController,
    viewModel: KrankenwagenViewModel,
    userRegistered: Boolean,
    sesionViewModel: SesionViewModel,
    hospitalViewModel: HospitalViewModel,
    ambulancesViewModel: AmbulancesViewModel,
    editHosp: Boolean,
    editAmb: Boolean
) {
    val drawerState1 = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerState2 = rememberDrawerState(initialValue = DrawerValue.Closed)
    val createHosp by viewModel.createHosp.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState1,
        drawerContent = {
            ModalDrawerSheet( modifier = Modifier.fillMaxWidth(0.3f)) {
                NavigationMenu(navController, viewModel)
            }
        }
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ){
            ModalNavigationDrawer(
                drawerState = drawerState2,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight()
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ){
                            DialogSesion(viewModel = viewModel, sesionViewModel = sesionViewModel)
                        }
                    }
                }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    PrevContHosp(
                        navController,
                        userRegistered,
                        viewModel,
                        sesionViewModel,
                        hospitalViewModel,
                        ambulancesViewModel,
                        editHosp,
                        editAmb,
                        drawerState1,
                        drawerState2,
                        createHosp
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrevContHosp(
    navController: NavHostController,
    userRegistered: Boolean,
    viewModel: KrankenwagenViewModel,
    sesionViewModel: SesionViewModel,
    hospitalViewModel: HospitalViewModel,
    ambulancesViewModel: AmbulancesViewModel,
    editHosp: Boolean,
    editAmb: Boolean,
    drawerState1: DrawerState,
    drawerState2: DrawerState,
    createHosp: Boolean

) {
    // Se almacena el nombre del Dr para mostrarlo en pantalla
    val nombreDocReg by sesionViewModel.nombreDoc.collectAsState()

    // Scaffold que compone la pantalla
    Scaffold(topBar = {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mensaje de bienvenida al Dr, se muestra el nombre una vez iniciada la sesión
            Bienvenida(
                bienvenidoADrHouseTextContent = "Bienvenido/a Dr $nombreDocReg"
            )
        }
    }, content = {
        // Contenido del Scaffold
        ContenidoHospitals(
            navController = navController,
            userDesplegado = userRegistered,
            viewModel = viewModel,
            sesionViewModel = sesionViewModel,
            hospitalViewModel = hospitalViewModel,
            editHosp = editHosp,
            ambulancesViewModel = ambulancesViewModel,
            editAmb = editAmb
        )
        if(createHosp){
            CreateHospital(hospitalViewModel, viewModel)
        }
    }, bottomBar = {
        // Barra para acceder al menú y a las opciones de sesión
        BarraMenu(viewModel = viewModel, drawerState1 = drawerState1, drawerState2 = drawerState2)
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ){
            Button(onClick = { viewModel.acCreateHosp() },
                colors = ButtonDefaults.buttonColors(Color(74, 121, 66)),
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            ) {
                Text(text = "Crear hospital",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )
            }
        }
    })
}

/**
 * Composable que crea el contenido de la pantalla de hospitales.
 */
@Composable
fun ContenidoHospitals(
    navController: NavHostController,
    userDesplegado: Boolean,
    viewModel: KrankenwagenViewModel,
    sesionViewModel: SesionViewModel,
    hospitalViewModel: HospitalViewModel,
    ambulancesViewModel: AmbulancesViewModel,
    editHosp: Boolean,
    editAmb: Boolean,
) {
    val context = LocalContext.current
    // Variable que se usa para gestionar el estado del dropDownMenu de selección de provincia
    var expanded by remember { mutableStateOf(false) }
    // Lista que almacena los nombres de las provincias para mostrarlos en el dropDownMenu, sólo se usa aquí
    val provincias =
        listOf("Almeria", "Cadiz", "Cordoba", "Granada", "Huelva", "Jaen", "Malaga", "Sevilla")
    // Variable que almacena la provincia seleccionada en el dropDownMenu
    val selectedProvincia = remember { mutableStateOf("Selecciona una provincia") }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        // Fondo de la pantalla
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        )
        {
            Row(modifier = Modifier.padding(start = 60.dp, top = 100.dp)) {
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        " Filtrar por provincia ->   ",
                        fontSize = 30.sp,
                        modifier = Modifier.background(color = Color.LightGray)
                    )
                }

                // Columna que muestra la provincia actual seleccionada y el dropDownMenu
                Column(modifier = Modifier.clickable(onClick = { expanded = true })) {
                    Text(
                        text = selectedProvincia.value,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(2.dp)),
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))

                    //DropDownMenu que muestra las provincias para filtrar hospitales
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        provincias.forEach { provincia ->
                            DropdownMenuItem(onClick = {
                                selectedProvincia.value = provincia
                                // Se asigna el valor de SelectedProvincia a la variable que la gestiona en el HospitalViewModel
                                viewModel.setProv(selectedProvincia.value)
                                // Se filtran los hospitales
                                viewModel.getHosp(selectedProvincia.value) {
                                    expanded = false
                                }
                            }) {
                                Text(
                                    text = provincia,
                                    fontSize = 30.sp
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 70.dp)) {
                // LazyRow con la lista de hospitales
                LazyHospital(
                    viewModel = viewModel,
                    hospitalViewModel = hospitalViewModel,
                    arrangement = Arrangement.Center,
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    editHosp = editHosp
                )
            }

        }
        // Cuando se modifica alguna de las variables que lo gestionan se muestran los distintos menús
        when {
            userDesplegado -> DialogSesion(viewModel, sesionViewModel)
            editHosp -> EditarHosp(viewModel, hospitalViewModel, ambulancesViewModel)
            editAmb -> EditarAmb(viewModel, ambulancesViewModel)
        }
    }
}


/**
 * Composable que muestra los hospitales de la base de datos, pueden aparecer filtrados por provincia o no
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Suppress("UNUSED_PARAMETER")
@Composable
fun LazyHospital(
    viewModel: KrankenwagenViewModel,
    hospitalViewModel: HospitalViewModel,
    arrangement: Arrangement.HorizontalOrVertical,
    modifier: Modifier,
    editHosp: Boolean
) {
    // Varibale que muestra el valor de listHospitals del viewModel
    val miListaHosp by viewModel.listHospitals.collectAsState()
    LazyRow {
        items(miListaHosp) { hospital ->
            Card(modifier = Modifier
                .padding(20.dp)
                .size(250.dp)
                .clickable {
                    // Al hacer click asignamos los valores del hospital clickado al hospitalViewModel
                    hospitalViewModel.asignHospFields(hospital) {
                        // Tras asignar los valores activamos el menú de edición
                        viewModel.activaEditHosp()
                    }
                })
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.7f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Imagen del hospital
                        Image(
                            painter = painterResource(id = R.drawable.hospital),
                            contentDescription = "Hosp avatar",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Row {
                        // Nombre del hospital
                        Text(
                            text = hospital.name,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}







