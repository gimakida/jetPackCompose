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
import java.text.DecimalFormat

@Composable
fun DecimalMaskedTextField() {
    var rawValue by remember { mutableStateOf("") }

    OutlinedTextField(
        value = rawValue,
        onValueChange = { input ->
            // Apenas números
            rawValue = input.filter { it.isDigit() }
        },
        label = { Text("Valor") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = DecimalVisualTransformation()
    )
}

class DecimalVisualTransformation : VisualTransformation {
    private val formatter = DecimalFormat("#,##0.00")

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.text.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        // Converte string em número (sem ponto/virgula ainda)
        val parsed = text.text.toLong()
        val formatted = formatter.format(parsed / 100.0) // divide por 100 p/ ajustar casas decimais

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = formatted.length
                override fun transformedToOriginal(offset: Int): Int = text.length
            }
        )
    }
}
