package uz.artikov.modul_6_lesson_3_1.db

import uz.artikov.modul_6_lesson_3_1.models.Bootcamp

interface DataBaseService {

    fun addBasic(bootcamp: Bootcamp)
    fun deleteBasic(bootcamp: Bootcamp)
    fun updateBasic(bootcamp: Bootcamp):Int
    fun getBasicById(id:Int):Bootcamp
    fun getAllBasic():ArrayList<Bootcamp>

}