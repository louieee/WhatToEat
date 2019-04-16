package com.wordpress.louieefitness.whattoeat2.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    val getAllFood: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("Select * from $TABLE_NAME ORDER BY $ID DESC", null)
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IMAGE + " TEXT NOT NULL," +
                NAME + " TEXT NOT NULL," +
                DESC + " TEXT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    @Throws(SQLException::class)
    fun insertData(Food_picture: String, Food_Name: String, About_Food: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(IMAGE, Food_picture)
        contentValues.put(NAME, Food_Name)
        contentValues.put(DESC, About_Food)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()

    }

    fun maxFoodID(): Int {
        var bb: Int
        try {
            val db = this.writableDatabase
            val b = db.query(TABLE_NAME, arrayOf("MAX($ID) AS MAX"), null, null, null, null, ID)
            b.moveToPosition(b.count - 1)
            val index = b.getColumnIndex("MAX")
            val data = b.getString(index)
            bb = Integer.parseInt(data)
            b.close()
            return bb
        } catch (E: NumberFormatException) {
            E.printStackTrace()
        }

        return 0
    }

    fun updateData(id: String, Food_picture: String, Food_Name: String, About_Food: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(IMAGE, Food_picture)
        contentValues.put(NAME, Food_Name)
        contentValues.put(DESC, About_Food)
        val result = db.update(TABLE_NAME, contentValues, "$ID=?", arrayOf(id))
        return result > 0

    }

    fun deleteFood(id: String): Int? {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$ID=?", arrayOf(id))
    }

    fun tableExist(): Int {
        val DB = this.writableDatabase
        val cursor = DB.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun inDatabase(value: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT " + value + " FROM " + TABLE_NAME +
                " WHERE " + NAME + " = " + value, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    companion object {
        private const val DATABASE_NAME = "Food.db"
        private const val TABLE_NAME = "My_Food"
        const val ID = "ID"
        const val IMAGE = "Image"
        const val NAME = "Name"
        const val DESC = "Description"
    }
}
