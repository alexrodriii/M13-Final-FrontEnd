package com.example.hospitalfrontend.ui.nurses.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.theme.Primary

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(navController: NavController, nurseViewModel: NurseViewModel) {
    // Initialize the state for the profile image and the image picker launcher
    var profileImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
            }
        }

    // Retrieving nurse details from the ViewModel
    val nurseState = nurseViewModel.nurseState.value
    val displayedProfile = "${nurseState?.name} ${nurseState?.surname}"
    val displayedEmail = nurseState?.email ?: ""

    // Form fields state
    val nameValue = rememberSaveable { mutableStateOf(nurseState?.name ?: "") }
    val surnameValue = rememberSaveable { mutableStateOf(nurseState?.surname ?: "") }
    val emailValue = rememberSaveable { mutableStateOf(nurseState?.email ?: "") }
    val birthdayValue = rememberSaveable { mutableStateOf(nurseState?.age ?: "") }
    val specialityValue = rememberSaveable { mutableStateOf(nurseState?.speciality ?: "") }
    val passwordValue = rememberSaveable { mutableStateOf("") }

    val isEmailValid =
        Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matches(emailValue.value)

    fun isFormValid(): Boolean {
        return nameValue.value.isNotEmpty() && surnameValue.value.isNotEmpty() && emailValue.value.isNotEmpty() && birthdayValue.value.isNotEmpty() && passwordValue.value.isNotEmpty() && specialityValue.value.isNotEmpty() && isEmailValid
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF8282E1), Color(0xFFFFFFFF))
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {
                // Back Button and Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Profile",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(enabled = isFormValid(), onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Change",
                            tint = if (isFormValid()) Color.Green else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Profile Picture with click functionality to open the gallery
                Image(
                    painter = painterResource(R.drawable.icon_user),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }, // Open the gallery
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = displayedProfile,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = displayedEmail,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))
                MyTextUpdateField(
                    labelValue = "Name",
                    icon = (Icons.Default.Person),
                    textValue = nameValue
                )
                MyTextUpdateField(
                    labelValue = "Surname",
                    icon = (Icons.Default.Person),
                    textValue = surnameValue
                )
                MyTextUpdateField(
                    labelValue = "Email",
                    icon = (Icons.Default.Email),
                    textValue = emailValue
                )
                DateTextUpdateField(
                    labelValue = "Birthday",
                    icon = (Icons.Default.Today),
                    dateValue = birthdayValue
                )
                PasswordTextUpdateField(
                    labelValue = "Password",
                    icon = (Icons.Default.Password),
                    passwordValue = passwordValue
                )
                SpecialityUpdateDropdown(nurseViewModel, specialityValue)
                Spacer(modifier = Modifier.height(20.dp))

                // LogOut Button
                Button(
                    onClick = {
                        nurseViewModel.disconnectNurse()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "LogOut",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LogOut",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                // Delete Button
                Button(
                    onClick = { TODO() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Delete",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MyTextUpdateField(
    labelValue: String,
    icon: ImageVector,
    textValue: MutableState<String>
) {
    OutlinedTextField(
        value = textValue.value,
        onValueChange = { textValue.value = it },
        label = { Text(text = labelValue) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Color.DarkGray,
            unfocusedIndicatorColor = Color.Gray
        )
    )
}

@Composable
fun DateTextUpdateField(
    labelValue: String,
    icon: ImageVector,
    dateValue: MutableState<String>
) {
    val datePattern = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")
    val isDateValid = datePattern.matches(dateValue.value)
    val isDateEmpty = dateValue.value.isEmpty()

    OutlinedTextField(
        value = dateValue.value,
        onValueChange = { dateValue.value = it },
        label = { Text(text = labelValue) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        isError = !isDateValid && !isDateEmpty,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Color.DarkGray,
            unfocusedIndicatorColor = if (isDateEmpty || isDateValid) Color.Gray else Color.Red
        )
    )

    if (!isDateValid && !isDateEmpty) {
        Text(
            text = "Please enter a valid date (dd/MM/yyyy)",
            color = Color.Red,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PasswordTextUpdateField(
    labelValue: String,
    icon: ImageVector,
    passwordValue: MutableState<String>
) {
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{4,}$")
    val isPasswordValid = passwordPattern.matches(passwordValue.value)
    val isPasswordEmpty = passwordValue.value.isEmpty()

    OutlinedTextField(
        value = passwordValue.value,
        onValueChange = { passwordValue.value = it },
        label = { Text(text = labelValue) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        trailingIcon = {
            val visibilityIcon =
                if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                Icon(imageVector = visibilityIcon, contentDescription = null)
            }
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !isPasswordValid && !isPasswordEmpty,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Color.DarkGray,
            unfocusedIndicatorColor = if (isPasswordEmpty || isPasswordValid) Color.Gray else Color.Red
        )
    )

    if (!isPasswordValid && !isPasswordEmpty) {
        Text(
            text = "Password must include uppercase, lowercase, and a number.",
            color = Color.Red,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SpecialityUpdateDropdown(
    viewModel: NurseViewModel,
    selectedSpeciality: MutableState<String>
) {
    val specialityList by viewModel.specialityNurse.collectAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedSpeciality.value,
            onValueChange = { },
            label = { Text("Speciality") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isDropdownExpanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Open dropdown")
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Primary,
                cursorColor = Primary,
                focusedLabelColor = Color.DarkGray,
            )
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }) {
            specialityList.forEach { speciality ->
                DropdownMenuItem(text = { Text(speciality) }, onClick = {
                    selectedSpeciality.value = speciality
                    isDropdownExpanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    HospitalFrontEndTheme {
        val navController = rememberNavController()
        ProfileScreen(navController, nurseViewModel = NurseViewModel())
    }
}
