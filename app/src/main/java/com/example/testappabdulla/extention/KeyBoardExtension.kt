package com.example.testappabdulla.extention

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun View.showKeyboard() = (this.context as? AppCompatActivity)?.showKeyboard()
fun View.hideKeyboard() = (this.context as? AppCompatActivity)?.hideKeyboard()

fun Fragment.showKeyboard() = activity?.let(FragmentActivity::showKeyboard)
fun Fragment.hideKeyboard() = activity?.hideKeyboard()

fun Context.showKeyboard() = (this as? AppCompatActivity)?.showKeyboard()
fun Context.hideKeyboard() = (this as? AppCompatActivity)?.hideKeyboard()

fun AppCompatActivity.showKeyboard() = WindowInsetsControllerCompat(window, window.decorView).show(
    WindowInsetsCompat.Type.ime())
fun AppCompatActivity.hideKeyboard() = WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())