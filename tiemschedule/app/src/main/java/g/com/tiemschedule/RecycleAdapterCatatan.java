package g.com.tiemschedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecycleAdapterCatatan extends RecyclerView.Adapter<RecycleAdapterCatatan.MyViewHolder> {
    private Context context;
    private List<CatatanDAO> result;

    public RecycleAdapterCatatan(Context context, List<CatatanDAO> result){
        this.context = context;
        this.result = result;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView mCatatan;
        public ImageView mEdit, mDone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCatatan = itemView.findViewById(R.id.textCatatan);
            mEdit = itemView.findViewById(R.id.editButtonCatatan);
            mDone = itemView.findViewById(R.id.doneButtonCatatan);
        }

        public void onClick(View view){
            Toast.makeText(context, "Hey You Clicked On Me", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_catatan, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CatatanDAO cttn = result.get(position);
        holder.mCatatan.setText(cttn.getCatatan());
        holder.mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption(cttn.getIds().toString());
                diaBox.show();
            }
        });
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentEditCatatan frag = new FragmentEditCatatan();
                frag.setId_catatan(cttn.getIds().toString());
                frag.setCatatan(cttn.getCatatan().toString());
                frag.setPrioritas(cttn.getPrioritas().toString());
                ((dashboardActivity) v.getContext()).getFragmentManager().beginTransaction().replace(R.id.framelay, frag).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    private AlertDialog AskOption(final String idj)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Apakah Catatan ini sudah selesai ?")

                .setPositiveButton("Sudah", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        setRecycleView(idj);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private void setRecycleView(final String idj){
        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl("https://tiemschedule.thekingcorp.org/catatan/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiCatatan apiCatatan= retrofit.create(ApiCatatan.class);
        Call<CatatanDAO> catatanDAOCall = apiCatatan.deleteCatatan(idj);

        catatanDAOCall.enqueue(new Callback<CatatanDAO>() {
            @Override
            public void onResponse(Call<CatatanDAO> call, Response<CatatanDAO> response) {

                Toast.makeText(context, "Berhasil di hapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CatatanDAO> call, Throwable t) {
                Toast.makeText(context, "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
