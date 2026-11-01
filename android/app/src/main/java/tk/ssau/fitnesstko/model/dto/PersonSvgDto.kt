package tk.ssau.fitnesstko.model.dto

/**
 * SVG-изображения анатомии человека.
 * @param front URL или содержимое SVG для передней части тела.
 * @param back URL или содержимое SVG для задней части тела.
 */
data class PersonSvgDto(
    val front: String?,
    val back: String?
)
