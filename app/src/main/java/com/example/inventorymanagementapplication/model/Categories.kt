package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// CategoriesState and its arguments with default values:
data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)


/* CategoriesRes and its argument with a default value. The API method
getCategories inherits this data class and the categories argument of
this structure is set as a value of the list argument in an instance created
from CategoriesState:
 */
data class CategoriesRes(val categories: List<CategoryItem> = emptyList())


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
