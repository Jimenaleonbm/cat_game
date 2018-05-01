package com.jimenaleon.catgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class InstructionsActivity extends AppCompatActivity {

    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        name = (EditText) findViewById(R.id.name);
    }

    public void iniciar(View view) {

        String textName = name.getText().toString();
        if(TextUtils.isEmpty(name.getText())){
            name.setError("Ingrese un nombre");
        }else{
            Intent intent = new Intent(InstructionsActivity.this, MainActivity.class);
            intent.putExtra("NAME", textName);
            startActivity(intent);
        }

    }
}
