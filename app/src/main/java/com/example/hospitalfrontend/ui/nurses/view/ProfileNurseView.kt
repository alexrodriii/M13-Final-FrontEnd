package com.example.hospitalfrontend.ui.nurses.view

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.network.RemoteApiMessageBoolean
import com.example.hospitalfrontend.network.RemoteApiMessageNurse
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.Primary

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel,
    remoteViewModel: RemoteViewModel
) {
    val messageApi = remoteViewModel.remoteApiMessage.value
    val remoteApiMessageBoolean = remoteViewModel.remoteApiMessageBoolean.value
    // State for controller of dialog visibility
    var dialogTitle by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }
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
                    //Update button
                    IconButton(onClick = {
                        if (nurseState?.id != null) {
                            //Update nurse method
                            val updateNurse = NurseState(
                                id = nurseState.id ?: 0,
                                name = nameValue.value,
                                surname = surnameValue.value,
                                age = birthdayValue.value,
                                email = emailValue.value,
                                password = passwordValue.value,
                                speciality = specialityValue.value
                            )
                            remoteViewModel.updateNurse(nurseState.id, updateNurse)
                        } else {
                            Log.d("ERROR", "Nurse ID is null")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Change",
                            tint = Color.Green ,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    // Show a pop up
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("OK")
                                }
                            },
                            title = {
                                Text(text = dialogTitle)
                            },
                            text = {
                                Text(text = dialogMessage)
                            }
                        )
                    }

                    LaunchedEffect(messageApi) {
                        when (messageApi) {
                            is RemoteApiMessageNurse.Success -> {
                                if (nurseState?.id != null) {
                                    nurseViewModel.updatedNurse(nurseState.id, messageApi.message)
                                }
                                remoteViewModel.clearApiMessage() // Change the message to Loading to avoid repeated messages when user is logout
                            }

                            is RemoteApiMessageNurse.Error -> {
                                // Show dialog with a specific message
                                dialogTitle = "ERROR: Update"
                                dialogMessage = "Error updating data nurse"
                                showDialog = true // Show the dialog
                            }

                            RemoteApiMessageNurse.Loading -> Log.d("Loading", "Updating nurse...")
                        }
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
                    onClick = {
                        val nurseState = nurseViewModel.nurseState.value
                        val nurseId = nurseState?.id
                        if (nurseId != null) {
                            remoteViewModel.deleteNurse(nurseId)
                        } else {
                            Log.d("Error", "Not found ID nurse")
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                )
                {
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
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogMessage)
            }
        )
    }
    LaunchedEffect(remoteApiMessageBoolean) {
        when (remoteApiMessageBoolean) {
            is RemoteApiMessageBoolean.Success -> {
                val nurseState = nurseViewModel.nurseState.value
                val nurseId = nurseState?.id
                if (nurseId != null) {
                    nurseViewModel.deleteNurse(nurseId)
                    remoteViewModel.clearApiMessage() // Clear the repeat message
                }
            }

            is RemoteApiMessageBoolean.Error -> {
                // Show the message Error
                dialogTitle = "ERROR: Delete"
                dialogMessage = "Error to delete a nurse"
                showDialog = true
            }

            RemoteApiMessageBoolean.Loading -> Log.d("Loading", "Delete nurse...")
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

/*@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    HospitalFrontEndTheme {
        val navController = rememberNavController()
        ProfileScreen(navController, nurseViewModel = NurseViewModel())
    }
}*/
