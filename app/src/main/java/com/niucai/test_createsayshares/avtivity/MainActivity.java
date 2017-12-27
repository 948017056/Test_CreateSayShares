package com.niucai.test_createsayshares.avtivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.niucai.test_createsayshares.CreateSaySharesAdapter;
import com.niucai.test_createsayshares.R;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_create_title;
    private ImageView iv_create_back;
    private FrameLayout fl_create_head;
    private EditText et_create_title;
    private RadioButton rb_Btn_Notfree;
    private RadioButton rb_Btn_ThreeDaysFree;
    private RadioButton rb_Btn_SevenDaysFree;
    private RadioGroup rg_Create_RadioGroup;
    private EditText et_create_synopsis;
    private EditText et_create_synopsisinfo;
    private TextView tv_create_chapter;
    private Spinner spn_chapter;
    private TextView tv_create_edit;
    private TextView tv_create_delete;
    private ListView lv_createListView;
    private TextView tv_create_addtitle;
    private Button btn_release;
    private Button btn_save;

    private List<Integer> addTitleList = new LinkedList<>();
    private CreateSaySharesAdapter createSaySharesAdapter;
    private int count = 0;
    private List<String> unPullMenuList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 软键盘弹出时把布局顶上去，控件乱套解决方法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);


        initView();
        initData();
        initAdapter();
        initListener();

    }

    private void initListener() {
        spn_chapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+unPullMenuList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_create_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unPullMenuList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spn_chapter.setAdapter(adapter);

    }

    private void initData() {
        tv_create_addtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                addTitleList.add(count);
                createSaySharesAdapter.notifyDataSetChanged();

            }
        });

        for (int i = 0; i < 5; i++) {
            unPullMenuList.add(""+i);
        }
    }

    private void initView() {
        tv_create_title = (TextView) findViewById(R.id.tv_create_title);
        iv_create_back = (ImageView) findViewById(R.id.iv_create_back);
        fl_create_head = (FrameLayout) findViewById(R.id.fl_create_head);
        et_create_title = (EditText) findViewById(R.id.et_create_title);
        rb_Btn_Notfree = (RadioButton) findViewById(R.id.rb_Btn_Notfree);
        rb_Btn_ThreeDaysFree = (RadioButton) findViewById(R.id.rb_Btn_ThreeDaysFree);
        rb_Btn_SevenDaysFree = (RadioButton) findViewById(R.id.rb_Btn_SevenDaysFree);
        rg_Create_RadioGroup = (RadioGroup) findViewById(R.id.rg_Create_RadioGroup);
        et_create_synopsis = (EditText) findViewById(R.id.et_create_synopsis);
        et_create_synopsisinfo = (EditText) findViewById(R.id.et_create_synopsisinfo);
        tv_create_chapter = (TextView) findViewById(R.id.tv_create_chapter);
        spn_chapter = (Spinner) findViewById(R.id.spn_chapter);
        tv_create_edit = (TextView) findViewById(R.id.tv_create_edit);
        tv_create_delete = (TextView) findViewById(R.id.tv_create_delete);
        lv_createListView = (ListView) findViewById(R.id.lv_createListView);
        tv_create_addtitle = (TextView) findViewById(R.id.tv_create_addtitle);
        btn_release = (Button) findViewById(R.id.btn_release);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_release.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        createSaySharesAdapter = new CreateSaySharesAdapter(addTitleList, this);
        lv_createListView.setAdapter(createSaySharesAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_release:

                break;
            case R.id.btn_save:

                break;
        }
    }

    private void submit() {
        // validate
        String title = et_create_title.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "title不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String synopsis = et_create_synopsis.getText().toString().trim();
        if (TextUtils.isEmpty(synopsis)) {
            Toast.makeText(this, "synopsis不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String synopsisinfo = et_create_synopsisinfo.getText().toString().trim();
        if (TextUtils.isEmpty(synopsisinfo)) {
            Toast.makeText(this, "synopsisinfo不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
