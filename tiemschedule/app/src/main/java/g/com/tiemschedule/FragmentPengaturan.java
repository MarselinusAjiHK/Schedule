package g.com.tiemschedule;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;



public class FragmentPengaturan extends Fragment {
    View myView;
    private String sId;
    private String sNama;
    private String sEmail;
    private String sPassword;
    SessionManagement session;
    private TextView mEmail;
    private TextView mNama;
    public ProgressDialog progress;
    ImageView img_logo;
    protected static final int CAMERA_REQUEST = 0;
    private Button  ubah_pengaturan, ubah_tampilan , ubah_profil, hapus_akun;
    protected static final int GALLERY_PICTURE = 1;
    Activity activity;
    String selectedImagePath;
    private Intent pictureActionIntent = null;
    Bitmap tempBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.pengaturan, container, false);
        ((dashboardActivity)getActivity()).setTitleCustom("Akun");
        session = new SessionManagement(getActivity().getApplicationContext());
        sId = ((dashboardActivity)getActivity()).getIdUser();
        HashMap<String, String> user = session.getUserDetails();
        sNama = user.get(SessionManagement.KEY_ID);
        ((dashboardActivity)getActivity()).setNamaPenggunaNav(sNama);
        sEmail = ((dashboardActivity)getActivity()).getEmailUser();
        sPassword = ((dashboardActivity)getActivity()).getPasswordUser();
        progress = new ProgressDialog(getActivity());
        ubah_pengaturan = myView.findViewById(R.id.btnPassword);
        ubah_tampilan = myView.findViewById(R.id.btnTampilan);
        ubah_profil = myView.findViewById(R.id.ubahProfil);
        hapus_akun = myView.findViewById(R.id.btnHapus);
        mEmail = myView.findViewById(R.id.emailView);
        mNama = myView.findViewById(R.id.namaPenggunaView);
        img_logo = myView.findViewById(R.id.profilePicture);
        tempBitmap = ((dashboardActivity)getActivity()).getBitmapImage();

        if(tempBitmap != null){
            img_logo.setImageBitmap(tempBitmap);
        }

        mEmail.setText(sEmail);
        mNama.setText(sNama);
        ubah_pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), pengaturan_sandi.class);
                i.putExtra("id", sId);
                i.putExtra("email", sEmail);
                i.putExtra("password", sPassword);
                startActivity(i);
            }
        });

        ubah_tampilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), pengaturan_tampilan.class);
                intent.putExtra("nama", sNama);
                intent.putExtra("id", sId);
                intent.putExtra("email", sEmail);
                intent.putExtra("password", sPassword);
                startActivity(intent);
            }
        });

        ubah_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                startDialog();
            }
        });
        hapus_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogDelete();
            }
        });



        return myView;
    }


    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(f));


                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }
    private void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23) return; if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 0);
        }
    }
    private void startDialogDelete() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Hapus Akun");
        myAlertDialog.setMessage("Apakah anda yakin ingin menghapus akun ?");

        myAlertDialog.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            progress.setMessage("Menghapus Akun . . ");
                            progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                            progress.setIndeterminate(true);
                            progress.show();
                            onClickDeleteUser();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        progress.dismiss();
                        arg0.dismiss();
                    }
                });
        myAlertDialog.show();
    }
//    delete akun
private void onClickDeleteUser() throws JSONException {

        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/user/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        ApiUser apiUser = retrofit.create(ApiUser.class);

        Call<UserDAO> UserDAOCall = apiUser.deleteUser(this.sId);
        UserDAOCall.enqueue(new Callback<UserDAO>() {
            @Override
            public void onResponse(Call<UserDAO> call, Response<UserDAO> response)  {

                progress.dismiss();
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if(response.body().getValue().toString().equals("200")) {
                    startIntent();
                }
            }

            @Override
            public void onFailure(Call<UserDAO> call, Throwable t) {

                progress.dismiss();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
}
    private void startIntent(){
        session = new SessionManagement(((dashboardActivity)getActivity()).getApplicationContext());
        session.logoutUser();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
//

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(((dashboardActivity) getActivity()),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                img_logo.setImageBitmap(bitmap);
                ((dashboardActivity)getActivity()).setImageProfileNavbar(bitmap);
                storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();


                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);


                img_logo.setImageBitmap(bitmap);
                ((dashboardActivity)getActivity()).setImageProfileNavbar(bitmap);
                storeImageTosdCard(bitmap);

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void storeImageTosdCard(Bitmap processedBitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child("images/"+this.sId+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        processedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            }
        });

    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}