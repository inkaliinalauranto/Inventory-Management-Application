package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// CategoriesState and its arguments with default values:
data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)


// CategoryState and its arguments with default values:
data class CategoryState(
    val categoryName: String = "",
    val loading: Boolean = false,
    val error: String? = null
)


/* CategoriesRes and its argument with a default value representing a
response from the API method getCategories. The categories argument
contains a list of CategoryItems.
 */
data class CategoriesRes(val categories: List<CategoryItem> = emptyList())


/* CategoryRes and its argument with a default value. The API method
getCategoryById returns a response that is in the format of this data
class. The category argument of this structure is in the form of the
CategoryItem data class object.
 */
data class CategoryRes(val category: CategoryItem)


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


/* UpdateCategoryReq and its argument with a default value. The API
method editCategory returns a response that is in the format of this data
class. The categoryName argument of this structure is in the form of
string.
 */
data class UpdateCategoryReq(
    @SerializedName("category_name")
    val categoryName: String = ""
)