package g.com.tiemschedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecycleAdapterJadwal extends RecyclerView.Adapter<RecycleAdapterJadwal.MyViewHolder>  {

    private Context context;
    private List<JadwalDAO> result;

    public RecycleAdapterJadwal(Context context, List<JadwalDAO> result){
        this.context = context;
        this.result = result;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView mJadwal, mWaktu, mTempat, mTanggal;
        public ImageView mEdit, mDone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mJadwal = itemView.findViewById(R.id.jadwal);
            mWaktu = itemView.findViewById(R.id.waktu);
            mTempat = itemView.findViewById(R.id.tempat);
            mTanggal = itemView.findViewById(R.id.tanggal);
            mEdit = itemView.findViewById(R.id.editButtonJadwal);
            mDone = itemView.findViewById(R.id.deleteButtonJadwal);
        }

        public void onClick(View view){
            Toast.makeText(context, "Hey You Clicked On Me", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public RecycleAdapterJadwal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_jadwal, viewGroup, false);
        final RecycleAdapterJadwal.MyViewHolder holder = new RecycleAdapterJadwal.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterJadwal.MyViewHolder holder, int position) {
        final JadwalDAO jadwal = result.get(position);
        holder.mJadwal.setText(jadwal.getJadwal());

        holder.mWaktu.setText(jadwal.getWaktu());

        holder.mTempat.setText(jadwal.getTempat());
        holder.mTanggal.setText(jadwal.getTanggal());
        holder.mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption(jadwal.getId().toString());
                diaBox.show();
            }
        });
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentEditJadwal frag = new FragmentEditJadwal();
                frag.setJadwal(jadwal.getJadwal().toString());
                frag.setWaktu(jadwal.getWaktu().toString());
                frag.setTanggal(jadwal.getTanggal().toString());
                frag.setTempat(jadwal.getTempat().toString());
                frag.setId_Jadwal(jadwal.getId().toString());
                frag.setPrioritas(jadwal.getPrioritas().toString());
                ((dashboardActivity) v.getContext()).getFragmentManager().beginTransaction().replace(R.id.framelay, frag).commit();
            }
        });
    }
    private AlertDialog AskOption(final String idj)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Apakah Anda ingin menghapus jadwal ini ?")

                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        setRecycleView(idj);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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
                .baseUrl("https://tiemschedule.thekingcorp.org/jadwal/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiJadwal apiJadwal= retrofit.create(ApiJadwal.class);
        Call<JadwalDAO> jadwalDAOCall= apiJadwal.deleteJadwal(idj);

        jadwalDAOCall.enqueue(new Callback<JadwalDAO>() {
            @Override
            public void onResponse(Call<JadwalDAO> call, Response<JadwalDAO> response) {

                Toast.makeText(context, "Diperbaharui", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JadwalDAO> call, Throwable t) {
                Toast.makeText(context, "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return result.size();
    }
}
