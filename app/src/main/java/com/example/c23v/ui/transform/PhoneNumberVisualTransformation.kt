package com.example.c23v.ui.transform

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Transform fields string to a phone number view
 * For example (916)905-71-06
 */
class PhoneNumberVisualTransformation : VisualTransformation {

    companion object {
        const val MAX_SYMBOL_LENGTH = 10
    }

    override fun filter(text: AnnotatedString): TransformedText {

        val trimmed = if (text.text.length >= MAX_SYMBOL_LENGTH) {
            text.text.substring(0..MAX_SYMBOL_LENGTH.dec())
        } else text.text

        var output = ""
        for (i in trimmed.indices) {
            if (i == 0 && trimmed.length >= 3) output += "("
            output += trimmed[i]
            if (i == 2 && trimmed.length >= 3) output += ")"
            else if (i == 5 || i == 7) output += "-"
        }

        val phoneNumberTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    // 2 first numbers. None offsets
                    offset <= 2 -> offset
                    // Add offsets for 2 brackets
                    offset <= 5 -> offset + 2
                    // Add offset for first dash
                    offset <= 7 -> offset + 3
                    // Add offset for second dash
                    offset <= 10 -> offset + 4
                    // Default
                    else -> offset
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    // 2 first numbers. None offsets
                    offset <= 2 -> offset
                    // Remove offsets for 2 brackets
                    offset <= 5 -> offset - 2
                    // Remove offset for first dash
                    offset <= 7 -> offset - 3
                    // Remove offset for second dash
                    offset <= 9 -> offset - 4
                    else -> offset
                }

            }

        }

        return TransformedText(
            AnnotatedString(output),
            phoneNumberTranslator
        )

    }

}