package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// CategoriesState and its properties with default values:
data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)


// CategoryState and its properties with default values:
data class CategoryState(
    val categoryName: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    /* The value transform of this property is used in LaunchedEffect of
    the CategoryEditScreen component.
    */
    val done: Boolean = false
)


// A data class for saving the id of the category to be removed:
data class CategoryDeleteState(
    val id: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
)


/* CategoriesRes and its property with a default value representing a
response from the API method getCategories. The categories property
contains a list of CategoryItems.
 */
data class CategoriesRes(val categories: List<CategoryItem> = emptyList())


/* CategoryRes and its property with a default value. The API method
getCategoryById returns a response that is in the format of this data
class. The category property of this structure is in the form of the
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

/* Although the structures of AddCategoryReq and UpdateCategoryReq data
classes are the same, they should not be combined into a single generic
data class due to semantic typing principles. Since CategoriesApi interface
has dedicated methods for POST and PUT requests with different
functionalities, separate data models are created.
*/
data class AddCategoryReq(
    @SerializedName("category_name")
    val categoryName: String = ""
)


/* UpdateCategoryReq data class represents the request body format for the
editCategory API method. It includes the categoryName field, which is
serialized as "category_name". The default value for categoryName is an
empty string.
 */
data class UpdateCategoryReq(
    @SerializedName("category_name")
    val categoryName: String = ""
)


// RentalItemsState and its properties with default values:
data class RentalItemsState(
    val list: List<RentalItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)


/* A single rental item structure. SerializedName tag is used for defining
a variable name that is used with serialization to json and deserialization
from json as a key of the variable value:
 */
data class RentalItem(
    @SerializedName("rental_item_id")
    val rentalItemId: Int = 0,
    @SerializedName("rental_item_name")
    val rentalItemName: String = ""
)


// A data class for saving the id of the rental item to be removed:
data class RentalItemDeleteState(
    val id: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
)


/* Represents a response from the API endpoint getRentalItemsByCategoryId.
Contains a list of rental items retrieved from the API response. The
items property must correspond to the key in the API response containing
the list of rental items.
 */
data class RentalItemsRes(val items: List<RentalItem> = emptyList())