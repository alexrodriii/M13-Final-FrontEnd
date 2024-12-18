package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.Nurse
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme

@Composable
fun ListNurseScreen(nurseViewModel: NurseViewModel) {
    val nurses by nurseViewModel.nurses.collectAsState()
    // Render the list into de view
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(items = nurses, itemContent = { nurse ->
            NurseListItem(nurse = nurse)
        })
    }
}


@Composable
fun NurseListItem(nurse: Nurse) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.nurse_profile),
                contentDescription = "Image Profile",
                modifier = Modifier.size(50.dp)

            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "ID Nurse: ${nurse.id}")
                Text(text = "Name: ${nurse.name}")
                Text(text = "Surname: ${nurse.surname}")
                Text(text = "Age: ${nurse.age}")
                Text(text = "Email: ${nurse.email}")
                Text(text = "Speciality: ${nurse.speciality}")
                Text(text = "Password: ${"*".repeat(nurse.password.length)}")
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun ListPage() {
    HospitalFrontEndTheme {
        ListNurseScreen(
            nurseViewModel = NurseViewModel()
        )
    }
}

