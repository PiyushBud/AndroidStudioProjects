package com.example.lab10_pbudhath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button camera_button;
    MyCustomAdapter myAdapter;
    ListView myList;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAdapter = new MyCustomAdapter();
        myList = findViewById(R.id.mylist);
        myList.setAdapter(myAdapter);
        camera_button = findViewById(R.id.Button_camera);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { open(); }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap b = myAdapter.getItem(position);
                Intent intentImage = new Intent(getApplicationContext(), DisplayImage.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intentImage.putExtra("image",byteArray);
                startActivity(intentImage);

            }
        });
    }

    public void open() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        myAdapter.addItem(bp);
    }

    private class MyCustomAdapter extends BaseAdapter {

        private ArrayList<Bitmap> mData = new ArrayList<Bitmap>();
        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final Bitmap item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void removeItem(final Bitmap item) {
            mData.remove(item);
            notifyDataSetChanged();
        }


        public int getCount() {
            return mData.size();
        }


        public Bitmap getItem(int position) {
            return mData.get(position);
        }


        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            mData.clear();
            notifyDataSetChanged();
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_row, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView)convertView.findViewById(R.id.Image_View);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Bitmap s = mData.get(position);
            holder.imageView.setImageBitmap(s);

            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

}
