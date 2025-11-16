package com.timon.anti_alk_app

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.timon.anti_alk_app.databinding.ActivityMainBinding
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lvTodoList: ListView
    private lateinit var fab: FloatingActionButton
    private lateinit var todoItems: ArrayList<String>
    private lateinit var itemAdapter: ArrayAdapter<String>

    override fun onStop() {
        super.onStop()
        val prefs = getSharedPreferences("einkaufsliste", MODE_PRIVATE)
        prefs.edit {
            putStringSet("todoItems", todoItems.toSet())
            apply()
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences("einkaufsliste", MODE_PRIVATE)
        val saved = prefs.getStringSet("todoItems", null)

        val loaded = saved?.toMutableList() ?: mutableListOf()

        todoItems.clear()
        todoItems.addAll(loaded)
        itemAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lvTodoList = findViewById(R.id.LvTodoList)
        fab = findViewById(R.id.floatingActionButton)

        todoItems = arrayListOf()
        itemAdapter  = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            todoItems)
        lvTodoList.adapter = itemAdapter

        fab.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Hinzufügen")

            var input = EditText(this)
            input.hint = "Text eingeben"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                if (input.text.toString().isEmpty()){
                    Toast.makeText(applicationContext, "Aufgabe kann nicht leer sein",
                        Toast.LENGTH_SHORT).show()
                }else {
                    todoItems.add(input.text.toString())
                    itemAdapter.notifyDataSetChanged()
                }
            }

            builder.setNegativeButton("Abbrechen") { _, _ ->
                Toast.makeText(applicationContext, "Abgebrochen", Toast
                    .LENGTH_SHORT).show()
            }
            builder.show()
        }

        lvTodoList.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, pos, id ->
            todoItems.removeAt(pos)
            itemAdapter.notifyDataSetChanged()
            Toast.makeText(applicationContext, "Aufgabe Erledigt", Toast
                .LENGTH_SHORT).show()
            true
        }
    }
}