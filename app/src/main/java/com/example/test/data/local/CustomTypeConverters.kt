package es.paytef.cepsastandalone.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import es.paytef.cepsastandalone.data.remote.models.request.CardInformation
import es.paytef.cepsastandalone.data.remote.models.request.Receipts
import es.paytef.cepsastandalone.data.remote.models.request.Timestamp

class CustomTypeConverters {
    @TypeConverter
    fun fromTimestampToJson(timestamp: Timestamp?):String{
        return Gson().toJson(timestamp)
    }
    @TypeConverter
    fun fromJsonToTimestamp(json:String):Timestamp{
        return Gson().fromJson(json, Timestamp::class.java)
    }
    @TypeConverter
    fun fromReceiptsToJson(receipts: Receipts?):String{
        return Gson().toJson(receipts)
    }
    @TypeConverter
    fun fromJsonToReceipts(json:String):Receipts{
        return Gson().fromJson(json, Receipts::class.java)
    }
    @TypeConverter
    fun fromCardInformationToJson(cardInformation: CardInformation?):String{
        return Gson().toJson(cardInformation)
    }
    @TypeConverter
    fun fromJsonToCardInformation(json:String):CardInformation{
        return Gson().fromJson(json, CardInformation::class.java)
    }

}