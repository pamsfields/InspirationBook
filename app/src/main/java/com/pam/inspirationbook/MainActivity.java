package com.pam.inspirationbook;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mEntryRecyclerView;
    public static EntryRecyclerAdapter sAdapter;
    public static EntryLoader sEntryLoader;
    public static EntryManager sEntryManager;

    //logging tag
    private static final String TAG = "Camera Main Activity";

    //UI components
    Button mTakePictureButton;
    ImageView mCameraPicture;
    Button mTakeNote;
    Button mViewGallery;

    //To identify the which permission request is returning a result
    private static final int REQUEST_SAVE_IMAGE_PERMISSON_REQUEST_CODE = 1001;

    //To identify that the camera is returning a result
    private static final int TAKE_PICTURE_REQUEST_CODE = 0;

    //For file storage, where is the current image stored?
    private String mImagePath;

    //The image to be displayed in the app
    private Bitmap mImage;

    //Used in the instance state Bundle, to preserve image when device is rotated
    private static final String IMAGE_FILEPATH_KEY = "image filepath key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUiElements();
        try{
            updateUI();

        }catch(ArrayIndexOutOfBoundsException e){
            Toast.makeText(MainActivity.this, "Please enter a note or image", Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(IMAGE_FILEPATH_KEY);
        }

        mTakeNote = (Button) findViewById(R.id.btn_create_note);
        mViewGallery = (Button) findViewById(R.id.btn_getgallery);
        mCameraPicture = (ImageView) findViewById(R.id.camera_picture);
        mTakePictureButton = (Button) findViewById(R.id.take_picture_button);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        mTakeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEntryDialog();
            }
        });
        mViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void takePhoto() {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Check to see if there is a camera on this device.
        if (pictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(MainActivity.this, "Your device does not have a camera", Toast.LENGTH_LONG).show();
        } else {
            //Create a unique filename for the image
            String imageFilename = "inspiration_book_" + new Date().getTime(); //Create a unique filename with a timestamp

            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //directory to store temp file
            File imageFile = null;
            Uri imageFileUri = null;

            try {
                //Create a temporary file with this name and path
                imageFile = File.createTempFile(imageFilename, ".jpg", storageDirectory);
                mImagePath = imageFile.getAbsolutePath(); //Save path in global variable
                //Create an URI from the path; the Intent will send this to the camera. A URI defines a location and how to access it
                //For example content://com.pam.simplecamera/my_images/simple_camera_1505239810320934713.jpg
                imageFileUri = FileProvider.getUriForFile(MainActivity.this, "com.pam.inspirationbook", imageFile);
            } catch (IOException ioe) {
                Log.e(TAG, "Error creating file for photo storage", ioe);
                return; //Will be unable to continue if unable to access storage
            }
            //So if creating the temporary file worked, should have a value for imageFileUri. Include this URI as an extra
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

            //And then request the camera is launched
            startActivityForResult(pictureIntent, TAKE_PICTURE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On Activity Result");

        if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE_REQUEST_CODE) {
            saveImageToMediaStore();
        }
    }

    private void showAddEntryDialog() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AddTextDialogFrag newFragment = new AddTextDialogFrag();
        newFragment.show(ft, "add entry dialog");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "OnWindowFocusChanged");

        if (hasFocus && mImagePath != null) {
            scaleBitmap();
            mCameraPicture.setImageBitmap(mImage);
        }
    }

    //@Override
    private void scaleBitmap() {
        //Step 1: what size is the ImageView?
        int imageViewHeight = mCameraPicture.getHeight();
        int imageViewWidth = mCameraPicture.getWidth();

        //If height or width are zero, there's no point doing this. Return.
        if (imageViewHeight == 0 || imageViewWidth == 0) {
            Log.w(TAG, "The image view size is zero. Unable to scale.");
            return;
        }
        //Step 2: decode file to find out how large the image is.

        //BitmapFactory is used to create bitmaps from pixels in a file.
        //Many options and settings, so use a BitMapFactory.Options object to store our desired settings.
        //Set the inJustDecodeBounds flag to true,
        //which means just the *information about* the picture is decoded and stored inbOptions
        //Not all of the pixels have to be read and stored in this process.
        //When we've done this, we can query bOptions to find out the orginal picture's height and width.


        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mImagePath, bOptions);

        int pictureHeight = bOptions.outHeight;
        int pictureWidth = bOptions.outWidth;

        //Step 3. Can use the original size and target size to calculate scale factor
        int scaleFactor = Math.min(pictureHeight / imageViewHeight, pictureWidth / imageViewWidth);

        //Step 4. Decode the image file into a new Bitmap, scaled to fit the ImageView
        bOptions.inJustDecodeBounds = false;
        bOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath, bOptions);
        mImage = bitmap;
    }

    //Handle rotation. Save the image pathname.
    //Bitmap objects are parcelable so can be put in a bundle, but the bitmap might be huge and take up more
    //memory that the app is allocated. So, pu the file path and re-decode the bitmap on rotation.

    //@Override
    public void onSavedInstanceState(Bundle outBundle) {
        outBundle.putString(IMAGE_FILEPATH_KEY, mImagePath);
    }

    private void saveImageToMediaStore() {
        //Add image to device's MediaStore  this makes the image accessible to the
        //gallery app, and any apps that can read from the MediaStore

        //Do we have permission to write to storage?

        //Marshmallow and before, we just need to request permission on AndroidManifest,
        //and this check will return true if we've done so. The user will be notified that this app uses the file system when they first install it.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            MediaStore.Images.Media.insertImage(getContentResolver(), mImage, "InspirationBook", "Photo taken by InspirationBook");
        }

        //WRITE_EXTERNAL_STORAGE is a dangerous permission. So for Nougat and above, need to request permission in the manifest
        //AND we will need to ask the user for permission when the app runs.
        else {
            //This request opens a dialog box for the user to accept the permission request.
            //When the user clicks ok or cancel, the onRequestPermission method (below) is called with the results
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SAVE_IMAGE_PERMISSON_REQUEST_CODE);
        }
    }


    private void setupUiElements()
    {
        sEntryLoader = new EntryLoader();
        sEntryLoader.loadMoreItems(100);

        EntryRecyclerAdapter era = new EntryRecyclerAdapter(sEntryLoader);
        mEntryRecyclerView = (RecyclerView) findViewById(R.id.recycler_mainview);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mEntryRecyclerView.setLayoutManager(sglm);
        mEntryRecyclerView.setAdapter(era);
    }

    public void updateUI() {
        ArrayList<Entry> entries = sEntryManager.getEntries();

        if (sAdapter == null) {
            sAdapter = new EntryRecyclerAdapter(sEntryLoader);
            mEntryRecyclerView.setAdapter(sAdapter);

        } else {
            sAdapter.setEntries(entries);
            sAdapter.notifyDataSetChanged();
        }
    }


//This is the callback for requestPermissions for adding image to media store

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_SAVE_IMAGE_PERMISSON_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Now should be able to save
                MediaStore.Images.Media.insertImage(getContentResolver(), mImage, "InspirationBook", "Photo taken by InspirationBook");
            } else {
                Log.w(TAG, "Permission to WRITE_EXTERNAL_STORAGE was NOT granted.");
                Toast.makeText(this, "The images taken will NOT be saved to the gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

