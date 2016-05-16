package mike.http1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class MyDialogActivity extends AppCompatActivity {

    final String [] mItems={"数据结构","Java","Android","算法","数据库","数据挖掘","自然语言处理1","自然语言处理2","自然语言处理3","自然语言处理4","自然语言处理5","自然语言处理6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog);

        //确定取消对话框
        Button btn=(Button)findViewById(R.id.btn_confirm_cancel);
        btn.setOnClickListener(btn_confirm_cancel_on_click);

        //多按钮对话框
        btn=(Button)findViewById(R.id.btn_multi);
        btn.setOnClickListener(btn_multi_on_click);

        //列表框
        btn=(Button)findViewById(R.id.list_dialog);
        btn.setOnClickListener(list_dialog_on_click);

        //进度条框
        btn=(Button)findViewById(R.id.progress_dialog);
        btn.setOnClickListener(progress_dialog_on_click);

        //单选列表框
        btn=(Button)findViewById(R.id.single_choice_dialog);
        btn.setOnClickListener(single_choice_dialog_on_click);

        //多选列表框

        btn=(Button)findViewById(R.id.mulit_choice_dialog);
        btn.setOnClickListener(mulit_choice_dialog_on_click);
    }

    View.OnClickListener btn_confirm_cancel_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new  AlertDialog.Builder(MyDialogActivity.this);
            builder.setIcon(android.R.drawable.btn_star).setTitle("确定取消对话框")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("取消");
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("确定");
                }
            }).create().show();


        }
    };

    View.OnClickListener btn_multi_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new  AlertDialog.Builder(MyDialogActivity.this);
            builder.setIcon(android.R.drawable.btn_star).setTitle("多按钮信息框").setMessage("你喜欢刀疤呀吗？")
                    .setNegativeButton("不喜欢", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("不喜欢");
                        }
                    }).setPositiveButton("喜欢", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("喜欢");
                }
            })
                    .setNeutralButton("一般般", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("一般般");
                        }
                    })
                    .create().show();
        }
    };


    View.OnClickListener list_dialog_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new  AlertDialog.Builder(MyDialogActivity.this);
            builder.setIcon(android.R.drawable.btn_star).setTitle("列表选择框")
                    .setItems(mItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast(which + " : " + mItems[which]);
                        }
                    }).create().show();

        }
    };

    View.OnClickListener progress_dialog_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialog dialog=new ProgressDialog(MyDialogActivity.this);
            dialog.setTitle("进度条对话框");
            dialog.setIcon(android.R.drawable.btn_star);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);

            dialog.setButton("确定1", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("确定1");
                }
            });

            dialog.setButton("确定2", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("确定2");
                }
            });

            dialog.setButton3("确定3", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showToast("确定3");
                }
            });


            new Thread(new Runnable() {
                @Override
                public void run() {
                    int progress=0;
                    while (progress<100){
                        try{
                            Thread.sleep(100);
                            progress++;
                            dialog.incrementProgressBy(1);
                        }catch (Exception er){
                            er.printStackTrace();
                        }

                    }
                }
            }).start();

            try {
                dialog.show();
            }catch (Exception err){
                err.printStackTrace();
            }

        }
    };

    Integer mwhich;
    View.OnClickListener single_choice_dialog_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder=new AlertDialog.Builder(MyDialogActivity.this);
            Dialog dialog=builder.setIcon(android.R.drawable.btn_star).setTitle("单项选择列表框")
                    .setSingleChoiceItems(mItems, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mwhich=Integer.valueOf(which);
                        }
                    }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("ID:"+mwhich+" : "+mItems[mwhich]);
                        }
                    }).create();
            dialog.show();

        }
    };

    Set<Integer> idList=new LinkedHashSet<>();
    //ArrayList<Integer> idList=new ArrayList<>(mItems.length);
    View.OnClickListener mulit_choice_dialog_on_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            idList.clear();
            boolean[] initbool=new boolean[mItems.length];
            for (int i=0;i<initbool.length;i++){
                initbool[i]=false;
            }

            AlertDialog.Builder builder=new AlertDialog.Builder(MyDialogActivity.this);
            Dialog dialog=builder.setIcon(android.R.drawable.btn_star).setTitle("多项项选择列表框")
                    .setMultiChoiceItems(mItems, initbool, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked){
                                    idList.add(which);
                                }else{
                                    idList.remove(which);
                                }
                        }
                    }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = "";
                            Iterator<Integer> iter = idList.iterator();
                            while (iter.hasNext()) {
                                Integer idx = iter.next();
                                result += mItems[idx] + ",";
                            }

                            showToast(result);
                        }
                    }).create();

            try {
                dialog.show();
            }catch (Exception err){
                err.printStackTrace();
            }

        }
    };

    private void showToast(CharSequence txt){
        Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG).show();
    }

}
