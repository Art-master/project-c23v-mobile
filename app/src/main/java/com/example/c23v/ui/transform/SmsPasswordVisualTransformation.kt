package com.example.c23v.ui.transform

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

/**
 * Transform fields string to a sms number password
 * For example  9 5 9 5
 */
class SmsPasswordVisualTransformation : VisualTransformation {

    companion object {
        const val MAX_SYMBOL_LENGTH = 4
    }

    override fun filter(text: AnnotatedString): TransformedText {

        val trimmed = if (text.text.length >= MAX_SYMBOL_LENGTH) {
            text.text.substring(0..MAX_SYMBOL_LENGTH.dec())
        } else text.text

        var output = ""
        for (i in 0..MAX_SYMBOL_LENGTH.dec()) {
            output += if (trimmed.isEmpty() || trimmed.length <= i) "_ "
            else "${trimmed[i]} "
        }

        val phoneNumberTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset < MAX_SYMBOL_LENGTH -> trimmed.length * 2
                    else -> (trimmed.length * 2).dec()
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset
            }

        }

        fun buildAnnotatedString(text: String): AnnotatedString {
            val builder = AnnotatedString.Builder()
            for (ch in text) {
                if (ch == '_') {
                    builder.withStyle(style = SpanStyle(color = Color.Gray)) {
                        append(ch)
                    }
                } else builder.append(ch)
            }


            return builder.toAnnotatedString()
        }

        return TransformedText(
            buildAnnotatedString(output),
            phoneNumberTranslator
        )

    }

}