package com.example.registeruser.DB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by 선오 on 2017-11-09.
 */
data class UserInfo(val name:String = "No Name",
                    val age:String = "0",
                    val TelNum:String = "No TelNum",
                    val pic_path:String)


class DBHandler(context: Context) : SQLiteOpenHelper(context,DB_Name,null,DB_Version) {
    //CursorFactory는 안드로이드에서 제공하는 기본 cursor 클래스를 사용한다는 의미로 null을 주었다.

    companion object {
        val DB_Name = "user.db"
        val DB_Version=1;
    }

    val TABLE_NAME = "user"
    val ID = "_id"
    val NAME = "name"
    val AGE = "age"
    val TELNUM = "telnum"
    val PIC_PATH = "pic_path"

    val TABLE_CREATE =   "CREATE TABLE if not exists " + TABLE_NAME+
                         "("+"${ID} integer PRIMARY KEY,t, ${NAME} text,"+
                         "${AGE} text, ${TELNUM} text, ${PIC_PATH} text"+ ")"

    fun getUserAllWithCursor():Cursor{
        return readableDatabase.query(TABLE_NAME, arrayOf(ID,NAME,AGE,TELNUM,PIC_PATH),
                null,null,null,null,null)
    }

    fun addUser(user:UserInfo){
        var info = ContentValues()
        info.put(NAME, user.name)
        info.put(AGE, user.age)
        info.put(TELNUM, user.TelNum)
        info.put(PIC_PATH, user.pic_path)
        writableDatabase.insert(TABLE_NAME,null,info)
    }

    fun deleteUser(id:Long) {
        writableDatabase.execSQL("DELETE FROM ${TABLE_NAME} WHERE ${ID} = ${id};");
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

