// M13-Final-FrontEnd/app/src/main/java/com/example/hospitalfrontend/navigation/AppScreen.kt
package com.example.hospitalfrontend.navigation

sealed class AppScreen(val route: String) {
    object SplashScreen : AppScreen(route = "splash_screen")
    object LoginOrRegisterScreen : AppScreen(route = "login_screen")
    object RoomViewScreen : AppScreen("room_view") // Ruta para la vista de habitaciones
    object RoomDetailScreen : AppScreen("room_detail/{roomId}") {
        fun createRoute(roomId: String) = "room_detail/$roomId"
    }
    object DiagnosisScreen : AppScreen("diagnosis/{patientId}") {
        fun createRoute(patientId: Int) = "diagnosis/$patientId"
    }
    object CareScreen : AppScreen("care/{patientId}") {
        fun createRoute(patientId: Int) = "care/$patientId"
    }
    // Añade las rutas para otras pantallas si las tienes o las vas a necesitar
    object PatientListScreen : AppScreen("patient_list")
    object ListNurseScreen : AppScreen("list_nurse")
    object ProfileNurseScreen : AppScreen("profile_nurse")
    object CreateNurseScreen : AppScreen("create_nurse")
    object FindByNameScreen : AppScreen("find_by_name")
}