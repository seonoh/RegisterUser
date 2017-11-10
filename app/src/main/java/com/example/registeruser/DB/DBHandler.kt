package com.example.registeruser.DB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable

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

    object UserTable{
        val TABLE_NAME = "user"
        val ID = "_id"
        val NAME = "name"
        val AGE = "age"
        val TELNUM = "telnum"
        val PIC_PATH = "pic_path"
    }



//    val TABLE_CREATE =   "CREATE TABLE if not exists " + TABLE_NAME+
//                         "("+"${ID} integer PRIMARY KEY,t, ${NAME} text,"+
//                         "${AGE} text, ${TELNUM} text, ${PIC_PATH} text"+ ")"

    fun getUserAllWithCursor():Cursor{
        return readableDatabase.query(UserTable.TABLE_NAME, arrayOf
        (UserTable.ID,UserTable.NAME,UserTable.AGE,UserTable.TELNUM,UserTable.PIC_PATH),
                null,null,null,null,null)
    }

    fun addUser(user:UserInfo){
        var info = ContentValues()
        info.put(UserTable.NAME, user.name)
        info.put(UserTable.AGE, user.age)
        info.put(UserTable.TELNUM, user.TelNum)
        info.put(UserTable.PIC_PATH, user.pic_path)

        writableDatabase.use {  //Anko use 함수 사용
                                //DB를 사용할 동안 블록을 걸어주고, DB를 사용하는 구간이 끝나고 난 다음 DB를 닫아줌
            writableDatabase.insert(UserTable.TABLE_NAME,null,info)
        }

    }

    fun deleteUser(id:Long) {
        writableDatabase.execSQL("DELETE FROM ${UserTable.TABLE_NAME} WHERE ${UserTable.ID} = ${id};");
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(UserTable.TABLE_NAME,true,
                Pair(UserTable.ID, INTEGER+PRIMARY_KEY),
                Pair(UserTable.NAME, TEXT),
                Pair(UserTable.AGE, TEXT),
                Pair(UserTable.TELNUM, TEXT),
                Pair(UserTable.PIC_PATH,TEXT))

//        db?.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

