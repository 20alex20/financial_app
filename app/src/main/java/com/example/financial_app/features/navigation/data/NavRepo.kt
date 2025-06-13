package com.example.financial_app.features.navigation.data

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.financial_app.R
import com.example.financial_app.features.navigation.domain.models.BarItem

enum class NavRoutes(val route: String) {
    Expenses("expenses"),
    Income("income"),
    Check("check"),
    Items("items"),
    Settings("settings")
}

@Composable
fun navBarItems(): List<BarItem> {
    return listOf(
        BarItem(
            title = stringResource(R.string.expenses),
            image = painterResource(R.drawable.expenses),
            route = "expenses"
        ),
        BarItem(
            title = stringResource(R.string.income),
            image = painterResource(R.drawable.income),
            route = "income"
        ),
        BarItem(
            title = stringResource(R.string.check),
            image = painterResource(R.drawable.check),
            route = "check"
        ),
        BarItem(
            title = stringResource(R.string.items),
            image = painterResource(R.drawable.items),
            route = "items"
        ),
        BarItem(
            title = stringResource(R.string.settings),
            image = painterResource(R.drawable.settings),
            route = "settings"
        ),
    )
}

@Composable
fun Items() {
    Text("Items Page", fontSize = 30.sp)
}
