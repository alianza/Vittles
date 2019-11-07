package com.example.vittles.services.scanner

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object DateFormatterService {

    private val regex = Regex("^([0-9&/]*)\$")
    private val charFormat = DateTimeFormat.forPattern("dd/MMM/yyyy")

    val numberFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

    /**
     * sets the retrieved text in the correct date format and returns the date
     *
     * @param text The text that has been retrieved from the camera
     */
    fun expirationDateFormatter(text: String): DateTime? {
        var replacedText = text.replace('-', '/').replace(':', '/').replace('.', '/').replace(' ', '/')
        replacedText = replacedText.replace("okt", "oct").replace("mei", "may").replace("mrt", "mar")

        val formatter = if (replacedText.matches(regex)) {
            numberFormat
        } else {
            charFormat
        }

        var expirationDate = formatter.parseDateTime(replacedText)
        expirationDate = expirationDate.withCenturyOfEra(DateTime.now().centuryOfEra)

        return expirationDate
    }
}