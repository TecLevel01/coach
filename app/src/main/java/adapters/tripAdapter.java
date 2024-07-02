package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.oli.coach.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import models.initTrip;

public class tripAdapter extends RecyclerView.Adapter<tripAdapter.MyViewHolder> {
    Context context;
    ArrayList<initTrip> initTripArrayList;

    public tripAdapter(Context context, ArrayList<initTrip> initTripArrayList) {
        this.context = context;
        this.initTripArrayList = initTripArrayList;
    }

    @NonNull
    @Override
    public tripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.trips_details, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull tripAdapter.MyViewHolder holder, int position) {
        initTrip initTrip = initTripArrayList.get(position);
        holder.tDestination.setText(initTrip.getDestination());
        holder.time.setText(dateFormat(initTrip.getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return initTripArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tDestination, time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tDestination = itemView.findViewById(R.id.tvPlate);
            time = itemView.findViewById(R.id.tvTime);
        }
    }
    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd MM yy . HH:mm");
        return simpleDateFormat.format(date);
    }
}
