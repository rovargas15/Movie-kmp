
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

fun String.toDateFormat(): String {
    val date = LocalDate.parse(this)
    return date.format(
        LocalDate.Format {
            this.dayOfMonth(padding = Padding.SPACE)
            char('/')
            this.monthNumber(padding = Padding.SPACE)
            char('/')
            this.year(Padding.SPACE)
        },
    )
}
