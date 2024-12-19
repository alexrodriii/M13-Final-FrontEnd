package com.example.hospitalfrontend.ui.login

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.R.color.colorText
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.theme.*

@Composable
fun HospitalLoginScreen(
    nurseViewModel: NurseViewModel, navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginOrRegisterScreen(navController, nurseViewModel)
        }
    }
}

@Composable
fun LoginOrRegisterScreen(
    navController: NavController, nurseViewModel: NurseViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image()
            Text(
                text = "Login", modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(), style = TextStyle(
                    fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal
                ), color = colorResource(id = colorText), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            UserForm(nurseViewModel, navController)
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp) // Add padding for better visual spacing
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = colorResource(id = colorText)
            )
        }
    }
}

@Composable
fun ToggleLoginRegisterText(navController: NavController) {

    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Don't have an account?")
        Text(text = "Register",
            modifier = Modifier
                .clickable { navController.navigate("create") }
                .padding(start = 5.dp))
    }
}

//This function is only for the image
@Composable
fun Image() {
    Image(
        painter = painterResource(id = R.drawable.login),
        contentDescription = "Login screen image",
        modifier = Modifier.size(100.dp)
    )
}


@Composable
fun UserForm(
    nurseViewModel: NurseViewModel, navController: NavController
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
    //Validate de login
    val mContext = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //The text field email
        EmailInput(
            emailState = email
        )
        //The text field password
        PasswordInput(
            passwordState = password, labelId = "Password", passwordVisible = passwordVisible
        )

        Spacer(modifier = Modifier.height(20.dp))
        // Go to register screen
        ToggleLoginRegisterText(navController)
        Spacer(modifier = Modifier.height(50.dp))

        SubmitButton(
            textId = "Login", inputValid = isValid
        ) {
            nurseViewModel.loginNurse(email.value, password.value)
            if (nurseViewModel.loginState.value.isLogin) {
                Toast.makeText(mContext, "Successfully Login", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            } else {
                Toast.makeText(mContext, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun SubmitButton(
    textId: String, inputValid: Boolean, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        enabled = inputValid,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Secundary, Primary)
                    ), shape = RoundedCornerShape(50.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = textId, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>, labelId: String, passwordVisible: MutableState<Boolean>
) {
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Primary,
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
            }
        })
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    //The icon show it eye open or eye close
    val image = if (passwordVisible.value) Icons.Default.VisibilityOff
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
        valueState = emailState, labelId = labelId,/*I have created this variable so that the
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    val navController = rememberNavController()
    val nurseViewModel = NurseViewModel()

    HospitalFrontEndTheme {
        HospitalLoginScreen(nurseViewModel, navController)
    }
}

