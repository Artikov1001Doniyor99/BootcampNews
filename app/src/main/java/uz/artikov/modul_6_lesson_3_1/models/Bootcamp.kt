package uz.artikov.modul_6_lesson_3_1.models

import java.io.Serializable

class Bootcamp : Serializable {

    var id: Int? = null
    var type: String? = null
    var name: String? = null
    var description: String? = null


    constructor(id: Int?, type: String?, name: String?, description: String?) {
        this.id = id
        this.type = type
        this.name = name
        this.description = description
    }

    constructor(type: String?, name: String?, description: String?) {
        this.type = type
        this.name = name
        this.description = description
    }

    constructor()
}