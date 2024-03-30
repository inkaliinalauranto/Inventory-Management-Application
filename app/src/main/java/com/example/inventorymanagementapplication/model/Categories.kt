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
    val error: String? = null,
    /* The value transform of this argument is used in LaunchedEffect of
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


// CategoryItemsState and its arguments with default values:
data class ItemsState(
    val list: List<Item> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)


/* A single item structure. SerializedName tag is used for defining
a variable name that is used with serialization to json as a key of the
variable value:
 */
data class Item(
    @SerializedName("rental_item_id")
    val itemId: Int = 0,
    @SerializedName("rental_item_name")
    val itemName: String = ""
)


// A data class for saving the id of the item to be removed:
data class ItemDeleteState(
    val id: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
)


/* ItemsRes and its argument with a default value representing a
response from the API method getItemsByCategoryId. The items argument
contains a list of objects in the form of Item data class.
 */
data class ItemsRes(val items: List<Item> = emptyList())