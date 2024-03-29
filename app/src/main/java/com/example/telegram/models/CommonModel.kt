package com.example.telegram.models

data class CommonModel(
    val id:String = "",
    var username:String = "",
    var bio:String = "",
    var fullname:String = "",
    var phone:String = "",
    var state:String = "",
    var photoUrl:String = "empty",

    var text:String = "",
    var type:String = "",
    var from:String = "",
    var timeStamp:Any = "",
    var fileUrl:String = "empty"


) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}

