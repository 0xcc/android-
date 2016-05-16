package mike.http1;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SQLiteOpActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText txtName;
    EditText txtWeapon;
    EditText txtAttack;
    EditText txt_defence;

    ListView listView;
    Button btn_save;
    Button btn_delete;
    Button btn_update;

    int Hero_ID;


    HerosDB db;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_op);
        try{
            findView();
            setupClick();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void findView(){
         txtName=(EditText)findViewById(R.id.txtName);
         txtWeapon=(EditText)findViewById(R.id.txtWeapon);
         txtAttack=(EditText)findViewById(R.id.txtAttack);
         txt_defence=(EditText)findViewById(R.id.txt_defence);

         listView=(ListView)findViewById(R.id.listView);
          btn_save=(Button)findViewById(R.id.btn_save);
          btn_delete=(Button)findViewById(R.id.btn_delete);
        btn_update=(Button)findViewById(R.id.btn_update);

        this.db=new HerosDB(this);
        this.cursor=db.select();
        this.cursor.moveToFirst();
        listView.setAdapter(new HeroListAdapter(this, cursor));

    }

    private void setupClick(){
        listView.setOnItemClickListener(this);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    add();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

    }

    private void add(){
        String name=txtName.getText().toString();
        String weapon=txtWeapon.getText().toString();
        String attack=txtAttack.getText().toString();
        String defence=txt_defence.getText().toString();

        if (isEmpty(name)|| isEmpty(weapon)|| isEmpty(attack) || isEmpty(defence) ){
            return;
        }
        db.insert(name,weapon,attack,defence);
        cursor.requery();
        cursor.moveToFirst();
        listView.invalidateViews();

        txtName.setText("");
        txtWeapon.setText("");
        txtName.setText("");
        txt_defence.setText("");
        Toast.makeText(SQLiteOpActivity.this,"插入成功",Toast.LENGTH_LONG).show();

    }

    private boolean isEmpty(String value){
        return  (value==null || value=="");
    }

    private void delete(){
        if (Hero_ID==0){
            return;
        }
        db.delete(Hero_ID);
        cursor.requery();
        listView.invalidateViews();

        txtName.setText("");
        txtWeapon.setText("");
        txtName.setText("");
        txt_defence.setText("");
        Toast.makeText(SQLiteOpActivity.this,"删除成功",Toast.LENGTH_LONG).show();


    }

    private void update(){
        String name=txtName.getText().toString();
        String weapon=txtWeapon.getText().toString();
        String attack=txtAttack.getText().toString();
        String defence=txt_defence.getText().toString();

        if (isEmpty(name)|| isEmpty(weapon)|| isEmpty(attack) || isEmpty(defence) ){
            return;
        }
        db.update(Hero_ID, name, weapon, attack, defence);
        cursor.requery();
        listView.invalidateViews();

        txtName.setText("");
        txtWeapon.setText("");
        txtName.setText("");
        txt_defence.setText("");
        Toast.makeText(SQLiteOpActivity.this,"更新成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        Hero_ID=cursor.getInt(0);

        txtName.setText(cursor.getString(1));
        txtWeapon.setText(cursor.getString(2));
        txtAttack.setText(cursor.getString(3));
        txt_defence.setText(cursor.getString(4));
    }
}
