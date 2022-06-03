package com.example.whatsappclonejetpackfirebase.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.example.whatsappclonejetpackfirebase.domain.model.ContactModel

class ContactRepository(val context: Context) {
    @SuppressLint("Range")
    suspend fun readContacts(): ArrayList<ContactModel>{
        var contacts = arrayListOf<ContactModel>()
        var cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contacts.add(ContactModel(id, name, phoneNumber))
        }
        return contacts
    }
}