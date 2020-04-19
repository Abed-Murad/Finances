package serg.chuprin.finances.core.impl.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import serg.chuprin.finances.core.api.domain.model.DataPeriodType
import serg.chuprin.finances.core.impl.data.datasource.database.firebase.contract.FirebaseUserFieldsContract
import serg.chuprin.finances.core.impl.data.datasource.database.firebase.contract.FirebaseUserFieldsContract.FIELD_DATA_PERIOD_TYPE
import java.util.*
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 17.04.2020.
 */
internal class DataPeriodTypeMapper @Inject constructor() :
    ModelMapper<DocumentSnapshot, DataPeriodType> {

    override fun invoke(model: DocumentSnapshot): DataPeriodType? {
        return when (model.getString(FIELD_DATA_PERIOD_TYPE)?.toLowerCase(Locale.ROOT)) {
            FirebaseUserFieldsContract.DATA_PERIOD_TYPE_MONTH -> DataPeriodType.MONTH
            else -> null
        }
    }

}