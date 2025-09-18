package br.com.fiap.goldwise_compose

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RealMaskedTextField() {
    var rawValue by remember { mutableStateOf("") }

    OutlinedTextField(
        value = rawValue,
        onValueChange = { input ->
            // Mantém somente números
            rawValue = input.filter { it.isDigit() }
        },
        label = { Text("Valor (R$)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = RealVisualTransformation()
    )
}

class RealVisualTransformation : VisualTransformation {
    private val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.text.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        // Converte a string em número e divide por 100 (2 casas decimais)
        val parsed = text.text.toLong()
        val formatted = formatter.format(parsed / 100.0)

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = formatted.length
                override fun transformedToOriginal(offset: Int): Int = text.length
            }
        )
    }
}

