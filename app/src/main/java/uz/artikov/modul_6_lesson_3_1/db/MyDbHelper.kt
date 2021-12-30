package uz.artikov.modul_6_lesson_3_1.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp
import uz.artikov.modul_6_lesson_3_1.utils.Constant

class MyDbHelper(context: Context) :SQLiteOpenHelper(context,Constant.DATABASE_NAME,null,Constant.DATABASE_VERSION),DataBaseService {
    override fun onCreate(p0: SQLiteDatabase?) {
        var query="create table ${Constant.TABLE_NAME}  (${Constant.ID} integer not null primary key autoincrement unique, ${Constant.TYPE} text not null, ${Constant.NAME} text not null, ${Constant.DESCRIPTION} text not null)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    override fun addBasic(bootcamp: Bootcamp) {
        val database = this.writableDatabase
        var contentValues= ContentValues()
        contentValues.put(Constant.TYPE,bootcamp.type)
        contentValues.put(Constant.NAME,bootcamp.name)
        contentValues.put(Constant.DESCRIPTION,bootcamp.description)
        database.insert(Constant.TABLE_NAME,null,contentValues)
    }

    override fun deleteBasic(bootcamp: Bootcamp) {
        val database = this.writableDatabase
        database.delete(Constant.TABLE_NAME,"${Constant.ID}=?", arrayOf("${bootcamp.id}"))
    }

    override fun updateBasic(bootcamp: Bootcamp): Int {
        val database = this.writableDatabase
        var contentValues=ContentValues()
        contentValues.put(Constant.ID,bootcamp.id)
        contentValues.put(Constant.TYPE,bootcamp.type)
        contentValues.put(Constant.NAME,bootcamp.name)
        contentValues.put(Constant.DESCRIPTION,bootcamp.description)
        return database.update(Constant.TABLE_NAME,contentValues,"${Constant.ID}=?", arrayOf("${bootcamp.id}"))
    }

    override fun getBasicById(id: Int): Bootcamp {

        val database = this.readableDatabase
        val cursor=database.query(Constant.TABLE_NAME, arrayOf(Constant.ID,Constant.TYPE,Constant.NAME,Constant.DESCRIPTION),"${Constant.ID}=?",
            arrayOf("$id"),null,null,null)
        cursor?.moveToFirst()
        return Bootcamp(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3))

    }

    override fun getAllBasic(): ArrayList<Bootcamp> {

        var list=ArrayList<Bootcamp>()
        val query="select * from ${Constant.TABLE_NAME}"
        var database = this.readableDatabase
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(0)
                val type = cursor.getString(1)
                val name = cursor.getString(2)
                val description = cursor.getString(3)
                val basic = Bootcamp(id, type, name, description)
                list.add(basic)
            }while (cursor.moveToNext())
        }
        return list

    }
}