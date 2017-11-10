package com.example.registeruser

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.registeruser.DB.DBHandler
import com.example.registeruser.DB.UserInfo
import kotlinx.android.synthetic.main.activity_save_user.*

class SaveUserActivity : AppCompatActivity() {
    val mDBHandler = DBHandler(this)
    val PICK_IMAGE = 1010
    val REQ_PERMISSION = 1011

    var mSelectedImgId:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onClickImage(view: View?){
        val check = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(check != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),REQ_PERMISSION)
        }else{
            val i = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i,PICK_IMAGE)
        }
    }
    fun showSetting()
    {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var notGranted = kotlin.arrayOfNulls<String>(permissions.size)
        when(requestCode){
            REQ_PERMISSION->{
                var index:Int =0
                for(i in 0..permissions.size-1){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        val rationale = ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])
                        if(!rationale){
                            //Show Dialog that explain to grant permission
                            val dialogBuild = AlertDialog.Builder(this).setTitle("권한 설정")
                                    .setMessage("이미지 썸네일을 만들기 위해서 저장권한이 필요합니다. 승인하지 않으면 이미지를 설정할 수 없습니다.")
                                    .setCancelable(true)
                                    .setPositiveButton("설정하러 가기"){
                                        dialog, whichButton -> showSetting()
                                    }
                            dialogBuild.create().show()
                            return

                        }else{
                                notGranted[index++] = permissions[i]
                        }
                        if(notGranted.isNotEmpty()){
                            ActivityCompat.requestPermissions(this, notGranted, REQ_PERMISSION)
                        }



                    }
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PICK_IMAGE->{
                val uri = data?.getData()
                uri?:return

                mSelectedImgId = getImageID(uri)
                if(mSelectedImgId == 1L) return
                val bitmap:Bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver,
                        mSelectedImgId,
                        MediaStore.Images.Thumbnails.MICRO_KIND,null)

                sel_image.setImageBitmap(bitmap)
            }
        }
    }
    fun getImageID(uri:Uri) :Long{
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndex(MediaStore.Images.Media._ID)

        if(column_index == -1) return -1

        cursor.moveToFirst()
        val id = cursor.getLong(column_index)
        cursor.close()
        return id
    }

    fun onClickSaveBtn(view:View){
        val user:UserInfo = UserInfo(edit_name.text.toString(),
                edit_age.text.toString(),
                edit_tel.text.toString(),
                mSelectedImgId.toString())

        mDBHandler.addUser(user)
        mDBHandler.close()
        finish()
    }
}
