package com.example.inventorymanagementapplication.model

import com.google.gson.annotations.SerializedName

// RentalItemState and its properties with default values:
data class RentalItemState(
    /* categoryId is needed, when navigation back from RentalItemAddScreen
    to RentalItemsScreen. categoryId property defines the parent category
    of RentalItemsScreen.
     */
    val categoryId: Int = 0,
    val rentalItemName: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    /* The transform of this property's value is used in LaunchedEffect in
    the RentalItemEditScreen composable.
    */
    val done: Boolean = false
)

/* Data class used in RentalItemsScreen to specify the rental item to be
edited.
*/
data class RentalItemCategoryState(
    val categoryId: Int = 0,
    val rentalItemId: Int = 0
)

// A data class for saving the id of the rental item to be removed:
data class RentalItemDeleteState(
    val id: Int = 0,
    val error: String? = null,
    val loading: Boolean = false
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

// RentalItemsState and its properties with default values:
data class RentalItemsState(
    val categoryId: Int = 0,
    val list: List<RentalItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

/* UpdateRentalItemReq data class represents the request body format for the
editRentalItem API method. It includes rentalItemName property, which is
serialized as "rental_item_name". The default value for the rentalItemName is
an empty string.
 */
data class UpdateRentalItemReq(
    @SerializedName("rental_item_name")
    val rentalItemName: String = ""
)

/* AddRentalItemReq data class represents the request body format for the
addRentalItem API method.
 */
data class AddRentalItemReq(
    @SerializedName("rental_item_name")
    val rentalItemName: String = "",
    @SerializedName("created_by_user_id")
    val createdByUserId: Int = 0
)

/* RentalItemRes and its property with a default value. The API method
getRentalItemById returns a response that is in the format of this data
class. The rentalItem property of this structure is in the form of the
RentalItem data class object.
 */
data class RentalItemRes(
    @SerializedName("rental_item_id")
    val rentalItemId: Int = 0,
    @SerializedName("rental_item_name")
    val rentalItemName: String = "",
)

/* Represents a response from the API endpoint getRentalItemsByCategoryId.
Contains a list of rental items retrieved from the API response. The
items property must correspond to the key in the API response containing
the list of rental items.
 */
data class RentalItemsRes(val items: List<RentalItem> = emptyList())

/* Represents a response from the addRentalItem API method. Contains a
RentalItem object retrieved from the API response.
 */
data class AddRentalItemRes(val rentalItem: RentalItem)