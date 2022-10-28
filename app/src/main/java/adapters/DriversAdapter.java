package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bdm.R;
import java.util.ArrayList;
import activities.DriverDetails;
import models.Driver;

public class DriversAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final ArrayList<Driver> drivers;
    private Context context;


    public DriversAdapter(ArrayList<Driver> drivers, Context context) {
        this.drivers = drivers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(drivers.get(position));
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvUname;
    private final Context context;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUname = itemView.findViewById(R.id.tvUname);
        context = itemView.getContext();

    }

    public void setData(Driver driver) {
        tvUname.setText(driver.getName());
        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DriverDetails.class);
            intent.putExtra("driver", driver);
            context.startActivity(intent);
        });

    }

}
