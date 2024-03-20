fun Int.toHour(): String {
    val hour = this / 60
    val mint = this % 60
    return "$hour hr : $mint mint"
}
