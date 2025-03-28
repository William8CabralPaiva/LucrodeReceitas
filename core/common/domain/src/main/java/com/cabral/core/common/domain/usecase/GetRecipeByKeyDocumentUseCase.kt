package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipeByKeyDocumentUseCase(private val repository: RecipeRepository) {
    operator fun invoke(keyDocument: String): Flow<Recipe> {
        return repository.getRecipeByKeyDocument(keyDocument)
    }
}