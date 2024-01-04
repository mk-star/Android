package dduwcom.mobile.finalreport.data
import java.io.Serializable
data class BookDto(val id: Long, var photo: Int, var title: String, var author: String, var publisher: String, var price: String) : Serializable {
}