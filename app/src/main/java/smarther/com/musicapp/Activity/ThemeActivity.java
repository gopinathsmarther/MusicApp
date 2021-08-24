package smarther.com.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.ThemeAdapter;
import smarther.com.musicapp.Model.ThemeObject;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.MusicIntentReceiver;
import smarther.com.musicapp.Utils.MusicUtils;
import smarther.com.musicapp.Utils.SessionManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends AppCompatActivity {
RecyclerView recycler_view_themes;
List<ThemeObject>themeObjectList =new ArrayList<>();
public static  LinearLayout dummy_theme;
public static  LinearLayout img_theme_bg;
    RelativeLayout filemanager_selection;
    public static SessionManager sessionManager;
    ImageView proceed_with_selected_theme;
    ImageView close_button;
    public static int permission = 1;
    public static int proceed_permission = 2;
    public MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MusicIntentReceiver();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
        setContentView(R.layout.activity_theme);

        dummy_theme = (LinearLayout) findViewById(R.id.dummy_theme);
        img_theme_bg = (LinearLayout) findViewById(R.id.img_theme_bg);
        filemanager_selection = (RelativeLayout) findViewById(R.id.filemanager_selection);
        proceed_with_selected_theme = (ImageView) findViewById(R.id.proceed_with_selected_theme);
        close_button = (ImageView) findViewById(R.id.close_button);
        recycler_view_themes = (RecyclerView) findViewById(R.id.recycler_view_themes);
        recycler_view_themes.setLayoutManager(new GridLayoutManager(ThemeActivity.this, 1, LinearLayoutManager.HORIZONTAL, false));
        recycler_view_themes.setHasFixedSize(true);
        recycler_view_themes.setNestedScrollingEnabled(false);
        themeObjectList=new ArrayList<>();
        sessionManager=new SessionManager(ThemeActivity.this);
        if (MusicUtils.isMarshmallow()) {
            themeObjectList.add(new ThemeObject(R.drawable.theme_1));
            themeObjectList.add(new ThemeObject(R.drawable.theme_2));
            themeObjectList.add(new ThemeObject(R.drawable.theme_3));
            themeObjectList.add(new ThemeObject(R.drawable.theme_4));
            themeObjectList.add(new ThemeObject(R.drawable.theme_5));
//            themeObjectList.add(new ThemeObject(R.drawable.theme5));
//            themeObjectList.add(new ThemeObject(R.drawable.theme6));
//            themeObjectList.add(new ThemeObject(R.drawable.theme7));
        } else {
            themeObjectList.add(new ThemeObject(R.drawable.theme_1));
            themeObjectList.add(new ThemeObject(R.drawable.theme_2));
            themeObjectList.add(new ThemeObject(R.drawable.theme_3));
            themeObjectList.add(new ThemeObject(R.drawable.theme_4));
            themeObjectList.add(new ThemeObject(R.drawable.theme_5));
//            themeObjectList.add(new ThemeObject(R.drawable.theme5));
        }
        ThemeAdapter themeAdapter=new ThemeAdapter(ThemeActivity.this,themeObjectList,ThemeActivity.this);
        recycler_view_themes.setAdapter(themeAdapter);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        filemanager_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aji proceed with your theme");
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                    if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(ThemeActivity.this,
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                        } else {
//
//                            ActivityCompat.requestPermissions(ThemeActivity.this,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
//                        }
//                    } else {
//                        ActivityCompat.requestPermissions(ThemeActivity.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
//
//                    }
//                }
//                else
//                {
//                    if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(ThemeActivity.this,
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                        } else {
//
//                            ActivityCompat.requestPermissions(ThemeActivity.this,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
//                        }
//                    } else {
//                        ActivityCompat.requestPermissions(ThemeActivity.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
//
//                    }
//                }

//                TedPermission.with(ThemeActivity.this)
//                        .setPermissionListener(permissionlistener_pick_image)
//                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();
                if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ThemeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
                }
                if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ThemeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permission);
                }

            }
        });

        proceed_with_selected_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aji proceed set tick");
//                TedPermission.with(ThemeActivity.this)
//                        .setPermissionListener(permissionlistener)
//                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();

//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                    if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(ThemeActivity.this,
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                        } else {
//
//                            ActivityCompat.requestPermissions(ThemeActivity.this,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, proceed_permission);
//                        }
//                    } else {
//                        ActivityCompat.requestPermissions(ThemeActivity.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, proceed_permission);
//
//                    }
//                } else{
//                    if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(ThemeActivity.this,
//                                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                        } else {
//
//                            ActivityCompat.requestPermissions(ThemeActivity.this,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, proceed_permission);
//                        }
//                    } else {
//                        ActivityCompat.requestPermissions(ThemeActivity.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, proceed_permission);
//
//                    }
//                }
                if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("aji 1 set tick");
                    ActivityCompat.requestPermissions(ThemeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, proceed_permission);
                }
                else if (ContextCompat.checkSelfPermission(ThemeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("aji 2 set tick");
                    ActivityCompat.requestPermissions(ThemeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, proceed_permission);
                }
                else
                {
                    Global.intent_method(ThemeActivity.this,HomeScreen.class);
                }
//                pickImage();


            }
        });
    }
    public void pickImage() {
        System.out.println("aji pickimage");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//        }
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                System.out.println("aji onactivity null");
                return;
            }
            Drawable bg;
            Uri selectedImageUri=null;
            try {
                selectedImageUri = data.getData();

                File f = new File(Global.getRealPathFromURI(ThemeActivity.this,selectedImageUri));
                bg= Drawable.createFromPath(f.getAbsolutePath());



//                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                bg = Drawable.createFromStream(inputStream, selectedImageUri.toString());
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//                encodeTobase64(bitmap);
            }
            catch (Exception e)
            {
                bg = ContextCompat.getDrawable(this, R.drawable.theme_1);
                e.printStackTrace();
            }
            System.out.println("aji onactivity worked");
            dummy_theme.setBackground(bg);
            img_theme_bg.setBackground(bg);
            sessionManager.setIsDefaultThemeSelected(false);
            sessionManager.setFileManagerTheme( selectedImageUri.toString());
        }
    }
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        Drawable dr = new BitmapDrawable(image);
        dummy_theme.setBackgroundDrawable(dr);
        img_theme_bg.setBackgroundDrawable(dr);
        sessionManager.setIsDefaultThemeSelected(false);
        sessionManager.setFileManagerTheme(imageEncoded);
        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {

        if (requestCode == permission) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("aji onrequqest permission");

                pickImage();
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        else if(requestCode == proceed_permission)
        {
            System.out.println("aji onrequqest procedd");
            sessionManager.setLoginStatus(true);
            Global.intent_method(ThemeActivity.this,HomeScreen.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            sessionManager.setLoginStatus(true);
            Global.intent_method(ThemeActivity.this,HomeScreen.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(ThemeActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    PermissionListener permissionlistener_pick_image = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            pickImage();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(ThemeActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }
    @Override public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }
}
