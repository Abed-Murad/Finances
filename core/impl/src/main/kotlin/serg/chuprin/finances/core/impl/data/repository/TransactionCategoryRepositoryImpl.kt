package serg.chuprin.finances.core.impl.data.repository

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategoryType
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategoryWithParent
import serg.chuprin.finances.core.api.domain.model.category.query.TransactionCategoriesQuery
import serg.chuprin.finances.core.api.domain.model.category.query.result.CategoriesQueryResult
import serg.chuprin.finances.core.api.domain.repository.TransactionCategoryRepository
import serg.chuprin.finances.core.impl.data.TransactionCategoryLinker
import serg.chuprin.finances.core.impl.data.datasource.assets.PredefinedTransactionCategoriesDataSource
import serg.chuprin.finances.core.impl.data.datasource.assets.TransactionCategoryAssetDto
import serg.chuprin.finances.core.impl.data.datasource.firebase.FirebaseTransactionCategoryDataSource
import serg.chuprin.finances.core.impl.data.mapper.category.FirebaseTransactionCategoryMapper
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 19.04.2020.
 */
internal class TransactionCategoryRepositoryImpl @Inject constructor(
    private val mapper: FirebaseTransactionCategoryMapper,
    private val categoryLinker: TransactionCategoryLinker,
    private val firebaseDataSource: FirebaseTransactionCategoryDataSource,
    private val predefinedCategoriesDataSource: PredefinedTransactionCategoriesDataSource
) : TransactionCategoryRepository {

    override suspend fun deleteCategories(categories: List<TransactionCategory>) {
        firebaseDataSource.deleteCategories(categories)
    }

    override fun categoriesFlow(query: TransactionCategoriesQuery): Flow<CategoriesQueryResult> {
        return firebaseDataSource
            .categoriesFlow(query)
            .map { documents ->
                CategoriesQueryResult(
                    documents.mapNotNull(mapper::mapFromSnapshot).linkWithParents()
                )
            }
    }

    override suspend fun createPredefinedCategories(ownerId: Id) {
        coroutineScope {
            val allCategories = predefinedCategoriesDataSource.getCategories().run {
                (expenseCategories + incomeCategories).mapNotNull { dto -> dto.map(ownerId) }
            }
            firebaseDataSource.createCategories(allCategories)
        }
    }

    private fun TransactionCategoryAssetDto.map(ownerId: Id): TransactionCategory? {
        val type = if (isIncome) {
            TransactionCategoryType.INCOME
        } else {
            TransactionCategoryType.EXPENSE
        }
        return TransactionCategory.create(
            id = id,
            type = type,
            name = name,
            colorHex = colorHex,
            ownerId = ownerId.value,
            parentCategoryId = parentCategoryId
        )
    }

    private fun List<TransactionCategory>.linkWithParents(): Map<Id, TransactionCategoryWithParent> {
        return categoryLinker.linkWithParents(this)
    }

}