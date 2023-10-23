package pt.isel.leic.daw.gomokuRoyale.http.media.siren

import org.springframework.http.MediaType
import java.net.URI

class Action (
    val title:String?=null,
    val actionClass: List<String>?= null,
    val method : String? = null, //TODO maybe create enum class
    val href:URI,
    val type:MediaType?=null,
    val field:List<Field>?=null
)