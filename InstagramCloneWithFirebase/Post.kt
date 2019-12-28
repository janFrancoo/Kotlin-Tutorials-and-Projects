package com.janfranco.kotlinfirebaseinsta

class Post {

    var email : String
    var comment : String
    var downloadUrl : String

    constructor(email : String, comment : String, downloadUrl : String) {
        this.email = email
        this.comment = comment
        this.downloadUrl = downloadUrl
    }

}
