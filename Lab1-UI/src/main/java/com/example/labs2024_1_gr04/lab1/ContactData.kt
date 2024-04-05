package com.example.labs2024_1_gr04.lab1

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.labs2024_1_gr04.lab1.api.CitiesViewModel
import com.example.labs2024_1_gr04.lab1.api.CitiesViewModelFactory
import com.example.labs2024_1_gr04.lab1.api.RetrofitInstance
import com.example.labs2024_1_gr04.lab1.navigation.Screens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContactDataScreen(navController: NavController) {
    val title = stringResource(R.string.title_activity_contact_data)
    val phoneLabel = stringResource(id = R.string.phone)
    val addressLabel = stringResource(id = R.string.address)
    val countryLabel = stringResource(id = R.string.country)
    val cityLabel = stringResource(id = R.string.city)
    val emailLabel = stringResource(id = R.string.email)

    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    val country = rememberSaveable { mutableStateOf("") }
    val city = rememberSaveable { mutableStateOf("") }

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            ConstraintLayout {
                val (form) = createRefs()
                Column(modifier = Modifier.constrainAs(form) {
                    top.linkTo(parent.top)
                }
                ) {
                    Title(title)
                    Divider(color = Color.Gray, thickness = 1.dp)
                    PhoneFiled(phone, onLastNameChange = { phone = it })
                    EmailFiled(email, onLastNameChange = { email = it })
                    CountryField(country)
                    CityField(city)
                    AddressField(address = address, onLastNameChange = { address = it })
                }
            }
        }

        Configuration.ORIENTATION_LANDSCAPE -> {
            ConstraintLayout {
                val (form) = createRefs()
                Column(modifier = Modifier.constrainAs(form) {
                    top.linkTo(parent.top)
                }
                ) {
                    Title(title)
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        CountryField(country)
                        CityField(city)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        PhoneFiled(phone, onLastNameChange = { phone = it })
                        EmailFiled(email, onLastNameChange = { email = it })
                    }
                    AddressField(address = address, onLastNameChange = { address = it })
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Button(
            onClick = {
                Log.i(
                    "CONTACT_DATA_LOG",
                    "$title: " +
                            "\n$phoneLabel: $phone" +
                            if (address.isNotEmpty()) {
                                "\n$addressLabel: $address"
                            } else {
                                ""
                            }
                            + "\n$emailLabel: $email "
                            + "\n$countryLabel: ${country.value}" +
                            if (city.value.isNotEmpty()) {
                                "\n$cityLabel: ${city.value}"
                            } else {
                                ""
                            }
                )
                if (phone.isNotEmpty() && email.isNotEmpty() && country.value.isNotEmpty()) {
                    navController.popBackStack()
                }
            },
            Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(),
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Composable
fun PhoneFiled(
    phoneNumber: String,
    onLastNameChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Phone, true)
        TextFieldComponent(
            text = phoneNumber,
            onTextChange = onLastNameChange,
            labelText = stringResource(R.string.phone),
            keyboardType = KeyboardType.Phone,
            true
        )
    }
}

@Composable
fun EmailFiled(
    email: String,
    onLastNameChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Email, true)
        TextFieldComponent(
            text = email,
            onTextChange = onLastNameChange,
            labelText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
            required = true,
            capitalize = false
        )
    }
}

@Composable
fun CountryField(category: MutableState<String>) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Home, true)
        AutoComplete(
            listOf(
                "Colombia",
                "Argentina",
                "Brazil",
                "Chile",
                "Ecuador",
                "Peru",
                "Venezuela"
            ),
            "${stringResource(R.string.country)}*", category
        )
    }
}

@Composable
fun CityField(category: MutableState<String>) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Place, true)
        AsyncAutoComplete(
            stringResource(R.string.city),
            category
        )
    }
}

@Composable
fun AddressField(
    address: String,
    onLastNameChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.LocationOn, true)
        TextFieldComponent(
            text = address,
            onTextChange = onLastNameChange,
            labelText = stringResource(R.string.address),
            keyboardType = KeyboardType.Text,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(dropdownItems: List<String>, name: String, category: MutableState<String>) {
    var textFieldSize by remember {  mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = name,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Column {
            Row {
                TextField(
                    modifier = Modifier
                        .width(250.dp)
                        .border(
                            width = 1.8.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = category.value,
                    onValueChange = {
                        category.value = it
                        expanded = true
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Gray
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .width(250.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 15.dp
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (category.value.isNotEmpty()) {
                            items(
                                dropdownItems.filter {
                                    it.lowercase()
                                        .contains(category.value.lowercase()) || it.lowercase()
                                        .contains("others")
                                }
                                    .sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category.value = title
                                    expanded = false
                                }
                            }
                        } else {
                            items(
                                dropdownItems.sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category.value = title
                                    expanded = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsyncAutoComplete(name: String, category: MutableState<String>) {
    var textFieldSize by remember {  mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val viewModel: CitiesViewModel = viewModel(factory = CitiesViewModelFactory(RetrofitInstance.apiService))
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val cities by viewModel.data.collectAsState()

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = name,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
        )
        Column {
            Row {
                TextField(
                    modifier = Modifier
                        .width(250.dp)
                        .border(
                            width = 1.8.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = category.value,
                    onValueChange = { it ->
                        category.value = it
                        expanded = true
                        searchJob?.cancel()
                        searchJob = coroutineScope.launch {
                            viewModel.fetchCities(category.value)
                            delay(1000)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Gray
                    ),
                    textStyle = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Gray
                            )
                        }
                    }
                )
            }
            AnimatedVisibility(visible = expanded) {
                Card(
                    modifier = Modifier
                        .width(250.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 15.dp
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        items(cities) { city ->
                            CategoryItems(title = city.name) { title ->
                                category.value = title
                                expanded = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .width(250.dp)
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}