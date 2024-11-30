package com.example.hospitalfrontend.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hospitalfrontend.R

//This function is only for the image
@Composable
fun Image(
) {
    androidx.compose.foundation.Image(
        painter = painterResource(id = R.drawable.login),
        contentDescription = "Image",
        modifier = Modifier.size(100.dp)
    )
}

@Preview
@Composable
fun HospitalLoginScreen() {
    //Create a variable bool that I'll use
    val showLoginScreen = rememberSaveable() {
        mutableStateOf(true)
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //If teh nurse login, a new account isn't created
            if (showLoginScreen.value) {
                Image()
                Text(text = "Login")
                UserForm(
                    isCreateAccount = false
                ) { email, password ->
                    //When we click on 'Login' in the logcat it shows us a message
                    Log.d("Nurse", "Login with $email and $password")
                }
                //If the nurse register, a new account is created
            } else {
                Text(text = "Register")
                UserForm(
                    isCreateAccount = true
                ) { email, password ->
                    Log.d("Nurse", "Create account with $email and $password")
                }
            }
        }
    }
}

//OptIn(ExperimentalComposeUiAppi::class)
@Composable
fun UserForm(
    //bool variable if the nurse creates a new account or not
    isCreateAccount: Boolean = false,
    //Once the nurse introduces her email and password the onDone will do a callback.
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    //Create variables for the form
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    //If the password is visible or not
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }

    //To hide the login button
    val isValid = rememberSaveable(email.value, password.value) {
        //trims() to remove the white space and .isNotEmpty for that isn't empty
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    //message
    var message = remember {
        mutableStateOf("")
    }
    //Validate de login
    val mContext = LocalContext.current
    //To hide a keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    //I used the column so that the text fields are aligned
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //The text field email
        EmailInput(
            emailState = email
        )
        //The text field password
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if (isCreateAccount) "Create account" else "Login",
            inputValido = isValid
        ) {
            //To hide the login button
            onDone(email.value.trim(), password.value.trim())
            //To hide a keyboard
            keyboardController?.hide()
            val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")

            if (email.value == "admin@gmail.com"
                && password.value == "Admin^04"
                && emailPattern.matches(email.value)
                && passwordPattern.matches(password.value)
            ) {
                if (email.value == "admin@gmail.com" && password.value == "Admin^04") {
                    Toast.makeText(mContext, "Successfully Login", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(mContext, "Error Login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(mContext, "Invalid Email or Password Format", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

@Composable
fun SubmitButton(
    textId: String, inputValido: Boolean, onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        //Design the input login
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
            //The border radius
            shape = CircleShape,
            enabled = inputValido
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(
                (5.dp)
            )
        )
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
){
    //If the value password is  true then the password is visible
    val visualTransformation = if (passwordVisible.value) VisualTransformation.None
    //If the value password is false the the password is invisible
    else PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        //Hide the password
        visualTransformation = visualTransformation,
        //create the icon for hide or show the password
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    //The icon show it eye open or eye close
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else Icons.Default.Visibility
    //When we write the password we can see the icon
    IconButton(onClick = {
        //We active the icon and when we click the password is visible or invisible
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image, contentDescription = ""
        )
    }
}

//This function is of the text field email
@Composable
fun EmailInput(
    emailState: MutableState<String>,
    //I used labelId so that there is a label that puts email in its text field.
    labelId: String = "Email"
) {
    // I have created this function so that I can create other non-email fields later.
    InputField(
        valueState = emailState,
        labelId = labelId,
        /*I have created this variable so that the
        @ sign appears when the nurse enters her e-mail.*/
        keyboardType = KeyboardType.Email
    )
}

//This function is for the layout of the texts fields.
@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
    )
}