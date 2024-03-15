package com.example.labs2024_1_gr04.lab1.navigation

sealed class Screens(val route: String){
    object PersonalDataScreen: Screens("personal_data_screen")
    object ContactDataScreen: Screens("contact_data_screen")
}