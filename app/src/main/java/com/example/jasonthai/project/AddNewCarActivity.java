package com.example.jasonthai.project;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;

public class AddNewCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
    }


    public void buttonOnClick(View v) {
        //String writeText = "/*\n" + findViewById(R.id.editText) + "\n" + findViewById(R.id.editText2) +
        //        "\n" + findViewById(R.id.editText3) + findViewById(R.id.editText4) + "\n" +
        //        findViewById(R.id.editText5) + "\n" + findViewById(R.id.editText6) + "\n" + "*/";
        EditText mEdit, mEdit1, mEdit2, mEdit3, mEdit4, mEdit5, mEdit6;
        mEdit = (EditText)findViewById(R.id.editText);
        mEdit2 = (EditText)findViewById(R.id.editText2);
        mEdit3 = (EditText)findViewById(R.id.editText3);
        mEdit4 = (EditText)findViewById(R.id.editText4);
        mEdit5 = (EditText)findViewById(R.id.editText5);
        mEdit6 = (EditText)findViewById(R.id.editText6);
        String writeText = "/*\n" + mEdit.getText().toString() + "\n" +
                mEdit2.getText().toString() + "\n" +
                mEdit3.getText().toString() + "\n" +
                mEdit4.getText().toString() + "\n" +
                mEdit5.getText().toString() + "\n" +
                mEdit6.getText().toString() + "\n*/";
        //if(validate(writeText))
        writeToFile(writeText, AddNewCarActivity.this);
        finish();
    }

    public boolean validate(String validateText) {
        //check values of entries
        return true;
    }

    private void writeToFile(String data,Context context) {
        if(isExternalStorageWritable()) {
            String filename = "myfile.txt";
            FileOutputStream outputStream;
            try {
                //File file = new File(context.getFilesDir(), filename);
                outputStream = openFileOutput(filename, context.MODE_PRIVATE);
                outputStream.write(data.getBytes());
                outputStream.close();
            }  catch (Exception e) {
                e.printStackTrace();
            }
            //File file;
            //file = new File(getExternalFilesDir(null),filename);
            /*try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_WORLD_WRITEABLE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }*/
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
