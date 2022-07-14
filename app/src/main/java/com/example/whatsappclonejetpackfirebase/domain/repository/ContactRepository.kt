package com.example.whatsappclonejetpackfirebase.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.example.whatsappclonejetpackfirebase.domain.model.ContactModel

class ContactRepository(val context: Context) {
    @SuppressLint("Range")
    suspend fun readContacts(): Map<String, String>{
        var contacts = mutableMapOf<String, String>()
        var cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber: String = "+62" + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).drop(1)
            contacts.put(phoneNumber, name)
        }
        return contacts
    }

    @SuppressLint("Range")
    suspend fun readContactsPhoneNumber(): ArrayList<String> {
        var contactsPhoneNumber = arrayListOf<String>()
        var cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val phoneNumber: String = "+62" + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).drop(1)
            contactsPhoneNumber.add(phoneNumber)
        }
        return contactsPhoneNumber
    }
}