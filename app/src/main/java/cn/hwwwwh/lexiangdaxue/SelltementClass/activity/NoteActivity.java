package cn.hwwwwh.lexiangdaxue.SelltementClass.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.hwwwwh.lexiangdaxue.R;
import me.majiajie.swipeback.SwipeBackActivity;

public class NoteActivity extends SwipeBackActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private EditText note_edit;
    private TextView note_num;
    private TextView note1;
    private TextView note2;
    private TextView note3;
    private TextView note4;
    private TextView note5;
    private TextView note6;
    private Button note_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
        intiView();
        toolbar.setTitle("添加备注");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setTitleTextAppearance(this, R.style.TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        note_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note_num.setText(note_edit.getText().length()+"/100");
                if(note_edit.getText().length()==100){
                    Toast.makeText(NoteActivity.this,"字符数已满100字",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SharedPreferences pref=getSharedPreferences("SettlementData",MODE_PRIVATE);
        String noteContent=pref.getString("note","");
        if(noteContent.length()!=0){
            note_edit.append(noteContent);
        }
    }

    private void intiView(){
        toolbar=(Toolbar)findViewById(R.id.toolbar_note);
        note_edit=(EditText)findViewById(R.id.note_edit);
        note_num=(TextView)findViewById(R.id.note_num);
        note1=(TextView)findViewById(R.id.note1);
        note2=(TextView)findViewById(R.id.note2);
        note3=(TextView)findViewById(R.id.note3);
        note4=(TextView)findViewById(R.id.note4);
        note5=(TextView)findViewById(R.id.note5);
        note6=(TextView)findViewById(R.id.note6);
        note1.setOnClickListener(this);
        note2.setOnClickListener(this);
        note3.setOnClickListener(this);
        note4.setOnClickListener(this);
        note5.setOnClickListener(this);
        note6.setOnClickListener(this);
        note_finish=(Button)findViewById(R.id.note_finish);
        note_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.note1:
                note_edit.append(note1.getText().toString()+" ");
                break;
            case R.id.note2:
                note_edit.append(note2.getText().toString()+" ");
                break;
            case R.id.note3:
                note_edit.append(note3.getText().toString()+" ");
                break;
            case R.id.note4:
                note_edit.append(note4.getText().toString()+" ");
                break;
            case R.id.note5:
                note_edit.append(note5.getText().toString()+" ");
                break;
            case R.id.note6:
                note_edit.append(note6.getText().toString()+" ");
                break;
            case R.id.note_finish:
                SharedPreferences.Editor editor=getSharedPreferences("SettlementData",MODE_PRIVATE).edit();
                editor.putString("note",note_edit.getText().toString());
                editor.commit();
                setResult(RESULT_OK, null);
                finish();
                break;
        }
    }
}
