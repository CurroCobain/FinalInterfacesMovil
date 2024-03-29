package com.example.proyectofinalintmov.krankenwagen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinalintmov.krankenwagen.model.Routes
import com.example.proyectofinalintmov.krankenwagen.screens.Ambulances
import com.example.proyectofinalintmov.krankenwagen.screens.Create
import com.example.proyectofinalintmov.krankenwagen.screens.Hospitals
import com.example.proyectofinalintmov.krankenwagen.screens.WelcomePage
import com.example.proyectofinalintmov.krankenwagen.viewModels.AmbulancesViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.HospitalViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.KrankenwagenViewModel
import com.example.proyectofinalintmov.krankenwagen.viewModels.SesionViewModel

/**
 * Gestiona la navegación dentro de la aplicación.
 */
@Composable
fun NavManager(
    viewModel: KrankenwagenViewModel,
    sesionViewModel: SesionViewModel,
    ambulancesViewModel: AmbulancesViewModel,
    hospitalViewModel: HospitalViewModel)
{
    val navController = rememberNavController()
    val showMenu by viewModel.showMenu.collectAsState()
    val userRegistered by viewModel.userRegistererd.collectAsState()
    val editHosp by viewModel.editHosp.collectAsState()
    val editAmb by viewModel.editAmb.collectAsState()

    // Configuración del sistema de navegación
    NavHost(
        navController = navController,
        startDestination = Routes.PantallaWelcome.route
    ) {
        // Definición de las diferentes pantallas y sus rutas
        composable(Routes.PantallaWelcome.route) {
            WelcomePage(navController, viewModel, showMenu,userRegistered, sesionViewModel) }
        composable(Routes.PantallaAmbulances.route) {
            Ambulances(navController, viewModel, showMenu, userRegistered, sesionViewModel, ambulancesViewModel) }
        composable(Routes.PantallaHospitals.route) {
            Hospitals(navController, viewModel, showMenu,userRegistered, sesionViewModel, hospitalViewModel, ambulancesViewModel, editHosp, editAmb) }
        composable(Routes.PantallaCreate.route) {
            Create(navController, viewModel, showMenu,userRegistered, sesionViewModel,ambulancesViewModel, hospitalViewModel) }
    }
}