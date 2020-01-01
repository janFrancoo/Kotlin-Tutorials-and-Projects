package com.janfranco.kotlinlistview

class Landmark {

    var imgId : Int? = null
    var name: String

    constructor(name: String, imdId: Int) {
        this.name = name
        this.imgId = imdId
    }

    override fun toString(): String {
        return name
    }

}
