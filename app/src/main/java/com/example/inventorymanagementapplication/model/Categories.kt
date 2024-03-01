package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// CategoriesState and its arguments with default values:
data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

/* A single category item structure. SerializedName tag is used for defining
a variable name that is used with serialization to json as a key of the
variable value:
 */
data class CategoryItem(
    @SerializedName("category_id")
    val categoryId: Int = 0,
    @SerializedName("category_name")
    val categoryName: String = ""
)