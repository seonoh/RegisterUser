package com.example.registeruser

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.registeruser.DB.DBHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mAdapter:UserListAdapter? =null
    public var mDBHandler:DBHandler = DBHandler(this)
    companion object {
        val REQUEST_ADD_USER = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val newOne = mDBHandler.getUserAllWithCursor()
        if(newOne?.count!=0){
            mAdapter = UserListAdapter(this, newOne)
            user_list.adapter = mAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_ADD_USER->{
                val newOne = mDBHandler.getUserAllWithCursor()
                if(mAdapter==null){
                    mAdapter= UserListAdapter(applicationContext,newOne)
                    user_list.adapter = mAdapter
                }
                mAdapter?.changeCursor(newOne)
                mAdapter?.notifyDataSetInvalidated()
            }
        }
    }

    fun onClickDelete(view: View){
        mDBHandler.deleteUser(view.tag as Long)
        val newOne = mDBHandler.getUserAllWithCursor()
        mAdapter?.changeCursor(newOne)

    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter?.cursor?.close()
        mDBHandler.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){

            R.id.add_user->{
                val intent:Intent = Intent(this, SaveUserActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD_USER)
            }
            R.id.anko->{
                val layout : Intent = Intent(this, AnkoDSLActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
