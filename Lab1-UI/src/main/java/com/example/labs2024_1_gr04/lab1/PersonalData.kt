package com.example.labs2024_1_gr04.lab1

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.labs2024_1_gr04.lab1.navigation.Screens
import java.util.Calendar
import java.util.Date

@Composable
fun PersonalDataScreen(navController: NavController) {
    val title = stringResource(id = R.string.title_activity_personal_data)
    val birthDateLabel = stringResource(id = R.string.birth_date)
    val scholarGradeLabel = stringResource(id = R.string.scholar_grade)
    var genderOptions = listOf(stringResource(R.string.female), stringResource(R.string.male))
    val scholarSelectedItem = rememberSaveable { mutableStateOf(scholarGradeLabel) }

    val configuration = LocalConfiguration.current

    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    val selectedOption = rememberSaveable { mutableStateOf("") }
    val mDate = rememberSaveable { mutableStateOf("$birthDateLabel*") }


    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            ConstraintLayout {
                val (form) = createRefs()
                Column(modifier = Modifier.constrainAs(form) {
                    top.linkTo(parent.top)
                }) {
                    Title(title)
                    Divider(color = Color.Gray, thickness = 1.dp)
                    NameField(name) { name = it }
                    LastNameField(lastName) { lastName = it }
                    GenderRadioButtonGroup(genderOptions, selectedOption)
                    BirthDateComponent(mDate)
                    ScholarGradeSpinner(
                        scholarSelectedItem
                    ) { selected ->
                        scholarSelectedItem.value = selected
                    }
                }
            }
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            ConstraintLayout {
                val (form) = createRefs()
                Column(
                    modifier = Modifier.constrainAs(form) {
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Title(title)
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        NameField(name) { name = it }
                        LastNameField(lastName) { lastName = it }
                    }
                    GenderRadioButtonGroup(genderOptions, selectedOption)
                    BirthDateComponent(mDate)
                    ScholarGradeSpinner(
                        scholarSelectedItem
                    ) { selected ->
                        scholarSelectedItem.value = selected
                    }
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
                    "PERSONAL_DATA_LOG",
                    "$title: " +
                            "\n$name $lastName " +
                            if (selectedOption.value.isNotEmpty()) {
                                "\n${selectedOption.value}"
                            } else {
                                ""
                            }
                            + "\nNació el ${mDate.value} " +
                            if (scholarSelectedItem.value.isNotEmpty()) {
                                "\n${scholarSelectedItem.value}"
                            } else {
                                ""
                            }
                )
                if (name.isNotEmpty() && lastName.isNotEmpty() && mDate.value != "$birthDateLabel*") {
                    name = ""
                    lastName = ""
                    selectedOption.value = ""
                    mDate.value = "$birthDateLabel*"
                    scholarSelectedItem.value = scholarGradeLabel
                    navController.navigate(route = Screens.ContactDataScreen.route)
                }
            },
            Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = stringResource(R.string.next), color = Color.White)
        }
    }
}

@Composable
fun Title(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.headlineSmall, color = Color.Gray,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 10.dp, start = 20.dp)
            .fillMaxWidth()
    )
}

@Composable
fun NameField (name: String, onNameChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Person, true)
        TextFieldComponent(
            name,
            onNameChange,
            stringResource(R.string.name),
            KeyboardType.Text,
            true
        )
    }
}

@Composable
fun LastNameField (lastName: String, onLastNameChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp)
    ) {
        IconComponent(Icons.Default.Person, true)
        TextFieldComponent(
            lastName,
            onLastNameChange,
            stringResource(R.string.last_name),
            KeyboardType.Text,
            true
        )
    }
}

@Composable
fun IconComponent (icon: ImageVector, textFieldIcon: Boolean = false) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        Modifier.padding(top = if (textFieldIcon) 20.dp else 0.dp, end = 10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent (
    text: String,
    onTextChange: (String) -> Unit,
    labelText: String,
    keyboardType: KeyboardType,
    required: Boolean = false,
) {
    Column{
        Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = if (required) "$labelText*" else labelText,
            fontSize = 15.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        TextField(
            value = text,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            onValueChange = onTextChange,
            modifier = Modifier
                .width(250.dp)
                .border(1.8.dp, Color.Gray, shape = RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            singleLine = true
        )
    }
}

@Composable
fun GenderRadioButtonGroup (options: List<String>, selectedOption: MutableState<String>) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
        IconComponent(Icons.Default.Person)
        Text(fontWeight = FontWeight.Bold, text = stringResource(R.string.gender))
        options.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = text == selectedOption.value, onClick = { selectedOption.value = text })
                Text(
                    text = text,
                    color = Color.Gray,
                    modifier = Modifier.clickable { selectedOption.value = text })
            }
        }
    }
}

@Composable
fun ScholarGradeSpinner(
    selectedItem: MutableState<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        stringResource(R.string.elementary),
        stringResource(R.string.secondary),
        stringResource(R.string.university),
        stringResource(R.string.other)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
    ) {
        IconComponent(Icons.Default.Info)
        Box(
            modifier = Modifier
                .width(250.dp)
        ) {
            Text(
                text = selectedItem.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .border(2.dp, Color.Gray)
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { label ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(label)
                        expanded = false
                    }, text = { Text(text = label) })
                }
            }
        }
    }
}

@Composable
fun BirthDateComponent(mDate: MutableState<String>) {
    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${if ((mMonth + 1) < 10) "0" + (mMonth + 1) else (mMonth + 1)}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
    ) {
        IconComponent(Icons.Default.DateRange)
        Text(
            fontWeight = FontWeight.Bold,
            text = mDate.value
        )
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = {
                mDatePickerDialog.show()
            },
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = stringResource(R.string.change), color = Color.White)
        }
    }
}
